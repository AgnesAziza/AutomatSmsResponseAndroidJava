package com.example.automateresponseandroidjava.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.adapter.ResponseAdapter;
import com.example.automateresponseandroidjava.model.Contact;
import com.example.automateresponseandroidjava.model.Response;
import com.example.automateresponseandroidjava.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class AutomaticRespondFragment extends Fragment implements ResponseAdapter.OnResponseClickListener {

    private RecyclerView recyclerView;
    private ResponseAdapter responseAdapter;
    private List<Response> responseList;
    private SharedViewModel sharedViewModel;

    private static final int REQUEST_SMS_PERMISSION = 2;

    @Override
    public void onContactSelected(Contact contact) {
        // Implémentez le comportement souhaité lorsque le contact est sélectionné
        // Par exemple, vous pouvez mettre à jour le contact sélectionné dans le ViewModel partagé
        sharedViewModel.setSelectedContact(contact);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("AutomaticRespondFragment", "onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_automatic_response, container, false);
        TextView nameTextView = rootView.findViewById(R.id.name_text_view);
        TextView phoneTextView = rootView.findViewById(R.id.phone_text_view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Get the selected contact from the shared view model instead
        Contact selectedContact = sharedViewModel.getSelectedContact();
        if (selectedContact != null) {
            nameTextView.setText(selectedContact.getName());
            phoneTextView.setText(selectedContact.getPhoneNumber());
        }

        recyclerView = rootView.findViewById(R.id.responseRecyclerView);
        responseList = getSavedResponses();

        setupRecyclerView(rootView);

        Button sendMessageButton = rootView.findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact selectedContact = sharedViewModel.getSelectedContact();
                if (selectedContact != null) {
                    String phoneNumber = selectedContact.getPhoneNumber();
                    String automaticResponse = sharedViewModel.getAutomaticResponse();
                    if (phoneNumber != null && automaticResponse != null) {
                        sendSms(phoneNumber, automaticResponse);
                    } else {
                        Toast.makeText(getContext(), "Invalid phone number or message", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No contact selected", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée, vous pouvez maintenant envoyer le SMS
                Contact selectedContact = sharedViewModel.getSelectedContact();
                String phoneNumber = selectedContact.getPhoneNumber();
                String automaticResponse = sharedViewModel.getAutomaticResponse();
                sendSms(selectedContact.getPhoneNumber(), automaticResponse);
            } else {
                // Permission refusée, affichez un message à l'utilisateur
                Toast.makeText(getContext(), "Permission to send SMS not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupRecyclerView(View rootView) {
        responseAdapter = new ResponseAdapter(responseList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(responseAdapter);

        Button sendMessageButton = rootView.findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact selectedContact = sharedViewModel.getSelectedContact();
                if (selectedContact != null) {
                    String phoneNumber = selectedContact.getPhoneNumber();
                    String automaticResponse = sharedViewModel.getAutomaticResponse();
                    sendSms(phoneNumber, automaticResponse);
                } else {
                    Toast.makeText(getContext(), "No contact selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private List<Response> getSavedResponses() {
        return new ArrayList<>();
    }

    @Override
    public void onResponseClick(String response) {
        Contact selectedContact = sharedViewModel.getSelectedContact();
        String phoneNumber = selectedContact.getPhoneNumber();
        String message = response;
        Log.d("CheckValues", "Phone number: " + phoneNumber);

        // Vérifiez la permission d'envoyer des SMS
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // La permission est déjà accordée, vous pouvez envoyer le SMS
            sendSms(phoneNumber, message);
        } else {
            // La permission n'est pas accordée, vous devez demander la permission à l'utilisateur
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
        }
    }

    private void sendSms(String phoneNumber, String message) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getContext(), "Message sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
        }
    }

}

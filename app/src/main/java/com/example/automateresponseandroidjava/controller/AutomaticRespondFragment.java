package com.example.automateresponseandroidjava.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_automatic_response, container, false);
            TextView nameTextView = view.findViewById(R.id.name_text_view);
            TextView phoneTextView = view.findViewById(R.id.phone_text_view);

            sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

            // Get the selected contact from the shared view model instead
            Contact selectedContact = sharedViewModel.getSelectedContact();
            if (selectedContact != null) {
                nameTextView.setText(selectedContact.getName());
                phoneTextView.setText(selectedContact.getPhoneNumber());
            }

            recyclerView = view.findViewById(R.id.responseRecyclerView);
            responseList = getSavedResponses();

            setupRecyclerView();

            return view;
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée, vous pouvez maintenant envoyer le SMS
                Contact selectedContact = sharedViewModel.getSelectedContact();
                String automaticResponse = sharedViewModel.getAutomaticResponse();
                sendSms(selectedContact.getPhoneNumber(), automaticResponse);
            } else {
                // Permission refusée, affichez un message à l'utilisateur
                Toast.makeText(getContext(), "Permission to send SMS not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setupRecyclerView() {
        responseAdapter = new ResponseAdapter(responseList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(responseAdapter);
    }

    private List<Response> getSavedResponses() {
        return new ArrayList<>();
    }

    @Override
    public void onResponseClick(String response) {
        Contact selectedContact = sharedViewModel.getSelectedContact();
        String phoneNumber = selectedContact.getPhoneNumber();
        Log.d("CheckValues", "Phone number: " + phoneNumber);

        if (phoneNumber == null || phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            Log.d("CheckValues", "Invalid phone number");
        } else {
            Log.d("CheckValues", "Phone number is valid");
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

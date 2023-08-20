package com.example.automateresponseandroidjava.controller;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
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

    // Méthode pour récupérer les réponses

    private List<Response> getSavedResponses() {
        List<Response> responses = new ArrayList<>();
        responses.add(new Response("Je ne suis pas disponible, je vous recontacterai", false));
        return responses;
    }

    // Lorsque l'utilisateur sélectionne une réponse, cela met à jour le ViewModel

    @Override
    public void setResponse(String response) {
        sharedViewModel.setAutomaticResponse(response);
    }

    // Méthode pour initialiser la vue du fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_automatic_response, container, false);
        // Initialiser le ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        responseList = getSavedResponses();
        // Initialiser la vue de la liste des réponses
        setupRecyclerView(rootView);
        // Initialiser le bouton d'activation/désactivation de la réponse automatique
        Switch autoResponseSwitch = rootView.findViewById(R.id.automaticResponseSwitch);

        SharedPreferences prefs = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        boolean isAutoResponseEnabled = prefs.getBoolean("autoReplyEnabled", false);

        autoResponseSwitch.setChecked(isAutoResponseEnabled);
        autoResponseSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("autoReplyEnabled", isChecked);
            editor.apply();

            Toast.makeText(getContext(), isChecked ? "Réponse automatique activée" : "Réponse automatique désactivée", Toast.LENGTH_SHORT).show();
        });

        // Initialiser le bouton pour envoyer un message
        Button sendMessageButton = rootView.findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(v -> {
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
        });

        return rootView;
    }
    // Méthode pour initialiser le RecyclerView
    private void setupRecyclerView(View rootView) {
        recyclerView = rootView.findViewById(R.id.responseRecyclerView);
        responseAdapter = new ResponseAdapter(responseList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(responseAdapter);
    }

    // Lorsqu'une réponse est cliquée, elle est enregistrée dans les préférences et prête à être envoyée
    @Override
    public void onResponseClick(String response) {
        Contact selectedContact = sharedViewModel.getSelectedContact();
        if (selectedContact == null) {
            Toast.makeText(getContext(), "Veuillez sélectionner un contact", Toast.LENGTH_SHORT).show();
            return;
        }
        String phoneNumber = selectedContact.getPhoneNumber();
        sharedViewModel.setAutomaticResponse(response);
        String message = response;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("auto_response_contacts", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(selectedContact.getPhoneNumber() + "_response", response);
        editor.putBoolean(selectedContact.getPhoneNumber() + "_saved", true);
        editor.apply();
    }

    // Méthode pour envoyer un SMS
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

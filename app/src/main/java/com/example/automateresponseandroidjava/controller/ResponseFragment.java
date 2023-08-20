package com.example.automateresponseandroidjava.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.adapter.ResponseAdapter;
import com.example.automateresponseandroidjava.model.Response;
import com.example.automateresponseandroidjava.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Fragment pour afficher et gérer les réponses
public class ResponseFragment extends Fragment implements ResponseAdapter.OnResponseClickListener {
    private SharedViewModel sharedViewModel;
    private static final String PREFS_NAME = "ResponsePrefs";
    private static final String PREF_RESPONSES = "SavedResponses";

    private RecyclerView recyclerView;
    private ResponseAdapter responseAdapter;
    private List<Response> responseList;
    private SharedPreferences preferences;

    private EditText responseEditText;
    private Button addResponseButton;

    private String selectedContact;

    private String selectedResponse;

    private OnSendMessageListener sendMessageListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSendMessageListener) {
            sendMessageListener = (OnSendMessageListener) context;
        } else {
            throw new IllegalArgumentException("Activity must implement OnSendMessageListener");
        }
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }
    public interface OnSendMessageListener {
        void onSendMessage(String phoneNumber, String message);
    }

    @Override
    public void setResponse(String response) {
        setSelectedContact(response);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_response, container, false);
        recyclerView = view.findViewById(R.id.responseRecyclerView);
        responseEditText = view.findViewById(R.id.responseEditText);
        addResponseButton = view.findViewById(R.id.addResponseButton);

        preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        responseList = getSavedResponses();

        // Configuration de la liste des réponses.
        setupRecyclerView();

        // Événement lorsqu'un nouveau message est ajouté.
        addResponseButton.setOnClickListener(v -> {
            String newResponse = responseEditText.getText().toString().trim();
            if (!newResponse.isEmpty()) {
                addResponse(newResponse);
                responseEditText.setText("");
            }
        });
        return view;
    }

    // Configure le RecyclerView.
    private void setupRecyclerView() {
        responseAdapter = new ResponseAdapter(responseList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(responseAdapter);
    }

    // Récupère la liste des réponses enregistrées.
    private List<Response> getSavedResponses() {
        List<Response> responses = new ArrayList<>();
        // Messages par défaut
        Set<String> savedResponses = preferences.getStringSet(PREF_RESPONSES, null);
        if (savedResponses == null || savedResponses.isEmpty()) {
            responses.add(new Response("Je vous rappelle", true));
            responses.add(new Response("Je te rappelle", true));
            responses.add(new Response("J'arrive dans 10 minutes", true));
        } else {
            for (String response : savedResponses) {
                responses.add(new Response(response, false));
            }
        }
        return responses;
    }

    // Enregistre la liste des réponses.
    private void saveResponses() {
        Set<String> savedResponses = new HashSet<>();
        for (Response response : responseList) {
            savedResponses.add(response.getResponse());
        }
        preferences.edit().putStringSet(PREF_RESPONSES, savedResponses).apply();
    }

    // Ajoute une nouvelle réponse à la liste.
    private void addResponse(String response) {
        responseList.add(new Response(response, false));
        responseAdapter.notifyDataSetChanged();
        saveResponses();
    }

    // Événement lorsqu'une réponse est sélectionnée si contact non sélectionné.
    @Override
    public void onResponseClick(String response) {
        if (selectedContact == null) {
            Toast.makeText(getContext(), "Veuillez sélectionner un contact", Toast.LENGTH_SHORT).show();
            return;
        }
        selectedResponse = response;
        sharedViewModel.setAutomaticResponse(response);
        Toast.makeText(getContext(), "Message sélectionné: " + response, Toast.LENGTH_SHORT).show();
        if (sendMessageListener != null) {
            sendMessageListener.onSendMessage(selectedContact, response);
        }
        if (getActivity() instanceof OnSendMessageListener) {
            OnSendMessageListener listener = (OnSendMessageListener) getActivity();
            listener.onSendMessage(selectedContact, response);
        }
    }

    // Définit le contact sélectionné.
    public void setSelectedContact(String contact) {
            if(contact != null) {
                if(sharedViewModel.getSelectedContact() != null) {
                    this.selectedContact = sharedViewModel.getSelectedContact().getPhoneNumber();
                } else {
                    Toast.makeText(getContext(), "Erreur: pas de contact.", Toast.LENGTH_SHORT).show();
                }
            }
    }
}

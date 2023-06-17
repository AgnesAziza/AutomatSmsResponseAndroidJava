package com.example.automateresponseandroidjava.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.adapter.ResponseAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AutomaticRespondFragment extends Fragment {

    private static final String PREFS_NAME = "AutoResponsePrefs";
    private static final String PREF_AUTO_RESPONSES = "SavedAutoResponses";

    private RecyclerView recyclerView;
    private ResponseAdapter responseAdapter;
    private ArrayList<String> autoResponseList;

    private SharedPreferences preferences;

    public AutomaticRespondFragment() {
        // Empty constructor required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_automatic_response, container, false);

        recyclerView = view.findViewById(R.id.autoResponsesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        autoResponseList = new ArrayList<>();
        responseAdapter = new ResponseAdapter(autoResponseList, this::onResponseClick);
        recyclerView.setAdapter(responseAdapter);

        preferences = requireContext().getSharedPreferences(PREFS_NAME, 0);

        loadSavedAutoResponses();

        FloatingActionButton addResponseFAB = view.findViewById(R.id.addResponseFAB);
        addResponseFAB.setOnClickListener(v -> {
            // Gérer l'événement de clic sur le bouton FAB
        });

        return view;
    }

    private void loadSavedAutoResponses() {
        Set<String> savedResponses = preferences.getStringSet(PREF_AUTO_RESPONSES, null);
        if (savedResponses != null) {
            autoResponseList.addAll(savedResponses);
            responseAdapter.notifyDataSetChanged();
        }
    }

    private void saveAutoResponses(ArrayList<String> autoResponses) {
        Set<String> savedResponses = new HashSet<>(autoResponses);
        preferences.edit().putStringSet(PREF_AUTO_RESPONSES, savedResponses).apply();
    }

    private void onResponseClick(String response) {
        // Gérer l'événement de clic sur une réponse
    }
}

package com.example.automateresponseandroidjava.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.automateresponseandroidjava.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ResponseFragment extends Fragment {

    public interface OnSendMessageListener {
        void onSendMessage(String phoneNumber, String message);
    }

    private static final String PREFS_NAME = "ResponsePrefs";
    private static final String PREF_RESPONSES = "SavedResponses";

    private ListView responseListView;
    private EditText responseEditText;
    private Button addButton;
    private ArrayAdapter<String> responseAdapter;
    private ArrayList<String> responseList;

    private SharedPreferences preferences;

    public ResponseFragment() {
        // Empty constructor required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_response, container, false);

        responseListView = view.findViewById(R.id.responseListView);
        responseEditText = view.findViewById(R.id.responseEditText);
        addButton = view.findViewById(R.id.addButton);

        preferences = requireContext().getSharedPreferences(PREFS_NAME, 0);

        responseList = new ArrayList<>();
        responseAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_single_choice, responseList);
        responseListView.setAdapter(responseAdapter);
        responseListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = responseEditText.getText().toString().trim();
                if (!response.isEmpty()) {
                    responseList.add(response);
                    responseAdapter.notifyDataSetChanged();
                    responseEditText.setText("");
                    saveResponses(responseList);
                }
            }
        });

        loadSavedResponses();

        return view;
    }

    private void loadSavedResponses() {
        Set<String> savedResponses = preferences.getStringSet(PREF_RESPONSES, null);
        if (savedResponses != null) {
            responseList.addAll(savedResponses);
            responseAdapter.notifyDataSetChanged();
        }
    }

    private void saveResponses(ArrayList<String> responses) {
        Set<String> savedResponses = new HashSet<>(responses);
        preferences.edit().putStringSet(PREF_RESPONSES, savedResponses).apply();
    }
}

package com.example.automateresponseandroidjava.controller;

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
import com.example.automateresponseandroidjava.model.Response;

import java.util.ArrayList;
import java.util.List;

public class AutomaticRespondFragment extends Fragment implements ResponseAdapter.OnResponseClickListener {

    private RecyclerView recyclerView;
    private ResponseAdapter responseAdapter;
    private List<Response> responseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_automatic_response, container, false);
        recyclerView = view.findViewById(R.id.responseRecyclerView);
        responseList = getSavedResponses();

        setupRecyclerView();

        return view;
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
    }
}

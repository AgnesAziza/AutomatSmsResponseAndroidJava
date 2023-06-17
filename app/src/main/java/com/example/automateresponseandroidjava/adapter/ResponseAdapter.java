package com.example.automateresponseandroidjava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automateresponseandroidjava.R;

import java.util.List;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ResponseViewHolder> {

    private List<String> responseList;
    private OnResponseClickListener responseClickListener;

    public ResponseAdapter(List<String> responseList, OnResponseClickListener responseClickListener) {
        this.responseList = responseList;
        this.responseClickListener = responseClickListener;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_response, parent, false);
        return new ResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        String response = responseList.get(position);
        holder.bind(response);
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    class ResponseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView responseTextView;

        ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            responseTextView = itemView.findViewById(R.id.responseTextView);
            itemView.setOnClickListener(this);
        }

        void bind(String response) {
            responseTextView.setText(response);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String response = responseList.get(position);
                responseClickListener.onResponseClick(response);
            }
        }
    }

    public interface OnResponseClickListener {
        void onResponseClick(String response);
    }
}

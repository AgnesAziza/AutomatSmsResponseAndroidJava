package com.example.automateresponseandroidjava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.model.Response;

import java.util.List;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ResponseViewHolder> {

    private List<Response> responseList;
    private OnResponseClickListener clickListener;

    public ResponseAdapter(List<Response> responseList, OnResponseClickListener clickListener) {
        this.responseList = responseList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_response, parent, false);
        return new ResponseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        Response responseItem = responseList.get(position);
        holder.responseTextView.setText(responseItem.getResponse());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onResponseClick(responseItem.getResponse());
            }
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class ResponseViewHolder extends RecyclerView.ViewHolder {

        TextView responseTextView;

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            responseTextView = itemView.findViewById(R.id.responseTextView);
        }
    }

    public interface OnResponseClickListener {
        void onResponseClick(String response);
    }
}

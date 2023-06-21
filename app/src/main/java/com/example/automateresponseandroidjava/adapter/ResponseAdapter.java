package com.example.automateresponseandroidjava.adapter;

import android.util.Log;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.response_item, parent, false);
        return new ResponseViewHolder(itemView);
    }

    public class ResponseViewHolder extends RecyclerView.ViewHolder {
        TextView responseTextView;
        CheckBox checkBox;

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            responseTextView = itemView.findViewById(R.id.responseTextView);
            checkBox = itemView.findViewById(R.id.autoResponseCheckBox);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Log.d("ResponseViewHolder", "Checkbox checked state changed to: " + isChecked);
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Response response = responseList.get(adapterPosition);
                    response.setSelected(isChecked);
                    if (isChecked) {
                        clickListener.setResponse(response.getResponse());
                    }
                }
            });

            itemView.setOnClickListener(v -> {
                Log.d("ResponseViewHolder", "Item clicked");
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Response response = responseList.get(adapterPosition);
                    clickListener.onResponseClick(response.getResponse());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        Response response = responseList.get(position);
        holder.responseTextView.setText(response.getResponse());
        holder.checkBox.setChecked(response.isSelected());
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public interface OnResponseClickListener {
        void onResponseClick(String response);
        void setResponse(String response);
    }
}

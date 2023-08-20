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

    // Liste des réponses automatisées
    private List<Response> responseList;
    // Écouteur pour les événements de clic sur une réponse
    private OnResponseClickListener clickListener;

    // Constructeur pour initialiser l'adapter avec une liste de réponses et un écouteur de clic
    public ResponseAdapter(List<Response> responseList, OnResponseClickListener clickListener) {
        this.responseList = responseList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Charger le layout d'un élément de la liste de réponses
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.response_item, parent, false);
        return new ResponseViewHolder(itemView);
    }

    public class ResponseViewHolder extends RecyclerView.ViewHolder {
        TextView responseTextView; // Vue pour afficher la réponse
        CheckBox checkBox;        // Case à cocher pour sélectionner/désélectionner la réponse

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            responseTextView = itemView.findViewById(R.id.responseTextView);
            checkBox = itemView.findViewById(R.id.autoResponseCheckBox);

            // Créer un écouteur de clic pour la réponse et la case à cocher
            View.OnClickListener onClickListener = v -> {
                int adapterPosition = getAdapterPosition();

                // Vérifier si la position est valide
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Response response = responseList.get(adapterPosition);

                    // Inverser l'état de sélection de la réponse
                    boolean newSelectedState = !response.isSelected();
                    response.setSelected(newSelectedState);
                    checkBox.setChecked(newSelectedState);

                    // Si la réponse est sélectionnée, informer l'écouteur
                    if (newSelectedState) {
                        clickListener.setResponse(response.getResponse());
                    }
                    clickListener.onResponseClick(response.getResponse());
                }
            };

            // Appliquer l'écouteur à l'itemView et à la case à cocher
            itemView.setOnClickListener(onClickListener);
            checkBox.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        // Obtenir la réponse à la position donnée
        Response response = responseList.get(position);

        // Mettre à jour les vues du ViewHolder avec les informations de la réponse
        holder.responseTextView.setText(response.getResponse());
        holder.checkBox.setChecked(response.isSelected());
    }

    @Override
    public int getItemCount() {
        // Retourner le nombre d'éléments dans la liste des réponses
        return responseList.size();
    }

    // Interface pour gérer les événements de clic sur une réponse
    public interface OnResponseClickListener {
        // Appelé lors du clic sur une réponse
        void onResponseClick(String response);
        // Appelé pour définir une réponse sélectionnée
        void setResponse(String response);
    }
}

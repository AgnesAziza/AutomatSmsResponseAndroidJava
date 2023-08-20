package com.example.automateresponseandroidjava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.model.Contact;

import java.util.List;

// Adapter pour gérer l'affichage de la liste des contacts dans un RecyclerView
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    // Liste des contacts à afficher
    private List<Contact> contactList;

    // Écouteur pour les événements de clic sur un contact
    private OnContactClickListener contactClickListener;

    // Interface pour gérer les événements de clic sur un contact
    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    // Méthode pour mettre à jour la liste des contacts
    public void setContacts(List<Contact> newContacts) {
        this.contactList = newContacts;
    }

    // Méthode pour définir l'écouteur des événements de clic
    public void setOnContactClickListener(OnContactClickListener listener) {
        this.contactClickListener = listener;
    }

    // Constructeur pour initialiser l'adapter avec une liste de contacts
    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Charger le layout d'un élément de la liste
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        // Obtenir le contact à la position donnée
        Contact contact = contactList.get(position);

        // Mettre à jour les vues du ViewHolder avec les informations du contact
        holder.nameTextView.setText(contact.getName());
        holder.phoneTextView.setText(contact.getPhoneNumber());
        holder.checkBox.setChecked(contact.isSelected());

        // Ajouter un écouteur pour l'itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inverser l'état de sélection du contact lors du clic
                contact.setSelected(!contact.isSelected());
                holder.checkBox.setChecked(contact.isSelected());
                // Si un écouteur de clic est défini, l'appeler avec le contact sélectionné
                if (contactClickListener != null) {
                    contactClickListener.onContactClick(contact);
                }
            }
        });
    }

    // Retourner le nombre d'éléments dans la liste des contacts
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // ViewHolder pour représenter visuellement un élément de la liste des contacts
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;      // Vue pour le nom du contact
        TextView phoneTextView;    // Vue pour le numéro de téléphone du contact
        CheckBox checkBox;         // Case à cocher pour sélectionner/désélectionner le contact

        public ContactViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            phoneTextView = itemView.findViewById(R.id.phone_text_view);
            checkBox = itemView.findViewById(R.id.contact_check_box);

            // Désactiver les événements de clic et de focus pour la case à cocher
            checkBox.setClickable(false);
            checkBox.setFocusable(false);
        }
    }
}

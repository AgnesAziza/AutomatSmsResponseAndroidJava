package com.example.automateresponseandroidjava.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.adapter.ContactAdapter;
import com.example.automateresponseandroidjava.model.Contact;
import com.example.automateresponseandroidjava.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class ContactFragment extends Fragment implements ContactAdapter.OnContactClickListener {

    // Variables pour la gestion de l'interface et des données
    private OnContactSelectedListener onContactSelectedListener;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<Contact> contacts;
    private Contact currentSelectedContact = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialisation du listener pour la sélection de contact
        if (getActivity() instanceof OnContactSelectedListener) {
            onContactSelectedListener = (OnContactSelectedListener) getActivity();
        }
    }
    // Méthode pour gérer la désélection d'un contact
    public void onContactDeselected() {
        // Afficher un message indiquant que le contact a été désélectionné
        Toast.makeText(getContext(), "Contact désélectionné", Toast.LENGTH_SHORT).show();

        // Mise à jour de l'interface
        if (onContactSelectedListener != null) {
            onContactSelectedListener.onContactDeselected();
        }
        // Mise à jour du modèle de données
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.setSelectedContact(null);
    }

    // Méthode appelée lors du clic sur un contact
    @Override
    public void onContactClick(Contact contact) {
        if (currentSelectedContact != null && currentSelectedContact.equals(contact)) {
            Toast.makeText(getContext(), "Contact désélectionné: " + contact.getName() + " Téléphone: " + contact.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            currentSelectedContact = null;
            onContactDeselected();
        } else {
            // Toast pour la sélection d'un contact
            Toast.makeText(getContext(), "Contact sélectionné: " + contact.getName() + " Téléphone: " + contact.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            currentSelectedContact = contact;
            onContactSelected(contact);
        }
    }

    // Méthode pour gérer la sélection d'un contact
    public void onContactSelected(Contact contact) {
        // Mise à jour de l'interface
        if (onContactSelectedListener != null) {
            onContactSelectedListener.onContactSelected(contact);
        }
        // Mise à jour du modèle de données
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.setSelectedContact(contact);
    }

    // Rafraîchir la liste des contacts lorsque le fragment reprend la main
    @Override
    public void onResume() {
        super.onResume();
        contacts = getContacts();
        if (contacts != null && contactAdapter != null) {
            contactAdapter.setContacts(contacts);
            contactAdapter.notifyDataSetChanged();
        }
    }

    // Créer la vue du fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    // Configurer les composants de la vue une fois qu'elle est créée
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation de la RecyclerView
        recyclerView = view.findViewById(R.id.contactsRecyclerView);
        contacts = getContacts();

        if (contacts != null) {
            contactAdapter = new ContactAdapter(contacts);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(contactAdapter);
            contactAdapter.setOnContactClickListener(this);
        }
    }

    // Récupérer la liste des contacts du téléphone
    private List<Contact> getContacts() {
        HashSet<Contact> uniqueContacts = new HashSet<>();
        // Vérifier la permission pour lire les contacts
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // Récupération des contacts
            Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                uniqueContacts.add(new Contact(name, phoneNumber));
            }
            cursor.close();
        } else {
            // Demande de permission pour lire les contacts
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }

        // Convertir le HashSet en ArrayList
        List<Contact> contacts = new ArrayList<>(uniqueContacts);
        // Trier les contacts par nom en ordre alphabétique
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });

        // Demande de permission pour envoyer des SMS
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 2);
        }

        return contacts;
    }


    // Gérer le résultat de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Si la permission de lire les contacts est accordée, rafraîchir la liste
        if (requestCode == 1) {
            // Refresh the contacts
            contacts = getContacts();
            contactAdapter.notifyDataSetChanged();
        }

        // Gérer le résultat de la demande de permission pour envoyer des SMS
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée pour envoyer des SMS
                Toast.makeText(getContext(), "Permission to send SMS granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission refusée pour envoyer des SMS
                Toast.makeText(getContext(), "Permission to send SMS not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
package com.example.automateresponseandroidjava.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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
import java.util.List;

public class ContactFragment extends Fragment implements ContactAdapter.OnContactClickListener {
    private OnContactSelectedListener onContactSelectedListener;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<Contact> contacts;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize onContactSelectedListener from the activity
        if (getActivity() instanceof OnContactSelectedListener) {
            onContactSelectedListener = (OnContactSelectedListener) getActivity();
        }
    }


    @Override
    public void onContactClick(Contact contact) {
        Toast.makeText(getContext(), "Contact sélectionné: " + contact.getName() + " Téléphone: " + contact.getPhoneNumber(), Toast.LENGTH_SHORT).show();
        onContactSelected(contact);
    }

    public void onContactSelected(Contact contact) {
        Log.d("ContactsFragment", "Contact sélectionné: " + contact.getName());
        if (onContactSelectedListener != null) {
            onContactSelectedListener.onContactSelected(contact);
        }
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.setSelectedContact(contact);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        recyclerView = view.findViewById(R.id.contactsRecyclerView);
        contacts = getContacts();

        if (contacts != null) {
            contactAdapter = new ContactAdapter(contacts);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(contactAdapter);

            // définir le gestionnaire de clics
            contactAdapter.setOnContactClickListener(this);
        }


        return view;
    }

    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add(new Contact(name, phoneNumber));
            }
            cursor.close();
            // Trier les contacts par nom en ordre alphabétique
            Collections.sort(contacts, new Comparator<Contact>() {
                @Override
                public int compare(Contact c1, Contact c2) {
                    return c1.getName().compareTo(c2.getName());
                }
            });
        } else {
            // Demande de permission pour lire les contacts
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }

        // Demande de permission pour envoyer des SMS
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 2);
        }

        return contacts;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            // Refresh the contacts
            contacts = getContacts();
            contactAdapter.notifyDataSetChanged();
        }

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

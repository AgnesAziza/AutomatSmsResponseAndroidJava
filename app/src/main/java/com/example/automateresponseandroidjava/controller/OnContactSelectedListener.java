package com.example.automateresponseandroidjava.controller;

import com.example.automateresponseandroidjava.model.Contact;

// Interface pour gérer les événements liés à la sélection/désélection des contacts.
public interface OnContactSelectedListener {

    // Méthode appelée lorsqu'un contact est sélectionné.
    void onContactSelected(Contact contact);

    // Méthode appelée lorsqu'un contact est désélectionné.
    void onContactDeselected();

}
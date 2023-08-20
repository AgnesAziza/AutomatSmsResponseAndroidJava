package com.example.automateresponseandroidjava.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.automateresponseandroidjava.model.Contact;

// La classe SharedViewModel sert à partager des données entre différentes parties de l'application
// tout en respectant le cycle de vie des composants Android (comme les activités et les fragments).
public class SharedViewModel extends ViewModel {

    // Contact actuellement sélectionné
    private Contact selectedContact;
    // Réponse automatique actuellement définie
    private String automaticResponse;

    // Définir le contact sélectionné pour ce ViewModel
    public void setSelectedContact(Contact contact) {
        this.selectedContact = contact;
    }

    // Obtenir le contact actuellement sélectionné
    public Contact getSelectedContact() {
        return this.selectedContact;
    }

    // Définir la réponse automatique pour ce ViewModel
    public void setAutomaticResponse(String response) {
            this.automaticResponse = response;
        }

    // Obtenir la réponse automatique actuellement définie
    public String getAutomaticResponse() {
            return this.automaticResponse;
        }}
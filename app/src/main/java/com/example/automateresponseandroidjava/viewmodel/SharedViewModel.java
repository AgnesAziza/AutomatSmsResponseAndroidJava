package com.example.automateresponseandroidjava.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.automateresponseandroidjava.model.Contact;

public class SharedViewModel extends ViewModel {

    private Contact selectedContact;
    private String automaticResponse;

    public void setSelectedContact(Contact contact) {
        this.selectedContact = contact;
    }

    public Contact getSelectedContact() {
        return this.selectedContact;
    }

    public void setAutomaticResponse(String response) {
        this.automaticResponse = response;
        Log.d("SharedViewModel", "setAutomaticResponse called, setting: " + this.automaticResponse);
    }

    public String getAutomaticResponse() {
        Log.d("SharedViewModel", "getAutomaticResponse called, returning: " + this.automaticResponse);
        return this.automaticResponse;
    }
}

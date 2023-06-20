package com.example.automateresponseandroidjava.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.automateresponseandroidjava.model.Contact;
import com.example.automateresponseandroidjava.model.Response;

public class SharedViewModel extends ViewModel {

    private Contact selectedContact;
    private String automaticResponse;



    public Contact getSelectedContact() {
        return selectedContact;
    }

    public void setSelectedContact(Contact selectedContact) {
        this.selectedContact = selectedContact;
    }

    public String getAutomaticResponse() {
        return automaticResponse;
    }

    public void setAutomaticResponse(String automaticResponse) {
        this.automaticResponse = automaticResponse;
    }

}

package com.example.automateresponseandroidjava.model;

import java.util.Objects;

public class Contact {
    private String name;
    private String phoneNumber;
    private boolean isSelected;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    // Méthode pour vérifier l'égalité de deux objets Contact.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return name.equals(contact.name) &&
                phoneNumber.equals(contact.phoneNumber);
    }

    // Génère le code de hachage pour l'objet Contact.
    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber);
    }
}

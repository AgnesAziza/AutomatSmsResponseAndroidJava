package com.example.automateresponseandroidjava.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.adapter.ContactAdapter;
import com.example.automateresponseandroidjava.adapter.ViewPagerAdapter;
import com.example.automateresponseandroidjava.model.Contact;
import com.example.automateresponseandroidjava.viewmodel.SharedViewModel;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener, ResponseFragment.OnSendMessageListener {

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int REQUEST_CODE = 101;  // Choisir un code de demande unique pour ces permissions, REQUEST_CODE est un mécanisme pour savoir quelle demande de permission a été traitée,

        // Vérification des permissions pour recevoir et envoyer des SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Si les permissions ne sont pas accordées, les demander à l'utilisateur
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, REQUEST_CODE);
        }

        // Initialization of ViewPager et du TabLayout
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Initialisation du ViewModel partagé
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Configuration de l'adaptateur pour le ViewPager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Ajout des fragments aux onglets
        viewPagerAdapter.addFragment(new ContactFragment(), "Contacts");
        viewPagerAdapter.addFragment(new ResponseFragment(), "Send SMS");
        viewPagerAdapter.addFragment(new AutomaticRespondFragment(), "Automatic SMS");

        // Association de l'adaptateur au ViewPager
        viewPager.setAdapter(viewPagerAdapter);

        // Association du TabLayout avec le ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    // Quand un contact est cliqué, on le met à jour dans le ViewModel
    public void onContactClick(Contact contact) {
        sharedViewModel.setSelectedContact(contact);
    }

    @Override
    public void onSendMessage(String phoneNumber, String message) {
        // Quand un message doit être envoyé, récupération de la réponse automatique depuis le ViewModel
        sharedViewModel.getAutomaticResponse();
    }
}

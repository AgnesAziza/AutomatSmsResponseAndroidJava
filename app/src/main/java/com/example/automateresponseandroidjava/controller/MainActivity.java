package com.example.automateresponseandroidjava.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.adapter.ContactAdapter;
import com.example.automateresponseandroidjava.adapter.ViewPagerAdapter;
import com.example.automateresponseandroidjava.model.Contact;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements ResponseFragment.OnSendMessageListener,
        ContactAdapter.OnContactClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ResponseFragment responseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        FragmentPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSendMessage(String phoneNumber, String message) {
        // Implémentez le code pour envoyer le message au numéro de téléphone donné
    }

    @Override
    public void onContactClick(Contact contact) {
        if (responseFragment != null) {
            responseFragment.setSelectedContact(contact.getPhoneNumber());
            viewPager.setCurrentItem(2); // Index 2 correspond au fragment de message dans le ViewPagerAdapter
        }
    }

    public void setResponseFragment(ResponseFragment responseFragment) {
        this.responseFragment = responseFragment;
    }
}

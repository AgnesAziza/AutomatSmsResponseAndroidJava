package com.example.automateresponseandroidjava.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

        // Initialization of ViewPager and TabLayout
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Setting up ViewPager adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ContactFragment(), "Contacts");
        viewPagerAdapter.addFragment(new ResponseFragment(), "Send SMS");
        viewPagerAdapter.addFragment(new AutomaticRespondFragment(), "Automatic SMS");

        viewPager.setAdapter(viewPagerAdapter);

        // Setting up TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onContactClick(Contact contact) {
        sharedViewModel.setSelectedContact(contact);
    }

    @Override
    public void onSendMessage(String phoneNumber, String message) {
        sharedViewModel.getAutomaticResponse();
    }
}

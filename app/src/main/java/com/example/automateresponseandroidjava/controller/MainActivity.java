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

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Initialization of ViewPager and TabLayout
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

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
        // Implement your desired behavior when a contact is clicked
        // For example, you could update the selected contact in the shared ViewModel
        sharedViewModel.setSelectedContact(contact);
    }
}

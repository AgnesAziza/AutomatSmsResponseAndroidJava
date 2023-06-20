package com.example.automateresponseandroidjava.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}

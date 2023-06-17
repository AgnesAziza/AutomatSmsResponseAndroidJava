package com.example.automateresponseandroidjava.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.automateresponseandroidjava.R;
import com.example.automateresponseandroidjava.adapter.ViewPagerAdapter;
import com.example.automateresponseandroidjava.controller.AutomaticRespondFragment;
import com.example.automateresponseandroidjava.controller.ContactFragment;
import com.example.automateresponseandroidjava.controller.ResponseFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements ResponseFragment.OnSendMessageListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        // Création de l'adaptateur de fragments
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ContactFragment();
                    case 1:
                        return new ResponseFragment();
                    case 2:
                        return new AutomaticRespondFragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Contacts";
                    case 1:
                        return "Réponses";
                    case 2:
                        return "Réponse automatique";
                    default:
                        return null;
                }
            }
        };

        // Configuration du ViewPager avec l'adaptateur
        viewPager.setAdapter(adapter);

        // Liaison du ViewPager avec le TabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSendMessage(String phoneNumber, String message) {
        // Code pour envoyer le message au numéro de téléphone donné
    }
}

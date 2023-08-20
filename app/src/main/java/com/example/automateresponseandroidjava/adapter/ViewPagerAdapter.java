package com.example.automateresponseandroidjava.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

// Classe qui hérite de FragmentStatePagerAdapter, permettant de gérer un ensemble de Fragments avec un ViewPager classique.
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    // Liste des fragments à afficher dans le ViewPager.
    private final List<Fragment> fragmentList = new ArrayList<>();
    // Liste des titres correspondant à chaque fragment (pour les tabs, par exemple).
    private final List<String> fragmentTitleList = new ArrayList<>();

    // Constructeur qui prend en paramètre un gestionnaire de fragments.
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    // Ajoute un fragment et son titre correspondant à la liste.
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Nullable
    @Override
    // Retourne le titre d'un fragment à une position donnée.
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    // Retourne le fragment correspondant à une position donnée.
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    // Retourne le nombre total de fragments.
    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

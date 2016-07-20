package com.example.shelockapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by shini_000 on 7/14/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> listFragment = new ArrayList<>();
    private final ArrayList<String> listFragmentTitle = new ArrayList<>();

    public ViewPagerAdapter (FragmentManager manager){
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    public void addFragment(Fragment fragment, String title){
        listFragment.add(fragment);
        listFragmentTitle.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return listFragmentTitle.get(position);
    }
}

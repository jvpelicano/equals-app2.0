package com.philcode.equals.PWD;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PWD_AvailableJobs_FragmentAdapter extends FragmentStateAdapter {

    public PWD_AvailableJobs_FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new PWD_AvailableJobOffers_2_Fragment();
            case 2:
                return new PWD_AvailableJobOffers_3_Fragment();
        }
        return new PWD_AvailableJobOffers_1_Fragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

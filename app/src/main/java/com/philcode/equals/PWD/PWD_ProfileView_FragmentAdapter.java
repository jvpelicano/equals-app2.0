package com.philcode.equals.PWD;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PWD_ProfileView_FragmentAdapter extends FragmentStateAdapter {

    public PWD_ProfileView_FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new PWD_WorkExperience_Fragment();
        }
        return new PWD_BasicInfo_Fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

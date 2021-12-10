package com.philcode.equals.EMP;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class EMP_FragmentAdapter extends FragmentStateAdapter {
    public EMP_FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new EMP_PotentialApplicant_WorkExp_Fragment();
        }
        return new EMP_PotentialApplicant_BasicInfo_Fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

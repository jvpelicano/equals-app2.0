package com.philcode.equals.PWD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.philcode.equals.R;

public class PWD_AvailableJobsList_Activity extends AppCompatActivity {

    private TabLayout tabLayout_availableJobs;
    private ViewPager2 viewpager_availableJobs;
    private PWD_AvailableJobs_FragmentAdapter availableJobs_fragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_pwdavailablejobslist_view);

        tabLayout_availableJobs = findViewById(R.id.tabLayout_availableJobs);
        viewpager_availableJobs = findViewById(R.id.viewpager_availableJobs);
        availableJobs_fragmentAdapter = new PWD_AvailableJobs_FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewpager_availableJobs.setAdapter(availableJobs_fragmentAdapter);

        tabLayout_availableJobs.addTab(tabLayout_availableJobs.newTab().setText("100%"));
        tabLayout_availableJobs.addTab(tabLayout_availableJobs.newTab().setText("50%"));
        tabLayout_availableJobs.addTab(tabLayout_availableJobs.newTab().setText("20%"));

        tabLayout_availableJobs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager_availableJobs.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewpager_availableJobs.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout_availableJobs.selectTab(tabLayout_availableJobs.getTabAt(position));
            }
        });
    }
}
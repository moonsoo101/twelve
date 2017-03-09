package com.example.wisebody.twelve.PagerMenu;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.wisebody.twelve.R;
import com.example.wisebody.twelve.User;

public class MainActivity extends AppCompatActivity {


    User loginUser;
    Bundle args;
    int position;
    static int loadcount;
    Thread loadThread;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getIntent = getIntent();
        loginUser = (User) getIntent.getSerializableExtra("loginUser");
        args = new Bundle();
        args.putSerializable("loginUser",loginUser);
        loadcount  = 0;



    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setEnabled(false);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                restFragment(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        View view1 = getLayoutInflater().inflate(R.layout.customtab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_active);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        View view2 = getLayoutInflater().inflate(R.layout.customtab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_limit);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));


        View view3 = getLayoutInflater().inflate(R.layout.customtab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_stardom);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        View view4 = getLayoutInflater().inflate(R.layout.customtab, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_best);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));

        View view5 = getLayoutInflater().inflate(R.layout.customtab, null);
        view5.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab_me);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view5));


        tabLayout.removeTabAt(0);
        tabLayout.removeTabAt(0);
        tabLayout.removeTabAt(0);
        tabLayout.removeTabAt(0);
        tabLayout.removeTabAt(0);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("resume","resume");
        mViewPager.setEnabled(true);
        position = mViewPager.getCurrentItem();
        if(loadThread == null) {
            loadThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (loadcount == 5) {
                            mViewPager.setEnabled(true);
                            position = mViewPager.getCurrentItem();
                            //restFragment(position);
                            loadThread = null;
                            break;
                        }
                    }
                }
            });
            loadThread.start();
        }
    }
    @Override
    public void onPause()
    {
        super.onPause();
      /*  if(loadThread!=null)
            loadThread = null;*/
    }

    protected void restFragment(int curposition)
    {
        ActiveFragment activeFragment = (ActiveFragment) mSectionsPagerAdapter.instantiateItem(mViewPager,0);
        LimitFragment limitFragment = (LimitFragment) mSectionsPagerAdapter.instantiateItem(mViewPager,1);
        StardomFragment stardomFragment = (StardomFragment) mSectionsPagerAdapter.instantiateItem(mViewPager,2);
        BestFragment bestFragment = (BestFragment) mSectionsPagerAdapter.instantiateItem(mViewPager,3);
        MeFragment meFragment = (MeFragment) mSectionsPagerAdapter.instantiateItem(mViewPager,4);
        if(curposition==0)
        {
            activeFragment.shownpage = true;
            limitFragment.shownpage = false;
            bestFragment.shownpage = false;
            meFragment.shownpage = false;
            if(activeFragment.loadData)
            activeFragment.restFragment();
            if(limitFragment.loadData)
                limitFragment.restFragment();
            if(bestFragment.loadData)
                bestFragment.restFragment();
            if(meFragment.loadData)
                meFragment.restFragment();
            activeFragment = null;
            limitFragment = null;
            bestFragment = null;
            stardomFragment = null;
            meFragment = null;
        }
        else if(curposition == 1)
        {
            activeFragment.shownpage = false;
            limitFragment.shownpage = true;
            bestFragment.shownpage = false;
            meFragment.shownpage = false;
            if(activeFragment.loadData)
                activeFragment.restFragment();
            if(limitFragment.loadData)
                limitFragment.restFragment();
            if(bestFragment.loadData)
                bestFragment.restFragment();
            if(meFragment.loadData)
                meFragment.restFragment();
            activeFragment = null;
            limitFragment = null;
            bestFragment = null;
            stardomFragment = null;
            meFragment = null;
        } else if(curposition == 2)
        {
            activeFragment.shownpage = false;
            limitFragment.shownpage = false;
            bestFragment.shownpage = false;
            meFragment.shownpage = false;
            if(activeFragment.loadData)
                activeFragment.restFragment();
            if(limitFragment.loadData)
                limitFragment.restFragment();
            if(bestFragment.loadData)
                bestFragment.restFragment();
            if(meFragment.loadData)
                meFragment.restFragment();
            activeFragment = null;
            limitFragment = null;
            bestFragment = null;
            ((AppCompatActivity)meFragment.getActivity()).setSupportActionBar(null);
            ((AppCompatActivity)stardomFragment.getActivity()).setSupportActionBar(stardomFragment.toolbar);
            ((AppCompatActivity)stardomFragment.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            stardomFragment = null;
            meFragment = null;
        }
        else if(curposition == 3)
        {
            activeFragment.shownpage = false;
            limitFragment.shownpage = false;
            bestFragment.shownpage = true;
            meFragment.shownpage = false;
            if(activeFragment.loadData)
                activeFragment.restFragment();
            if(limitFragment.loadData)
                limitFragment.restFragment();
            if(bestFragment.loadData)
                bestFragment.restFragment();
            if(meFragment.loadData)
                meFragment.restFragment();
            activeFragment = null;
            limitFragment = null;
            bestFragment = null;
            stardomFragment = null;
            meFragment = null;
        }
        else
        {
            activeFragment.shownpage = false;
            limitFragment.shownpage = false;
            bestFragment.shownpage = false;
            meFragment.shownpage = true;
            if(activeFragment.loadData)
                activeFragment.restFragment();
            if(limitFragment.loadData)
                limitFragment.restFragment();
            if(bestFragment.loadData)
                bestFragment.restFragment();
            if(meFragment.loadData)
                meFragment.restFragment();
            activeFragment = null;
            limitFragment = null;
            bestFragment = null;
            ((AppCompatActivity)stardomFragment.getActivity()).setSupportActionBar(null);
            ((AppCompatActivity)meFragment.getActivity()).setSupportActionBar(meFragment.toolbar);
            ((AppCompatActivity)meFragment.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            stardomFragment = null;
            meFragment = null;
        }


    }
    public ViewPager getViewPager() {
        if (null == mViewPager) {
            mViewPager = (ViewPager) findViewById(R.id.container);
        }
        return mViewPager;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("get",Integer.toString(position));
            if(position==0)
            {
                ActiveFragment fragment = new ActiveFragment();
                fragment.setArguments(args);
                return fragment;
            }
            else if (position == 1) {
                LimitFragment fragment = new LimitFragment();
                fragment.setArguments(args);
                return fragment;
            }
            else if (position == 2) {
                StardomFragment fragment = new StardomFragment();
                fragment.setArguments(args);
                return fragment;
            }
            else if (position == 3) {
                BestFragment fragment = new BestFragment();
                fragment.setArguments(args);
                return fragment;
            }
            else  {
                MeFragment fragment = new MeFragment();
                fragment.setArguments(args);
                return fragment;
            }
        }
        @Override
        public int getCount() {

            return 5;
        }
    }
}

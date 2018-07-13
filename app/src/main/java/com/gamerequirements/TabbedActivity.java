package com.gamerequirements;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.gamerequirements.Blog.BlogActivityMain;
import com.gamerequirements.Home.HomeMain;
import com.gamerequirements.Requirements.GameListActivity;

import org.codechimp.apprater.AppRater;

public class TabbedActivity extends AppCompatActivity
{

    Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_document);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_console);

        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(TabbedActivity.this, R.color.colorAccent);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(TabbedActivity.this, R.color.plainWhite);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        AppRater.setDarkTheme();
        AppRater.app_launched(this);
        try
        {
            AutoStartforVariousManufacturers();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public ViewPager getmViewPager()
    {
        return mViewPager;
    }

    Intent getManufacturerIntent()
    {
        Intent intent = new Intent();
        String thisManufacturer = android.os.Build.MANUFACTURER;
        if ("xiaomi".equalsIgnoreCase(thisManufacturer))
        {
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
        } else if ("oppo".equalsIgnoreCase(thisManufacturer))
        {
            intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
        } else if ("vivo".equalsIgnoreCase(thisManufacturer))
        {
            intent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.MainGuideActivity."));
        } else
        {
            intent = null;
        }
        return intent;
    }

    void AutoStartforVariousManufacturers() throws Exception
    {

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean permissionAlreadyAsked = sharedpreferences.getBoolean("autostart", false);
        if (!permissionAlreadyAsked)
        {
            Intent intent = getManufacturerIntent();
            if (intent != null)
            {
                Toast.makeText(this, "Allow autostart permission for GameRequirements", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("autostart", true);
                editor.apply();
                startActivity(intent);
            }
        }

    }

    @Override
    public void onBackPressed()
    {

        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position)
            {
                case 0:
                    fragment = HomeMain.newInstance();
                    break;
                case 1:
                    fragment = BlogActivityMain.newInstance();
                    break;
                case 2:
                    fragment = GameListActivity.newInstance();
                    break;


            }
            return (fragment);
        }

        @Override
        public int getCount()
        {
            // Show 2 total pages.
            return 3;
        }




    }
}


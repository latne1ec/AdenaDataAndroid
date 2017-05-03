package com.adenadata.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.fragment.EventsListFragmentWithStateAdapter;
import com.adenadata.android.fragment.HomeFragment;
import com.adenadata.android.fragment.JobPostingsFragment;
import com.adenadata.android.fragment.NewsListFragmentWithSearchInsideRecyclerViewAndState;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    // We need to show/hide the counter and + button
    TextView mToolbarTextView;
    ImageView mToolbarAddNewImage;

    // For the counter
    ScheduledExecutorService mExecutor;
    Future<?> mFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //--SAVE Data
        SharedPreferences preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        //--READ data
        String hasMigrated = preferences.getString("hasMigrated","");

        if (hasMigrated.equals("yes")) {
            // Continue
        } else {
            ParseUser.logOut();;
            editor.putString("hasMigrated", "yes");
            editor.apply();
        }


        // Redirect to TourActivity the first time the app is opened
        if (ParseUser.getCurrentUser() == null) {
//            Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
            Intent i = new Intent(MainActivity.this, TourActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        // Save Installation for the very first time by subscribing to channel 'global'
        // Note that this code will run only once ever - the first time the app is run
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        List<String> subscribedChannels = installation.getList("channels");
        if (subscribedChannels == null) {
            // The Installation hasn't been saved to Parse yet -> subscribe to channel 'global'
            // so that the Installation gets saved for the very first time
            ParsePush.subscribeInBackground("global");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // We need to set the title empty here because if we do it in the Manifest.xml
        // the app name below the icon disappears
        getSupportActionBar().setTitle("");

        // Create the adapter that will return a fragment for each of the sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Create Executor to schedule counter
        mExecutor = Executors.newSingleThreadScheduledExecutor();

        // We want to show the counter and the + only when the user is looking at tab 'NEWS'. Otherwise hide
        mToolbarTextView = (TextView) toolbar.findViewById(R.id.main_toolbar_text);
        mToolbarAddNewImage = (ImageView) toolbar.findViewById(R.id.main_toolbar_add_new_image);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
//                Log.i(TAG, "Position: " + position);
                if (position == 1) {
                    scheduleExecutor();
                    // Hide Countdown
                    mToolbarTextView.setVisibility(View.INVISIBLE);
                    mToolbarAddNewImage.setVisibility(View.VISIBLE);
                    mToolbarTextView.setTextColor(getResources().getColor(R.color.accent));
                } else {
                    cancelExecutor();
//                    mToolbarTextView.setVisibility(View.INVISIBLE);
                    mToolbarAddNewImage.setVisibility(View.GONE);
                    String toolbarTitle = "";
                    if (position == 0) {
                        toolbarTitle = "Home";
                    } else if (position == 2) {
                        toolbarTitle = "Events";
                    } else if (position == 3) {
                        toolbarTitle = "Jobs";
                    }
                    mToolbarTextView.setText(toolbarTitle);
                    mToolbarTextView.setTextColor(getResources().getColor(R.color.black_87));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // + button
        mToolbarAddNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddNewActivity.class);
                startActivity(i);
            }
        });

        // Set TabLayout to ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        // Show NEWS tab when opening app - http://stackoverflow.com/a/11802484
        mViewPager.setCurrentItem(0);
    }


    private void scheduleExecutor() {
        mFuture = mExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                final String countdown = calculateCountdown();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mToolbarTextView.setText(countdown);
                    }
                });
            }
        }, 0L, 1, TimeUnit.SECONDS);
    }


    // Shows 25:03:22 :(
    /*
    private String calculateCountdownBad() {
        // now GMT/UTC
        Calendar nowGMT = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
//                Log.i(TAG, "nowGMT hour: " + nowGMT.get(Calendar.HOUR_OF_DAY));

        // now Local
        Calendar nowLocal = new GregorianCalendar(TimeZone.getDefault());
//                Log.i(TAG, "nowLocal hour: " + nowLocal.get(Calendar.HOUR_OF_DAY));

        int hoursFromGMT = nowLocal.get(Calendar.HOUR_OF_DAY) - nowGMT.get(Calendar.HOUR_OF_DAY);

        // Countdown
//                int hours = nowEST.get(Calendar.HOUR_OF_DAY) - midnightEST.get(Calendar.HOUR_OF_DAY);
        int hours = 23 + hoursFromGMT + 4 - nowLocal.get(Calendar.HOUR_OF_DAY);
        int minutes = 60 - nowLocal.get(Calendar.MINUTE);
        int seconds = 60 - nowLocal.get(Calendar.SECOND);
        return String.format("%02d:%02d:%02d", hours, minutes,  seconds);
    }
    */


    // midnight NY
//        Calendar midnightNY = new GregorianCalendar(TimeZone.getTimeZone("US/Eastern"));
//        midnightNY.set(Calendar.HOUR_OF_DAY, 0);
//        midnightNY.set(Calendar.MINUTE, 0);
//        midnightNY.set(Calendar.SECOND, 0);
//        midnightNY.add(Calendar.DAY_OF_MONTH, 1);
//        Log.i(TAG, "midnight NY: " + midnightNY.get(Calendar.HOUR_OF_DAY));


    private String calculateCountdown() {
        // Timezone can be "US/Eastern" or "America/New_York"
        // now NY
//        Calendar nowNY = new GregorianCalendar(TimeZone.getTimeZone("US/Eastern"));
        Calendar nowNY = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
//        Log.i(TAG, "now NY: " + nowNY.get(Calendar.HOUR_OF_DAY));

        int hours = 23 - nowNY.get(Calendar.HOUR_OF_DAY);
        int minutes = 59 - nowNY.get(Calendar.MINUTE);
        int seconds = 59 - nowNY.get(Calendar.SECOND);
        return String.format("%02d:%02d:%02d", hours, minutes,  seconds);
    }


    private void cancelExecutor() {
        if (mFuture != null) {
            Log.i(TAG, "Cancel future");
            mFuture.cancel(true);
        }
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    // http://stackoverflow.com/questions/29916428/swiperefreshlayout-getting-stuck-around-recyclerview-inside-a-viewpager
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
//                    return new NewsListFragment();
                    //return new NewsListFragmentWithSearchInsideRecyclerViewAndState();
                    return new JobPostingsFragment();
                case 2:
                    return new EventsListFragmentWithStateAdapter();
                case 3:
                    return new JobPostingsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HOME";
                case 1:
                    return "JOBS";
                case 2:
                    return "EVENTS";
                case 3:
                    return "JOBS";
            }
            return null;
        }
    }

}

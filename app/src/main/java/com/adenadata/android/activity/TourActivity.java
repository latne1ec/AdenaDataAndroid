package com.adenadata.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.fragment.WelcomeFragment;

public class TourActivity extends AppCompatActivity {

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

    ImageView mCircle1;
    ImageView mCircle2;
    ImageView mCircle3;
    ImageView mCircle4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        // Circles
        mCircle1 = (ImageView) findViewById(R.id.tour_circle1);
        mCircle2 = (ImageView) findViewById(R.id.tour_circle2);
//        mCircle3 = (ImageView) findViewById(R.id.tour_circle3);
//        mCircle4 = (ImageView) findViewById(R.id.tour_circle4);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.tour_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mCircle1.setImageResource(R.drawable.circle_on);
                        mCircle2.setImageResource(R.drawable.circle_off);
//                        mCircle3.setImageResource(R.drawable.circle_off);
//                        mCircle4.setImageResource(R.drawable.circle_off);
                        break;
                    case 1:
                        mCircle1.setImageResource(R.drawable.circle_off);
                        mCircle2.setImageResource(R.drawable.circle_on);
//                        mCircle3.setImageResource(R.drawable.circle_off);
//                        mCircle4.setImageResource(R.drawable.circle_off);
                        break;
                    case 2:
                        mCircle1.setImageResource(R.drawable.circle_off);
                        mCircle2.setImageResource(R.drawable.circle_off);
//                        mCircle3.setImageResource(R.drawable.circle_on);
//                        mCircle4.setImageResource(R.drawable.circle_off);
                        break;
                    case 3:
                        mCircle1.setImageResource(R.drawable.circle_off);
                        mCircle2.setImageResource(R.drawable.circle_off);
//                        mCircle3.setImageResource(R.drawable.circle_off);
//                        mCircle4.setImageResource(R.drawable.circle_on);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour, menu);
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 1) {
                return new WelcomeFragment();
            } else {
                return PlaceholderFragment.newInstance(position);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        /*
        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
        */
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private int mSectionNumber = 0;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments() != null) {
                mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            }

            View v = inflater.inflate(R.layout.fragment_tour, container, false);

            String text = "";
            int image = R.drawable.tour1;

            switch (mSectionNumber) {
                case 0:
                   // text = "See what's happening all around you within the next 24 hours!";
                   // image = R.drawable.tour1;
                    text = "Looking for a job where you fit? Let AD Jobs Help!";
                    image = R.drawable.tour3;
                    break;
//                case 1:
//                    text = "Check out some up and coming events.";
//                    image = R.drawable.tour2;
//                    break;
//                case 2:
//                    text = "Looking for a job where you fit? Let AD Jobs Help!";
//                    image = R.drawable.tour3;
//                    break;
            }

            TextView textView = (TextView) v.findViewById(R.id.tour_textView);
            textView.setText(text);

            ImageView imageView = (ImageView) v.findViewById(R.id.tour_imageView);
            imageView.setImageResource(image);

            return v;
        }
    }

}

package com.adenadata.android.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.adapter.JobsAdapter;
import com.adenadata.android.model.Job;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albertvilacalvo on 30/7/15.
 */
public class JobsListActivity extends AppCompatActivity {

    private static final String TAG = "JobsListActivity";

    // Bundle extras - From JobPostingsFragment/Adapter and FiltersActivity
    public static final String EXTRA_TITLE = "com.adenadata.EXTRA_TITLE";
    public static final String EXTRA_LOCATION = "com.adenadata.EXTRA_LOCATION";
    // Bundle extras - From FiltersActivity
    public static final String EXTRA_POSITION_TYPE = "com.adenadata.EXTRA_POSITION_TYPE";
    public static final String EXTRA_EMPLOYMENT_TYPE = "com.adenadata.EXTRA_EMPLOYMENT_TYPE";
    public static final String EXTRA_EDUCATION_REQUIREMENTS = "com.adenadata.EXTRA_EDUCATION_REQUIREMENTS";

    private ArrayList<Job> mJobs = new ArrayList<Job>();
    private JobsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressWheel mProgressWheel;
    private TextView mEmptyListTextView;

    // Bundle extras - From JobPostingsFragment/Adapter and FiltersActivity
    private String mJobTitle;
    private String mJobLocation;
    // Bundle extras - From FiltersActivity
    private String mPositionType;
    private String mEmploymentType;
    private String mEducationRequirements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jobs_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show back button (arrow)
        // Back arrow orange color http://stackoverflow.com/q/26788464/4034572
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Change toolbar title
        TextView titleTextView = (TextView) findViewById(R.id.toolbar_title);
        titleTextView.setText("Search Results");

        // Bundle extras - From JobPostingsFragment/Adapter and FiltersActivity
        mJobTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mJobLocation = getIntent().getStringExtra(EXTRA_LOCATION);
        // Bundle extras - From FiltersActivity
        mPositionType = getIntent().getStringExtra(EXTRA_POSITION_TYPE);
        mEmploymentType = getIntent().getStringExtra(EXTRA_EMPLOYMENT_TYPE);
        mEducationRequirements = getIntent().getStringExtra(EXTRA_EDUCATION_REQUIREMENTS);

        // Set up RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.jobs_list_RecyclerView);
        mProgressWheel = (ProgressWheel) findViewById(R.id.jobs_list_ProgressWheel);
        mEmptyListTextView = (TextView) findViewById(R.id.jobs_list_empty_TextView);

        // Improve performance - https://developer.android.com/training/material/lists-cards.html
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new JobsAdapter(mJobs);
        mRecyclerView.setAdapter(mAdapter);

        query();
    }

    private void query() {
        Log.i(TAG, "JobsListFragment - query()");
        ParseQuery<Job> query = ParseQuery.getQuery(Job.class);
        query.orderByDescending("createdAt");
        if (mJobTitle != null && !mJobTitle.trim().isEmpty()) {
            query.whereMatches("jobTitle", mJobTitle.trim(), "i");
        }
        if (mJobLocation != null && !mJobLocation.trim().isEmpty()) {
            //query.whereMatches("jobLocation", mJobLocation.trim(), "i");
            query.whereEqualTo("jobLocationTagsArray", mJobLocation.trim());
        }
        if (mPositionType != null && !mPositionType.equals("All")) {
            query.whereMatches("positionType", mPositionType, "i");
        }
        if (mEmploymentType != null) {
            query.whereMatches("employmentType", mEmploymentType, "i");
        }
        if (mEducationRequirements != null) {
            query.whereMatches("educationRequirement", mEducationRequirements, "i");
        }
        query.findInBackground(new FindCallback<Job>() {
            @Override
            public void done(List<Job> jobs, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Success query jobs: " + jobs.size() + " elements");
                    if (jobs.size() == 0) {
                        // Show empty list TextView
                        mRecyclerView.setVisibility(View.GONE);
                        mProgressWheel.setVisibility(View.GONE);
                        mEmptyListTextView.setVisibility(View.VISIBLE);
                    } else {
                        // Hide ProgressWheel and empty list TextView
                        mProgressWheel.setVisibility(View.GONE);
                        mEmptyListTextView.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        // Add stuff
                        mJobs.clear();
                        mJobs.addAll(jobs);
                        mAdapter.notifyDataSetChanged();
                    }
                }
//                else {
//                    Log.i(TAG, "Error query jobs: " + e.getMessage());
//                    // Show empty list TextView with text 'Network error'
//                    mEmptyListTextView.setText("Network Error");
//                    mRecyclerView.setVisibility(View.GONE);
//                    mProgressWheel.setVisibility(View.GONE);
//                    mEmptyListLayout.setVisibility(View.VISIBLE);
//                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

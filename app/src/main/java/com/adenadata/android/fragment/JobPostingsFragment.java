package com.adenadata.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adenadata.android.R;
import com.adenadata.android.adapter.JobPostingsAdapter;
import com.adenadata.android.model.Job;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobPostingsFragment extends Fragment {

    private static final String TAG = "JobPostingsFragment";

    private ArrayList<Job> mJobs = new ArrayList<Job>();
    private JobPostingsAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mPullRefreshLayout;


    public JobPostingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_job_postings, container, false);

//        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.job_postings_RecyclerView);

        // Improve performance - https://developer.android.com/training/material/lists-cards.html
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new JobPostingsAdapter(mJobs,getContext());
        mRecyclerView.setAdapter(mAdapter);

        mPullRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.job_postings_pullRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        // If the app is refreshing and you swipe to left and then come back, it could be that
        // the refresh remains there
        mPullRefreshLayout.setRefreshing(false);
        // TODO set color scheme for pull refresh

        query();

        return v;
    }
    

    private void query() {
        Log.i(TAG, "JobPostingsFragment - query()");
        ParseQuery<Job> query = ParseQuery.getQuery(Job.class);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Job>() {
            @Override
            public void done(List<Job> jobs, ParseException e) {
                mPullRefreshLayout.setRefreshing(false);
                if (e == null) {
                    Log.i(TAG, "Success query jobs: " + jobs.size() + " elements");
                    // Add stuff
                    mJobs.clear();
                    mJobs.addAll(jobs);
                    mAdapter.notifyDataSetChanged();
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

}

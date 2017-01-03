package com.adenadata.android.fragment.old;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.adapter.old.EventsAdapter;
import com.adenadata.android.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsListFragment extends Fragment {

    private static final String TAG = "EventsListFragment";

    private ArrayList<Event> mEvents = new ArrayList<Event>();
    private EventsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressWheel mProgressWheel;
    private TextView mEmptyListTextView;



    public EventsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.events_list_RecyclerView);
        mProgressWheel = (ProgressWheel) v.findViewById(R.id.events_list_ProgressWheel);
        mEmptyListTextView = (TextView) v.findViewById(R.id.events_list_empty_TextView);

        // Improve performance - https://developer.android.com/training/material/lists-cards.html
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new EventsAdapter(mEvents);
        mRecyclerView.setAdapter(mAdapter);

        query();

        return v;
    }


    private void query() {
        Log.i(TAG, "EventsListFragment - query()");
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Success query events: " + events.size() + " elements");
                    if (events.size() == 0) {
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
                        mEvents.clear();
                        mEvents.addAll(events);
                        mAdapter.notifyDataSetChanged();
                    }
                }
//                else {
//                    Log.i(TAG, "Error query events: " + e.getMessage());
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

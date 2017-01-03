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
import com.adenadata.android.adapter.EventsAdapterWithState;
import com.adenadata.android.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsListFragmentWithStateAdapter extends Fragment {

    private static final String TAG = "EventsListFragment";

    private ArrayList<Event> mEvents = new ArrayList<Event>();
    private EventsAdapterWithState mAdapter;
    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mPullRefreshLayout;


    public EventsListFragmentWithStateAdapter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events_list_with_state_adapter, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.events_list_RecyclerView);

        // Improve performance - https://developer.android.com/training/material/lists-cards.html
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new EventsAdapterWithState(mEvents, EventsAdapterWithState.State.LOADING);
        mRecyclerView.setAdapter(mAdapter);

        mPullRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.events_list_PullRefreshLayout);
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
        Log.i(TAG, "EventsListFragment - query()");
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                mPullRefreshLayout.setRefreshing(false);
                if (e == null) {
                    Log.i(TAG, "Success query events: " + events.size() + " elements");
                    if (events.size() == 0) {
                        // Show empty list TextView
                        mAdapter.setState(EventsAdapterWithState.State.EMPTY);
                    } else {
                        // Add stuff
                        mEvents.clear();
                        mEvents.addAll(events);
                        // No need to call mAdapter.notifyDataSetChanged(); since it's done in EventsAdapterWithState
                        // Hide ProgressWheel and empty list TextView
                        mAdapter.setState(EventsAdapterWithState.State.LIST);
                    }
                } else {
                    // Error
                    mAdapter.setState(EventsAdapterWithState.State.ERROR);
                }
            }
        });
    }

}

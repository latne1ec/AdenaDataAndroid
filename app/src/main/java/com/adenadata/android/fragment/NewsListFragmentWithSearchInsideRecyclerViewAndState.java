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
import com.adenadata.android.adapter.NewsAdapterWithSearchAndState;
import com.adenadata.android.model.New;
import com.adenadata.android.util.SearchListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragmentWithSearchInsideRecyclerViewAndState extends Fragment implements SearchListener {

    private static final String TAG = "NewsListFragmentSearch";

    private ArrayList<New> mNews = new ArrayList<New>();
    private ArrayList<New> mNewsBackup = new ArrayList<New>();
    private NewsAdapterWithSearchAndState mAdapter;
    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mPullRefreshLayout;


    public NewsListFragmentWithSearchInsideRecyclerViewAndState() {
        // Required empty public constructor
    }


    // HEADER SEARCH BUTTON INTERFACE
    // ******************************
    @Override
    public void performSearch(String searchText) {
//        query(searchText);
        filter(searchText);
    }

    // undoSearch() is not used
    @Override
    public void undoSearch() {
//        query("");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_list_with_search_inside_recyclerview_and_state, container, false);

//        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.news_list_RecyclerView);

        // Improve performance - https://developer.android.com/training/material/lists-cards.html
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new NewsAdapterWithSearchAndState(mNews, this, NewsAdapterWithSearchAndState.State.LOADING);
        mRecyclerView.setAdapter(mAdapter);

        mPullRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.news_list_PullRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query("");
            }
        });
        // If the app is refreshing and you swipe to left and then come back, it could be that
        // the refresh remains there
        mPullRefreshLayout.setRefreshing(false);
        // TODO set color scheme for pull refresh

        query("");

        return v;
    }


    private void query(String searchText) {
        Log.i(TAG, "NewsListFragmentWithSearchInsideRecyclerView - query()");
        ParseQuery<New> query = ParseQuery.getQuery(New.class);
        if (searchText.trim().length() > 0) {
            query.whereContains("searchedTitle", searchText);
        } else {
            query.orderByDescending("createdAt");
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);
            query.whereGreaterThan("createdAt", yesterday.getTime());
        }
        query.findInBackground(new FindCallback<New>() {
            @Override
            public void done(List<New> news, ParseException e) {
                mPullRefreshLayout.setRefreshing(false);
                if (e == null) {
                    Log.i(TAG, "Success query news: " + news.size() + " elements");
                    // Add stuff
                    mNews.clear();
                    mNews.addAll(news);
                    mNewsBackup.clear();
                    mNewsBackup.addAll(news);
                    // No need to call mAdapter.notifyDataSetChanged(); since it's done in EventsAdapterWithState
                    // Hide ProgressWheel and empty list TextView
                    if (news.size() == 0) {
                        mAdapter.setState(NewsAdapterWithSearchAndState.State.EMPTY);
                    } else {
                        mAdapter.setState(NewsAdapterWithSearchAndState.State.LIST);
                    }
                } else {
                    // Error
                    mAdapter.setState(NewsAdapterWithSearchAndState.State.ERROR);
                }
            }
        });
    }


    private void filter(String searchText) {
        if (searchText.trim().length() > 0) {
            // Filter mNews
            mNews.clear();
            for (int i = 0; i < mNewsBackup.size(); i++) {
                String searchedTitle = mNewsBackup.get(i).getSearchedTitle();
                if (searchedTitle != null && searchedTitle.contains(searchText)) {
                    mNews.add(mNewsBackup.get(i));
                }
            }
            mAdapter.setState(NewsAdapterWithSearchAndState.State.LIST);
        } else {
            // Show all items again
            // We use mNewsBackup to fill mNews again with all items
            mNews.clear();
            mNews.addAll(mNewsBackup);
            mAdapter.setState(NewsAdapterWithSearchAndState.State.LIST);
        }
    }


    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_new:
                Intent i = new Intent(getActivity(), AddNewActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}

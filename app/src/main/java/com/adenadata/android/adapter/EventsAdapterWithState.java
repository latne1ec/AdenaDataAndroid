package com.adenadata.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.activity.EventActivity;
import com.adenadata.android.model.Event;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by albertvilacalvo on 29/7/15.
 */
public class EventsAdapterWithState extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "EventsAdapterWithState";

    private static final int TYPE_LIST = 0;
    private static final int TYPE_LOADING = 1;
    private static final int TYPE_EMPTY = 2;
    private static final int TYPE_ERROR = 3;

    public enum State {
        LIST, LOADING, EMPTY, ERROR
    }

    ArrayList<Event> mEvents;
    State mState;

    Integer mHeight;


    public EventsAdapterWithState(ArrayList<Event> events, State state) {
        mEvents = events;
        mState = state;
    }


    public void setState(State state) {
        mState = state;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (mState == State.LIST) {
            return mEvents.size();
        } else {
            return 1;
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (mState) {
            case LIST:
                return TYPE_LIST;
            case LOADING:
                return TYPE_LOADING;
            case EMPTY:
                return TYPE_EMPTY;
            case ERROR:
                return TYPE_ERROR;
            default:
                return TYPE_EMPTY;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeight == null) {
            mHeight = parent.getMeasuredHeight();
        }
        if (viewType == TYPE_LIST) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent, false);
            return new ViewHolder(v);
        } else if (viewType == TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_loading, parent, false);
            return new ViewHolderLoading(v);
        } else {
            // TYPE_EMPTY and TYPE_ERROR
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_text, parent, false);
            return new ViewHolderText(v);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (mState == State.LIST) {
            ViewHolder vh = (ViewHolder) viewHolder;
            final Event event = mEvents.get(position);
            vh.title.setText(event.getTitle());
            vh.date.setText(event.getDate());
            String url = event.getImageUrl();
            if (url != null && !url.isEmpty()) {
                Glide.with(vh.image.getContext()).load(url).centerCrop().crossFade().into(vh.image);
            }
            vh.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EventActivity.class);
                    intent.putExtra(EventActivity.EXTRA_OBJECT_ID, event.getObjectId());
                    intent.putExtra(EventActivity.EXTRA_TITLE, event.getTitle());
                    intent.putExtra(EventActivity.EXTRA_DATE, event.getDate());
                    intent.putExtra(EventActivity.EXTRA_URL, event.getUrl());
                    intent.putExtra(EventActivity.EXTRA_DESCRIPTION, event.getDescription());
                    intent.putExtra(EventActivity.EXTRA_IMAGE_URL, event.getImageUrl());
                    context.startActivity(intent);
                }
            });
        } else if (mState == State.LOADING) {
            ViewHolderLoading vh = (ViewHolderLoading) viewHolder;
            vh.frame.setMinimumHeight(mHeight);
        } else if (mState == State.EMPTY) {
            ViewHolderText vh = (ViewHolderText) viewHolder;
            vh.frame.setMinimumHeight(mHeight);
            vh.text.setText("No events now.\nCheck back soon!");
        } else if (mState == State.ERROR) {
            ViewHolderText vh = (ViewHolderText) viewHolder;
            vh.frame.setMinimumHeight(mHeight);
            vh.text.setText("An error occurred.\nPlease try again.");
        }
    }


    // TYPE_LIST
    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title;
        TextView date;
        ImageView image;

        public ViewHolder(View v) {
            super(v);

            view = v;
            title = (TextView) v.findViewById(R.id.list_item_event_title);
            date = (TextView) v.findViewById(R.id.list_item_event_date);
            image = (ImageView) v.findViewById(R.id.list_item_event_image);
        }
    }


    // TYPE_LOADING
    static class ViewHolderLoading extends RecyclerView.ViewHolder {
        FrameLayout frame;
        public ViewHolderLoading(View v) {
            super(v);
            frame = (FrameLayout) v.findViewById(R.id.recyclerview_frame);
        }
    }


    // TYPE_EMPTY and TYPE_ERROR
    static class ViewHolderText extends RecyclerView.ViewHolder {

        FrameLayout frame;
        TextView text;

        public ViewHolderText(View v) {
            super(v);

            frame = (FrameLayout) v.findViewById(R.id.recyclerview_frame);
            text = (TextView) v.findViewById(R.id.recyclerview_text);
        }
    }

}


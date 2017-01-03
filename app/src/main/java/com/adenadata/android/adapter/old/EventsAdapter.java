package com.adenadata.android.adapter.old;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private static final String TAG = "EventsAdapter";

    ArrayList<Event> mEvents;

    public EventsAdapter(ArrayList<Event> events) {
        mEvents = events;
    }


    @Override
    public int getItemCount() {
        return mEvents.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
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
    }


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

}


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
import com.adenadata.android.activity.NewActivity;
import com.adenadata.android.model.New;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by albertvilacalvo on 29/7/15.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final String TAG = "NewsAdapter";

    ArrayList<New> mNews;

    public NewsAdapter(ArrayList<New> news) {
        mNews = news;
    }


    @Override
    public int getItemCount() {
        return mNews.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        final New theNew = mNews.get(position);
        vh.title.setText(theNew.getTitle());
        vh.location.setText(theNew.getLocation());
        String url = theNew.getImageUrl();
        if (url != null && !url.isEmpty()) {
            Glide.with(vh.image.getContext()).load(url).centerCrop().placeholder(R.drawable.ad_thumbnail).crossFade().into(vh.image);
        }
        vh.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, NewActivity.class);
                intent.putExtra(NewActivity.EXTRA_OBJECT_ID, theNew.getObjectId());
                intent.putExtra(NewActivity.EXTRA_TITLE, theNew.getTitle());
                intent.putExtra(NewActivity.EXTRA_LOCATION, theNew.getLocation());
                intent.putExtra(NewActivity.EXTRA_TEXT, theNew.getText());
                intent.putExtra(NewActivity.EXTRA_DATE, theNew.getDateString());
                intent.putExtra(NewActivity.EXTRA_IMAGE_URL, theNew.getImageUrl());
                context.startActivity(intent);
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title;
        TextView location;
        ImageView image;

        public ViewHolder(View v) {
            super(v);

            view = v;
            title = (TextView) v.findViewById(R.id.list_item_new_title);
            location = (TextView) v.findViewById(R.id.list_item_new_location);
            image = (ImageView) v.findViewById(R.id.list_item_new_image);
        }
    }

}


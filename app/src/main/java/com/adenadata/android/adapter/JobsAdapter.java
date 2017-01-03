package com.adenadata.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.activity.JobActivity;
import com.adenadata.android.model.Job;

import java.util.ArrayList;

/**
 * Created by albertvilacalvo on 29/7/15.
 */
public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private static final String TAG = "JobsAdapter";

    ArrayList<Job> mJobs;

    public JobsAdapter(ArrayList<Job> jobs) {
        mJobs = jobs;
    }


    @Override
    public int getItemCount() {
        return mJobs.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_job, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        final Job job = mJobs.get(position);
        vh.title.setText(job.getTitle());
        vh.location.setText(job.getCompany() + " - " + job.getLocation());
        vh.date.setText(job.getDateString());
        vh.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, JobActivity.class);
                intent.putExtra(JobActivity.EXTRA_OBJECT_ID, job.getObjectId());
                intent.putExtra(JobActivity.EXTRA_TITLE, job.getTitle());
                intent.putExtra(JobActivity.EXTRA_COMPANY, job.getCompany());
                intent.putExtra(JobActivity.EXTRA_LOCATION, job.getLocation());
                intent.putExtra(JobActivity.EXTRA_POSITION, job.getPositionDetails());
                intent.putExtra(JobActivity.EXTRA_REQUIREMENTS, job.getRequirements());
                intent.putExtra(JobActivity.EXTRA_LINK, job.getLink());
                intent.putExtra(JobActivity.EXTRA_DATE, job.getDateString());
                context.startActivity(intent);
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title;
        TextView location;
        TextView date;

        public ViewHolder(View v) {
            super(v);

            view = v;
            title = (TextView) v.findViewById(R.id.list_item_job_title);
            location = (TextView) v.findViewById(R.id.list_item_job_location);
            date = (TextView) v.findViewById(R.id.list_item_job_date);
        }
    }

}


package com.adenadata.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.activity.FiltersActivity;
import com.adenadata.android.activity.JobActivity;
import com.adenadata.android.activity.JobsListActivity;
import com.adenadata.android.model.Job;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.util.ArrayList;

/**
 * Created by albertvilacalvo on 29/7/15.
 */
public class JobPostingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "JobPostingsAdapter";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_JOB = 1;
    Context context;

    ArrayList<Job> mJobs;

    public JobPostingsAdapter(ArrayList<Job> jobs, Context context) {
        mJobs = jobs;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return mJobs.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_JOB;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_postings_header, parent, false);
            return new ViewHolderHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_job, parent, false);
            return new ViewHolderJob(v);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            // TYPE_HEADER
            final ViewHolderHeader vhh = (ViewHolderHeader) viewHolder;
            vhh.filtersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent i = new Intent(context, FiltersActivity.class);
                    context.startActivity(i);
                }
            });
            vhh.searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent i = new Intent(context, JobsListActivity.class);
                    String title = vhh.jobTitleEditText.getText().toString().trim();
                    i.putExtra(JobsListActivity.EXTRA_TITLE, title);
                    String location = vhh.jobLocationEditText.getText().toString().trim();
                    i.putExtra(JobsListActivity.EXTRA_LOCATION, location);
                    context.startActivity(i);
                }
            });
            // Search with keyboard return - http://stackoverflow.com/a/3205405/4034572
            // THIS CODE MUST BE THE SAME THAT THE searchButton ABOVE

            vhh.jobLocationEditText.setOnPlaceSelectedListener(
                    new OnPlaceSelectedListener() {
                        @Override
                        public void onPlaceSelected(final Place place) {
                            // do something awesome with the selected place

                            vhh.jobLocationEditText.getDetailsFor(place, new DetailsCallback() {
                                @Override
                                public void onSuccess(final PlaceDetails details) {

                                    String city = "";
                                    String state = "";

                                    for (AddressComponent component : details.address_components) {
                                        for (AddressComponentType type : component.types) {
                                            switch (type) {
                                                case LOCALITY:
                                                    city = component.long_name;
                                                    break;
                                                case ADMINISTRATIVE_AREA_LEVEL_1:
                                                    state = component.short_name;
                                                    break;
                                            }
                                        }
                                    }

                                    Intent i = new Intent(context, JobsListActivity.class);
                                    String title = vhh.jobTitleEditText.getText().toString().trim();
                                    i.putExtra(JobsListActivity.EXTRA_TITLE, title);
                                    String location = !city.isEmpty() ? city : state;
                                    i.putExtra(JobsListActivity.EXTRA_LOCATION, location);
                                    context.startActivity(i);
                                }

                                @Override
                                public void onFailure(Throwable throwable) {

                                }
                            });
                        }
                    }
            );
        } else {
            // TYPE_JOB
            final Job job = mJobs.get(position - 1);
            ViewHolderJob vh = (ViewHolderJob) viewHolder;
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
    }


    static class ViewHolderHeader extends RecyclerView.ViewHolder {

        View view;
        Button filtersButton;
        Button searchButton;
        EditText jobTitleEditText;
        PlacesAutocompleteTextView jobLocationEditText;

        public ViewHolderHeader(View v) {
            super(v);

            view = v;
            filtersButton = (Button) v.findViewById(R.id.job_postings_header_filters_button);
            searchButton = (Button) v.findViewById(R.id.job_postings_header_search_button);
            jobTitleEditText = (EditText) v.findViewById(R.id.job_postings_header_title);
            jobLocationEditText = (PlacesAutocompleteTextView) v.findViewById(R.id.job_postings_header_location);
        }
    }


    static class ViewHolderJob extends RecyclerView.ViewHolder {

        View view;
        TextView title;
        TextView location;
        TextView date;

        public ViewHolderJob(View v) {
            super(v);

            view = v;
            title = (TextView) v.findViewById(R.id.list_item_job_title);
            location = (TextView) v.findViewById(R.id.list_item_job_location);
            date = (TextView) v.findViewById(R.id.list_item_job_date);
        }
    }

}


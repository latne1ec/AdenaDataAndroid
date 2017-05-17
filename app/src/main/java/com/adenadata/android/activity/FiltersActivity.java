package com.adenadata.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adenadata.android.R;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

/**
 * Created by albertvilacalvo on 30/7/15.
 */
public class FiltersActivity extends AppCompatActivity {

    private static final String TAG = "FiltersActivity";

    private CharSequence[] mPositionItems = {"All", "Job", "Internship", "Volunteer"};
    private CharSequence[] mEmploymentItems = {"Temporary", "Per-Diem", "Part-Time", "Full-Time"};
    private CharSequence[] mEducationItems = {"No requirement", "High School", "2-Year Degree", "4-Year Degree", "Masters Degree", "J.D.", "M.D.", "Doctorate"};

    private Button mPositionButton;
    private Button mEmploymentButton;
    private Button mEducationButton;

    EditText jobTitleEditText;
    PlacesAutocompleteTextView jobLocationEditText;

    private int mSelectedPosition = -1;
    private int mSelectedEmployment = -1;
    private int mSelectedEducation = -1;

    private String locationPlace = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

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
        titleTextView.setText("Search Jobs");

        // Position Type
        mPositionButton = (Button) findViewById(R.id.filters_position_button);
        mPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FiltersActivity.this);
                builder.setTitle("Position Type");
                builder.setSingleChoiceItems(mPositionItems, mSelectedPosition, null);
                builder.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSelectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        mPositionButton.setText(mPositionItems[mSelectedPosition]);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.show();
            }
        });

        // Employment Type
        mEmploymentButton = (Button) findViewById(R.id.filters_employment_button);
        mEmploymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FiltersActivity.this);
                builder.setTitle("Employment Type");
                builder.setSingleChoiceItems(mEmploymentItems, mSelectedEmployment, null);
                builder.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSelectedEmployment = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        mEmploymentButton.setText(mEmploymentItems[mSelectedEmployment]);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.show();
            }
        });

        // Employment Type
        mEducationButton = (Button) findViewById(R.id.filters_education_button);
        mEducationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FiltersActivity.this);
                builder.setTitle("Education Requirements");
                builder.setSingleChoiceItems(mEducationItems, mSelectedEducation, null);
                builder.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSelectedEducation = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        mEducationButton.setText(mEducationItems[mSelectedEducation]);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                builder.show();
            }
        });

        // With Material Dialog lib - https://github.com/afollestad/material-dialogs
        // There is a bug with radio button colors - https://github.com/afollestad/material-dialogs/issues/564
        /*
        mPositionButton = (Button) findViewById(R.id.filters_position_button);
        mPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(FiltersActivity.this)
                        .title("Position Type")
                        .positiveText("SELECT")
                        .negativeText("CANCEL")
                        .items(mPositionItems)
                        .itemsCallbackSingleChoice(mSelectedPosition, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {
                                if (i >= 0) {
                                    mPositionButton.setText(text);
                                    mSelectedPosition = i;
                                }
                                Log.i(TAG, "i: " + i);
                                return true;
                            }
                        })
                        .show();

            }
        });
        */

        jobTitleEditText = (EditText) findViewById(R.id.filters_title);
        jobLocationEditText = (PlacesAutocompleteTextView) findViewById(R.id.filters_location);

        jobLocationEditText.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        // do something awesome with the selected place

                        jobLocationEditText.getDetailsFor(place, new DetailsCallback() {
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

                                locationPlace = !city.isEmpty() ? city : state;
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    }
                }
        );

        findViewById(R.id.filters_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FiltersActivity.this, JobsListActivity.class);
                String title = jobTitleEditText.getText().toString().trim();
                i.putExtra(JobsListActivity.EXTRA_TITLE, title);

                String location = "";
                if(locationPlace.isEmpty())
                    location = jobLocationEditText.getText().toString().trim();
                else if (!jobLocationEditText.getText().toString().isEmpty())
                    location = locationPlace;

                i.putExtra(JobsListActivity.EXTRA_LOCATION, location);

                if (mSelectedPosition >= 0) {
                    i.putExtra(JobsListActivity.EXTRA_POSITION_TYPE, mPositionItems[mSelectedPosition]);
                }
                if (mSelectedEmployment >= 0) {
                    i.putExtra(JobsListActivity.EXTRA_EMPLOYMENT_TYPE, mEmploymentItems[mSelectedEmployment]);
                }
                if (mSelectedEducation >= 0) {
                    i.putExtra(JobsListActivity.EXTRA_EDUCATION_REQUIREMENTS, mEducationItems[mSelectedEducation]);
                }
                startActivity(i);
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

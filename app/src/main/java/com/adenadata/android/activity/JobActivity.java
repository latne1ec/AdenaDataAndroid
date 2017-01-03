package com.adenadata.android.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.adenadata.android.R;

public class JobActivity extends AppCompatActivity {

    private static final String TAG = "JobActivity";

    public static final String EXTRA_OBJECT_ID = "com.adenadata.EXTRA_OBJECT_ID";
    public static final String EXTRA_TITLE = "com.adenadata.EXTRA_TITLE";
    public static final String EXTRA_COMPANY = "com.adenadata.EXTRA_COMPANY";
    public static final String EXTRA_LOCATION = "com.adenadata.EXTRA_LOCATION";
    public static final String EXTRA_POSITION = "com.adenadata.EXTRA_POSITION";
    public static final String EXTRA_REQUIREMENTS = "com.adenadata.EXTRA_REQUIREMENTS";
    public static final String EXTRA_LINK = "com.adenadata.EXTRA_LINK";
    public static final String EXTRA_DATE = "com.adenadata.EXTRA_DATE";

    private String mShareSubject = "";
    private String mShareText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show back button (arrow)
        // Back arrow orange color http://stackoverflow.com/q/26788464/4034572
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String jobObjectId = getIntent().getStringExtra(EXTRA_OBJECT_ID);
        String jobTitle = getIntent().getStringExtra(EXTRA_TITLE);
        String jobCompany = getIntent().getStringExtra(EXTRA_COMPANY);
        String jobLocation = getIntent().getStringExtra(EXTRA_LOCATION);
        String jobPosition = getIntent().getStringExtra(EXTRA_POSITION);
        String jobRequirements = getIntent().getStringExtra(EXTRA_REQUIREMENTS);
        String jobLink = getIntent().getStringExtra(EXTRA_LINK);
//        String jobDate = getIntent().getStringExtra(EXTRA_DATE);

        if (jobTitle != null) {
            mShareSubject = "Adena Data Job - " + jobTitle;
            mShareText = "Adena Data Job - " + jobTitle + "\n\n";
            TextView titleTextView = (TextView) findViewById(R.id.toolbar_title);
            titleTextView.setText(jobTitle);
        }
        if (jobCompany != null) {
            mShareText = mShareText + jobCompany + "\n\n";
            TextView companyTextView = (TextView) findViewById(R.id.job_company);
            companyTextView.setText(jobCompany);
        }
        if (jobLocation != null) {
//            mShareText = mShareText + jobLocation + "\n\n";
            TextView locationTextView = (TextView) findViewById(R.id.job_location);
            locationTextView.setText(jobLocation);
        }
        if (jobPosition != null) {
//            mShareText = mShareText + jobPosition + "\n\n";
            TextView positionTextView = (TextView) findViewById(R.id.job_position);
            positionTextView.setText(jobPosition);
        }
        if (jobRequirements != null) {
//            mShareText = mShareText + jobRequirements + "\n\n";
            TextView requirementsTextView = (TextView) findViewById(R.id.job_requirements);
            requirementsTextView.setText(jobRequirements);
        }
        if (jobLink != null) {
            mShareText = mShareText + jobLink + "\n\n";
            TextView linkTextView = (TextView) findViewById(R.id.job_link);
            linkTextView.setText(jobLink);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                // http://developer.android.com/training/sharing/send.html
                // http://stackoverflow.com/a/17168698/4034572
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, mShareSubject);
                i.putExtra(Intent.EXTRA_TEXT, mShareText);
                startActivity(Intent.createChooser(i, "Share via"));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

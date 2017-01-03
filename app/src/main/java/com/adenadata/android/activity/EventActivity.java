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
import android.widget.ImageView;
import android.widget.TextView;

import com.adenadata.android.R;
import com.bumptech.glide.Glide;

public class EventActivity extends AppCompatActivity {

    private static final String TAG = "EventActivity";

    public static final String EXTRA_OBJECT_ID = "com.adenadata.EXTRA_OBJECT_ID";
    public static final String EXTRA_TITLE = "com.adenadata.EXTRA_TITLE";
    public static final String EXTRA_DATE = "com.adenadata.EXTRA_DATE";
    public static final String EXTRA_URL = "com.adenadata.EXTRA_URL";
    public static final String EXTRA_DESCRIPTION = "com.adenadata.EXTRA_DESCRIPTION";
    public static final String EXTRA_IMAGE_URL = "com.adenadata.EXTRA_IMAGE_URL";

    private String mShareSubject = "";
    private String mShareText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show back button (arrow)
        // Back arrow orange color http://stackoverflow.com/q/26788464/4034572
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String eventObjectId = getIntent().getStringExtra(EXTRA_OBJECT_ID);
        String eventTitle = getIntent().getStringExtra(EXTRA_TITLE);
        String eventDate = getIntent().getStringExtra(EXTRA_DATE);
        String eventUrl = getIntent().getStringExtra(EXTRA_URL);
        String eventDescription = getIntent().getStringExtra(EXTRA_DESCRIPTION);
        String eventImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);

        if (eventTitle != null) {
            mShareSubject = "Adena Data - " + eventTitle;
            mShareText = "Adena Data - " + eventTitle + "\n\n";
            TextView titleTextView = (TextView) findViewById(R.id.toolbar_title);
            titleTextView.setText(eventTitle);
        }
        if (eventDate != null) {
            mShareText = mShareText + eventDate + "\n\n";
            TextView dateTextView = (TextView) findViewById(R.id.event_date);
            dateTextView.setText(eventDate);
        }
        if (eventUrl != null) {
            mShareText = mShareText + eventUrl + "\n\n";
            TextView urlTextView = (TextView) findViewById(R.id.event_url);
            urlTextView.setText(eventUrl);
        }
        if (eventDescription != null) {
            mShareText = mShareText + eventDescription + "\n\n";
            TextView descriptionTextView = (TextView) findViewById(R.id.event_description);
            descriptionTextView.setText(eventDescription);
        }
        if (eventImageUrl != null && !eventImageUrl.isEmpty()) {
            ImageView imageView = (ImageView) findViewById(R.id.event_image);
            Glide.with(EventActivity.this).load(eventImageUrl).centerCrop().crossFade().into(imageView);
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

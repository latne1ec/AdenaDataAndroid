package com.adenadata.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adenadata.android.R;
import com.adenadata.android.model.New;
import com.bumptech.glide.Glide;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NewActivity extends AppCompatActivity {

    private static final String TAG = "NewActivity";

    private static final String FLAGGED_POSTS_KEY = "flaggedPosts";

    public static final String EXTRA_OBJECT_ID = "com.adenadata.EXTRA_OBJECT_ID";
    public static final String EXTRA_TITLE = "com.adenadata.EXTRA_TITLE";
    public static final String EXTRA_LOCATION = "com.adenadata.EXTRA_LOCATION";
    public static final String EXTRA_TEXT = "com.adenadata.EXTRA_TEXT";
    public static final String EXTRA_DATE = "com.adenadata.EXTRA_DATE";
    public static final String EXTRA_IMAGE_URL = "com.adenadata.EXTRA_IMAGE_URL";
    public static final String EXTRA_FLAG_COUNT = "com.adenadata.EXTRA_FLAG_COUNT";

    private String mShareSubject = "";
    private String mShareText = "";

    private TextView mFlagPostTextView;

    // From Bundle extras
    private String mObjectId;
    private int mFlagCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show back button (arrow)
        // Back arrow orange color http://stackoverflow.com/q/26788464/4034572
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mObjectId = getIntent().getStringExtra(EXTRA_OBJECT_ID);
        String newTitle = getIntent().getStringExtra(EXTRA_TITLE);
        String newLocation = getIntent().getStringExtra(EXTRA_LOCATION);
        String newText = getIntent().getStringExtra(EXTRA_TEXT);
        String newDate = getIntent().getStringExtra(EXTRA_DATE);
        String newImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        mFlagCount = getIntent().getIntExtra(EXTRA_FLAG_COUNT, 0);

        if (newTitle != null) {
//            mShareSubject = "Adena Data - " + newTitle;
//            mShareText = "Adena Data - " + newTitle + "\n\n";
            mShareSubject = newTitle + " at ";
            mShareText = newTitle + " at ";
            TextView titleTextView = (TextView) findViewById(R.id.toolbar_title);
            titleTextView.setText(newTitle);
        }
        if (newLocation != null) {
//            mShareText = mShareText + newLocation + "\n\n";
            mShareSubject = mShareText + newLocation + ": https://google.com/#q=" + newLocation.replace(" ", "-");
            mShareText = mShareText + newLocation + ": https://google.com/#q=" + newLocation.replace(" ", "-");
            TextView locationTextView = (TextView) findViewById(R.id.new_location);
            locationTextView.setText(newLocation);
            // Open https://www.google.com/#q=
            final Uri uri = Uri.parse("https://www.google.com/#q=" + Uri.encode(newLocation));
            locationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                }
            });

        }
        if (newText != null) {
//            mShareText = mShareText + newText + "\n\n";
            TextView urlTextView = (TextView) findViewById(R.id.new_text);
            urlTextView.setText(newText);
        }
        if (newDate != null) {
            // 2 days ago
            TextView dateTextView = (TextView) findViewById(R.id.new_date);
            dateTextView.setText(newDate);
        }
        ;
        if (newImageUrl != null && !newImageUrl.isEmpty()) {
            ImageView imageView = (ImageView) findViewById(R.id.new_image);
            Glide.with(NewActivity.this).load(newImageUrl).centerCrop().crossFade().into(imageView);
        }

        // Flag Post - onClick
        mFlagPostTextView = (TextView) findViewById(R.id.new_flag_post_text);
        mFlagPostTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagPostClicked();
            }
        });

        // Flag Post - change text if already flagged
        if (isPostFlagged()) {
            mFlagPostTextView.setText("Flagged");
        }
    }


    private boolean isPostFlagged() {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> flaggedPosts = p.getStringSet(FLAGGED_POSTS_KEY, new HashSet<String>());
        return flaggedPosts.contains(mObjectId);
    }


    private void flagPostClicked() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            Log.i(TAG, "ParseUser.getCurrentUser() is null");
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // We must create a new HashSet!
        Set<String> flaggedPosts = new HashSet<> (prefs.getStringSet(FLAGGED_POSTS_KEY, new HashSet<String>()));

        if (flaggedPosts.contains(mObjectId)) {
            Toast.makeText(this, "Already Flagged", Toast.LENGTH_SHORT).show();
        } else {
            mFlagPostTextView.setText("Flagged");
            // Save to Shared Prefs
            flaggedPosts.add(mObjectId);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet(FLAGGED_POSTS_KEY, flaggedPosts);
            editor.apply();
            // Add objectId to user Array
//            user.addUnique(FLAGGED_POSTS_KEY, mObjectId);
            user.put(FLAGGED_POSTS_KEY, new ArrayList<String>(flaggedPosts));
            user.saveEventually();
            // Increment post flag count
            ParseObject aNew = ParseObject.createWithoutData(New.class, mObjectId);
            aNew.increment("flagCount");
            aNew.saveEventually();
            mFlagCount++;
        }

        // Delete if flagged 5 times
        if (mFlagCount >= 5) {
            ParseObject aNew = ParseObject.createWithoutData(New.class, mObjectId);
            aNew.deleteEventually();
            Log.i(TAG, "deleted post");
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

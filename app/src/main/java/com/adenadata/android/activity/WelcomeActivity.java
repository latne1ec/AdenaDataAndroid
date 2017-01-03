package com.adenadata.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adenadata.android.R;
import com.parse.ParseUser;

/**
 * Created by albertvilacalvo on 28/7/15.
 */
public class WelcomeActivity extends AppCompatActivity {

    // NOT USED ANYMORE - SEE WelcomeFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        Button continueButton = (Button) findViewById(R.id.welcome_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // After calling enableAutomaticUser(), ParseUser.getCurrentUser() will always have a value - https://parse.com/docs/android/api/
                ParseUser.enableAutomaticUser();
                ParseUser.getCurrentUser().saveInBackground();
                // Go back to MainActivity
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        TextView termsTextView = (TextView) findViewById(R.id.welcome_terms_text);
        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.adenadata.com/terms.html"));
                startActivity(i);
            }
        });
    }
}

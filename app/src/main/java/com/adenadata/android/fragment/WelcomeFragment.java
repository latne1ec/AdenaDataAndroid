package com.adenadata.android.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.activity.MainActivity;
import com.parse.ParseUser;

/**
 * Created by albertvilacalvo on 9/8/15.
 */
public class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_welcome, container, false);

        Button continueButton = (Button) v.findViewById(R.id.welcome_continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // After calling enableAutomaticUser(), ParseUser.getCurrentUser() will always have a value - https://parse.com/docs/android/api/
                ParseUser.enableAutomaticUser();
                ParseUser.getCurrentUser().saveInBackground();
                // Go back to MainActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        TextView termsTextView = (TextView) v.findViewById(R.id.welcome_terms_text);
        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.adenadata.com/terms.html"));
                startActivity(i);
            }
        });

        return v;
    }
}

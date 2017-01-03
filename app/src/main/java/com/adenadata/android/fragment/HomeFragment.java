package com.adenadata.android.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adenadata.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView webButton = (ImageView) v.findViewById(R.id.home_web_image);
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.adenadata.com/"));
                startActivity(i);
            }
        });

        ImageView twitterButton = (ImageView) v.findViewById(R.id.home_twitter_image);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/AdenaDataLLC"));
                startActivity(i);
            }
        });

        ImageView facebookButton = (ImageView) v.findViewById(R.id.home_facebook_image);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/LikeAdenaData"));
                startActivity(i);
            }
        });

        return v;
    }


}

package com.adenadata.android.util;

import android.app.Application;

import com.adenadata.android.model.Event;
import com.adenadata.android.model.Job;
import com.adenadata.android.model.New;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by albertvilacalvo on 28/7/15.
 */
public class App extends Application {

//    public static final String FAVORITES_KEY = "favMerchants";

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
//        Parse.enableLocalDatastore(this);

        // Must call before calling Parse.initialize()
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(New.class);
        ParseObject.registerSubclass(Job.class);

        //Parse.initialize(this, "fmyKfeKVisMjd17R32lojagYFn7NQcpYy7z9Oaqz", "FIYdqi1M0bmswOvDyMGtUwdCjaeMThJcklHeOeBl");

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fmyKfeKVisMjd17R32lojagYFn7NQcpYy7z9Oaqz")
                .server("https://adenadata.herokuapp.com/api/")
                .build()
        );

    }
}

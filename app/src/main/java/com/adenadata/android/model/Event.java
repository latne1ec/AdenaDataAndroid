package com.adenadata.android.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by albertvilacalvo on 29/7/15.
 */
@ParseClassName("Events")
public class Event extends ParseObject {

    public Event() {
        // https://parse.com/docs/android_guide#subclasses
        // Ensure that your subclass has a public default (i.e. zero-argument) constructor.
        // You must not modify any ParseObject fields in this constructor.
    }


    public String getTitle() {
        return getString("eventName");
    }

    public String getDate() {
        return getString("eventDate");
    }

    public String getUrl() {
        return getString("eventUrl");
    }

    public String getDescription() {
        return getString("eventDescription");
    }

    // returns null if there is no image!
    public String getImageUrl() {
//        ParseFile img = (ParseFile) get("eventImage");
//        if (img != null) {
//            return img.getUrl();
//        } else {
//            return null;
//        }
        return getString("eventImageUrl");

    }

}


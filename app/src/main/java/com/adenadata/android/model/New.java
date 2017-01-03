package com.adenadata.android.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by albertvilacalvo on 29/7/15.
 */
@ParseClassName("Posts")
public class New extends ParseObject {

    public New() {
        // https://parse.com/docs/android_guide#subclasses
        // Ensure that your subclass has a public default (i.e. zero-argument) constructor.
        // You must not modify any ParseObject fields in this constructor.
    }


    public String getTitle() {
        return getString("postTitle");
    }

    public String getSearchedTitle() {
        return getString("searchedTitle");
    }

    public String getLocation() {
        return getString("postLocation");
    }

    public String getText() {
        return getString("postText");
    }

    public String getDateString() {
        // Use createdAt to return "5 hours ago", "2 days ago" etc
        // http://stackoverflow.com/questions/11045526/how-to-get-number-of-days-between-two-dates-in-java
        Date now = new Date();
        int diff = (int) ((now.getTime() - getCreatedAt().getTime()) / (1000 * 60)); // diff is minutes
        if (diff < 60) {
            return diff + " minutes ago";
        }
        diff = diff / 60; // diff is hours
        if (diff < 24) {
            return diff + " hours ago";
        }
        diff = diff / 24; // diff is days
        return diff + " days ago";
    }

    public String getUrl() {
        return getString("eventUrl");
    }

    public int getFlagCount() {
        return getInt("flagCount");
    }

    // returns null if there is no image!
    public String getImageUrl() {
        ParseFile img = (ParseFile) get("postImage");
        if (img != null) {
            return img.getUrl();
        } else {
            return null;
        }
    }

}


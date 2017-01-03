package com.adenadata.android.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by albertvilacalvo on 29/7/15.
 */
@ParseClassName("Jobs")
public class Job extends ParseObject {

    public Job() {
        // https://parse.com/docs/android_guide#subclasses
        // Ensure that your subclass has a public default (i.e. zero-argument) constructor.
        // You must not modify any ParseObject fields in this constructor.
    }


    public String getTitle() {
        return getString("jobTitle");
    }

    public String getCompany() {
        return getString("jobEmployer");
    }

    public String getLocation() {
        return getString("jobLocation");
    }

    public String getPositionDetails() {
        return getString("positionDetails");
    }

    public String getRequirements() {
        return getString("jobRequirements");
    }

    public String getLink() {
        return getString("jobLink");
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

}


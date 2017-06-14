package com.example.calendarevents1;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Андрей on 12.06.2017.
 */

public class MyCalendarEvent implements Serializable, Comparable {

    private String mCalendarID;
    private String mTitle;
    private String mDescription;
    private long mDateStartInMillis;
    private long mDateEndInMillis;
    private long mID;
    private int mHours;
    private int mMinutes;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getHours() {
        return mHours;
    }

    public void setHours(int hours) {
        mHours = hours;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public void setMinutes(int minutes) {
        mMinutes = minutes;
    }

    public long getID() {
        return mID;
    }

    public void setID(long ID) {
        mID = ID;
    }

    public String getCalendarID() {
        return mCalendarID;
    }

    public void setCalendarID(String calendarID) {
        mCalendarID = calendarID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getDateStartInMillis() {
        return mDateStartInMillis;
    }

    public void setDateStartInMillis(long dateStartInMillis) {
        mDateStartInMillis = dateStartInMillis;
    }

    public long getDateEndInMillis() {
        return mDateEndInMillis;
    }

    public void setDateEndInMillis(long dateEndInMillis) {
        mDateEndInMillis = dateEndInMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyCalendarEvent that = (MyCalendarEvent) o;

        if (mDateStartInMillis != that.mDateStartInMillis) return false;
        if (mDateEndInMillis != that.mDateEndInMillis) return false;
        if (mCalendarID != null ? !mCalendarID.equals(that.mCalendarID) : that.mCalendarID != null)
            return false;
        return mTitle != null ? mTitle.equals(that.mTitle) : that.mTitle == null;

    }

    @Override
    public int hashCode() {
        int result = mCalendarID != null ? mCalendarID.hashCode() : 0;
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (int) (mDateStartInMillis ^ (mDateStartInMillis >>> 32));
        result = 31 * result + (int) (mDateEndInMillis ^ (mDateEndInMillis >>> 32));
        return result;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        MyCalendarEvent secondEvent = (MyCalendarEvent) o;
        return (int) ((int) this.getDateStartInMillis() - secondEvent.getDateStartInMillis());
    }
}

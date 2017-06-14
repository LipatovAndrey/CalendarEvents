package com.example.calendarevents1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 12.06.2017.
 */

public class EventManager {
    private static final String EVENT_TIMEZONE = "Moscow";
    private static final int REQUEST_WRITE_CODE = 35;
    private static final int REQUEST_READ_CODE = 26;
    private static final String TAG = "EventManager";
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final String mID;

    public EventManager(Context context, String id) {

        mContext = context;
        mContentResolver = mContext.getContentResolver();
        mID = id;

    }

    public List<MyCalendarEvent> readEvents(long startTimeOfDayInMillis, long endTimeOfDayInMillis) {
        List<MyCalendarEvent> myCalendarEvents = new ArrayList<>();
        Cursor eventCursor = getEventCursor(startTimeOfDayInMillis, endTimeOfDayInMillis);

        if (eventCursor != null) {
            eventCursor.moveToFirst();
            while (!eventCursor.isAfterLast()) {

                myCalendarEvents.add(getMyCalendarEventFromCursor(eventCursor, startTimeOfDayInMillis));
                eventCursor.moveToNext();
            }

            eventCursor.close();
        } else {
            Log.e(TAG, "cursor is null");
        }

        return myCalendarEvents;
    }

    private Cursor getEventCursor(long startTime, long endTime) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_READ_CODE);
            return null;
        }
        String selection = "((" + CalendarContract.Events.DTSTART + " > ?) AND (" + CalendarContract.Events.DTEND + " <= ?) )";
        String[] selectionArgs = new String[]{
                String.valueOf(startTime), String.valueOf(endTime)
        };
        Cursor cursor = mContentResolver.query(CalendarContract.Events.CONTENT_URI, null, selection, selectionArgs, null);

        return cursor;
    }

    private MyCalendarEvent getMyCalendarEventFromCursor(Cursor eventCursor, long mStartOfDayMillis) {

        String eventTitle = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.TITLE));
        String eventDescription = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
        long startCalendarEventInMillis = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTSTART));
        long endInMillis = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTEND));
        long id = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events._ID));

        MyCalendarEvent myCalendarEvent = new MyCalendarEvent();
        myCalendarEvent.setTitle(eventTitle);
        myCalendarEvent.setDescription(eventDescription);
        myCalendarEvent.setDateStartInMillis(startCalendarEventInMillis);
        myCalendarEvent.setID(id);
        myCalendarEvent.setHours((int) ((startCalendarEventInMillis - mStartOfDayMillis) / 3600000) % 24);
        myCalendarEvent.setMinutes((int) ((startCalendarEventInMillis - mStartOfDayMillis) / 60000) % 60);
        myCalendarEvent.setDateEndInMillis(endInMillis);

        return myCalendarEvent;
    }

    public void insertEvent(String titleOfEvent, String description, long startTimeOfEventMillis, long endTimeOfEventMillis) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_WRITE_CODE);
            return;
        }

        Uri contentUri = CalendarContract.Events.CONTENT_URI;

        mContentResolver.insert(contentUri, createContentValuesForEvent(startTimeOfEventMillis, endTimeOfEventMillis, titleOfEvent, description));
    }

    private ContentValues createContentValuesForEvent(long startOfEventMillis, long endOfEventMillis, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startOfEventMillis);
        values.put(CalendarContract.Events.DTEND, endOfEventMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, mID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, EVENT_TIMEZONE);
        return values;
    }

    public void deleteEvent(MyCalendarEvent myCalendarEvent) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_WRITE_CODE);
            return;
        }
        Uri contentUri = CalendarContract.Events.CONTENT_URI;
        String selection = "(" + CalendarContract.Events._ID + " = ? )";
        String[] selectionArgs = new String[]{String.valueOf(myCalendarEvent.getID())};

        mContentResolver.delete(contentUri, selection, selectionArgs);

    }

    public void updateEvent(String title, String description, long startMillis, MyCalendarEvent myCalendarEvent) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_WRITE_CODE);
            return;
        }

        Uri contentUri = CalendarContract.Events.CONTENT_URI;
        String selection = "(" + CalendarContract.Events._ID + " = ? )";
        String[] selectionArgs = new String[]{String.valueOf(myCalendarEvent.getID())};

        mContentResolver.update(contentUri, createContentValuesForEvent(startMillis, myCalendarEvent.getDateEndInMillis(), title, description), selection, selectionArgs);
    }

}

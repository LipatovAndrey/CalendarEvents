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

/**
 * Created by Андрей on 12.06.2017.
 */

public class CalendarManager {
    private static final String CALENDAR_NAME = "calendar";
    private static final int REQUEST_READ_CALENDAR = 4;
    private static final int REQUEST_WRITE_CALENDAR = 5;
    private final Context mContext;
    private final ContentResolver mContentResolver;
    private String mID;

    public CalendarManager(Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();
        if (!hasCalendar()) {
            createNewCalendar(mContentResolver);
            hasCalendar();
        }
    }

    public String getID() {
        return mID;
    }

    private boolean hasCalendar() {
        boolean hasCalendar = false;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_READ_CALENDAR);
            return false;
        }

        Cursor cursor = mContentResolver.query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mID = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                cursor.moveToNext();
            }

            if (mID != null) {
                hasCalendar = true;
            }
            cursor.close();
        } else {
            Log.e("cursor", "null");
        }
        return hasCalendar;
    }

    private void createNewCalendar(ContentResolver contentResolver) {

        Uri CalendarUri = CalendarContract.Calendars.CONTENT_URI;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_WRITE_CALENDAR);
            return;
        }
        contentResolver.insert(CalendarUri, createCalendarContentValues());
    }

    private ContentValues createCalendarContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.NAME, CALENDAR_NAME);
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
        return contentValues;
    }

}

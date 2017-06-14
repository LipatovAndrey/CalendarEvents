package com.example.calendarevents1;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Андрей on 13.06.2017.
 */

public class CalendarEventLoader extends AsyncTaskLoader<List<MyCalendarEvent>> {

    private final CalendarEventsObserver mCalendarEventsObserver;
    private long mStartOfDayMillis;
    private long mEndOfDayMillis;
    private EventManager eventManager;


    public CalendarEventLoader(Context context, long startOfDayMillis, long endOfDayMillis, String calId) {
        super(context);
        mStartOfDayMillis = startOfDayMillis;
        mEndOfDayMillis = endOfDayMillis;
        mCalendarEventsObserver = new CalendarEventsObserver();
        context.getContentResolver().registerContentObserver(
                CalendarContract.Events.CONTENT_URI,
                false, mCalendarEventsObserver
        );
        eventManager = new EventManager(context, calId);

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        getContext().getContentResolver().unregisterContentObserver(mCalendarEventsObserver);
    }

    @Override
    public List<MyCalendarEvent> loadInBackground() {
        List<MyCalendarEvent> myCalendarEvents = eventManager.readEvents(mStartOfDayMillis, mEndOfDayMillis);
        return myCalendarEvents;
    }

    private class CalendarEventsObserver extends ContentObserver {

        public CalendarEventsObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            onContentChanged();
        }
    }
}

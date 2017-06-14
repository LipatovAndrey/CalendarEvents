package com.example.calendarevents1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String START_TIME = "start";
    private static final String END_TIME = "end_time";
    private static final String CAL_ID = "id";
    private static final String DIALOG_ADD = "AddEvent";
    private static final String DIALOG_UPDATE = "UpdateEvent";
    private static final String EVENT = "myCalendarEvent";
    private static final int LOADER_ID = 1;

    public EventManager mEventManager;
    private CalendarView mCalendarView;
    private ListView mEventsListView;
    private Button mAddButton;
    private long mStartOfDayInMillis, mEndOfDayInMillis;
    private String mCalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Calendar calendar = Calendar.getInstance();

        mStartOfDayInMillis = calendar.getTimeInMillis();
        mEndOfDayInMillis = mStartOfDayInMillis + 86400000;

        CalendarManager calendarManager = new CalendarManager(this);
        mCalID = calendarManager.getID();
        mEventManager = new EventManager(this, mCalID);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mEventsListView = (ListView) findViewById(R.id.eventsListView);

        mCalendarView.setDate(mStartOfDayInMillis);
        mAddButton = (Button) findViewById(R.id.addButton);
        getSupportLoaderManager().initLoader(LOADER_ID, null, new CalendarEventLoaderCallbacks());

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertEventDialogFragment dialogFragment = new InsertEventDialogFragment();
                Bundle args = new Bundle();

                args.putLong(START_TIME, mStartOfDayInMillis);
                args.putLong(END_TIME, mEndOfDayInMillis);
                args.putString(CAL_ID, mCalID);

                dialogFragment.setArguments(args);
                dialogFragment.show(getFragmentManager(), DIALOG_ADD);
            }
        });
        mEventsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final MyCalendarEvent myCalendarEvent = (MyCalendarEvent) parent.getItemAtPosition(position);
                myCalendarEvent.setCalendarID(mCalID);
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());

                builder.setNegativeButton(R.string.long_click_dialog_negative_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mEventManager.deleteEvent(myCalendarEvent);
                    }
                });

                builder.setPositiveButton(R.string.long_click_dialog_positive_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        UpdateEventDialogFragment dialogFragment = new UpdateEventDialogFragment();
                        Bundle args = new Bundle();
                        args.putSerializable(EVENT, myCalendarEvent);
                        args.putLong(START_TIME, mStartOfDayInMillis);
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getFragmentManager(), DIALOG_UPDATE);
                    }
                });

                builder.setTitle(R.string.long_click_dialog_title);
                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Date date = new Date(year, month, dayOfMonth);
                Date date1 = new Date(year, month, dayOfMonth + 1);

                mStartOfDayInMillis = date.getTime();
                mEndOfDayInMillis = date1.getTime();

                getSupportLoaderManager().restartLoader(LOADER_ID, null, new CalendarEventLoaderCallbacks());
            }
        });
    }


    private class CalendarEventLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<MyCalendarEvent>> {

        @Override
        public Loader<List<MyCalendarEvent>> onCreateLoader(int id, Bundle args) {
            return new CalendarEventLoader(MainActivity.this, mStartOfDayInMillis, mEndOfDayInMillis, mCalID);
        }

        @Override
        public void onLoadFinished(Loader<List<MyCalendarEvent>> loader, List<MyCalendarEvent> data) {
            mEventsListView.setAdapter(new MyCalendarListEventsAdapter(data));
        }

        @Override
        public void onLoaderReset(Loader<List<MyCalendarEvent>> loader) {

        }
    }


}

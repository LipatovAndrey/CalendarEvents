package com.example.calendarevents1;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by Андрей on 13.06.2017.
 */

public class UpdateEventDialogFragment extends DialogFragment {
    private static final String EVENT = "myCalendarEvent";
    private static final String START_TIME = "start";
    private Context mContext;
    private long mDateInMillis;
    private String mID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final MyCalendarEvent myCalendarEvent = (MyCalendarEvent) getArguments().getSerializable(EVENT);
        mDateInMillis = getArguments().getLong(START_TIME);
        mID = myCalendarEvent.getCalendarID();
        mContext = getActivity();

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.change_event, null, false);

        final EditText titleEditText = (EditText) dialogView.findViewById(R.id.changedialogTitleOfEvent);
        final EditText descriptionEditText = (EditText) dialogView.findViewById(R.id.changeDialogDescriptionOfEvent);
        Button changeEventButton = (Button) dialogView.findViewById(R.id.changeDialogButtonChange);
        Button cancelEventButton = (Button) dialogView.findViewById(R.id.changeDialogButtonCancel);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.changeDialogTimePicker);

        titleEditText.setText(myCalendarEvent.getTitle());
        descriptionEditText.setText(myCalendarEvent.getDescription());
        timePicker.setIs24HourView(true);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        changeEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                int hour = timePicker.getCurrentHour();
                int minutes = timePicker.getCurrentMinute();
                long startTime = mDateInMillis + (hour * 60 * 60 * 1000) + (minutes * 60 * 1000);

                EventManager eventManager = new EventManager(mContext, mID);
                eventManager.updateEvent(title, description, startTime, myCalendarEvent);
                alertDialog.dismiss();
            }
        });
        cancelEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        return alertDialog;
    }

}

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

public class InsertEventDialogFragment extends DialogFragment {

    private static final String START_TIME = "start";
    private static final String END_TIME = "end_time";
    private static final String CAL_ID = "id";

    private Context mContext;
    private long mStartOfDayInMillis;
    private long mEndOfDayInMillis;
    private String mID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mStartOfDayInMillis = getArguments().getLong(START_TIME);
        mEndOfDayInMillis = getArguments().getLong(END_TIME);
        mID = getArguments().getString(CAL_ID);
        mContext = getActivity();

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.add_event, null, false);

        final EditText titleEditText = (EditText) dialogView.findViewById(R.id.dialogTitleOFEvent);
        final EditText descriptionEditText = (EditText) dialogView.findViewById(R.id.dialogDescriptionOfEvent);
        Button addEventButton = (Button) dialogView.findViewById(R.id.dialogButtonAdd);
        Button cancelEventButton = (Button) dialogView.findViewById(R.id.dialogButtonCancel);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.dialogtimePicker);
        timePicker.setIs24HourView(true);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                int hour = timePicker.getCurrentHour();
                int minutes = timePicker.getCurrentMinute();
                long startTime = mStartOfDayInMillis + (hour * 60 * 60 * 1000) + (minutes * 60 * 1000);

                EventManager eventManager = new EventManager(mContext, mID);
                eventManager.insertEvent(title, description, startTime, mEndOfDayInMillis);
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

package com.example.calendarevents1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Андрей on 12.06.2017.
 */

public class MyCalendarListEventsAdapter extends BaseAdapter {
    private List<MyCalendarEvent> mMyCalendarEventList;

    public MyCalendarListEventsAdapter(List<MyCalendarEvent> myCalendarEventList) {
        mMyCalendarEventList = new ArrayList<>();
        mMyCalendarEventList.addAll(myCalendarEventList);
        Collections.sort(mMyCalendarEventList);
        
    }

    @Override
    public int getCount() {
        return mMyCalendarEventList.size();
    }

    @Override
    public MyCalendarEvent getItem(int position) {
        return mMyCalendarEventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMyCalendarEventList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            ViewHolder viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
            viewHolder.mTitleTextView = (TextView) convertView.findViewById(R.id.eventTitle);
            viewHolder.mDescriptionView = (TextView) convertView.findViewById(R.id.eventDescription);
            viewHolder.mTimeStart = (TextView) convertView.findViewById(R.id.timeStart);
            convertView.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.mTitleTextView.setText(getItem(position).getTitle());
        viewHolder.mDescriptionView.setText(getItem(position).getDescription());
        viewHolder.mTimeStart.setText(parent.getContext().getString(R.string.date_formatter, getItem(position).getHours(), getItem(position).getMinutes()));
        return convertView;
    }

    private class ViewHolder {
        TextView mTitleTextView;
        TextView mDescriptionView;
        TextView mTimeStart;
    }
}

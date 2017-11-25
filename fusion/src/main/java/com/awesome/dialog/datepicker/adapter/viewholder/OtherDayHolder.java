package com.awesome.dialog.datepicker.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.awesome.R;
import com.awesome.dialog.datepicker.model.Day;
import com.awesome.dialog.datepicker.view.CalendarView;


public class OtherDayHolder extends BaseDayHolder {

    public OtherDayHolder(View itemView, CalendarView calendarView) {
        super(itemView, calendarView);
        tvDay = (TextView) itemView.findViewById(R.id.tv_day_number);
    }

    public void bind(Day day) {
        tvDay.setText(String.valueOf(day.getDayNumber()));
        tvDay.setTextColor(calendarView.getOtherDayTextColor());
    }
}

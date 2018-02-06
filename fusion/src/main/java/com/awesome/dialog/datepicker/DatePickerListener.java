package com.awesome.dialog.datepicker;

import android.view.View;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ncode on 6/2/18.
 */

public interface DatePickerListener {
    void onDatePicked(View v, List<Calendar> dates);
}

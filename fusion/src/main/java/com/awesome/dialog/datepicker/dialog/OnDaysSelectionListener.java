package com.awesome.dialog.datepicker.dialog;


import com.awesome.dialog.datepicker.model.Day;

import java.util.List;

public interface OnDaysSelectionListener {
    void onDaysSelected(List<Day> selectedDays);
}

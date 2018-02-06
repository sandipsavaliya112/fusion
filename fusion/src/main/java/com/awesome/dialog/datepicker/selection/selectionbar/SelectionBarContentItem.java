package com.awesome.dialog.datepicker.selection.selectionbar;


import com.awesome.dialog.datepicker.model.Day;

public class SelectionBarContentItem implements SelectionBarItem {

    private Day day;

    public SelectionBarContentItem(Day day) {
        this.day = day;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}


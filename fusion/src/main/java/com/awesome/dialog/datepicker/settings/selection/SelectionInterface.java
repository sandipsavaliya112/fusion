package com.awesome.dialog.datepicker.settings.selection;


import com.awesome.dialog.datepicker.utils.SelectionType;

public interface SelectionInterface {

    @SelectionType
    int getSelectionType();

    void setSelectionType(@SelectionType int selectionType);
}

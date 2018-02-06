package com.awesome.dialog.datepicker.settings.selection;


import com.awesome.dialog.datepicker.utils.SelectionType;

public class SelectionModel implements SelectionInterface {

    //Selecton type SINGLE, MULTIPLE, RANGE, NONE
    @SelectionType
    private int selectionType;

    @Override
    @SelectionType
    public int getSelectionType() {
        return selectionType;
    }

    @Override
    public void setSelectionType(@SelectionType int selectionType) {
        this.selectionType = selectionType;
    }
}

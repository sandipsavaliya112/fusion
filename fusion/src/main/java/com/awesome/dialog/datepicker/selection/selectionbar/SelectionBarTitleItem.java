package com.awesome.dialog.datepicker.selection.selectionbar;

public class SelectionBarTitleItem implements SelectionBarItem {

    private String title;

    public SelectionBarTitleItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

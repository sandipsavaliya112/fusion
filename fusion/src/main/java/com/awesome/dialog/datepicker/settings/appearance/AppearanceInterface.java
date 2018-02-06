package com.awesome.dialog.datepicker.settings.appearance;

public interface AppearanceInterface {

    int getCalendarBackgroundColor();

    void setCalendarBackgroundColor(int calendarBackgroundColor);

    int getMonthTextColor();

    void setMonthTextColor(int monthTextColor);

    int getOtherDayTextColor();

    void setOtherDayTextColor(int otherDayTextColor);

    int getDayTextColor();

    void setDayTextColor(int dayTextColor);

    int getWeekendDayTextColor();

    void setWeekendDayTextColor(int weekendDayTextColor);

    int getWeekDayTitleTextColor();

    void setWeekDayTitleTextColor(int weekDayTitleTextColor);

    int getSelectedDayTextColor();

    void setSelectedDayTextColor(int selectedDayTextColor);

    int getSelectedDayBackgroundColor();

    void setSelectedDayBackgroundColor(int selectedDayBackgroundColor);

    int getSelectedDayBackgroundStartColor();

    void setSelectedDayBackgroundStartColor(int selectedDayBackgroundStartColor);

    int getSelectedDayBackgroundEndColor();

    void setSelectedDayBackgroundEndColor(int selectedDayBackgroundEndColor);

    int getCurrentDayTextColor();

    void setCurrentDayTextColor(int currentDayTextColor);

    int getCurrentDayIconRes();

    void setCurrentDayIconRes(int currentDayIconRes);

    int getCurrentDaySelectedIconRes();

    void setCurrentDaySelectedIconRes(int currentDaySelectedIconRes);

    int getCalendarOrientation();

    void setCalendarOrientation(int calendarOrientation);

    int getConnectedDayIconRes();

    void setConnectedDayIconRes(int connectedDayIconRes);

    int getConnectedDaySelectedIconRes();

    void setConnectedDaySelectedIconRes(int connectedDaySelectedIconRes);

    int getConnectedDayIconPosition();

    void setConnectedDayIconPosition(int connectedDayIconPosition);

    int getDisabledDayTextColor();

    void setDisabledDayTextColor(int disabledDayTextColor);

    int getSelectionBarMonthTextColor();

    void setSelectionBarMonthTextColor(int selectionBarMonthTextColor);

    int getPreviousMonthIconRes();

    void setPreviousMonthIconRes(int previousMonthIconRes);

    int getNextMonthIconRes();

    void setNextMonthIconRes(int nextMonthIconRes);

    boolean isShowDaysOfWeek();

    void setShowDaysOfWeek(boolean showDaysOfWeek);

    boolean isShowDaysOfWeekTitle();

    void setShowDaysOfWeekTitle(boolean showDaysOfWeekTitle);
}

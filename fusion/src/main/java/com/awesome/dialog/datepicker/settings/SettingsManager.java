package com.awesome.dialog.datepicker.settings;



import android.support.v7.widget.LinearLayoutManager;

import com.awesome.dialog.datepicker.settings.appearance.AppearanceInterface;
import com.awesome.dialog.datepicker.settings.appearance.AppearanceModel;
import com.awesome.dialog.datepicker.settings.appearance.ConnectedDayIconPosition;
import com.awesome.dialog.datepicker.settings.date.DateInterface;
import com.awesome.dialog.datepicker.settings.date.DateModel;
import com.awesome.dialog.datepicker.settings.lists.CalendarListsInterface;
import com.awesome.dialog.datepicker.settings.lists.CalendarListsModel;
import com.awesome.dialog.datepicker.settings.lists.DisabledDaysCriteria;
import com.awesome.dialog.datepicker.settings.lists.connected_days.ConnectedDays;
import com.awesome.dialog.datepicker.settings.lists.connected_days.ConnectedDaysManager;
import com.awesome.dialog.datepicker.settings.selection.SelectionInterface;
import com.awesome.dialog.datepicker.settings.selection.SelectionModel;
import com.awesome.dialog.datepicker.utils.SelectionType;

import java.util.Calendar;
import java.util.Set;

public class SettingsManager implements AppearanceInterface, DateInterface, CalendarListsInterface, SelectionInterface {

    //Default values
    public static final int DEFAULT_MONTH_COUNT = 20;
    public static final int DEFAULT_SELECTION_TYPE = SelectionType.SINGLE;
    public static final int DEFAULT_FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    public static final int DEFAULT_ORIENTATION = LinearLayoutManager.VERTICAL;
    public static final int DEFAULT_CONNECTED_DAY_ICON_POSITION = ConnectedDayIconPosition.BOTTOM;

    //Models
    private AppearanceModel appearanceModel;
    private DateModel dateModel;
    private CalendarListsModel calendarListsModel;
    private SelectionModel selectionModel;

    public SettingsManager() {
        appearanceModel = new AppearanceModel();
        dateModel = new DateModel();
        calendarListsModel = new CalendarListsModel();
        selectionModel = new SelectionModel();
    }

    @Override
    @SelectionType
    public int getSelectionType() {
        return selectionModel.getSelectionType();
    }

    @Override
    public void setSelectionType(@SelectionType int selectionType) {
        selectionModel.setSelectionType(selectionType);
    }

    @Override
    public int getCalendarBackgroundColor() {
        return appearanceModel.getCalendarBackgroundColor();
    }

    @Override
    public void setCalendarBackgroundColor(int calendarBackgroundColor) {
        appearanceModel.setCalendarBackgroundColor(calendarBackgroundColor);
    }

    @Override
    public int getMonthTextColor() {
        return appearanceModel.getMonthTextColor();
    }

    @Override
    public void setMonthTextColor(int monthTextColor) {
        appearanceModel.setMonthTextColor(monthTextColor);
    }

    @Override
    public int getOtherDayTextColor() {
        return appearanceModel.getOtherDayTextColor();
    }

    @Override
    public void setOtherDayTextColor(int otherDayTextColor) {
        appearanceModel.setOtherDayTextColor(otherDayTextColor);
    }

    @Override
    public int getDayTextColor() {
        return appearanceModel.getDayTextColor();
    }

    @Override
    public void setDayTextColor(int dayTextColor) {
        appearanceModel.setDayTextColor(dayTextColor);
    }

    @Override
    public int getWeekendDayTextColor() {
        return appearanceModel.getWeekendDayTextColor();
    }

    @Override
    public void setWeekendDayTextColor(int weekendDayTextColor) {
        appearanceModel.setWeekendDayTextColor(weekendDayTextColor);
    }

    @Override
    public int getWeekDayTitleTextColor() {
        return appearanceModel.getWeekDayTitleTextColor();
    }

    @Override
    public void setWeekDayTitleTextColor(int weekDayTitleTextColor) {
        appearanceModel.setWeekDayTitleTextColor(weekDayTitleTextColor);
    }

    @Override
    public int getSelectedDayTextColor() {
        return appearanceModel.getSelectedDayTextColor();
    }

    @Override
    public void setSelectedDayTextColor(int selectedDayTextColor) {
        appearanceModel.setSelectedDayTextColor(selectedDayTextColor);
    }

    @Override
    public int getSelectedDayBackgroundColor() {
        return appearanceModel.getSelectedDayBackgroundColor();
    }

    @Override
    public void setSelectedDayBackgroundColor(int selectedDayBackgroundColor) {
        appearanceModel.setSelectedDayBackgroundColor(selectedDayBackgroundColor);
    }

    @Override
    public int getSelectedDayBackgroundStartColor() {
        return appearanceModel.getSelectedDayBackgroundStartColor();
    }

    @Override
    public void setSelectedDayBackgroundStartColor(int selectedDayBackgroundStartColor) {
        appearanceModel.setSelectedDayBackgroundStartColor(selectedDayBackgroundStartColor);
    }

    @Override
    public int getSelectedDayBackgroundEndColor() {
        return appearanceModel.getSelectedDayBackgroundEndColor();
    }

    @Override
    public void setSelectedDayBackgroundEndColor(int selectedDayBackgroundEndColor) {
        appearanceModel.setSelectedDayBackgroundEndColor(selectedDayBackgroundEndColor);
    }

    @Override
    public int getCurrentDayTextColor() {
        return appearanceModel.getCurrentDayTextColor();
    }

    @Override
    public void setCurrentDayTextColor(int currentDayTextColor) {
        appearanceModel.setCurrentDayTextColor(currentDayTextColor);
    }

    @Override
    public int getCurrentDayIconRes() {
        return appearanceModel.getCurrentDayIconRes();
    }

    @Override
    public void setCurrentDayIconRes(int currentDayIconRes) {
        appearanceModel.setCurrentDayIconRes(currentDayIconRes);
    }

    @Override
    public int getCurrentDaySelectedIconRes() {
        return appearanceModel.getCurrentDaySelectedIconRes();
    }

    @Override
    public void setCurrentDaySelectedIconRes(int currentDaySelectedIconRes) {
        appearanceModel.setCurrentDaySelectedIconRes(currentDaySelectedIconRes);
    }

    @Override
    public int getCalendarOrientation() {
        return appearanceModel.getCalendarOrientation();
    }

    @Override
    public void setCalendarOrientation(int calendarOrientation) {
        appearanceModel.setCalendarOrientation(calendarOrientation);
    }

    @Override
    public int getConnectedDayIconRes() {
        return appearanceModel.getConnectedDayIconRes();
    }

    @Override
    public void setConnectedDayIconRes(int connectedDayIconRes) {
        appearanceModel.setConnectedDayIconRes(connectedDayIconRes);
    }

    @Override
    public int getConnectedDaySelectedIconRes() {
        return appearanceModel.getConnectedDaySelectedIconRes();
    }

    @Override
    public void setConnectedDaySelectedIconRes(int connectedDaySelectedIconRes) {
        appearanceModel.setConnectedDaySelectedIconRes(connectedDaySelectedIconRes);
    }

    @Override
    public int getConnectedDayIconPosition() {
        return appearanceModel.getConnectedDayIconPosition();
    }

    @Override
    public void setConnectedDayIconPosition(int connectedDayIconPosition) {
        appearanceModel.setConnectedDayIconPosition(connectedDayIconPosition);
    }

    @Override
    public int getDisabledDayTextColor() {
        return appearanceModel.getDisabledDayTextColor();
    }

    @Override
    public void setDisabledDayTextColor(int disabledDayTextColor) {
        appearanceModel.setDisabledDayTextColor(disabledDayTextColor);
    }

    @Override
    public int getSelectionBarMonthTextColor() {
        return appearanceModel.getSelectionBarMonthTextColor();
    }

    @Override
    public void setSelectionBarMonthTextColor(int selectionBarMonthTextColor) {
        appearanceModel.setSelectionBarMonthTextColor(selectionBarMonthTextColor);
    }

    @Override
    public int getPreviousMonthIconRes() {
        return appearanceModel.getPreviousMonthIconRes();
    }

    @Override
    public void setPreviousMonthIconRes(int previousMonthIconRes) {
        appearanceModel.setPreviousMonthIconRes(previousMonthIconRes);
    }

    @Override
    public int getNextMonthIconRes() {
        return appearanceModel.getNextMonthIconRes();
    }

    @Override
    public void setNextMonthIconRes(int nextMonthIconRes) {
        appearanceModel.setNextMonthIconRes(nextMonthIconRes);
    }

    @Override
    public boolean isShowDaysOfWeek() {
        return appearanceModel.isShowDaysOfWeek();
    }

    @Override
    public void setShowDaysOfWeek(boolean showDaysOfWeek) {
        appearanceModel.setShowDaysOfWeek(showDaysOfWeek);
    }

    @Override
    public boolean isShowDaysOfWeekTitle() {
        return appearanceModel.isShowDaysOfWeekTitle();
    }

    @Override
    public void setShowDaysOfWeekTitle(boolean showDaysOfWeekTitle) {
        appearanceModel.setShowDaysOfWeekTitle(showDaysOfWeekTitle);
    }

    @Override
    public Set<Long> getDisabledDays() {
        return calendarListsModel.getDisabledDays();
    }

    @Override
    public void setDisabledDays(Set<Long> disabledDays) {
        calendarListsModel.setDisabledDays(disabledDays);
    }

    @Override
    public ConnectedDaysManager getConnectedDaysManager() {
        return calendarListsModel.getConnectedDaysManager();
    }

    @Override
    public Set<Long> getWeekendDays() {
        return calendarListsModel.getWeekendDays();
    }

    @Override
    public void setWeekendDays(Set<Long> weekendDays) {
        calendarListsModel.setWeekendDays(weekendDays);
    }

    @Override
    public DisabledDaysCriteria getDisabledDaysCriteria() {
        return calendarListsModel.getDisabledDaysCriteria();
    }

    @Override
    public void setDisabledDaysCriteria(DisabledDaysCriteria criteria) {
        calendarListsModel.setDisabledDaysCriteria(criteria);
    }

    @Override
    public void addConnectedDays(ConnectedDays connectedDays) {
        calendarListsModel.addConnectedDays(connectedDays);
    }

    @Override
    public int getFirstDayOfWeek() {
        return dateModel.getFirstDayOfWeek();
    }

    @Override
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        dateModel.setFirstDayOfWeek(firstDayOfWeek);
    }
}

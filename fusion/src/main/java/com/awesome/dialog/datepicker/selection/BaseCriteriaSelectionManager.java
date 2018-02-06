package com.awesome.dialog.datepicker.selection;

import com.awesome.dialog.datepicker.model.Day;
import com.awesome.dialog.datepicker.selection.criteria.BaseCriteria;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCriteriaSelectionManager extends BaseSelectionManager {

    protected List<BaseCriteria> criteriaList;

    public BaseCriteriaSelectionManager() {
    }

    public void setCriteriaList(List<BaseCriteria> criteriaList) {
        this.criteriaList = new ArrayList<>(criteriaList);
        notifyCriteriaUpdates();
    }

    public void clearCriteriaList() {
        if (criteriaList != null) {
            criteriaList.clear();
        }
        notifyCriteriaUpdates();
    }

    public void addCriteriaList(List<BaseCriteria> criteriaList) {
        if (this.criteriaList != null) {
            this.criteriaList.addAll(criteriaList);
        } else {
            setCriteriaList(criteriaList);
        }
        notifyCriteriaUpdates();
    }

    public void addCriteria(BaseCriteria criteria) {
        if (criteriaList == null) {
            criteriaList = new ArrayList<>();
        }
        criteriaList.add(criteria);
        notifyCriteriaUpdates();
    }

    public void removeCriteria(BaseCriteria criteria) {
        if (criteriaList != null) {
            criteriaList.remove(criteria);
        }
        notifyCriteriaUpdates();
    }

    public void removeCriteriaList(final List<BaseCriteria> listToDelete) {
        if (criteriaList != null) {
            criteriaList.removeAll(listToDelete);
        }
        notifyCriteriaUpdates();
    }

    private void notifyCriteriaUpdates() {
        if (onDaySelectedListener != null) {
            onDaySelectedListener.onDaySelected();
        }
    }

    public boolean hasCriteria() {
        return !(criteriaList == null || criteriaList.isEmpty());
    }

    public boolean isDaySelectedByCriteria(Day day) {
        if (hasCriteria()) {
            for (BaseCriteria criteria : criteriaList) {
                if (criteria.isCriteriaPassed(day)) {
                    return true;
                }
            }
        }
        return false;
    }
}

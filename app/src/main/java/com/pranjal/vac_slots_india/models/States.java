package com.pranjal.vac_slots_india.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class States{
    private Integer stateId;
    private String stateName;

    public States(Integer stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public States() {
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}

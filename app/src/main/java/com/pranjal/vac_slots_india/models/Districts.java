package com.pranjal.vac_slots_india.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Districts {
    private Integer districtId;
    private String districtName;

    public Districts(Integer districtId, String districtName) {
        this.districtId = districtId;
        this.districtName = districtName;
    }

    public Districts() {
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }


    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}

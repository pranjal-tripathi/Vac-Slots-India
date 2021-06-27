package com.pranjal.vac_slots_india.models;

import android.os.Parcel;
import android.os.Parcelable;

public class VaccineFees implements Parcelable {
    private Integer centerId;
    private String vaccine;
    private String fee;

    public VaccineFees() {
    }

    protected VaccineFees(Parcel in) {
        if (in.readByte() == 0) {
            centerId = null;
        } else {
            centerId = in.readInt();
        }
        vaccine = in.readString();
        fee = in.readString();
    }

    public static final Creator<VaccineFees> CREATOR = new Creator<VaccineFees>() {
        @Override
        public VaccineFees createFromParcel(Parcel in) {
            return new VaccineFees(in);
        }

        @Override
        public VaccineFees[] newArray(int size) {
            return new VaccineFees[size];
        }
    };

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }

    @Override
    public String toString() {
        return  vaccine + " - " +
                "₹ " + fee + "/-";
    }
    public String toStringDialog() {
        return  vaccine + " - " +
                "₹ " + fee + "/-\n";
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (centerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(centerId);
        }
        dest.writeString(vaccine);
        dest.writeString(fee);
    }
}

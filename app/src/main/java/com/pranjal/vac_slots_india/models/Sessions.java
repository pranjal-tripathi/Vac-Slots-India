package com.pranjal.vac_slots_india.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Sessions implements Parcelable {
    private Integer centerId;
    private String session_id;
    private String date;
    private Integer available_capacity;
    private Integer min_age_limit;
    private String vaccine;
    private Integer available_capacity_dose1;
    private Integer available_capacity_dose2;
    private boolean notified;

    protected Sessions(Parcel in) {
        if (in.readByte() == 0) {
            centerId = null;
        } else {
            centerId = in.readInt();
        }
        session_id = in.readString();
        date = in.readString();
        if (in.readByte() == 0) {
            available_capacity = null;
        } else {
            available_capacity = in.readInt();
        }
        if (in.readByte() == 0) {
            min_age_limit = null;
        } else {
            min_age_limit = in.readInt();
        }
        vaccine = in.readString();
        if (in.readByte() == 0) {
            available_capacity_dose1 = null;
        } else {
            available_capacity_dose1 = in.readInt();
        }
        if (in.readByte() == 0) {
            available_capacity_dose2 = null;
        } else {
            available_capacity_dose2 = in.readInt();
        }
        notified = in.readByte() != 0;
    }

    public static final Creator<Sessions> CREATOR = new Creator<Sessions>() {
        @Override
        public Sessions createFromParcel(Parcel in) {
            return new Sessions(in);
        }

        @Override
        public Sessions[] newArray(int size) {
            return new Sessions[size];
        }
    };

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public Sessions() {
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAvailable_capacity() {
        return available_capacity;
    }

    public void setAvailable_capacity(Integer available_capacity) {
        this.available_capacity = available_capacity;
    }

    public Integer getMin_age_limit() {
        return min_age_limit;
    }

    public void setMin_age_limit(Integer min_age_limit) {
        this.min_age_limit = min_age_limit;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public Integer getAvailable_capacity_dose1() {
        return available_capacity_dose1;
    }

    public void setAvailable_capacity_dose1(Integer available_capacity_dose1) {
        this.available_capacity_dose1 = available_capacity_dose1;
    }

    public Integer getAvailable_capacity_dose2() {
        return available_capacity_dose2;
    }

    public void setAvailable_capacity_dose2(Integer available_capacity_dose2) {
        this.available_capacity_dose2 = available_capacity_dose2;
    }

    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }

    @Override
    public String toString() {
        return
                "       Age Limit - " + min_age_limit + "\n" +
                "       Date - " + date + "\n" +
                "       Available Capacity - " + available_capacity + "\n" +
                "       Vaccine - " + vaccine + "\n" +
                "       Dose 1 - " + available_capacity_dose1 + "\n" +
                "       Dose 2 - " + available_capacity_dose2 + "\n";
    }
    public String toStringDialog() {
        return
                "Date -\t" + date + "\n" +
                        "Age Limit -\t" + min_age_limit + "\n" +
                        "Available Capacity -\t" + available_capacity + "\n" +
                        "Vaccine -\t" + vaccine + "\n" +
                        "Dose 1 -\t" + available_capacity_dose1 + "\n" +
                        "Dose 2 -\t" + available_capacity_dose2 + "\n\n";
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
        dest.writeString(session_id);
        dest.writeString(date);
        if (available_capacity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(available_capacity);
        }
        if (min_age_limit == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(min_age_limit);
        }
        dest.writeString(vaccine);
        if (available_capacity_dose1 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(available_capacity_dose1);
        }
        if (available_capacity_dose2 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(available_capacity_dose2);
        }
        dest.writeByte((byte) (notified ? 1 : 0));
    }
}

package com.pranjal.vac_slots_india.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

public class Centers implements Parcelable {
    private Integer centerId;
    private String centerName;
    private Integer pincode;
    private String fee_type;
    private String address;
    private String centerDistrict;
    private String centerState;
    private List<Sessions> sessionsList;
    private List<VaccineFees> vaccineFeesList;

    public Centers() {
    }

    protected Centers(Parcel in) {
        if (in.readByte() == 0) {
            centerId = null;
        } else {
            centerId = in.readInt();
        }
        centerName = in.readString();
        if (in.readByte() == 0) {
            pincode = null;
        } else {
            pincode = in.readInt();
        }
        fee_type = in.readString();
        address = in.readString();
        centerDistrict = in.readString();
        centerState = in.readString();
        sessionsList = in.createTypedArrayList(Sessions.CREATOR);
        vaccineFeesList = in.createTypedArrayList(VaccineFees.CREATOR);
    }

    public static final Creator<Centers> CREATOR = new Creator<Centers>() {
        @Override
        public Centers createFromParcel(Parcel in) {
            return new Centers(in);
        }

        @Override
        public Centers[] newArray(int size) {
            return new Centers[size];
        }
    };

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public List<Sessions> getSessionsList() {
        return sessionsList;
    }

    public void setSessionsList(List<Sessions> sessionsList) {
        this.sessionsList = sessionsList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<VaccineFees> getVaccineFeesList() {
        return vaccineFeesList;
    }

    public void setVaccineFeesList(List<VaccineFees> vaccineFeesList) {
        this.vaccineFeesList = vaccineFeesList;
    }

    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }

    public String getCenterDistrict() {
        return centerDistrict;
    }

    public void setCenterDistrict(String centerDistrict) {
        this.centerDistrict = centerDistrict;
    }

    public String getCenterState() {
        return centerState;
    }

    public void setCenterState(String centerState) {
        this.centerState = centerState;
    }

    @Override
    public String toString() {
        if(vaccineFeesList.isEmpty())
            return  "\n" + centerName + " | " +
                    "Pincode - " + pincode + " | " +
                    "Fee type - " + fee_type + " | " +
                    "Address - " + address +
                    "\nAvailable Sessions -\n" + Arrays.toString(sessionsList.toArray()).replace(", ", "\n")
                    .replace("[", "").replace("]", "") + "\n";
        else
            return  "\n" + centerName + " | " +
                "Pincode - " + pincode + " | " +
                "Fee type - " + fee_type + " | " +
                "Address - " + address +
                "\n\nVaccine Fees -\n" + Arrays.toString(vaccineFeesList.toArray()).replace(", ", " | ")
                .replace("[", "").replace("]", "")+ "\n" +
                "\nAvailable Sessions -\n" + Arrays.toString(sessionsList.toArray()).replace(", ", "\n")
                .replace("[", "").replace("]", "") + "\n";
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
        dest.writeString(centerName);
        if (pincode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pincode);
        }
        dest.writeString(fee_type);
        dest.writeString(address);
        dest.writeString(centerDistrict);
        dest.writeString(centerState);
        dest.writeTypedList(sessionsList);
        dest.writeTypedList(vaccineFeesList);
    }

    public String toStringDialog() {
        if(vaccineFeesList.isEmpty())
            return  "\n" + centerName + "\n" +
                    "Address - " + address + "\n" +
                    "Pincode - " + pincode + "\n" +
                    "Fee type - " + fee_type + "\n\n";
        else
            return  "\n" + centerName + "\n" +
                    "Address - " + address + "\n" +
                    "Pincode - " + pincode + "\n" +
                    "Fee type - " + fee_type + "\n\n";
    }
}

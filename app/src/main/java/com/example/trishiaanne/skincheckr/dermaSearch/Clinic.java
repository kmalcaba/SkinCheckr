package com.example.trishiaanne.skincheckr.dermaSearch;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Clinic implements Parcelable {

    public Clinic(Parcel in) {
        address = in.readString();
        location = in.readString();
        name = in.readString();
        fee = in.readString();
        directions = in.readString();
        schedDay = new ArrayList<>();
        in.readStringList(schedDay);
        schedTime = new ArrayList<>();
        in.readStringList(schedTime);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(location);
        parcel.writeString(name);
        parcel.writeString(fee);
        parcel.writeString(directions);
        parcel.writeStringList(schedDay);
        parcel.writeStringList(schedTime);
    }

    public static final Creator<Clinic> CREATOR = new Creator<Clinic>() {
        @Override
        public Clinic createFromParcel(Parcel in) {
            return new Clinic(in);
        }

        @Override
        public Clinic[] newArray(int size) {
            return new Clinic[size];
        }
    };

    public Clinic() {

    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the fee
     */
    public String getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(String fee) {
        this.fee = fee;
    }

    /**
     * @return the directions
     */
    public String getDirections() {
        return directions;
    }

    /**
     * @param directions the directions to set
     */
    public void setDirections(String directions) {
        this.directions = directions;
    }

    /**
     * @param index
     * @return the schedDay
     */
    public String getSchedDay(int index) {
        return schedDay.get(index);
    }
    /**
     * @return the schedDay
     */
    public List<String> getSchedDays() {
        return schedDay;
    }

    /**
     * @param schedDay the schedDay to set
     */
    public void setSchedDay(String schedDay) {
        this.schedDay.add(schedDay);
    }

    /**
     * @param index
     * @return the schedTime
     */
    public String getSchedTime(int index) {
        return schedTime.get(index);
    }
    /**
     * @return the schedTime
     */
    public List<String>getSchedTimes() {
        return schedTime;
    }

    /**
     * @param schedTime the schedTime to set
     */
    public void setSchedTime(String schedTime) {
        this.schedTime.add(schedTime);
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    private String address;
    private String location;
    private String name;
    private String fee;
    private String directions;
    private List<String> schedDay = new ArrayList<>();
    private List<String> schedTime = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }


}

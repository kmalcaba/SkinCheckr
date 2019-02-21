package com.example.trishiaanne.skincheckr.dermaSearch;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Dermatologist implements Parcelable {

    public Dermatologist(Parcel in) {
        clinics = new ArrayList<>();
        in.readList(clinics, Clinic.class.getClassLoader());
        name = in.readString();
        location = in.readString();
        years = in.readInt();
        fee = in.readString();
        info = in.readString();
        services = new ArrayList<>();
        in.readStringList(services);
    }

    public Dermatologist() {
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(clinics);
        parcel.writeString(name);
        parcel.writeString(location);
        parcel.writeInt(years);
        parcel.writeString(fee);
        parcel.writeString(info);
        parcel.writeStringList(services);
    }

    public static final Creator<Dermatologist> CREATOR = new Creator<Dermatologist>() {
        @Override
        public Dermatologist createFromParcel(Parcel in) {
            return new Dermatologist(in);
        }

        @Override
        public Dermatologist[] newArray(int size) {
            return new Dermatologist[size];
        }
    };

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
     * @return the years
     */
    public int getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(int years) {
        this.years = years;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * @return the clinics
     */
    public List<Clinic> getClinics() {
        return clinics;
    }

    /**
     * @param index
     * @return
     */
    public Clinic getClinic(int index) {
        return clinics.get(index);
    }

    /**
     * @param clinics the clinics to set
     */
    public void setClinics(List<Clinic> clinics) {
        this.clinics = clinics;
    }

    /**
     * @param address
     * @param schedDay
     * @param schedTime
     * @param location
     * @param name
     * @param fee
     * @param directions
     */
    public void setClinic(String address, String schedDay, String schedTime, String location, String name, String fee, String directions) {
        Clinic c = new Clinic();
        c.setAddress(address);
        c.setSchedDay(schedDay);
        c.setSchedTime(schedTime);
        c.setLocation(location);
        c.setName(name);
        c.setFee(fee);
        c.setDirections(directions);
        this.clinics.add(c);
    }

    public void setClinicSched(int index, String schedDay, String schedTime) {
        this.clinics.get(index).setSchedDay(schedDay);
        this.clinics.get(index).setSchedTime(schedTime);
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
     * @return the services
     */
    public List<String> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(List<String> services) {
        this.services = services;
    }

    /**
     * @param service
     */
    public void setService(String service) {
        this.services.add(service);
    }

    private int id;
    private String name;
    private String location;
    private int years;
    private String fee;
    private String info;

    private List<String> services = new ArrayList<>();
    private List<Clinic> clinics = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }


}


package com.bluetag.wc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stadium implements Serializable {

    @SerializedName("stadium_id")
    @Expose
    private String stadiumId;
    @SerializedName("stadium_name")
    @Expose
    private String stadiumName;
    @SerializedName("stadium_detail_url")
    @Expose
    private String stadiumDetailUrl;
    @SerializedName("stadium_image_url")
    @Expose
    private String stadiumImageUrl;
    @SerializedName("stadium_host_city")
    @Expose
    private String stadiumHostCity;
    @SerializedName("stadium_capacity")
    @Expose
    private String stadiumCapacity;
    @SerializedName("stadium_location")
    @Expose
    private String stadiumLocation;

    public String getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(String stadiumId) {
        this.stadiumId = stadiumId;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public String getStadiumDetailUrl() {
        return stadiumDetailUrl;
    }

    public void setStadiumDetailUrl(String stadiumDetailUrl) {
        this.stadiumDetailUrl = stadiumDetailUrl;
    }

    public String getStadiumImageUrl() {
        return stadiumImageUrl;
    }

    public void setStadiumImageUrl(String stadiumImageUrl) {
        this.stadiumImageUrl = stadiumImageUrl;
    }

    public String getStadiumHostCity() {
        return stadiumHostCity;
    }

    public void setStadiumHostCity(String stadiumHostCity) {
        this.stadiumHostCity = stadiumHostCity;
    }

    public String getStadiumCapacity() {
        return stadiumCapacity;
    }

    public void setStadiumCapacity(String stadiumCapacity) {
        this.stadiumCapacity = stadiumCapacity;
    }

    public String getStadiumLocation() {
        return stadiumLocation;
    }

    public void setStadiumLocation(String stadiumLocation) {
        this.stadiumLocation = stadiumLocation;
    }

}

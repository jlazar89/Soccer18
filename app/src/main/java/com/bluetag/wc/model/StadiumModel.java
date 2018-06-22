
package com.bluetag.wc.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StadiumModel {

    @SerializedName("stadiums")
    @Expose
    private List<Stadium> stadiums = null;

    public List<Stadium> getStadiums() {
        return stadiums;
    }

    public void setStadiums(List<Stadium> stadiums) {
        this.stadiums = stadiums;
    }

}

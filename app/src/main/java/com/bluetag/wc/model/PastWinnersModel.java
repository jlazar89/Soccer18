
package com.bluetag.wc.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PastWinnersModel {

    @SerializedName("past_winners")
    @Expose
    private List<PastWinner> pastWinners = null;

    public List<PastWinner> getPastWinners() {
        return pastWinners;
    }

    public void setPastWinners(List<PastWinner> pastWinners) {
        this.pastWinners = pastWinners;
    }

}

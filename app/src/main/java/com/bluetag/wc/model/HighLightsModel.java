
package com.bluetag.wc.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HighLightsModel {

    @SerializedName("highlights")
    @Expose
    private List<HighLight> highlights = null;

    public List<HighLight> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<HighLight> highlights) {
        this.highlights = highlights;
    }

    public class HighLight {

        @SerializedName("match_no")
        @Expose
        private String matchNo;
        @SerializedName("match_name")
        @Expose
        private String matchName;
        @SerializedName("match_video_id")
        @Expose
        private String matchVideoId;

        public String getMatchNo() {
            return matchNo;
        }

        public void setMatchNo(String matchNo) {
            this.matchNo = matchNo;
        }

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public String getMatchVideoId() {
            return matchVideoId;
        }

        public void setMatchVideoId(String matchVideoId) {
            this.matchVideoId = matchVideoId;
        }

    }


}

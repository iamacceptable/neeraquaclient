
package com.vgeekers.neeraqua.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchAllStatesResponse {

    @SerializedName("states")
    @Expose
    private List<String> states = null;

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

}

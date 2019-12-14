
package com.vgeekers.neeraqua.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocalityResponse extends CommonResponse {

    @SerializedName("localities")
    @Expose
    private List<String> localities = null;

    public List<String> getLocalities() {
        return localities;
    }

    public void setLocalities(List<String> localities) {
        this.localities = localities;
    }

}

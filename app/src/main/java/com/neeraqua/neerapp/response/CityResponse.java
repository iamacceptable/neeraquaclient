
package com.neeraqua.neerapp.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityResponse {

    @SerializedName("cities")
    @Expose
    private List<String> cities = null;

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

}

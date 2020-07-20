
package com.neeraqua.neerapp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("u_name")
    @Expose
    private String uName;
    @SerializedName("u_mobile")
    @Expose
    private String uMobile;
    @SerializedName("u_hno")
    @Expose
    private String uHno;
    @SerializedName("u_locality")
    @Expose
    private String uLocality;
    @SerializedName("u_state")
    @Expose
    private String uState;
    @SerializedName("u_city")
    @Expose
    private String uCity;
    @SerializedName("u_active")
    @Expose
    private String uActive;

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public String getUMobile() {
        return uMobile;
    }

    public void setUMobile(String uMobile) {
        this.uMobile = uMobile;
    }

    public String getUHno() {
        return uHno;
    }

    public void setUHno(String uHno) {
        this.uHno = uHno;
    }

    public String getULocality() {
        return uLocality;
    }

    public void setULocality(String uLocality) {
        this.uLocality = uLocality;
    }

    public String getUState() {
        return uState;
    }

    public void setUState(String uState) {
        this.uState = uState;
    }

    public String getUCity() {
        return uCity;
    }

    public void setUCity(String uCity) {
        this.uCity = uCity;
    }

    public String getUActive() {
        return uActive;
    }

    public void setUActive(String uActive) {
        this.uActive = uActive;
    }

}

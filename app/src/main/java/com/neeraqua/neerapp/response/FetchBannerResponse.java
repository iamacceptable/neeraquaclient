
package com.neeraqua.neerapp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FetchBannerResponse {

    @SerializedName("banner")
    @Expose
    private List<String> bannerList = new ArrayList<>();
    @SerializedName("errorCode")
    @Expose
    private String errorCode;

    public List<String> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<String> booking) {
        this.bannerList = booking;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}

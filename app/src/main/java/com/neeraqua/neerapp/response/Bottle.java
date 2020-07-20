
package com.neeraqua.neerapp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bottle {

    @SerializedName("b_id")
    @Expose
    private String bId = "";
    @SerializedName("b_brand")
    @Expose
    private String bBrand = "";
    @SerializedName("b_description")
    @Expose
    private String bDescription = "";
    @SerializedName("b_image")
    @Expose
    private String bImage = "";
    @SerializedName("b_price")
    @Expose
    private String bPrice = "";
    @SerializedName("b_vendor")
    @Expose
    private String bVendorId = "";
    @SerializedName("offer")
    @Expose
    private int mOffer = 0;

    public String getBId() {
        return bId;
    }

    public void setBId(String bId) {
        this.bId = bId;
    }

    public String getBBrand() {
        return bBrand;
    }

    public void setBBrand(String bBrand) {
        this.bBrand = bBrand;
    }

    public String getBDescription() {
        return bDescription;
    }

    public void setBDescription(String bDescription) {
        this.bDescription = bDescription;
    }

    public String getBImage() {
        return bImage;
    }

    public void setBImage(String bImage) {
        this.bImage = bImage;
    }

    public String getBPrice() {
        return bPrice;
    }

    public void setBPrice(String bPrice) {
        this.bPrice = bPrice;
    }

    public String getBVendorId() {
        return bVendorId;
    }

    public void setBVendorId(String bVendorId) {
        this.bVendorId = bVendorId;
    }

    public int getmOffer() {
        return mOffer;
    }

    public void setmOffer(int mOffer) {
        this.mOffer = mOffer;
    }
}

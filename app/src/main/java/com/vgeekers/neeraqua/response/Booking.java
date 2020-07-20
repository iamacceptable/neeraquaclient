
package com.vgeekers.neeraqua.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Booking {

    @SerializedName("b_id")
    @Expose
    private String bId;
    @SerializedName("b_brand")
    @Expose
    private String bBrand;
    @SerializedName("b_image")
    @Expose
    private String bImage;
    @SerializedName("v_name")
    @Expose
    private String vendorName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("b_date")
    @Expose
    private String bDate;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("c_id")
    @Expose
    private String cartId;

    @SerializedName("price")
    @Expose
    private String perPrice;

    public String getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(String perPrice) {
        this.perPrice = perPrice;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getbBrand() {
        return bBrand;
    }

    public void setbBrand(String bBrand) {
        this.bBrand = bBrand;
    }

    public String getbImage() {
        return bImage;
    }

    public void setbImage(String bImage) {
        this.bImage = bImage;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getbDate() {
        return bDate;
    }

    public void setbDate(String bDate) {
        this.bDate = bDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

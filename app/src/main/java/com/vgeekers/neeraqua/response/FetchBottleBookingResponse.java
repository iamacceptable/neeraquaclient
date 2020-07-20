
package com.vgeekers.neeraqua.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FetchBottleBookingResponse {

    @SerializedName("orders")
    @Expose
    private List<Booking> booking = new ArrayList<>();
    @SerializedName("errorCode")
    @Expose
    private String errorCode;

    @SerializedName("vendorId")
    @Expose
    private String vendorId;

    @SerializedName("totalPrice")
    @Expose
    private String Botprice;



    public String getBotprice() {
        return Botprice;
    }

    public void setBotprice(String botprice) {
        Botprice = botprice;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public List<Booking> getBooking() {
        return booking;
    }

    public void setBooking(List<Booking> booking) {
        this.booking = booking;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}

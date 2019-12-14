
package com.vgeekers.neeraqua.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlotResponse {

    @SerializedName("timeSlot")
    @Expose
    private List<String> timeSlot = null;
    @SerializedName("errorCode")
    @Expose
    private String errorCode;

    public List<String> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(List<String> timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}

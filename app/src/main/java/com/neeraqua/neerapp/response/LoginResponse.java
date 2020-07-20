
package com.neeraqua.neerapp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("isAllowLogin")
    @Expose
    private Boolean isAllowLogin;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("errorCode")
    @Expose
    private String errorCode;

    public Boolean getIsAllowLogin() {
        return isAllowLogin;
    }

    public void setIsAllowLogin(Boolean isAllowLogin) {
        this.isAllowLogin = isAllowLogin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}

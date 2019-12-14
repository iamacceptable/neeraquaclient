package com.vgeekers.neeraqua.pani_services;

import com.vgeekers.neeraqua.response.CityResponse;
import com.vgeekers.neeraqua.response.CommonResponse;
import com.vgeekers.neeraqua.response.FetchAllBottlesResponse;
import com.vgeekers.neeraqua.response.FetchAllStatesResponse;
import com.vgeekers.neeraqua.response.DetailResponse;
import com.vgeekers.neeraqua.response.FetchBannerResponse;
import com.vgeekers.neeraqua.response.FetchBottleBookingResponse;
import com.vgeekers.neeraqua.response.LocalityResponse;
import com.vgeekers.neeraqua.response.LoginResponse;
import com.vgeekers.neeraqua.response.TimeSlotResponse;
import com.vgeekers.neeraqua.response.UserProfileResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PaniServices {

    @GET("fetch_all_states.php")
    Call<FetchAllStatesResponse> getAllStates();

    @GET("fetch_privacy_policy.php")
    Call<DetailResponse> fetchPrivacyPolicy();

    @GET("fetch_tnc.php")
    Call<DetailResponse> fetchTncResponse();

    @POST("sign_up.php")
    @FormUrlEncoded
    Call<CommonResponse> getSignUpResponse(@Field("name") String userName, @Field("mobile") String mobileNumber, @Field("password") String password, @Field("hno") String address, @Field("state") String state, @Field("city") String city, @Field("locality") String locality);

    @POST("update_profile.php")
    @FormUrlEncoded
    Call<CommonResponse> getUpdateProfileResponse(@Field("userId") String userId, @Field("name") String userName, @Field("mobile") String mobileNumber, @Field("hno") String address, @Field("state") String state, @Field("city") String city, @Field("locality") String locality);

    @POST("booking.php")
    @FormUrlEncoded
    Call<CommonResponse> getBookingResponse(@Field("userId") String userId, @Field("timeSlot") String timeSlot, @Field("b_date") String date);

    @POST("login.php")
    @FormUrlEncoded
    Call<LoginResponse> getLoginResponse(@Field("mobile") String mobileNumber, @Field("password") String password);

    @POST("forgot_password.php")
    @FormUrlEncoded
    Call<CommonResponse> getForgotPasswordResponse(@Field("mobile") String mobileNumber, @Field("password") String password);

    @POST("fetch_all_cities.php")
    @FormUrlEncoded
    Call<CityResponse> getCitiesResponse(@Field("state") String state);

    @POST("fetch_all_localities.php")
    @FormUrlEncoded
    Call<LocalityResponse> getLocalitiesResponse(@Field("city") String city);

    @POST("fetch_bottle_details.php")
    @FormUrlEncoded
    Call<FetchAllBottlesResponse> getAllBottlesResponse(@Field("userId") String userId);

    @POST("fetch_profile.php")
    @FormUrlEncoded
    Call<UserProfileResponse> getUserProfileResponse(@Field("userId") String userId);

    @POST("fetch_time_slot.php")
    @FormUrlEncoded
    Call<TimeSlotResponse> getTimeSlotList(@Field("vendorId") String vendorId);

    @POST("fetch_myorder.php")
    @FormUrlEncoded
    Call<FetchBottleBookingResponse> getBottleList(@Field("userId") String vendorId);

    @POST("fetch_cart.php")
    @FormUrlEncoded
    Call<FetchBottleBookingResponse> getMyCartList(@Field("userId") String vendorId);

    @POST("add_to_cart.php")
    @FormUrlEncoded
    Call<CommonResponse> addToCartServerCall(@Field("userId") String userId, @Field("bottleId") String bottleId, @Field("quantity") String quantity,@Field("vendorId") String vendorId);

    @POST("delete_to_cart.php")
    @FormUrlEncoded
    Call<CommonResponse> deleteFromCartServerCall(@Field("cartId") String cartId, @Field("userId") String userId);

    @GET("fetch_banner.php")
    Call<FetchBannerResponse> getBannerList();

    @GET("fetch_subbanner.php")
    Call<FetchBannerResponse> getSubBannerList();
}

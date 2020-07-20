package com.vgeekers.neeraqua.retrofit;

import com.vgeekers.neeraqua.pani_services.PaniServices;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {

    private static final String BASE_URL = "http://Neeraqua.com/api/";

    private static PaniServices sPaniServices = null;

    public static PaniServices getPaniServicesObject() {
        if (null == sPaniServices) {
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            sPaniServices = retrofit.create(PaniServices.class);
        }
        return sPaniServices;
    }
}

package com.intabux.gobussines.retrofit;


import com.intabux.gobussines.Models.FCMBody;
import com.intabux.gobussines.Models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-type:application/json",
            "Authorization:key=AAAAttTgILI:APA91bEiaAA5kL_Dcw0RCYdqDooSk-rRuJC8JWC65ho41Vvw2FKuG2PrmS7ydowQSOmSzMiecL-x549iKwem2brY26c_ze_ganszsbNMTXaP4ucwPIoPhC-vvqq7_GykEL5V2fn9Yunu"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}

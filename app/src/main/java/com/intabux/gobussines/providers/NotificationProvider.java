package com.intabux.gobussines.providers;

import com.intabux.gobussines.Models.FCMBody;
import com.intabux.gobussines.Models.FCMResponse;
import com.intabux.gobussines.retrofit.IFCMApi;
import com.intabux.gobussines.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {
    }
    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFCMApi.class).send(body);
    }
}

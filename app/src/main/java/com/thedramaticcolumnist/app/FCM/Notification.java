package com.thedramaticcolumnist.app.FCM;

import android.content.Context;
import android.util.Log;

import com.thedramaticcolumnist.app.Model.Data;
import com.thedramaticcolumnist.app.Model.MyResponse;
import com.thedramaticcolumnist.app.Model.NotificationSender;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification {
    public static void sendNotification(String Type, String message, String Token, Context context) {

        APIService apiService = Client.getClient(context).create(APIService.class);

        NotificationSender sender = new NotificationSender(new Data(message, Type), Token);

        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getSuccess() == 1) {
                        Log.i("SANJAY", "onResponse: SEND ");
                    } else {
                        Log.e("SANJAY", "onResponse: FAILED :: ");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e("SANJAY", "onResponse: Failure --------> " + t.getMessage());
            }
        });
    }
}

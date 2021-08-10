package com.thedramaticcolumnist.app.FCM;

import com.thedramaticcolumnist.app.Model.MyResponse;
import com.thedramaticcolumnist.app.Model.NotificationSender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAcGWKJ6g:APA91bH67vsqVGxEm1JLPI-i14sufOuwd6K1ubkZNevUtrJLsGW32Fl85uPv8bK8UNJK6jwahCe1VV7oZSpzxK5gWcCAAy61gvFzt08-9MASWhElCW7LH_x3ZM77gxeu_wLnMdFdiU9S" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}

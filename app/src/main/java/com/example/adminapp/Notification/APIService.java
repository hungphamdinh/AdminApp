package com.example.adminapp.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAnBfZYR8:APA91bFQiESeOiLbAadp4XBodL5-HiSCBXkW1oMJVuiH-Ou5H7LFBzVDmsQLA6qIoPY9Upzlo_GN_Xf7nE0ehSUwXtr_RbiIfeVPAGh_hLyU3pkbR3gFUDireaVjNgC5fkPvivEJ4SEM"
            }
    )
    @POST("fcm/send")
    Call<MyRespone>sendNotification(@Body Sender body);
}

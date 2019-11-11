package com.free.pubg.free.uc.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.free.pubg.free.uc.api.RetrofitClient;
import com.free.pubg.free.uc.models.DefaultResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");

        //Use the referrer
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateUcRefer(referrer, "2");
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {

            }

            @Override
            public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {

            }
        });
    }
}

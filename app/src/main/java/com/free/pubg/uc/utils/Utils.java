package com.free.pubg.uc.utils;

import android.content.Context;
import android.util.Log;

import com.free.pubg.uc.api.RetrofitClient;
import com.free.pubg.uc.models.DefaultResponse;
import com.free.pubg.uc.sharedprefrences.SharedPrefManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    public static void updateUc(Context context, String points, String bonus_type) {
        String user_id = SharedPrefManager.getInstance(context).getUserId();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateUc(user_id, bonus_type, points);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {

                if (response.body() != null) {

                    Log.d("niraj", "points");
                    String oldPoints = SharedPrefManager.getInstance(context).getUc();
                    float newPoints = Float.parseFloat(oldPoints) + Float.parseFloat(points);
                    SharedPrefManager.getInstance(context).saveUc(String.valueOf(newPoints));
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }

}

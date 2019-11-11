package com.free.pubg.free.uc.api;

import com.free.pubg.free.uc.models.AdPoints;
import com.free.pubg.free.uc.models.DefaultResponse;
import com.free.pubg.free.uc.models.RedeemHistoryResponse;
import com.free.pubg.free.uc.models.RoyalPassHistoryResponse;
import com.free.pubg.free.uc.models.TransactionHistoryResponse;
import com.free.pubg.free.uc.models.User;
import com.free.pubg.free.uc.models.WinnerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("register")
    Call<User> register(@Field("user_id") String user_id,
                        @Field("name") String name,
                        @Field("email") String email,
                        @Field("refer_id") String refer_id);

    @FormUrlEncoded
    @POST("royalPassRequest")
    Call<DefaultResponse> royalPassRequest(@Field("user_id") String user_id,
                        @Field("name") String name,
                        @Field("email") String email,
                        @Field("request_date") String request_date);

    @FormUrlEncoded
    @POST("redeemRequest")
    Call<DefaultResponse> redeemRequest(@Field("user_id") String user_id,
                                           @Field("name") String name,
                                           @Field("type") String type,
                                           @Field("mobile_pubg_id") String mobile_pubg_id,
                                           @Field("uc") int uc);

    @FormUrlEncoded
    @POST("updateUc")
    Call<DefaultResponse> updateUc(@Field("user_id") String user_id,
                                   @Field("bonus_type") String bonus_type,
                                   @Field("uc") String uc);

    @FormUrlEncoded
    @POST("updateUcRefer")
    Call<DefaultResponse> updateUcRefer(@Field("refer_id") String refer_id,
                                        @Field("uc") String uc);

    @FormUrlEncoded
    @POST("getUc")
    Call<String> getUc(@Field("user_id") String user_id);

    @GET("getAdPoints")
    Call<AdPoints> getAdPoints();

    @GET("getRoyalPassWinners")
    Call<WinnerResponse> getRoyalPassWinners();

    @GET("getRoyalPassHistory")
    Call<RoyalPassHistoryResponse> getRoyalPassHistory();

    @FormUrlEncoded
    @POST("getTransactionHistory")
    Call<TransactionHistoryResponse> getTransactionHistory(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getRedeemHistory")
    Call<RedeemHistoryResponse> getRedeemHistory(@Field("user_id") String user_id);
}

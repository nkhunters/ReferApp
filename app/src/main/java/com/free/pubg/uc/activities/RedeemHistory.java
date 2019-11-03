package com.free.pubg.uc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.free.pubg.R;
import com.free.pubg.uc.adapters.RedeemHistoryAdapter;
import com.free.pubg.uc.api.RetrofitClient;
import com.free.pubg.uc.models.RedeemHistoryResponse;
import com.free.pubg.uc.sharedprefrences.SharedPrefManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemHistory extends AppCompatActivity {

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.go_back)
    void goBack()
    {
        finish();
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_history);
        ButterKnife.bind(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setupBannerAds();

        getData();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getData() {

        String strUserId = SharedPrefManager.getInstance(RedeemHistory.this).getUserId();

        ProgressDialog progressDialog = new ProgressDialog(RedeemHistory.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<RedeemHistoryResponse> call = RetrofitClient.getInstance().getApi().getRedeemHistory(strUserId);
        call.enqueue(new Callback<RedeemHistoryResponse>() {
            @Override
            public void onResponse(@NotNull Call<RedeemHistoryResponse> call, @NotNull Response<RedeemHistoryResponse> response) {

                progressDialog.cancel();
                if(response.body() != null)
                {
                    RedeemHistoryAdapter adapter = new RedeemHistoryAdapter(response.body().getRedeem_history());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NotNull Call<RedeemHistoryResponse> call, @NotNull Throwable t) {

                progressDialog.dismiss();
            }
        });
    }

    private void setupBannerAds() {

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                /*String banner_points = SharedPrefManager.getInstance(DailyBonus.this).getBannerPoints();
                Utils.updateUc(DailyBonus.this, banner_points);*/
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
}

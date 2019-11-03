package com.free.pubg.uc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.free.pubg.R;
import com.free.pubg.uc.adapters.WinnerAdapter;
import com.free.pubg.uc.api.RetrofitClient;
import com.free.pubg.uc.models.DefaultResponse;
import com.free.pubg.uc.models.Winner;
import com.free.pubg.uc.models.WinnerResponse;
import com.free.pubg.uc.sharedprefrences.SharedPrefManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoyalPass extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RewardedAd royalPassAd;

    @OnClick(R.id.go_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.participate_history)
    void participateHistory() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("niraj", "The interstitial wasn't loaded yet.");
            startActivity(new Intent(RoyalPass.this, ParticipateHistory.class));
        }
    }

    @OnClick(R.id.participate_now)
    void participateNowClick() {
        royalPassAd.show(RoyalPass.this, royalPassAdCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_royal_pass);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setupBannerAds();
        setupInterstitialAds();
        setupRewardAds();

        getWinners();
    }

    private void setupInterstitialAds() {
        mInterstitialAd = new InterstitialAd(RoyalPass.this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
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
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                startActivity(new Intent(RoyalPass.this, ParticipateHistory.class));
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                startActivity(new Intent(RoyalPass.this, ParticipateHistory.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void getWinners() {

        Call<WinnerResponse> call = RetrofitClient.getInstance().getApi().getRoyalPassWinners();
        call.enqueue(new Callback<WinnerResponse>() {
            @Override
            public void onResponse(@NotNull Call<WinnerResponse> call, @NotNull Response<WinnerResponse> response) {

                if (response.body() != null) {
                    setupRecyclerView(response.body().getWinners());
                }
            }

            @Override
            public void onFailure(@NotNull Call<WinnerResponse> call, @NotNull Throwable t) {

            }
        });
    }

    private void setupRecyclerView(ArrayList<Winner> winners) {

        recyclerView.setLayoutManager(new LinearLayoutManager(RoyalPass.this));
        WinnerAdapter adapter = new WinnerAdapter(RoyalPass.this, winners);
        recyclerView.setAdapter(adapter);
    }

    private void setupRewardAds() {

        royalPassAd = createAndLoadRewardedAd(getString(R.string.reward_id));
    }

    public RewardedAd createAndLoadRewardedAd(String adUnitId) {
        RewardedAd rewardedAd = new RewardedAd(this, adUnitId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    RewardedAdCallback royalPassAdCallback = new RewardedAdCallback() {
        @Override
        public void onRewardedAdOpened() {
            // Ad opened.
        }

        @Override
        public void onRewardedAdClosed() {
            // Ad closed.
            royalPassAd = createAndLoadRewardedAd(getString(R.string.reward_id));
            participate();
        }

        @Override
        public void onUserEarnedReward(@NonNull RewardItem reward) {
            // User earned reward.
            /*String reward_points = SharedPrefManager.getInstance(RoyalPass.this).getRewardPoints();
            Utils.updateUc(RoyalPass.this, reward_points);*/
        }

        @Override
        public void onRewardedAdFailedToShow(int errorCode) {
            // Ad failed to display
            participate();
        }
    };

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
                /*String banner_points = SharedPrefManager.getInstance(RoyalPass.this).getBannerPoints();
                Utils.updateUc(RoyalPass.this, banner_points, );*/
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

    public void participate() {
        String user_id = SharedPrefManager.getInstance(RoyalPass.this).getUserId();
        String name = SharedPrefManager.getInstance(RoyalPass.this).getUserName();
        String email = SharedPrefManager.getInstance(RoyalPass.this).getEmail();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

        ProgressDialog progressDialog = new ProgressDialog(RoyalPass.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().royalPassRequest(user_id, name, email, date);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {

                progressDialog.cancel();
                if (response.body() != null && response.code() == 201) {
                    Intent intent = new Intent(RoyalPass.this, RoyalPassRequest.class);
                    if (response.body().getError()) {
                        intent.putExtra("already_participated", 1);

                    } else {
                        intent.putExtra("already_participated", 0);
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(RoyalPass.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}

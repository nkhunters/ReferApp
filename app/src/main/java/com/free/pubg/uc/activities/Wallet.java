package com.free.pubg.uc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.free.pubg.R;
import com.free.pubg.uc.adapters.TransactionHistoryAdapter;
import com.free.pubg.uc.api.RetrofitClient;
import com.free.pubg.uc.models.TransactionHistoryResponse;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Wallet extends AppCompatActivity {

    private RewardedAd redeemAd;
    private InterstitialAd mInterstitialAd;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.go_back)
    void goBack()
    {
        finish();
    }

    @BindView(R.id.uc_text)
    TextView ucText;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @OnClick(R.id.redeem_btn)
    void redeem()
    {
        redeemAd.show(Wallet.this, royalPassAdCallback);
    }

    @OnClick(R.id.redeem_history)
    void redeemHistory()
    {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("niraj", "The interstitial wasn't loaded yet.");
            startActivity(new Intent(Wallet.this, RedeemHistory.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        String strUc = SharedPrefManager.getInstance(Wallet.this).getUc();
        ucText.setText(String.format("%.2f UC", Float.parseFloat(strUc)));

        setupBannerAds();
        setupRewardAds();
        setupInterstitialAds();
        getData();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getData() {

        String strUserId = SharedPrefManager.getInstance(Wallet.this).getUserId();
        Call<TransactionHistoryResponse> call = RetrofitClient.getInstance().getApi().getTransactionHistory(strUserId);
        call.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(@NotNull Call<TransactionHistoryResponse> call, @NotNull Response<TransactionHistoryResponse> response) {

                if(response.body() != null)
                {
                    TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(response.body().getTransaction_history());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NotNull Call<TransactionHistoryResponse> call, @NotNull Throwable t) {

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

    private void setupRewardAds() {

        redeemAd = createAndLoadRewardedAd(getString(R.string.reward_id));
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
            redeemAd = createAndLoadRewardedAd(getString(R.string.reward_id));
            startActivity(new Intent(Wallet.this, ReedemOptions.class));
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
            startActivity(new Intent(Wallet.this, ReedemOptions.class));
        }
    };

    private void setupInterstitialAds() {
        mInterstitialAd = new InterstitialAd(Wallet.this);
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
                startActivity(new Intent(Wallet.this, ReedemOptions.class));
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                startActivity(new Intent(Wallet.this, ReedemOptions.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }
}

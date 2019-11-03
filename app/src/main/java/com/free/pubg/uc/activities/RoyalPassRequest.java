package com.free.pubg.uc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;

import com.free.pubg.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoyalPassRequest extends AppCompatActivity {

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.go_back)
    void goBack() {
        finish();
    }

    @BindView(R.id.participate_img)
    ImageView participateImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_royal_pass_request);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setupBannerAds();

        int code = getIntent().getIntExtra("already_participated", 1);
        if(code == 1)
        {
            participateImg.setImageDrawable(getResources().getDrawable(R.drawable.royal_pass_already_participated_img));
        }
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
                /*String banner_points = SharedPrefManager.getInstance(RoyalPassRequest.this).getBannerPoints();
                Utils.updateUc(RoyalPassRequest.this, banner_points);*/
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

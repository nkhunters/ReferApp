package com.free.pubg.uc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.free.pubg.R;
import com.free.pubg.uc.sharedprefrences.SharedPrefManager;
import com.free.pubg.uc.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyBonus extends AppCompatActivity {

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.go_back)
    void goBack()
    {
        finish();
    }

    @BindView(R.id.daily_bonus_text)
    TextView dailyBonusText;

    @BindView(R.id.already_claimed)
    TextView alreadyClaimed;

    @OnClick(R.id.invite_now_btn)
    void inviteNow()
    {
        String refer_code  = SharedPrefManager.getInstance(DailyBonus.this).getReferId();
        String inviteText = "Hey please check this application " + "https://play.google.com/store/apps/details?id=" + getPackageName() +"&referrer="+refer_code+"";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, inviteText);
        sendIntent.setType("text/*");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_bonus);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setupBannerAds();

        getDailyBonus();
    }

    private void getDailyBonus() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());

        String daily_bonus_date = SharedPrefManager.getInstance(DailyBonus.this).getDailyBonusDate();
        if(daily_bonus_date != null)
        {
            if(!daily_bonus_date.equals(date))
            {
                String dailyBonus = SharedPrefManager.getInstance(DailyBonus.this).getDailyBonus();
                dailyBonusText.setText(dailyBonus + "UC");
                Utils.updateUc(DailyBonus.this, dailyBonus, "Daily Bonus");
                SharedPrefManager.getInstance(DailyBonus.this).saveDailyBonusDate(date);
            }
            else
            {
                alreadyClaimed.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            String dailyBonus = SharedPrefManager.getInstance(DailyBonus.this).getDailyBonus();
            dailyBonusText.setText(dailyBonus + "UC");
            Utils.updateUc(DailyBonus.this, dailyBonus, "Daily Bonus");
            SharedPrefManager.getInstance(DailyBonus.this).saveDailyBonusDate(date);
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

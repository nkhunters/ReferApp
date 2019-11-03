package com.free.pubg.uc.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.free.pubg.R;
import com.free.pubg.uc.api.RetrofitClient;
import com.free.pubg.uc.models.DefaultResponse;
import com.free.pubg.uc.sharedprefrences.SharedPrefManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LuckySpin extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 300000;
    private long timeCollapsed;
    private CountDownTimer timer;

    @BindView(R.id.adView)
    AdView mAdView;

    private RewardedAd luckySpinAd;

    @OnClick(R.id.go_back)
    void goBack() {
        finish();
    }

    private static final String[] sectors = {"0.05", "0.10", "0.15", "0.30",
            "0.25", "0.20", "0.60", "0.50", "0.40", "0.35", "0", "0.01"};
    private static final Random RANDOM = new Random();
    private static final float HALF_SECTOR = 360f / 12f / 2f;
    @BindView(R.id.spinBtn)
    Button spinBtn;
    @BindView(R.id.resultTv)
    TextView resultTv;
    @BindView(R.id.wheel)
    ImageView wheel;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorlayout;

    boolean isRunning = false;

    private int degree = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_spin);
        ButterKnife.bind(this);

        setupBannerAds();
        setupRewardAds();
    }

    private void setupRewardAds() {

        luckySpinAd = createAndLoadRewardedAd(getString(R.string.reward_id));
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

    RewardedAdCallback spinAdCallback = new RewardedAdCallback() {
        @Override
        public void onRewardedAdOpened() {
            // Ad opened.
            lockSpinner();
        }

        @Override
        public void onRewardedAdClosed() {
            // Ad closed.
            luckySpinAd = createAndLoadRewardedAd(getString(R.string.reward_id));
        }

        @Override
        public void onUserEarnedReward(@NonNull RewardItem reward) {
            // User earned reward.
            /*String reward_points = SharedPrefManager.getInstance(LuckySpin.this).getRewardPoints();
            Utils.updateUc(LuckySpin.this, reward_points);*/
        }

        @Override
        public void onRewardedAdFailedToShow(int errorCode) {
            // Ad failed to display
            lockSpinner();
        }
    };

    private void lockSpinner() {

        spinBtn.setClickable(false);

        long currentTime = SystemClock.elapsedRealtime();
        long storedTime = SharedPrefManager.getInstance(LuckySpin.this).getCurrentTime();
        long spinnerElapsedTime = SharedPrefManager.getInstance(LuckySpin.this).getSpinnerTime();
        long spinTime = 0;
        if (storedTime != 0)
        {
            spinTime = spinnerElapsedTime - (currentTime - storedTime) ;
        }

        Log.d("niraj", "current - "+ ((currentTime/1000)/60) +":"+ ((currentTime/1000)%60));
        Log.d("niraj", "stored - "+ ((storedTime/1000)/60) +":"+ ((storedTime/1000)%60));
        Log.d("niraj", "spintime "+spinTime);
        Log.d("niraj", "spin - "+ ((spinTime/1000)/60) +":"+ ((spinTime/1000)%60));


        if (isRunning == false) {
            timer = new CountDownTimer(spinTime <= 0 ? START_TIME_IN_MILLIS : spinTime, 1000) {

                public void onTick(long millisUntilFinished) {
                    isRunning = true;
                    timeCollapsed = millisUntilFinished;
                    long minutes = (millisUntilFinished / 1000) / 60;
                    long seconds = (millisUntilFinished / 1000) % 60;
                    spinBtn.setText(minutes + ":" + seconds);
                }

                public void onFinish() {
                    isRunning = false;
                    spinBtn.setClickable(true);
                    spinBtn.setText("Click to Spin");
                    SharedPrefManager.getInstance(LuckySpin.this).saveSpinnerTime(0);
                }
            }.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.spinBtn)
    public void spin(View v) {

        int degreeOld = degree % 360;
        // we calculate random angle for rotation of our wheel
        degree = RANDOM.nextInt(360) + 720;
        // rotation effect on the center of the wheel
        RotateAnimation rotateAnim = new RotateAnimation(degreeOld, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(3600);
        rotateAnim.setFillAfter(true);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        rotateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // we empty the result text view when the animation start
                resultTv.setText("");

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // we display the correct sector pointed by the triangle at the end of the rotate animation
                try {
                    String points = getSector(360 - (degree % 360));
                    updateRewards(points);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // we start the animation
        wheel.startAnimation(rotateAnim);
    }

    private void updateRewards(String points) {

        Log.d("niraj", points);
        String user_id = SharedPrefManager.getInstance(LuckySpin.this).getUserId();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateUc(user_id, "Lucky Spin Bonus", points);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {

                if (!(response.body() != null ? response.body().getError() : null)) {
                    if (points.equalsIgnoreCase("0")) {
                        resultTv.setText("Better luck next time");
                    } else {
                        resultTv.setText("You earned " + points + " UC");
                    }

                    String oldPoints = SharedPrefManager.getInstance(LuckySpin.this).getUc();
                    float newPoints = Float.parseFloat(oldPoints) + Float.parseFloat(points);
                    SharedPrefManager.getInstance(LuckySpin.this).saveUc(String.valueOf(newPoints));

                    luckySpinAd.show(LuckySpin.this, spinAdCallback);
                    //SharedPrefManager.getInstance(Spinner.this).updateSpin(false);
                } else
                    Snackbar.make(coordinatorlayout, "Something went wrong. Please check your internet.", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                Snackbar.make(coordinatorlayout, "Something went wrong. Please check your internet.", Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    private String getSector(int degrees) {
        int i = 0;
        String text = null;

        do {
            // start and end of each sector on the wheel
            float start = HALF_SECTOR * (i * 2 + 1);
            float end = HALF_SECTOR * (i * 2 + 3);

            if (degrees >= start && degrees < end) {

                text = sectors[i];

            }

            i++;
        } while (text == null && i < sectors.length);

        return text;
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
//                String banner_points = SharedPrefManager.getInstance(LuckySpin.this).getBannerPoints();
//                Utils.updateUc(LuckySpin.this, banner_points);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(LuckySpin.this).getSpinnerTime() != 0 ) {

            long currentTime = SystemClock.elapsedRealtime();
            long storedTime = SharedPrefManager.getInstance(LuckySpin.this).getCurrentTime();
            long spinnerElapsedTime = SharedPrefManager.getInstance(LuckySpin.this).getSpinnerTime();
            long spinTime = 0;
            if (storedTime != 0)
            {
                spinTime = spinnerElapsedTime - (currentTime - storedTime) ;
            }
            if(spinTime > 0)
                lockSpinner();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPrefManager.getInstance(LuckySpin.this).saveSpinnerTime(timeCollapsed);
        SharedPrefManager.getInstance(LuckySpin.this).saveCurrentTime(SystemClock.elapsedRealtime());
        isRunning = false;
    }

}

package com.free.pubg.free.uc.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.free.pubg.R;
import com.free.pubg.free.uc.api.RetrofitClient;
import com.free.pubg.free.uc.sharedprefrences.SharedPrefManager;
import com.free.pubg.free.uc.models.AdPoints;
import com.free.pubg.free.uc.utils.Logout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.content)
    RelativeLayout content;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_name)
    TextView toolbar_name;

    @BindView(R.id.uc_text)
    Button ucText;

    @OnClick(R.id.uc_text)
    void openWallet() {
        startActivity(new Intent(MainActivity.this, Wallet.class));
    }

    @OnClick(R.id.royal_box)
    void royalBox() {
        Toast.makeText(MainActivity.this, "Royal box not available. Please try after sometime.", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.win_royal_pass)
    void winRoyalPass() {
        startActivity(new Intent(MainActivity.this, RoyalPass.class));
    }

    @OnClick(R.id.menu_icon)
    void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.spin_now)
    void spinNow() {

        luckySpinAd.show(MainActivity.this, spinAdCallback);
    }

    @OnClick(R.id.daily_bonus)
    void dailyBonus() {
        dailyBonusAd.show(MainActivity.this, dailyBonusAdCallback);
    }

    @OnClick(R.id.extra_bonus)
    void extraBonus() {
        extraBonusAd.show(MainActivity.this, extraBonusAdCallback);
    }

    @OnClick(R.id.invite_earn)
    void inviteEarn() {

        //inviteAd.show(MainActivity.this, inviteAdCallback);
        startActivity(new Intent(MainActivity.this, InviteAndEarn.class));
    }


    String strUserName, strUserEmail, strUc, strUserId;
    private RewardedAd luckySpinAd;
    private RewardedAd dailyBonusAd;
    private RewardedAd extraBonusAd;
    private RewardedAd inviteAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setupDrawer();
        showTelegramDialog();
        getUser();
        getUc();
        setupDrawerHeader();
        getAdPoints();

        navigationView.setNavigationItemSelectedListener(this);

        MobileAds.initialize(this, initializationStatus -> {
        });

        setupBannerAds();
        setupRewardAds();
    }

    private void showTelegramDialog() {

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.telegram_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        Button subscribe = dialog.findViewById(R.id.subscribe);
        Button later = dialog.findViewById(R.id.later);

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://t.me/joinchat/AAAAAFSd-xsNmR8inzMPPw";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                dialog.dismiss();
            }
        });
    }

    private void setupRewardAds() {

        luckySpinAd = createAndLoadRewardedAd(getString(R.string.reward_id));
        dailyBonusAd = createAndLoadRewardedAd(getString(R.string.reward_id));
        extraBonusAd = createAndLoadRewardedAd(getString(R.string.reward_id));
        inviteAd = createAndLoadRewardedAd(getString(R.string.reward_id));
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
                /*String banner_points = SharedPrefManager.getInstance(MainActivity.this).getBannerPoints();
                Utils.updateUc(MainActivity.this, banner_points);
                String newPoints = SharedPrefManager.getInstance(MainActivity.this).getUc();
                ucText.setText(String.format("%.2f UC", Float.parseFloat(newPoints)));*/
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

    private void getUc() {

        Call<String> call = RetrofitClient.getInstance().getApi().getUc(strUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.body() != null) {
                    SharedPrefManager.getInstance(MainActivity.this).saveUc(response.body());
                    strUc = SharedPrefManager.getInstance(MainActivity.this).getUc();
                    ucText.setText(String.format("%.2f UC", Float.parseFloat(strUc)));
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

            }
        });
    }

    private void getAdPoints() {

        Call<AdPoints> call = RetrofitClient.getInstance().getApi().getAdPoints();
        call.enqueue(new Callback<AdPoints>() {
            @Override
            public void onResponse(@NotNull Call<AdPoints> call, @NotNull Response<AdPoints> response) {
                if (response.body() != null) {
                    AdPoints adPoints = response.body();
                    String banner_points = adPoints.getBanner();
                    String interstitial_points = adPoints.getInterstitial();
                    String reward_points = adPoints.getReward();
                    String daily_bonus = adPoints.getDaily_bonus();

                    SharedPrefManager.getInstance(MainActivity.this).saveBannerPoints(banner_points);
                    SharedPrefManager.getInstance(MainActivity.this).saveInterstitialPoints(interstitial_points);
                    SharedPrefManager.getInstance(MainActivity.this).saveRewardPoints(reward_points);
                    SharedPrefManager.getInstance(MainActivity.this).saveDailyBonus(daily_bonus);
                }

            }

            @Override
            public void onFailure(@NotNull Call<AdPoints> call, @NotNull Throwable t) {

            }
        });
    }

    private void getUser() {

        strUserName = SharedPrefManager.getInstance(MainActivity.this).getUserName();
        strUserEmail = SharedPrefManager.getInstance(MainActivity.this).getEmail();
        strUserId = SharedPrefManager.getInstance(MainActivity.this).getUserId();
    }

    private void setupDrawerHeader() {

        View headerLayout = navigationView.getHeaderView(0);
        TextView name = headerLayout.findViewById(R.id.user_name);
        TextView email = headerLayout.findViewById(R.id.user_email);

        name.setText(strUserName);
        email.setText(strUserEmail);
        toolbar_name.setText(strUserName);
    }

    private void setupDrawer() {

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
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
        }

        @Override
        public void onRewardedAdClosed() {
            // Ad closed.
            luckySpinAd = createAndLoadRewardedAd(getString(R.string.reward_id));
            startActivity(new Intent(MainActivity.this, LuckySpin.class));
        }

        @Override
        public void onUserEarnedReward(@NonNull RewardItem reward) {
            // User earned reward.
            /*Log.d("niraj", reward.getAmount() + "");
            String reward_points = SharedPrefManager.getInstance(MainActivity.this).getRewardPoints();
            Utils.updateUc(MainActivity.this, reward_points);
            strUc = SharedPrefManager.getInstance(MainActivity.this).getUc();
            ucText.setText(String.format("%.2f UC", Float.parseFloat(strUc)));*/
        }

        @Override
        public void onRewardedAdFailedToShow(int errorCode) {
            // Ad failed to display
            startActivity(new Intent(MainActivity.this, LuckySpin.class));
        }
    };

    RewardedAdCallback dailyBonusAdCallback = new RewardedAdCallback() {
        @Override
        public void onRewardedAdOpened() {
            // Ad opened.
        }

        @Override
        public void onRewardedAdClosed() {
            // Ad closed.
            dailyBonusAd = createAndLoadRewardedAd(getString(R.string.reward_id));
            startActivity(new Intent(MainActivity.this, DailyBonus.class));
        }

        @Override
        public void onUserEarnedReward(@NonNull RewardItem reward) {
            // User earned reward.
            /*Log.d("niraj", reward.getAmount() + "");
            String reward_points = SharedPrefManager.getInstance(MainActivity.this).getRewardPoints();
            Utils.updateUc(MainActivity.this, reward_points);
            strUc = SharedPrefManager.getInstance(MainActivity.this).getUc();
            ucText.setText(String.format("%.2f UC", Float.parseFloat(strUc)));*/
        }

        @Override
        public void onRewardedAdFailedToShow(int errorCode) {
            // Ad failed to display
            startActivity(new Intent(MainActivity.this, DailyBonus.class));
        }
    };

    RewardedAdCallback extraBonusAdCallback = new RewardedAdCallback() {
        @Override
        public void onRewardedAdOpened() {
            // Ad opened.
        }

        @Override
        public void onRewardedAdClosed() {
            // Ad closed.
            extraBonusAd = createAndLoadRewardedAd(getString(R.string.reward_id));
            //startActivity(new Intent(MainActivity.this, LuckySpin.class));
        }

        @Override
        public void onUserEarnedReward(@NonNull RewardItem reward) {
            // User earned reward.
            /*Log.d("niraj", reward.getAmount() + "");
            String reward_points = SharedPrefManager.getInstance(MainActivity.this).getRewardPoints();
            Utils.updateUc(MainActivity.this, reward_points);
            strUc = SharedPrefManager.getInstance(MainActivity.this).getUc();
            ucText.setText(String.format("%.2f UC", Float.parseFloat(strUc)));*/
        }

        @Override
        public void onRewardedAdFailedToShow(int errorCode) {
            // Ad failed to display
        }
    };

    RewardedAdCallback inviteAdCallback = new RewardedAdCallback() {
        @Override
        public void onRewardedAdOpened() {
            // Ad opened.
        }

        @Override
        public void onRewardedAdClosed() {
            // Ad closed.
            inviteAd = createAndLoadRewardedAd(getString(R.string.reward_id));
            startActivity(new Intent(MainActivity.this, InviteAndEarn.class));
        }

        @Override
        public void onUserEarnedReward(@NonNull RewardItem reward) {
            // User earned reward.
            /*Log.d("niraj", reward.getAmount() + "");
            String reward_points = SharedPrefManager.getInstance(MainActivity.this).getRewardPoints();
            Utils.updateUc(MainActivity.this, reward_points);
            strUc = SharedPrefManager.getInstance(MainActivity.this).getUc();
            ucText.setText(String.format("%.2f UC", Float.parseFloat(strUc)));*/

        }

        @Override
        public void onRewardedAdFailedToShow(int errorCode) {
            // Ad failed to display
            startActivity(new Intent(MainActivity.this, InviteAndEarn.class));
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.logout:
                Logout.logout(MainActivity.this);
                break;
            case R.id.wallet:
                startActivity(new Intent(MainActivity.this, Wallet.class));
                break;
            case R.id.rate_us:
            case R.id.update:
            case R.id.feedback:
                openMyAppInPlaystore();
                break;
            case R.id.how_it_work:
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "cMlP1y54iDo"));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + "cMlP1y54iDo"));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            break;
            case R.id.privacy_policy:
                String url = "https://www.freeprivacypolicy.com/privacy/view/742bd16c3be78fc03810a9bdacd5bfb0";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
        return true;
    }

    private void openMyAppInPlaystore() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}

package com.free.pubg.free.uc.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.free.pubg.R;
import com.free.pubg.free.uc.api.RetrofitClient;
import com.free.pubg.free.uc.sharedprefrences.SharedPrefManager;
import com.free.pubg.free.uc.models.DefaultResponse;
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

public class ReedemOptions extends AppCompatActivity {

    String user_id, name, strCurrUc;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.go_back)
    void goBack()
    {
        finish();
    }

    @OnClick({R.id.redeem_paytm_layout, R.id.redeem_btn_paytm})
    void redeemPaytm()
    {
        if(Float.parseFloat(strCurrUc) < 100)
            return;

        Dialog dialog = new Dialog(ReedemOptions.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reedem_paytm_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        Button laterBtn = dialog.findViewById(R.id.later_btn);
        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button submitBtn = dialog.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText mobile = dialog.findViewById(R.id.mobile);
                String strMobile = mobile.getText().toString().trim();
                if(TextUtils.isEmpty(strMobile))
                {
                    mobile.setError("Please enter Paytm number");
                    mobile.requestFocus();
                    return;
                }

                sendRequest(dialog,"Paytm", strMobile, 100);
            }
        });
    }

    @OnClick({R.id.layout_1, R.id.redeem_75_uc})
    void redeem75Uc()
    {
        if(Float.parseFloat(strCurrUc) < 75)
            return;

        Dialog dialog = new Dialog(ReedemOptions.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reedem_pubg_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        Button laterBtn = dialog.findViewById(R.id.later_btn);
        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button submitBtn = dialog.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText pubgId = dialog.findViewById(R.id.pubg_id);
                String strPubgId = pubgId.getText().toString().trim();
                if(TextUtils.isEmpty(strPubgId))
                {
                    pubgId.setError("Please enter Pubg Id");
                    pubgId.requestFocus();
                    return;
                }

                sendRequest(dialog,"Pubg", strPubgId, 75);
            }
        });
    }

    @OnClick({R.id.layout_2, R.id.redeem_220_uc})
    void redeem220Uc()
    {
        if(Float.parseFloat(strCurrUc) < 220)
            return;

        Dialog dialog = new Dialog(ReedemOptions.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reedem_pubg_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        Button laterBtn = dialog.findViewById(R.id.later_btn);
        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button submitBtn = dialog.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText pubgId = dialog.findViewById(R.id.pubg_id);
                String strPubgId = pubgId.getText().toString().trim();
                if(TextUtils.isEmpty(strPubgId))
                {
                    pubgId.setError("Please enter Pubg Id");
                    pubgId.requestFocus();
                    return;
                }

                sendRequest(dialog,"Pubg", strPubgId, 220);
            }
        });
    }

    @OnClick({R.id.layout_3, R.id.redeem_770_uc})
    void redeem770Uc()
    {
        if(Float.parseFloat(strCurrUc) < 770)
            return;

        Dialog dialog = new Dialog(ReedemOptions.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reedem_pubg_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        Button laterBtn = dialog.findViewById(R.id.later_btn);
        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button submitBtn = dialog.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText pubgId = dialog.findViewById(R.id.pubg_id);
                String strPubgId = pubgId.getText().toString().trim();
                if(TextUtils.isEmpty(strPubgId))
                {
                    pubgId.setError("Please enter Pubg Id");
                    pubgId.requestFocus();
                    return;
                }

                sendRequest(dialog,"Pubg", strPubgId, 770);
            }
        });
    }

    @OnClick({R.id.layout_4, R.id.redeem_2010_uc})
    void redeem2010Uc()
    {
        if(Float.parseFloat(strCurrUc) < 2010)
            return;

        Dialog dialog = new Dialog(ReedemOptions.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reedem_pubg_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        Button laterBtn = dialog.findViewById(R.id.later_btn);
        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button submitBtn = dialog.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText pubgId = dialog.findViewById(R.id.pubg_id);
                String strPubgId = pubgId.getText().toString().trim();
                if(TextUtils.isEmpty(strPubgId))
                {
                    pubgId.setError("Please enter Pubg Id");
                    pubgId.requestFocus();
                    return;
                }

                sendRequest(dialog,"Pubg", strPubgId, 2010);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reedem_options);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        user_id = SharedPrefManager.getInstance(ReedemOptions.this).getUserId();
        name = SharedPrefManager.getInstance(ReedemOptions.this).getUserName();
        String strUc = SharedPrefManager.getInstance(ReedemOptions.this).getUc();
        strCurrUc = String.format("%.2f", Float.parseFloat(strUc));

        setupBannerAds();
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

    private void sendRequest(Dialog dialog, String type, String strMobilePubgId, int uc) {

        ProgressDialog progressDialog = new ProgressDialog(ReedemOptions.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().redeemRequest(user_id, name, type, strMobilePubgId, uc);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {

                progressDialog.dismiss();
                if(response.body() != null && response.code() == 201)
                {
                    new AlertDialog.Builder(ReedemOptions.this)
                            .setMessage("Your request has been sent. Please wait for approval.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    dialog.dismiss();
                                    finish();
                                    startActivity(new Intent(ReedemOptions.this, MainActivity.class));
                                }
                            }).create().show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(ReedemOptions.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}

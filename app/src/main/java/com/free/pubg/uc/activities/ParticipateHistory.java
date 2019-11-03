package com.free.pubg.uc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.free.pubg.R;
import com.free.pubg.uc.adapters.HistoryAdapter;
import com.free.pubg.uc.adapters.RoyalPassHistoryAdapter;
import com.free.pubg.uc.api.RetrofitClient;
import com.free.pubg.uc.models.RoyalPassHistoryResponse;
import com.free.pubg.uc.models.Winner;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParticipateHistory extends AppCompatActivity {

    // A banner ad is placed in every 8th position in the RecyclerView.
    public static final int ITEMS_PER_AD = 8;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.adView)
    AdView mAdView;
    
    @OnClick(R.id.go_back)
    void goBack()
    {
        finish();
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    // List of banner ads and MenuItems that populate the RecyclerView.
    private List<Object> recyclerViewItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate_history);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        
        setupBannerAds();

        recyclerView.setHasFixedSize(true);

        // Specify a linear layout manager.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Update the RecyclerView item's list with menu items and banner ads.
        addMenuItemsFromJson();
        addBannerAds();
        loadBannerAds();

        // Specify an adapter.
        RecyclerView.Adapter<RecyclerView.ViewHolder> adapter = new HistoryAdapter(this,
                recyclerViewItems);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        for (Object item : recyclerViewItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.resume();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        for (Object item : recyclerViewItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.pause();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        for (Object item : recyclerViewItems) {
            if (item instanceof AdView) {
                AdView adView = (AdView) item;
                adView.destroy();
            }
        }
        super.onDestroy();
    }

    /**
     * Adds banner ads to the items list.
     */
    private void addBannerAds() {
        // Loop through the items array and place a new banner ad in every ith position in
        // the items List.
        for (int i = 0; i <= recyclerViewItems.size(); i += ITEMS_PER_AD) {
            final AdView adView = new AdView(ParticipateHistory.this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getResources().getString(R.string.banner_id));
            recyclerViewItems.add(i, adView);
        }
    }

    /**
     * Sets up and loads the banner ads.
     */
    private void loadBannerAds() {
        // Load the first banner ad in the items list (subsequent ads will be loaded automatically
        // in sequence).
        loadBannerAd(0);
    }

    /**
     * Loads the banner ads in the items list.
     */
    private void loadBannerAd(final int index) {

        if (index >= recyclerViewItems.size()) {
            return;
        }

        Object item = recyclerViewItems.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad"
                    + " ad.");
        }

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous banner ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadBannerAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous banner ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("niraj", "The previous banner ad failed to load. Attempting to"
                        + " load the next banner ad in the items list.");
                loadBannerAd(index + ITEMS_PER_AD);
            }
        });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
    }


    private void addMenuItemsFromJson() {

        ProgressDialog progressDialog = new ProgressDialog(ParticipateHistory.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<RoyalPassHistoryResponse> call = RetrofitClient.getInstance().getApi().getRoyalPassHistory();
        call.enqueue(new Callback<RoyalPassHistoryResponse>() {
            @Override
            public void onResponse(@NotNull Call<RoyalPassHistoryResponse> call, @NotNull Response<RoyalPassHistoryResponse> response) {

                progressDialog.dismiss();

                if(response.body() != null)
                {
                    Log.d("niraj", response.body().getRoyal_pass_history().toString());
                    ArrayList<Winner> dataList = response.body().getRoyal_pass_history();
                    for(Winner winner : dataList)
                    {
                        Object object = (Object) winner;
                        recyclerViewItems.add(object);
                    }

                }
            }

            @Override
            public void onFailure(Call<RoyalPassHistoryResponse> call, Throwable t) {

                progressDialog.dismiss();
            }
        });
    }



    /*private void getData() {

        ProgressDialog progressDialog = new ProgressDialog(ParticipateHistory.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<RoyalPassHistoryResponse> call = RetrofitClient.getInstance().getApi().getRoyalPassHistory();
        call.enqueue(new Callback<RoyalPassHistoryResponse>() {
            @Override
            public void onResponse(@NotNull Call<RoyalPassHistoryResponse> call, @NotNull Response<RoyalPassHistoryResponse> response) {

                progressDialog.dismiss();

                if(response.body() != null)
                {
                    setupRecyclerView(response.body().getRoyal_pass_history());
                }
            }

            @Override
            public void onFailure(Call<RoyalPassHistoryResponse> call, Throwable t) {

                progressDialog.dismiss();
            }
        });
    }*/

    private void setupRecyclerView(ArrayList<Winner> history) {

        recyclerView.setLayoutManager(new LinearLayoutManager(ParticipateHistory.this));
        RoyalPassHistoryAdapter adapter = new RoyalPassHistoryAdapter(ParticipateHistory.this, history);
        recyclerView.setAdapter(adapter);
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
}

package com.free.pubg.free.uc.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.free.pubg.R;
import com.free.pubg.free.uc.api.RetrofitClient;
import com.free.pubg.free.uc.sharedprefrences.SharedPrefManager;
import com.free.pubg.free.uc.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;

    @OnClick(R.id.login_with_google)
    void loginWithGoogle() {
        signIn();
    }

    @OnClick(R.id.login_with_fb)
    void loginWithFb() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Configure sign-in to request the user's ID, email address, and basic
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        setUpFbLogin();
    }

    private void setUpFbLogin() {

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        getUserDetailsFb(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private void startMainActivity() {

        finish();
        startActivity(new Intent(Login.this, MainActivity.class));
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            getUserDetailsGmail(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("niraj", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void getUserDetailsGmail(GoogleSignInAccount account) {

        String name = account.getDisplayName();
        String email = account.getEmail();
        saveUserInPrefs(name, email, account.getId());
    }

    private void saveUserInPrefs(String name, String email, String user_id) {

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<User> call = RetrofitClient.getInstance().getApi().register(user_id, name, email, getRandomReferer());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                progressDialog.dismiss();
                if(response.body() != null)
                {
                    User user = response.body();
                    String id = user.getId();
                    String uc = user.getUc();
                    String refer_id = user.getRefer_id();
                    if(! id.equals("0"))
                    {
                        SharedPrefManager.getInstance(Login.this).saveUserId(id);
                        SharedPrefManager.getInstance(Login.this).saveUserName(name);
                        SharedPrefManager.getInstance(Login.this).saveEmail(email);
                        SharedPrefManager.getInstance(Login.this).saveUc(uc);
                        SharedPrefManager.getInstance(Login.this).saveReferId(refer_id);
                        startMainActivity();
                    }
                    else
                    {
                        showToast("Something went wrong. Please try again");
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                showToast("Something went wrong. Please try again");
            }
        });
    }

    public static String getRandomReferer() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    private void showToast(String s) {

        Toast.makeText(Login.this, s, Toast.LENGTH_LONG).show();
    }

    private void getUserDetailsFb(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());

                        // Application code
                        try {
                            String name = object.getString("name");
                            saveUserInPrefs(name, "", accessToken.getUserId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        String user_id = SharedPrefManager.getInstance(Login.this).getUserId();

        if ((account != null || isLoggedIn) && user_id != null) {
            startMainActivity();
        }
    }

}


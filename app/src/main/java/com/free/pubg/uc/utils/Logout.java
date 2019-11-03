package com.free.pubg.uc.utils;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.free.pubg.uc.activities.Login;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class Logout {

    public static void logout(Context context) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

        if (account != null)
            logoutGoogle(context);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            LoginManager.getInstance().logOut();
            ((AppCompatActivity) context).finish();
            context.startActivity(new Intent(context, Login.class));
        }
    }

    private static void logoutGoogle(Context context) {
        // Configure sign-in to request the user's ID, email address, and basic
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        mGoogleSignInClient.signOut();
        ((AppCompatActivity) context).finish();
        context.startActivity(new Intent(context, Login.class));
    }

}

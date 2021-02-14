package lib.xdsdk.passport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleHelper {
    private static String mEmail;
    private static GoogleApiClient mGoogleApiClient;

    public static void getUsername(Activity activity) {
        try {
            if (mEmail == null) {
                pickUserAccount(activity);
                return;
            }
            CometPassport.model().signWithGoogleWithEmail(activity, mEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void geteEmail(Activity activity) {
        try {
            if (mEmail == null) {
                pickUserAccount2(activity);
                return;
            }
            CometPassport.model().guestbindingwithGoogle(activity, mEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onActivityResult(Activity activity, int i, int i2, Intent intent) {
        if (i == 1000 && i2 == -1) {
            mEmail = intent.getStringExtra("authAccount");
            SPTools.putString(activity, "mEmail", mEmail);
            getUsername(activity);
        }
        if (i == 1001 && i2 == -1) {
            mEmail = intent.getStringExtra("authAccount");
            geteEmail(activity);
        }
    }

    public static void pickUserAccount(Activity activity) {
        activity.startActivityForResult(AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null), 1000);
    }

    public static void pickUserAccount2(Activity activity) {
        activity.startActivityForResult(AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null), 1001);
    }

    public static void logout(Activity activity) {
        mGoogleApiClient = new GoogleApiClient.Builder(activity).addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("17685588992-jjf727icdguc8hne9nf953nh6edjnt6t.apps.googleusercontent.com").requestEmail().requestId().build()).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            public void onConnectionSuspended(int i) {
            }

            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.clearDefaultAccountAndReconnect();
                    mGoogleApiClient.disconnect();
                    mEmail = null;
                }
            }
        }).build();
        mGoogleApiClient.connect();
        GoogleSignInClient r0 = GoogleSignIn.getClient(activity, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        r0.revokeAccess().addOnCompleteListener(activity, task -> Log.d("GoogleHelperBackup", "revokeAccess+++++" + task.isSuccessful()));
        r0.signOut().addOnCompleteListener(activity, task -> Log.d("GoogleHelperBackup", "signOut+++++" + task.isSuccessful()));
    }
}
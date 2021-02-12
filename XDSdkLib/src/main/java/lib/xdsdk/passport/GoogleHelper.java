package lib.xdsdk.passport;

import android.app.Activity;
import android.content.Intent;
import com.google.android.gms.common.AccountPicker;

public class GoogleHelper {
    private static String mEmail;

    public static void getUsername(Activity activity) {
        try {
            if (mEmail == null) {
                pickUserAccount(activity);
                return;
            }
            CometPassport.model().signWithGoogle(activity, mEmail);
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
}
package lib.xdsdk.passport;

import android.app.Activity;
import android.content.Intent;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import java.util.Arrays;

public class FacebookHelper {
    public static boolean islogin = true;
    private static FacebookHelper m_obj;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    public static FacebookHelper model() {
        if (m_obj != null) {
            return m_obj;
        }
        m_obj = new FacebookHelper();
        return m_obj;
    }

    public void doLogin(final Activity activity) {
        if (this.callbackManager == null) {
            this.callbackManager = CallbackManager.Factory.create();
        }
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Profile r0 = Profile.getCurrentProfile();
                if (r0 == null) {
                    FacebookHelper.this.profileTracker = new ProfileTracker() {

                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            FacebookHelper.this.login(activity, profile2);
                            FacebookHelper.this.profileTracker.stopTracking();
                        }
                    };
                    FacebookHelper.this.profileTracker.startTracking();
                } else {
                    FacebookHelper.this.login(activity, r0);
                }
            }

            @Override
            public void onCancel() { }

            @Override
            public void onError(FacebookException facebookException) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
            }
        });
    }

    public void login(Activity activity, Profile profile) {
        String r0 = profile.getId();
        SPTools.putString(activity, Constants.FACEBOOK_ID, r0);
        SPTools.putString(activity, Constants.ASSCESS_TOKEN, "");
        SPTools.putString(activity, Constants.FB_NAME, profile.getName());
        SPTools.putString(activity, Constants.FB_GENDER, "");
        SPTools.putInt(activity, Constants.FB_AGE, 0);
        if (islogin) {
            CometPassport.model().signWithFacebookWithUid(activity, r0);
        } else {
            CometPassport.model().guestbindingwithFacebook(activity);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.callbackManager != null) {
            this.callbackManager.onActivityResult(i, i2, intent);
        }
    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }
}
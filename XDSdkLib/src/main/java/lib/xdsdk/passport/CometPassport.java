package lib.xdsdk.passport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.Scopes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;

import lib.xdsdk.passport.httpUrlConnectionUtil.HttpUrlConnectionHelper;
import lib.xdsdk.passport.httpUrlConnectionUtil.Key;
import lib.xdsdk.passport.httpUrlConnectionUtil.callback.HttpCallbackModelListener;

public class CometPassport {
    private static OnFacebookLoginCompleteListener FacebookLoginCompleteListener;
    private static OnGoogleLoginCompleteListener GoogleLoginCompleteListener;
    private static OnGuestLoginCompleteListener GuestLoginCompleteListener;
    private static OnWeGamesLoginCompleteListener WeGamesLoginCompleteListener;
    private static OnXdgLoginCompleteListener XdgLoginCompleteListener;
    private static CometPassport m_obj;

    public interface OnFacebookLoginCompleteListener {
        void onFinish(JSONObject jSONObject);
    }

    public interface OnGoogleLoginCompleteListener {
        void onFinish(JSONObject jSONObject);
    }

    public interface OnGuestLoginCompleteListener {
        void onFinish(JSONObject jSONObject);
    }

    public interface OnWeGamesLoginCompleteListener {
        void onFinish(JSONObject jSONObject);
    }

    public interface OnXdgLoginCompleteListener {
        void onFinish(JSONObject jSONObject);
    }

    public void setOnGuestLoginCompleteListener(OnGuestLoginCompleteListener listener) {
        GuestLoginCompleteListener = listener;
    }

    public void setOnGoogleLoginCompleteListener(OnGoogleLoginCompleteListener listener) {
        GoogleLoginCompleteListener = listener;
    }

    public void setOnXdgLoginCompleteListener(OnXdgLoginCompleteListener listener) {
        XdgLoginCompleteListener = listener;
    }

    public void setOnWeGamesLoginCompleteListener(OnWeGamesLoginCompleteListener listener) {
        WeGamesLoginCompleteListener = listener;
    }

    public void setOnFacebookLoginCompleteListener(OnFacebookLoginCompleteListener listener) {
        FacebookLoginCompleteListener = listener;
    }

    public static CometPassport model() {
        if (m_obj != null) {
            return m_obj;
        }
        m_obj = new CometPassport();
        return m_obj;
    }

    public void registerOnActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        GoogleHelper.onActivityResult(activity,requestCode,resultCode,data);
        FacebookHelper.model().onActivityResult(requestCode,resultCode,data);
    }

    public void signWithGuest(final Activity activity) {
        int r6 = SPTools.getInt(activity, Constants.UID, 0);
        HashMap<String, Object> r8 = new HashMap<>();
        r8.put("appid", urlencode("158714"));
        r8.put("ver", 8);
        r8.put("time", System.currentTimeMillis() / 1000);
        r8.put("fuid", urlencode("android_kr_snqx"));
        r8.put("device_id", urlencode(getAndroidID(activity)));
        r8.put("uid", r6);
        r8.put("autologin", "");
        r8.put("device_token", urlencode(""));

        try {
            HttpUrlConnectionHelper.doPost(activity, String.format(Locale.CHINESE, "https://%s/%s/guest", "p.txwy.tw", "api2"), new HttpCallbackModelListener<Object>() {

                @Override
                public void onFinish(Object obj) {
                    Log.d("CometPassport", "signWithGuest--onFinish : " + obj);
                    if ((obj instanceof JSONObject)) {
                        Log.d("Passport", obj.toString());
                        activity.runOnUiThread(() -> GuestLoginCompleteListener.onFinish((JSONObject) obj));
                    }
                }

                @Override
                public void onError(Exception exc) {
                    Log.e("CometPassport", "signWithGuest Exception e: " + exc.toString());
                }
            }, r8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signWithFacebook(final Activity activity) {
        if(!FacebookSdk.isInitialized()) FacebookSdk.sdkInitialize(activity);
        FacebookHelper.model().doLogin(activity);
    }

    public void signWithFacebookWithUid(final Activity activity, final String FacebookUid) {
        new Thread(() -> {
            try {
                HashMap<String, Object> r6 = new HashMap<>();
                r6.put("appid", "158714");
                r6.put("ver", 8);
                r6.put("time", System.currentTimeMillis() / 1000);
                r6.put("fbid", urlencode(FacebookUid));
                r6.put("fuid", urlencode("android_kr_snqx"));
                r6.put("device_id", urlencode(getAndroidID(activity)));
                r6.put("binding", 1);
                r6.put("accesstoken", "");
                r6.put("fbappid", urlencode("1889637967990346"));
                r6.put("device_token", "");
                HttpUrlConnectionHelper.doPost(activity, String.format(Locale.CHINESE, "https://%s/%s/fb", "p.17996api.com", "api2"), new HttpCallbackModelListener<Object>() {
                    @Override
                    public void onFinish(Object obj) {
                        if ((obj instanceof JSONObject)) {
                            FacebookLoginCompleteListener.onFinish((JSONObject)obj);
                        }
                    }

                    @Override
                    public void onError(Exception exc) {
                        exc.printStackTrace();
                        Toast.makeText(activity, "Login post Error", Toast.LENGTH_SHORT).show();
                    }
                }, r6);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void guestbindingwithFacebook(final Activity activity) {
        new Thread(() -> {
            try {
                String r1 = SPTools.getString(activity, Constants.FACEBOOK_ID, "");
                HashMap<String, Object> r9 = new HashMap<>();
                r9.put("appid", urlencode("158714"));
                r9.put("ver",8);
                r9.put("time", System.currentTimeMillis() / 1000);
                r9.put("fuid", urlencode("android_kr_snqx"));
                r9.put("uid", "");
                r9.put("fid",urlencode(r1));
                r9.put("device_id", urlencode(getAndroidID(activity)));
                HttpUrlConnectionHelper.doPost(activity, String.format(Locale.CHINESE, "https://%s/%s/guestbinding", "p.17996api.com", "api2"), new HttpCallbackModelListener<Object>() {
                    @Override
                    public void onFinish(Object obj) {
                        if ((obj instanceof JSONObject)) {
                            FacebookLoginCompleteListener.onFinish((JSONObject)obj);
                        }
                    }

                    @Override
                    public void onError(Exception exc) {
                        exc.printStackTrace();
                        Toast.makeText(activity, "Login post Error", Toast.LENGTH_SHORT).show();
                    }
                }, r9);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void signInWithXdg(final Activity activity, final String id, String pw) {
        new Thread(() -> {
            try {
                HashMap<String, Object> r8 = new HashMap<>();
                r8.put("appid", urlencode("158714"));
                r8.put("ver", 8);
                r8.put("time", System.currentTimeMillis() / 1000);
                String r9 = id;
                if (id.indexOf("@") == 0) {
                    r9 = id.substring(1);
                }
                r8.put("username", urlencode(r9));
                r8.put("password", urlencode(pw));
                r8.put("fuid", urlencode("android_kr_snqx"));
                r8.put("device_id", urlencode(getAndroidID(activity)));
                r8.put("binding", 1);
                r8.put("autologin", urlencode(""));
                r8.put("device_token", urlencode(""));
                HttpUrlConnectionHelper.doPost(activity, String.format(Locale.CHINESE, "https://%s/%s/signin", "p.17996api.com", "api2"), new HttpCallbackModelListener<Object>() {
                    @Override
                    public void onFinish(Object obj) {
                        if ((obj instanceof JSONObject)) {
                            XdgLoginCompleteListener.onFinish((JSONObject) obj);
                        }
                    }

                    @Override
                    public void onError(Exception exc) {
                        exc.printStackTrace();
                        Toast.makeText(activity, "Login post Error", Toast.LENGTH_SHORT).show();
                    }
                }, r8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void signWithGoogle(final Activity activity) {
        GoogleHelper.pickUserAccount(activity);
    }

    protected void signWithGoogleWithEmail(final Activity activity, final String email) {
        new Thread(() -> {
            String r3 = SPTools.getString(activity, Constants.GOOGLE_OPENID, "");
            String r9 = SPTools.getString(activity, Constants.GOOGLE_ID_TOKEN, "");
            HashMap<String, Object> r0 = new HashMap<>();
            r0.put("id", urlencode(r3));
            r0.put("appid", urlencode("158714"));
            r0.put("ver", 8);
            r0.put("time", System.currentTimeMillis() / 1000);
            r0.put("email", urlencode(email));
            r0.put("fuid", urlencode("android_kr_snqx"));
            r0.put("device_id", urlencode(getAndroidID(activity)));
            r0.put("binding", 1);
            r0.put("isguest", urlencode("google"));
            r0.put("device_token", urlencode(""));
            r0.put("idToken", urlencode(r9));
            try {
                HttpUrlConnectionHelper.doPost(activity, String.format(Locale.CHINESE, "http://%s/%s/google?", "p.17996api.com", "api2"), new HttpCallbackModelListener<Object>() {
                    @Override
                    public void onFinish(Object obj) {
                        if ((obj instanceof JSONObject)) {
                            activity.runOnUiThread(() -> GoogleLoginCompleteListener.onFinish((JSONObject) obj));
                        }
                    }

                    @Override
                    public void onError(Exception exc) {
                        exc.printStackTrace();
                        Toast.makeText(activity, "Login post Error", Toast.LENGTH_SHORT).show();
                    }
                }, r0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected void guestbindingwithGoogle(final Activity activity, String email) {
        new Thread(() -> {
            try {
                String r3 = SPTools.getString(activity, Constants.GOOGLE_OPENID, "");
                String r4 = SPTools.getString(activity, Constants.GOOGLE_ID_TOKEN, "");
                int r6 = SPTools.getInt(activity, Constants.UID, 0);
                HashMap<String, Object> r9 = new HashMap<>();
                r9.put("appid", urlencode("158714"));
                r9.put("ver", 8);
                r9.put("time", System.currentTimeMillis() / 1000);
                r9.put(Scopes.OPEN_ID, urlencode(r3) + "");
                r9.put("uid", r6);
                r9.put("fuid", urlencode("android_kr_snqx"));
                r9.put("device_id", urlencode(getAndroidID(activity)));
                r9.put("email", urlencode(email));
                r9.put("idToken", urlencode(r4));
                HttpUrlConnectionHelper.doPost(activity, String.format(Locale.CHINESE, "https://%s/%s/guestbindgoogle2", "p.17996api.com", "api2"), new HttpCallbackModelListener<Object>() {

                    @Override
                    public void onFinish(Object obj) {
                        if ((obj instanceof JSONObject)) {
                            try {
                                Toast.makeText(activity, ((JSONObject) obj).getString("msg"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Exception exc) {
                        Log.e("CometPassport", "guestbindingwithGoogle Exception e: " + exc.toString());
                    }
                }, r9);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void signWithWeGames(final Activity activity, final String id, String pw) {
        new Thread(() -> {
            try {
                HashMap<String, Object> r2 = new HashMap<>();
                r2.put("wg_game_code", "");
                r2.put("wg_method", "user.login");
                r2.put("wg_password", pw);
                r2.put("wg_time", System.currentTimeMillis() / 1000);
                r2.put("wg_username", id);
                HttpUrlConnectionHelper.doPost(activity, "https://api.wegames.com.tw/api/", new HttpCallbackModelListener<Object>() {
                    @Override
                    public void onFinish(Object obj) {
                        if ((obj instanceof JSONObject)) {
                            JSONObject r11 = (JSONObject) obj;
                            activity.runOnUiThread(() -> WeGamesLoginCompleteListener.onFinish(r11));
                        }
                    }

                    @Override
                    public void onError(Exception exc) {
                        Log.d("CometPassport", "signWithWegames Exception :" + exc.toString());
                    }
                }, r2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SuppressLint("HardwareIds")
    private String getAndroidID(Activity activity) {
        return Settings.Secure.getString(activity.getContentResolver(), "android_id");
    }

    public void logout(Activity activity) {
        GoogleHelper.logout(activity);
        if(FacebookSdk.isInitialized()) FacebookHelper.model().logout();
        activity.getSharedPreferences(SPTools.FILE_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static String urlencode(String str) {
        if (str == null) {
            return "";
        }
        try {
            return str.length() == 0 ? "" : URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str;
        } catch (Exception unused2) {
            return "";
        }
    }

    public static String urldecode(String str) {
        if (str == null) {
            return "";
        }
        try {
            return str.length() == 0 ? "" : URLDecoder.decode(str, Key.STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException unused) {
            return str;
        } catch (Exception unused2) {
            return "";
        }
    }
}

package lib.xdsdk.passport;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;

import lib.xdsdk.passport.httpUrlConnectionUtil.HttpUrlConnectioHelper;
import lib.xdsdk.passport.httpUrlConnectionUtil.Key;
import lib.xdsdk.passport.httpUrlConnectionUtil.callback.HttpCallbackModelListener;

public class CometPassport {
    public interface OnGuestLoginCompleteListener {
        void onFinish(JSONObject result);
    }
    private static OnGuestLoginCompleteListener GuestLoginCompleteListener;
    public void setOnGuestLoginCompleteListener(OnGuestLoginCompleteListener listener) {
        GuestLoginCompleteListener = listener;
    }

    public interface OnGoogleLoginCompleteListener {
        void onFinish(JSONObject result);
    }
    private static OnGoogleLoginCompleteListener GoogleLoginCompleteListener;
    public void setOnGoogleLoginCompleteListener(OnGoogleLoginCompleteListener listener) {
        GoogleLoginCompleteListener = listener;
    }

    public interface OnXdgLoginCompleteListener {
        void onFinish(JSONObject result);
    }
    private static OnXdgLoginCompleteListener XdgLoginCompleteListener;
    public void setOnXdgLoginCompleteListener(OnXdgLoginCompleteListener listener) {
        XdgLoginCompleteListener = listener;
    }

    private static CometPassport m_obj;
    public static CometPassport model() {
        if (m_obj != null) {
            return m_obj;
        }
        m_obj = new CometPassport();
        return m_obj;
    }

    public void signWithGuest(final Activity activity) {
        String r0 = "158714";
        String r1 = "android_kr_snqx";
        String r2 = Settings.Secure.getString(activity.getContentResolver(), "android_id");
        String r3 = ""; //FirebaseInstanceId.getInstance().getToken();
        final long r6 = System.currentTimeMillis() / 1000;
        int r4 = SPTools.getInt(activity, Constants.UID, 0);
        HashMap r9 = new HashMap();
        r9.put("appid", urlencode(r0));
        r9.put("ver",8);
        r9.put("time", r6);
        r9.put("fuid", urlencode(r1));
        r9.put("device_id", urlencode(r2));
        r9.put("uid", r4);
        r9.put("autologin", "");
        r9.put("device_token", urlencode(r3));

        try {
            HttpUrlConnectioHelper.doPostQueue(activity, String.format(Locale.CHINESE, "https://%s/%s/guest", "p.txwy.tw", "api2"), new HttpCallbackModelListener() {

                @Override
                public void onFinish(Object obj) {
                    Log.d("CometPassport", "signWithGuest--onFinish : " + obj);
                    if ((obj instanceof JSONObject)) {
                        Log.d("Passport", obj.toString());
                        activity.runOnUiThread(() -> GuestLoginCompleteListener.onFinish((JSONObject) obj));
                        //CometPassport.m_obj.processSignin(activity, (JSONObject) obj, r6, XDWayLoginActivity.LOGIN_TYPE_GUEST, "");
                    }
                }

                @Override
                // com.txwy.passport.xdsdk.httpUrlConnectionUtil.callback.HttpCallbackModelListener
                public void onError(Exception exc) {
                    Log.e("CometPassport", "signWithGuest Exception e: " + exc.toString());
                }
            }, r9, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signWithGoogle(final Activity activity) {
        GoogleHelper.pickUserAccount(activity);
    }

    protected void signWithGoogle(final Activity activity, final String str) {
        new Thread(() -> {
            String r0 = SPTools.getString(activity, Constants.GOOGLE_OPENID, "");
            String r1 = "158714";
            String r2 = "android_kr_snqx";
            String r3 = Settings.Secure.getString(activity.getContentResolver(), "android_id");
            String r4 = "google";
            String r5 = "";//FirebaseInstanceId.getInstance().getToken();
            String r6 = SPTools.getString(activity, Constants.GOOGLE_ID_TOKEN, "");
            final long r7 = System.currentTimeMillis() / 1000;

            HashMap r9 = new HashMap();
            r9.put("id", urlencode(r0));
            r9.put("appid", urlencode(r1));
            r9.put("ver", 8);
            r9.put("time", r7);
            r9.put("email", urlencode(str));
            r9.put("fuid", urlencode(r2));
            r9.put("device_id", urlencode(r3));
            r9.put("binding", 1);
            r9.put("isguest", urlencode(r4));
            r9.put("device_token", urlencode(r5));
            r9.put("idToken", urlencode(r6));
            try {
                HttpUrlConnectioHelper.doPostQueue(activity, String.format(Locale.CHINESE, "http://%s/api2/google?", "p.17996api.com", "api"), new HttpCallbackModelListener<Object>() {
                    @Override
                    public void onFinish(Object obj) {
                        if ((obj instanceof JSONObject)) {
                            activity.runOnUiThread(() -> {
                                GoogleLoginCompleteListener.onFinish((JSONObject) obj);
                            });
                            //CometPassport.m_obj.processSignin(activity, (JSONObject) obj, r7, XDWayLoginActivity.LOGIN_TYPE_GOOGLE, str);
                        }
                    }

                    @Override
                    public void onError(Exception exc) {
                        exc.printStackTrace();
                        Toast.makeText(activity, "Login post Error", Toast.LENGTH_SHORT).show();
                    }
                }, r9, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected void guestbindingwithGoogle(final Activity activity, String str) throws GooglePlayServicesNotAvailableException, IOException, GooglePlayServicesRepairableException {
        String r0 = "158714";
        String r1 = SPTools.getString(activity, Constants.GOOGLE_OPENID, "");
        String r2 = SPTools.getString(activity, Constants.GOOGLE_ID_TOKEN, "");
        String r3 = "android_kr_snqx";
        int r4 = SPTools.getInt(activity, Constants.UID, 0);
        String r9 = Settings.Secure.getString(activity.getContentResolver(), "android_id");
        HashMap r10 = new HashMap();
        r10.put("appid", urlencode(r0));
        r10.put("ver", 8);
        r10.put("time", System.currentTimeMillis() / 1000);
        r10.put("openid", urlencode(r1) + "");
        r10.put("uid", r4);
        r10.put("fuid", urlencode(r3));
        r10.put("device_id", urlencode(r9));
        r10.put("email", urlencode(str));
        r10.put("idToken", urlencode(r2));
        HttpUrlConnectioHelper.doPost(activity, String.format(Locale.CHINESE, "https://%s/%s/guestbindgoogle2", "p.17996api.com", "api2"), new HttpCallbackModelListener<Object>() {

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
        }, r10, "");
    }

    public void signWithWegames(final Activity activity, final String str, String str2) {
        new Thread(() -> {
            try {
                HashMap r2 = new HashMap();
                r2.put("wg_game_code", "");
                r2.put("wg_method", "user.login");
                r2.put("wg_password", str2);
                r2.put("wg_time", Long.valueOf(System.currentTimeMillis() / 1000));
                r2.put("wg_username", str);
                HttpUrlConnectioHelper.doPost(activity, "https://api.wegames.com.tw/api/", new HttpCallbackModelListener<Object>() {
                    @Override
                    public void onFinish(Object obj) {
                        if ((obj instanceof JSONObject)) {
                            JSONObject r11 = (JSONObject) obj;
                            activity.runOnUiThread(() -> XdgLoginCompleteListener.onFinish(r11));
                        }
                    }

                    @Override
                    public void onError(Exception exc) {
                        Log.d("CometPassport", "signWithWegames Exception :" + exc.toString());
                    }
                }, r2, HttpUrlConnectioHelper.TYPE_WEGAMES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void logout(Activity activity) {
        GoogleHelper.logout(activity);
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

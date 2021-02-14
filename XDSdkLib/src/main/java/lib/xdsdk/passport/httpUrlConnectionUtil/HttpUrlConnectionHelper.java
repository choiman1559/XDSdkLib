package lib.xdsdk.passport.httpUrlConnectionUtil;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lib.xdsdk.passport.Constants;
import lib.xdsdk.passport.CustomDeviceIdHelper;
import lib.xdsdk.passport.SPTools;
import lib.xdsdk.passport.httpUrlConnectionUtil.callback.HttpCallbackModelListener;

import static lib.xdsdk.passport.CometPassport.urldecode;
import static lib.xdsdk.passport.CometPassport.urlencode;
import static lib.xdsdk.passport.httpUrlConnectionUtil.Key.STRING_CHARSET_NAME;

public class HttpUrlConnectionHelper {
    private static final String httpHelper = "help";
    static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static String getSig(String str, String str2) {
        try {
            String r2 = URLDecoder.decode(str, STRING_CHARSET_NAME);
            SecretKeySpec r0 = new SecretKeySpec(md5(md5(str2) + "dGjrdfdd").getBytes(STRING_CHARSET_NAME), "HmacSHA1");
            Mac r3 = Mac.getInstance("HmacSHA1");
            r3.init(r0);
            return URLEncoder.encode(Base64.encodeToString(r3.doFinal(r2.getBytes(STRING_CHARSET_NAME)), 0).trim(), STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String md5(String str) {
        try {
            byte[] r5 = MessageDigest.getInstance("MD5").digest(str.getBytes(STRING_CHARSET_NAME));
            StringBuilder r0 = new StringBuilder(r5.length * 2);
            for (byte b : r5) {
                int r3 = b & 255;
                if (r3 < 16) {
                    r0.append("0");
                }
                r0.append(Integer.toHexString(r3));
            }
            return r0.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e2);
        }
    }

    public static String setPost(Activity activity, String str) throws GooglePlayServicesNotAvailableException, IOException, GooglePlayServicesRepairableException {
        DisplayMetrics r1 = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(r1);
        CustomDeviceIdHelper.saveCustomDeviceId(activity);
        return String.format(Locale.CHINESE, "%s&fbl=%s&os=%s&dev=%s&cpu=%s&men=%s&appver=%s&buildnumber=%s&sys=%s&adid=%s&platform=%s&l=%s&guid=%s", str, urlencode(String.format(Locale.CHINESE, "%d_%d", r1.widthPixels, r1.heightPixels)), urlencode(String.format(Locale.CHINESE, "%s(%s)", Build.VERSION.RELEASE, Build.VERSION.CODENAME)), urlencode(String.format(Locale.CHINESE, "%s", Build.MODEL)), urlencode(String.format(Locale.CHINESE, "%s", Build.CPU_ABI)), 0, urlencode(String.format(Locale.CHINESE, "%s", "2.0700_292")), urlencode(String.format(Locale.CHINESE, "%s", Integer.valueOf("292"))), urlencode("android"), urlencode(AdvertisingIdClient.getAdvertisingIdInfo(activity.getApplicationContext()).getId()), urlencode("txwy"), urldecode(urlencode("kr")), urlencode(SPTools.getString(activity, Constants.CUSTOM_DEVICE_ID, "")));
    }

    public static void doPost(final Activity activity, final String str, final HttpCallbackModelListener<Object> httpCallbackModelListener, Map<String, Object> map) throws GooglePlayServicesNotAvailableException, IOException, GooglePlayServicesRepairableException {
        String str3;
        StringBuilder r0 = new StringBuilder();
        if (map != null) {
            for (String str4 : map.keySet()) {
                if (r0.length() != 0) {
                    r0.append("&");
                }
                r0.append(str4);
                r0.append("=");
                r0.append(map.get(str4));
            }
        }
        String r7 = setPost(activity, r0.toString());
        str3 = getSig(r7, "c877be4fa24080987b01a1817b90f7c5");
        final String r8 = r7 + "&sig=" + str3;
        threadPool.execute(() -> {
            HttpURLConnection httpURLConnection = null;
            JSONObject jSONObject;
            try {
                URL r1 = new URL(str);
                httpURLConnection = (HttpURLConnection) r1.openConnection();
                try {
                    httpURLConnection.setRequestProperty("accept", "*/*");
                    httpURLConnection.setRequestProperty("connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    httpURLConnection.setRequestProperty("Content-Length", String.valueOf(r8.length()));
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    PrintWriter r01 = new PrintWriter(httpURLConnection.getOutputStream());
                    r01.write(r8);
                    r01.flush();
                    r01.close();
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream r001 = httpURLConnection.getInputStream();
                        BufferedReader r3 = new BufferedReader(new InputStreamReader(r001, STRING_CHARSET_NAME));
                        StringBuilder r2 = new StringBuilder();
                        while (true) {
                            String r4 = r3.readLine();
                            if (r4 == null) {
                                break;
                            }
                            r2.append(r4);
                        }
                        r3.close();
                        r01.close();
                        try {
                            jSONObject = (JSONObject) new JSONTokener(JsonFilter(r2.toString())).nextValue();
                        } catch (JSONException e3) {
                            e3.printStackTrace();
                            jSONObject = getErroJson();
                        }
                        new ResponseCall<>(activity, httpCallbackModelListener).doScuccess(jSONObject);
                    } else {
                        new ResponseCall<>(activity, httpCallbackModelListener).doFail(new NetworkErrorException("respons:" + httpURLConnection.getResponseMessage() + " err code:" + httpURLConnection.getResponseCode()));
                    }
                    if (httpURLConnection == null) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpURLConnection.disconnect();
        });
    }

    public static JSONObject getErroJson() {
        JSONObject r0 = new JSONObject();
        try {
            r0.put("ret", 1);
            r0.put("msg", "서버 연결 실패, 재시도 해주세요");
            r0.put("httpaction", httpHelper);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r0;
    }


    public static String JsonFilter(String str) {
        return str.substring(str.indexOf("{")).replace("\r\n", "\n");
    }
}

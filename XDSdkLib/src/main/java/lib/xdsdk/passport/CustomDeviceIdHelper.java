package lib.xdsdk.passport;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

import lib.xdsdk.passport.httpUrlConnectionUtil.Key;

public class CustomDeviceIdHelper {
    private static final String DEVICE_CACHE_DIR = "aray/cache/devices";
    private static final String DEVICE_FILE_NAME = ".DEVICES";

    public static void saveCustomDeviceId(Activity activity) {
        String r0 = getDeviceId(activity);
        String r1 = SPTools.getString(activity, Constants.CUSTOM_DEVICE_ID, "");
        if (!TextUtils.isEmpty(r1) && !TextUtils.equals(r0, r1)) {
            saveDeviceId(r1, activity);
            r0 = r1;
        }
        SPTools.putString(activity, Constants.CUSTOM_DEVICE_ID, r0);
    }

    private static String getDeviceId(Activity activity) {
        String r0 = readDeviceId(activity);
        if (!TextUtils.isEmpty(r0)) {
            return r0;
        }
        return getTimeAndroidID(activity);
    }

    private static String getTimeAndroidID(Activity activity) {
        String r0 = UUID.randomUUID().toString();
        saveDeviceId(r0, activity);
        return r0;
    }

    private static String readDeviceId(Context context) {
        File r5 = getDeviceIDFile(context);
        if (r5 == null) {
            return "";
        }
        StringBuilder r0 = new StringBuilder();
        try {
            FileInputStream r1 = new FileInputStream(r5);
            InputStreamReader r6 = new InputStreamReader(r1, Key.STRING_CHARSET_NAME);
            BufferedReader r2 = new BufferedReader(r6);
            while (true) {
                int r3 = r2.read();
                if (r3 > -1) {
                    r0.append((char) r3);
                } else {
                    r2.close();
                    r6.close();
                    r1.close();
                    return r0.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void saveDeviceId(String str, Context context) {
        File r3 = getDeviceIDFile(context);
        if (r3 != null) {
            try {
                FileOutputStream r0 = new FileOutputStream(r3);
                OutputStreamWriter r4 = new OutputStreamWriter(r0, Key.STRING_CHARSET_NAME);
                r4.write(str);
                r4.close();
                r0.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static File getDeviceIDFile(Context context) {
        if (lacksPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") || lacksPermission(context, "android.permission.READ_EXTERNAL_STORAGE")) {
            return null;
        } else if (Environment.getExternalStorageState().equals("mounted")) {
            File r2 = new File(Environment.getExternalStorageDirectory(), DEVICE_CACHE_DIR);
            if (!r2.exists()) {
                r2.mkdirs();
            }
            return new File(r2, DEVICE_FILE_NAME);
        } else {
            File r0 = new File(context.getFilesDir(), DEVICE_CACHE_DIR);
            if (!r0.exists()) {
                r0.mkdirs();
            }
            return new File(r0, DEVICE_FILE_NAME);
        }
    }

    private static boolean lacksPermission(Context context, String str) {
        return Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context, str) == -1;
    }
}
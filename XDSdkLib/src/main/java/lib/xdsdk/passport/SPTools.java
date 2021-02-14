package lib.xdsdk.passport;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SPTools {
    public static String FILE_NAME = "file_name";

    public static void putString(Activity activity, String str, String str2) {
        SharedPreferences r0 = activity.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (r0 != null) {
            SharedPreferences.Editor r1 = r0.edit();
            r1.putString(str, str2).apply();
        }
    }

    public static String getString(Context context, String str, String str2) {
        SharedPreferences r0 = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return r0 != null ? r0.getString(str, str2) : "";
    }

    public static void putInt(Activity activity, String str, int i) {
        SharedPreferences r0 = activity.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        if (r0 != null) {
            SharedPreferences.Editor r1 = r0.edit();
            r1.putInt(str, i).apply();
        }
    }

    public static int getInt(Activity activity, String str, int i) {
        SharedPreferences r0 = activity.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        if (r0 != null) {
            return r0.getInt(str, i);
        }
        return -1;
    }
}

# XDSdkLib

[![License](https://img.shields.io/badge/License-GPL%203.0-yellowgreen.svg)](https://opensource.org/licenses/GPL-3.0)

minimal implementation version of xdsdk .
This library only provides login api.

You can find sample app on [here](https://github.com/choiman1559/XDSdkLib/tree/master/app).

# How-to-use

## 1. download source and edit gradle
-  Download or clone [the source code](https://github.com/choiman1559/XDSdkLib) and copy the `XDSdkLib`folder to your project root directory.

- edit `settings.gradle` file

```
  include ':XDSdkLib' //add this line
  include ':app'
  rootProject.name = "YourProjectName"
```

- edit your app-level `build.gradle` file
```
dependencies {
    	implementation project(':XDSdkLib') //add this line
    	. . .
}
```

- add `android:usesCleartextTraffic` option in manifist if you haven't added it.

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example">
    <application	
	android:usesCleartextTraffic="true" <!-- add this line -->
         . . . >
    </application>
</manifest>
```

- Re-sync project with gradle files.

## 2. To use Google login or Facebook login
 - Add ```INTERNET``` permission in manifist if you haven't added it.

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example">

    <uses-permission android:name="android.permission.INTERNET" />  <!-- add this line -->

    <application
           ...
           
    </application>
</manifest>
```
- (Facebook only) Add the following `meta-data` element, activity for Facebook, activity for Chrome custom tab, and intent filter within the `application` element.
```
 <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id" />
        <activity
            android:name="com.facebook.FacebookActivity"
            android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:label="@string/app_name" />
        <activity
            android:name="com.facebook.CustomTabActivity"
            android:exported="true">
            <intent-filter><action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="@string/fb_login_protocol_scheme" />
            </intent-filter>
        </activity>
```

- edit your app's `strings.xml`

```
    <!-- strings for google login -->
    <string name="gcm_defaultSenderId">17685588992</string>
    <string name="google_api_key">AIzaSyCuCPdvoFJWXRkckmYJ7IatHhFTwYjJie4</string>
    <string name="google_app_id">1:17685588992:android:b7d610e365981e05</string>
    <string name="google_crash_reporting_api_key">AIzaSyCuCPdvoFJWXRkckmYJ7IatHhFTwYjJie4</string>
    <string name="google_login_way">old</string>
    <string name="google_server_client_id">17685588992-jjf727icdguc8hne9nf953nh6edjnt6t.apps.googleusercontent.com</string>
    <string name="google_storage_bucket">snqx-d9cde.appspot.com</string>
    
    <!-- strings for facebook login -->
    <string name="facebook_app_id">1889637967990346</string>
    <string name="fb_login_protocol_scheme">fb1889637967990346</string>
```
- Finally, call `CometPassport.registerOnActivityResult` in the `onActivityResult` method of the activity you want to call the login method to pass the login result to the `GoogleHelper` and `FacebookHelper` class.
```
 @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CometPassport.model().registerOnActivityResult(this,requestCode,resultCode,data);
    }
```

## Usage

#### Classes
in package `lib.xdsdk.passport` :
 - `CometPassport` :  Login and login listener implementation classes
 - `GoogleHelper` :  Provides additional API for Google login
 - `FacebookHelper` : Provides additional API for Facebook login

 #### Methods
 
 in class `CometPassport` :
 ```
 public static CometPassport model()
 ```
 Provides an instance of Cometpassport.
 
-----------------------------------------------
 
 in class `CometPassport` :
 
 ```
 public void signWithGuest(final Activity activity) 
 ```
 Log in with a guest account. At this time, the logged-in account is a one-time account that cannot be reused, and the account created when the data of the app is deleted is also deleted.
 
 -------------------------------------------------
 in class `CometPassport` :

 ```
 public void signWithXdg(final Activity activity, final String ID, String Password)
 ```
 Log in with your XD global account.
 
 -------------------------------------------------
 
 in class `CometPassport` : 
```
 public void signWithGoogle(final Activity activity)
```
Log in using your Google account. 
To use this method, you need to call the `CometPassport.registerOnActivityResult` method in `Activity#onActivityResult`.

--------------------------------------------------

in class `CometPassport` :

 ```
 public void signWithGoogleWithEmail(final Activity activity, final String email)
 ```
 Log in with your Google account using e-mail address.
 this method doesnt't need override `Activity#onActivityResult(int, int, Intent)`.
 
--------------------------------------------------

 in class `CometPassport` :

 ```
 public void signWithFacebook(Activity activity)
 ```
 Log in with your Facebook account.
 To use this method, you need to call the `CometPassport.registerOnActivityResult` method in `Activity#onActivityResult`.
 
 -------------------------------------------------
 
  in class `CometPassport` :

 ```
 public void signWithWeGames(final Activity activity, final String id, String pw)
 ```
 Log in with your WeGame account.
 
 -------------------------------------------------
 
 in class `CometPassport` :
 ```
  public void registerOnActivityResult(Activity activity, int requestCode, int resultCode, Intent data) 
 ```
 
 call this in the `onActivityResult` method of the activity you want to call the login method to pass the login result to the `GoogleHelper` and `FacebookHelper` class. If you do not call this method, you will not be able to log in when you log in through Google or Facebook.
  
  ----------------------------------------------

 in class `CometPassport` : 
 ```
 public void logout(Activity activity)
 ```
 
 Log out of all login sessions.
You may need to clear the app's cache in order to log out completely.

 -------------------------------------------------

 in class `CometPassport` : 
 ```
 public void setOnGuestLoginCompleteListener(OnGuestLoginCompleteListener listener)
 ```
 
This is a listener registration method that is called when logging in as a guest is complete.

-------------------------------------------------

 in class `CometPassport` : 
 ```
 public void setOnXdgLoginCompleteListener(OnXdgLoginCompleteListener listener)
 ```
 
This is a listener registration method that is called when logging in as a Xdg global account is complete.

-------------------------------------------------

 in class `CometPassport` : 
 ```
 public void setOnFacebookLoginCompleteListener(OnFacebookLoginCompleteListener listener)
 ```
 
This is a listener registration method that is called when logging in as a Facebook account is complete.

-------------------------------------------------

 in class `CometPassport` : 
 ```
 public void setOnWeGamesLoginCompleteListener(OnWeGamesLoginCompleteListener listener)
 ```
 
This is a listener registration method that is called when logging in as a WeGame account is complete.

-------------------------------------------------

 in class `CometPassport` : 
 ```
 public void setOnGoogleLoginCompleteListener(OnGoogleLoginCompleteListener listener)
 ```
 
This is a listener registration method that is called when logging in as a google account is complete.

#### Interfaces

 in class `CometPassport` : 
 ```
  public interface OnGuestLoginCompleteListener {
        void onFinish(JSONObject result);
    }
 ```

This is the interface used for `public void setOnGuestLoginCompleteListener(OnGuestLoginCompleteListener listener)`.
 - `JSONObject result`: Provides the login result query value.

-------------------------------------------------

 in class `CometPassport` : 
 ```
  public interface OnGoogleLoginCompleteListener {
        void onFinish(JSONObject result);
    }
 ```

This is the interface used for `public void setOnGoogleLoginCompleteListener(OnGoogleLoginCompleteListener listener)`.
 - `JSONObject result`: Provides the login result query value.

-------------------------------------------------

 in class `CometPassport` : 
 ```
  public interface OnXdgLoginCompleteListener {
        void onFinish(JSONObject result);
    }
 ```

This is the interface used for `public void setOnXdgLoginCompleteListener(OnXdgLoginCompleteListener listener)`.
 - `JSONObject result`: Provides the login result query value.

-------------------------------------------------

 in class `CometPassport` : 
 ```
  public interface OnFacebookLoginCompleteListener {
        void onFinish(JSONObject result);
    }
 ```

This is the interface used for `public void setOnFacebookLoginCompleteListener(OnFacebookLoginCompleteListener listener)`.
 - `JSONObject result`: Provides the login result query value.

-------------------------------------------------

 in class `CometPassport` : 
 ```
  public interface OnWeGamesLoginCompleteListener {
        void onFinish(JSONObject result);
    }
 ```

This is the interface used for `public void setOnWeGamesLoginCompleteListener(OnWeGamesLoginCompleteListener listener)`.
 - `JSONObject result`: Provides the login result query value.

-------------------------------------------------

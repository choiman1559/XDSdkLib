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

## Usage

#### Classes
in package `lib.xdsdk.passport` :
 - `CometPassport` :  Login and login listener implementation classes
 - `GoogleHelper` :  Provides additional API for Google login

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
To use this method, you need to call the `GoogleHelper#public static void onActivityResult(Activity activity, int i, int i2, Intent intent)` method in `Activity#onActivityResult(int, int, Intent)`.

--------------------------------------------------

 in class `CometPassport` : 
 ```
 public void logout(Activity activity)
 ```
 
 Log out of all login sessions.
You may need to clear the app's cache in order to log out completely.

--------------------------------------------------

 in class `GoogleHelper` : 
 ```
 public static void onActivityResult(Activity activity, int i, int i2, Intent intent)
 ```
 It is a method that should be called from `Activity#onActivityResult(int, int, Intent)` when using  `public void signWithGoogle(final Activity activity)` method.

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

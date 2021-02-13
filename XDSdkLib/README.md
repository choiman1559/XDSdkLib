# XDSdkLib

[![License](https://img.shields.io/badge/License-GPL%203.0-yellowgreen.svg)](https://opensource.org/licenses/GPL-3.0)

minimal implementation version of xdsdk .
This library only provides login api.

# How-to-use

## 1. download source and edit gradle
-  Download or clone [the source code](httphttps://github.com/choiman1559/XDSdkLib:// "the source code") and copy the `XDSdkLib`folder to your project root directory.

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

- Re-sync project with gradle files.

## Usage

#### Classes
in package `lib.xdsdk.passport` :
 - `CometPassport` :  Login and login listener implementation classes
 - `GoogleHelper` :  Provides additional API for Google login

 #### Methods
 
 in class `CometPassport` :
 
 ```
 public void signWithGuest(final Activity activity) 
 ```
 Log in with a guest account. At this time, the logged-in account is a one-time account that cannot be reused, and the account created when the data of the app is deleted is also deleted.
 
 -------------------------------------------------
 in class `CometPassport` :

 ```
 public void signWithWegames(final Activity activity, final String ID, String Password)
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

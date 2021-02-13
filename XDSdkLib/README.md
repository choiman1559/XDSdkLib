# XDSdkLib
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

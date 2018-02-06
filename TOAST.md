![Fusion](https://github.com/blackbeared/fusion/blob/master/logo.png)

## Fusion By BlackBeared  [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![](https://jitpack.io/v/blackbeared/fusion.svg)](https://jitpack.io/#blackbeared/fusion)

_An Easy-to-use **Kotlin** based Customizable Library with Material Layouts_ by [@blackbeared](http://www.linkedin.com/er-sandip-savaliya).

##  Fully Customizable Toasts with some inbuilt functions.

![Fusion](https://github.com/blackbeared/fusion/blob/master/toasts.gif)

## Sample Usage
A pre-styled toasts with fully customization facility.


```gradle

     AwesomeToast.success(this@Dashboard, "Green Success Toast").show()
     AwesomeToast.info(this@Dashboard, "Cyan Information Toast").show()
     AwesomeToast.warning(this@Dashboard,  "Orange Warning Toast").show()
     AwesomeToast.error(this@Dashboard,  "Red Error Toast").show()

```

## Customization

```gradle

        AwesomeToast.Config.getInstance()
            .setErrorColor(@ColorInt int errorColor) // optional
            .setInfoColor(@ColorInt int infoColor) // optional
            .setSuccessColor(@ColorInt int successColor) // optional
            .setWarningColor(@ColorInt int warningColor) // optional
            .setTextColor(@ColorInt int textColor) // optional
            .tintIcon(boolean tintIcon) // optional (apply textColor also to the icon)
            .setToastTypeface(@NonNull Typeface typeface) // optional
            .setTextSize(int sizeInSp) // optional
            .apply(); // required

```

You can reset the configuration by using ***reset()*** method:

```gradle

      AwesomeToast.Config.reset();

```


## Description

Use the above code to show different type of messages as well as add the customization code to your **Launcher** activity to customise toasts.

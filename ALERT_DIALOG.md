![Fusion](https://github.com/blackbeared/fusion/blob/master/logo.png)

## Fusion By BlackBeared  [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![](https://jitpack.io/v/blackbeared/fusion.svg)](https://jitpack.io/#blackbeared/fusion)

_An Easy-to-use **Kotlin** based Customizable Library with Material Layouts_ by [@blackbeared](http://www.linkedin.com/er-sandip-savaliya).

##  Fully Customizable Alert dialog.

![Fusion](https://github.com/blackbeared/fusion/blob/master/alert_dialog.gif)

## Sample Usage
Alert dialog provides a facility to select either positive or negative option with fully customizable interface.
Here is a kotlin code to use Alert Dialog.


```gradle

   AwesomeGeneralDialog(this)
                    .setTopColor(ContextCompat.getColor(this, R.color.color_deep_orange))
                    .setIcon(R.drawable.ic_alert)
                    .setTitle("Custom Alert...!")
                    .setPositiveButton("Ok", object : View.OnClickListener {
                        override fun onClick(p0: View?) {
                            AwesomeToast.success(this@Dashboard, "Ok clicked").show()
                        }

                    })
                    .setNegativeButton("Cancel", object : View.OnClickListener {
                        override fun onClick(p0: View?) {
                            AwesomeToast.error(this@Dashboard, "Cancel clicked").show()
                        }

                    })
                    .setMessage("Set Custom Ok and Cancel Handlers.")
                    .setPositiveButtonColor(ContextCompat.getColor(this, R.color.color_deep_orange))
                    .show()

```

## Description

Write the above code to get the custom alert message.

![Fusion](https://github.com/blackbeared/fusion/blob/master/logo.png)

## Fusion By BlackBeared  [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![](https://jitpack.io/v/blackbeared/fusion.svg)](https://jitpack.io/#blackbeared/fusion)

_An Easy-to-use **Kotlin** based Customizable Library with Material Layouts_ by [@blackbeared](http://www.linkedin.com/er-sandip-savaliya).

##  Custom Shared dialog with two different views.

![Fusion](https://github.com/blackbeared/fusion/blob/master/custom_dialog.gif)

## Sample Usage
Shared dialog provides a facility to add custom view to dialog.
Here is a kotlin code to use Custom Dialog.


```gradle

     AwesomeCustomDialog(this)
                    .setTopColor(Color.parseColor("#00BCD4"))
                    .setIcon(R.drawable.ic_register)
                    .setIconTintColor(Color.WHITE)
                    .setView(R.layout.bottomview)
                    .configureView(object: AwesomeCustomDialog.ViewConfigurator{
                        override fun configureView(v: View) {
                            // Configure your view here.
                        }
                    })
                    .show()

```

## Description

Use this dialog to add custom view to fully customizable dialog, and also you can configure those views.

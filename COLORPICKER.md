![Fusion](https://github.com/blackbeared/fusion/blob/master/logo.png)

## Fusion By BlackBeared  [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![](https://jitpack.io/v/blackbeared/fusion.svg)](https://jitpack.io/#blackbeared/fusion)

_An Easy-to-use **Kotlin** based Customizable Library with Material Layouts_ by [@blackbeared](http://www.linkedin.com/er-sandip-savaliya).

##  Fully customizable Colorpicker dialog.

![Fusion](https://github.com/blackbeared/fusion/blob/master/colorpicker.gif)

## Sample Usage
Colorpicker dialog provides a facility to pick color.
Here is a kotlin code to use Colorpicker Dialog.


```gradle

    AwesomeColorPickerDialog(this)
                    .setIconTintColor(Color.WHITE)
                    .setColorsArray(Palette.DEFAULT)
                    .setConfirmButton("Done", object : AwesomeColorPickerDialog.OnColorConfirmListener {
                        override fun onColorConfirmed(color: Int) {
                            AwesomeToast.info(this@Dashboard, 
                                "Color picked successfully: #" + Integer.toHexString(color)).show()
                        }
                    })
                    .show()

```
## Description

Write the above kotlin code to pick the color from the used color-pellate, you can change the colors of the colorpicker dialog, and get the selected color in the **AwesomeColorPickerDialog.OnColorConfirmListener's** onColorConfirmed method.

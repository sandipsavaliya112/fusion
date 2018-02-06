![Fusion](https://github.com/blackbeared/fusion/blob/master/logo.png)

## Fusion By BlackBeared  [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![](https://jitpack.io/v/blackbeared/fusion.svg)](https://jitpack.io/#blackbeared/fusion)

_An Easy-to-use **Kotlin** based Customizable Library with Material Layouts_ by [@blackbeared](http://www.linkedin.com/er-sandip-savaliya).

##  Multiple options selection dialog

![Fusion](https://github.com/blackbeared/fusion/blob/master/choice.gif)

## Sample Usage
You can select a multiple items from this dialog.


```gradle

     val items = resources.getStringArray(R.array.food)
            AwesomeChoiceDialog(this, R.style.AppTheme)
                    .setTopColorRes(R.color.color_cyan)
                    .setTitle("Order your food.")
                    .setIcon(R.drawable.ic_food)
                    .setItemsMultiChoice(items, object : AwesomeChoiceDialog.OnItemsSelectedListener<String> {
                        override fun onItemsSelected(positions: List<Int>, items: List<String>) {
                            AwesomeToast.success(this@Dashboard,
                                    getString(R.string.you_ordered, TextUtils.join("\n", items)))
                                    .show()
                        }
                    })
                    .setConfirmButtonText("Ok")
                    .show()

```

## Description

Add the above code to get the multiple items selection dialog, it will return you seleted items from the dialog.

![Fusion](https://github.com/blackbeared/fusion/blob/master/logo.png)

## Fusion By BlackBeared  [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![](https://jitpack.io/v/blackbeared/fusion.svg)](https://jitpack.io/#blackbeared/fusion)

_An Easy-to-use **Kotlin** based Customizable Library with Material Layouts_ by [@blackbeared](http://www.linkedin.com/er-sandip-savaliya).

##  Datepicker dialog with Customization.

![Fusion](https://github.com/blackbeared/fusion/blob/master/date_picker.gif)

## Sample Usage
This dialog can be used to pick SINGLE, MULTIPLE dates or DATE-RANGE.


```gradle

    AwesomeDatePickerDialog(this)
                    .setIcon(R.drawable.ic_calendar)
                    .setIconTintColor(Color.WHITE)
                    .setCalendarSelectionType(SelectionType.RANGE) // SINGLE | RANGE | MULTIPLE
                    .setCalendarAccentColor(resources.getColor(R.color.color_deep_purple))
                    .setConfirmButton("Done", object : DatePickerListener {
                        override fun onDatePicked(v: View?, dates: MutableList<Calendar>?) {
                            for (i in dates!!) {
                                AwesomeToast.success(this@Dashboard, "" + i.time.toLocaleString()).show()
                            }
                        }
                    })
                    .show()

```

## Description

Datepicker dialog is used to pick Single date, multiple dates or a date range. This is Fully customizable dialog.

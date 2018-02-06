![Fusion](https://github.com/blackbeared/fusion/blob/master/logo.png)

## Fusion By BlackBeared  [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![](https://jitpack.io/v/blackbeared/fusion.svg)](https://jitpack.io/#blackbeared/fusion)

_An Easy-to-use **Kotlin** based Customizable Library with Material Layouts_ by [@blackbeared](http://www.linkedin.com/er-sandip-savaliya).

##  Custom Shared dialog with two different views.

![Fusion](https://github.com/blackbeared/fusion/blob/master/shared_dialog.gif)

## Sample Usage
Shared dialog provides a facility to switch between two different dialogs.
Here is a kotlin code to use Shared Dialog.


```gradle

    AwesomeSwitchableDialog(this)
                    .setTopView(R.layout.topview)
                    .setBottomView(R.layout.bottomview)
                    .configureTopView(object: AwesomeSwitchableDialog.topViewConfigurator{
                        override fun configureViewTop(v: View) {
                            
                        }

                    })
                    .configureBottomView(object : AwesomeSwitchableDialog.bottomViewConfigurator{
                        override fun configureViewBottom(v: View) {
                        
                        }
                    })
                    .setTopViewColor(Color.parseColor("#00BCD4"))
                    .setBottomViewColor(Color.parseColor("#FFFFFF"))
                    .setTopViewIcon(R.drawable.ic_login, Color.parseColor("#FFFFFF"))
                    .setBottomViewIcon(R.drawable.ic_register, Color.parseColor("#00BCD4"))
                    .show()

```
Define two different layout xml files, and set them as top and bottom view and configure views inside **.configureTopView** and **.configureBottomview** 
and then Add this code to use shared dialog in your app to use shared Dialog.

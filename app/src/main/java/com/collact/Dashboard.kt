package com.collact

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.awesome.dialog.datepicker.utils.SelectionType
import com.awesome.shorty.AwesomeToast
import kotlinx.android.synthetic.main.content_dashboard.*
import android.text.TextUtils
import com.awesome.dialog.*
import com.awesome.dialog.datepicker.DatePickerListener
import com.awesome.layouts.parallaxstyle.VerticalMovingStyle
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.*


class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_dashboard)
        fab_menu.hideMenu(false)

        dialog1.setParallaxStyles(VerticalMovingStyle()) // Shared
        dialog2.setParallaxStyles(VerticalMovingStyle()) // Alert
        dialog3.setParallaxStyles(VerticalMovingStyle()) // Chooser
        dialog4.setParallaxStyles(VerticalMovingStyle()) //Progress
        dialog5.setParallaxStyles(VerticalMovingStyle()) //Colorpicker
        dialog6.setParallaxStyles(VerticalMovingStyle()) //Datepicker
        dialog7.setParallaxStyles(VerticalMovingStyle()) //Custom Dialog
        dialog8.setParallaxStyles(VerticalMovingStyle()) // FAB
        dialog9.setParallaxStyles(VerticalMovingStyle()) //Layouts
        dialog10.setParallaxStyles(VerticalMovingStyle()) //Toast

        dialog1.setOnClickListener {

            AwesomeSwitchableDialog(this)
                    .setTopView(R.layout.topview)
                    .setBottomView(R.layout.bottomview)
                    .setTopViewColor(Color.parseColor("#00BCD4"))
                    .setBottomViewColor(Color.parseColor("#FFFFFF"))
                    .setTopViewIcon(R.drawable.ic_login, Color.parseColor("#FFFFFF"))
                    .setBottomViewIcon(R.drawable.ic_register, Color.parseColor("#00BCD4"))
                    .show()
        }

        dialog2.setOnClickListener {
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
        }

        dialog3.setOnClickListener {

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
        }

        dialog4.setOnClickListener {
            val progress = AwesomeProgressDialog(this)
                    .setIcon(R.drawable.ic_progress)
                    .setTitle("Connecting to server")
                    .setTopColorRes(R.color.colorAccent)
                    .show()
            Handler().postDelayed(Runnable {
                progress.dismiss()
            }, 1000)
        }

        dialog5.setOnClickListener {
            AwesomeColorPickerDialog(this)
                    .setIconTintColor(Color.WHITE)
                    .setConfirmButton("Done", object : AwesomeColorPickerDialog.OnColorConfirmListener {
                        override fun onColorConfirmed(color: Int) {
                            AwesomeToast.info(this@Dashboard, "Color picked successfully: #" + Integer.toHexString(color)).show()
                        }
                    })
                    .show()
        }

        dialog6.setOnClickListener {
            AwesomeDatePickerDialog(this)
                    .setIcon(R.drawable.ic_calendar)
                    .setIconTintColor(Color.WHITE)
                    .setCalendarSelectionType(SelectionType.SINGLE) // SINGLE | RANGE | MULTIPLE
                    .setCalendarAccentColor(resources.getColor(R.color.color_deep_purple))
                    .setConfirmButton("Done", object : DatePickerListener {
                        override fun onDatePicked(v: View?, dates: MutableList<Calendar>?) {
                            for (i in dates!!) {
                                AwesomeToast.success(this@Dashboard, "" + i.time.toLocaleString()).show()
                            }
                        }
                    })
                    .show()
        }

        dialog7.setOnClickListener {

            AwesomeCustomDialog(this)
                    .setTopColor(Color.parseColor("#00BCD4"))
                    .setIcon(R.drawable.ic_register)
                    .setIconTintColor(Color.WHITE)
                    .setView(R.layout.bottomview)
                    .show()
        }

        dialog8.setOnClickListener {
            if (fab_menu.isMenuHidden) {
                fab_menu.showMenu(true)
            } else {
                fab_menu.hideMenu(true)
            }

        }

        dialog9.setOnClickListener {

            AwesomeSwitchableDialog(this)
                    .setTopView(R.layout.topview)
                    .setBottomView(R.layout.bottomview)
                    .setTopViewColor(Color.parseColor("#00BCD4"))
                    .setBottomViewColor(Color.parseColor("#FFFFFF"))
                    .setTopViewIcon(R.drawable.ic_login, Color.parseColor("#FFFFFF"))
                    .setBottomViewIcon(R.drawable.ic_register, Color.parseColor("#00BCD4"))
                    .show()
        }

        dialog10.setOnClickListener {
            AwesomeToast.success(this@Dashboard, "Success").show()
            AwesomeToast.warning(this@Dashboard, "Warning").show()
            AwesomeToast.error(this@Dashboard, "Error").show()
            AwesomeToast.info(this@Dashboard, "Information").show()

        }
    }
}

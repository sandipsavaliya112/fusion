package com.collact

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.awesome.dialog.AwesomeSwitchableDialog
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        openDiag.setOnClickListener {
            AwesomeSwitchableDialog(this)
                    .setTopView(R.layout.topview)
                    .setBottomView(R.layout.bottomview)
                    .setTopViewColor(Color.parseColor("#00BCD4"))
                    .setBottomViewColor(Color.parseColor("#FFFFFF"))
                    .setTopViewIcon(R.drawable.ic_login, Color.parseColor("#FFFFFF"))
                    .setBottomViewIcon(R.drawable.ic_register, Color.parseColor("#00BCD4"))
                    .show()
        }

    }
}

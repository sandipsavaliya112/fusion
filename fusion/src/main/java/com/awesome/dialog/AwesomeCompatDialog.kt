package com.awesome.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.view.View

/**
 * Created by Sandip Savaliya on 23.02.2017.
 */

object AwesomeCompatDialog {

    /**
     * If you don't want to change implemented interfaces when migrating from standard dialogs
     * to LovelyDialogs - use this method.
     */
    fun wrap(listener: DialogInterface.OnClickListener): View.OnClickListener {
        return DialogOnClickListenerAdapter(listener)
    }

    internal class DialogOnClickListenerAdapter(adapted: DialogInterface.OnClickListener) : View.OnClickListener {

        private val adapted: DialogInterface.OnClickListener?

        init {
            this.adapted = adapted
        }

        fun onClick(dialogInterface: DialogInterface, which: Int) {
            if (adapted != null) {
                adapted.onClick(dialogInterface, which)
            }
        }

        override fun onClick(v: View) {

        }
    }
}

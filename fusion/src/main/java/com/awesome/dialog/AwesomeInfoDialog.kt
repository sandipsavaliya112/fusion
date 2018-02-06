package com.awesome.dialog

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import android.widget.Button
import android.widget.CheckBox

import com.awesome.R


/**
 * Created by Sandip Savaliya on 16.04.2016.
 */
class AwesomeInfoDialog : AwesomeBase<AwesomeInfoDialog> {

    private var cbDontShowAgain: CheckBox? = null
    private var confirmButton: Button? = null

    private var infoDialogId: Int = 0

    override val layout: Int
        get() = R.layout.dialog_info

    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)

    init {
        cbDontShowAgain = findView(R.id.ld_cb_dont_show_again)
        confirmButton = findView(R.id.ld_btn_confirm)
        confirmButton!!.setOnClickListener(AwesomeBase.ClickListenerDecorator(null, true,dialog!!))
        infoDialogId = -1
    }

    fun setNotShowAgainOptionEnabled(dialogId: Int): AwesomeInfoDialog {
        infoDialogId = dialogId
        cbDontShowAgain!!.visibility = View.VISIBLE
        confirmButton!!.setOnClickListener {
            val notShow = cbDontShowAgain!!.isChecked
            storage(context).edit().putBoolean(infoDialogId.toString(), notShow).apply()
            dismiss()
        }
        return this
    }

    fun setNotShowAgainOptionChecked(defaultChecked: Boolean): AwesomeInfoDialog {
        cbDontShowAgain!!.isChecked = defaultChecked
        return this
    }

    fun setConfirmButtonText(@StringRes text: Int): AwesomeInfoDialog {
        return setConfirmButtonText(string(text))
    }

    fun setConfirmButtonText(text: String): AwesomeInfoDialog {
        confirmButton!!.text = text
        return this
    }

    fun setConfirmButtonColor(color: Int): AwesomeInfoDialog {
        confirmButton!!.setTextColor(color)
        return this
    }

    override fun show(): Dialog {
        if (infoDialogId == -1) {
            return super.show()
        }

        val shouldShowDialog = !storage(context).getBoolean(infoDialogId.toString(), false)
        return if (shouldShowDialog) {
            super.show()
        } else {
            super.create()!!
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_DONT_SHOW_AGAIN, cbDontShowAgain!!.isChecked)
    }

    override fun restoreState(savedState: Bundle) {
        super.restoreState(savedState)
        cbDontShowAgain!!.isChecked = savedState.getBoolean(KEY_DONT_SHOW_AGAIN)
    }

    companion object {

        private val STORAGE = "ld_dont_show"

        private val KEY_DONT_SHOW_AGAIN = "key_dont_show_again"

        fun reset(context: Context, dialogId: Int) {
            storage(context).edit().putBoolean(dialogId.toString(), false).apply()
        }

        private fun storage(context: Context): SharedPreferences {
            return context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
        }
    }
}

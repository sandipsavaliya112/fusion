package com.awesome.dialog

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView

import com.awesome.R


/**
 * Created by Sandip Savaliya on 16.04.2016.
 */
class AwesomeTextPromptDialog : AwesomeBase<AwesomeTextPromptDialog> {

    private var inputField: EditText? = null
    private var errorMessage: TextView? = null
    private var confirmButton: TextView? = null
    private var negativeButton: TextView? = null

    private var filter: TextFilter? = null

    protected override val layout: Int
        get() = R.layout.dialog_text_input

    constructor(context: Context) : super(context) {}

    constructor(context: Context, theme: Int) : super(context, theme) {}

    init {
        confirmButton = findView(R.id.ld_btn_confirm)
        negativeButton = findView(R.id.ld_btn_negative)
        inputField = findView(R.id.ld_text_input)
        errorMessage = findView(R.id.ld_error_message)
        inputField!!.addTextChangedListener(HideErrorOnTextChanged())
    }

    fun setConfirmButton(@StringRes text: Int, listener: OnTextInputConfirmListener): AwesomeTextPromptDialog {
        return setConfirmButton(string(text), listener)
    }

    fun setConfirmButton(text: String, listener: OnTextInputConfirmListener): AwesomeTextPromptDialog {
        confirmButton!!.text = text
        confirmButton!!.setOnClickListener(TextInputListener(listener))
        return this
    }

    fun setConfirmButtonColor(color: Int): AwesomeTextPromptDialog {
        confirmButton!!.setTextColor(color)
        return this
    }

    fun setNegativeButton(@StringRes text: Int, listener: View.OnClickListener): AwesomeTextPromptDialog {
        return setNegativeButton(string(text), listener)
    }

    fun setNegativeButton(text: String, listener: View.OnClickListener): AwesomeTextPromptDialog {
        negativeButton!!.visibility = View.VISIBLE
        negativeButton!!.text = text
        negativeButton!!.setOnClickListener(AwesomeBase.ClickListenerDecorator(listener, true, dialog!!))
        return this
    }

    fun setNegativeButtonColor(color: Int): AwesomeTextPromptDialog {
        negativeButton!!.setTextColor(color)
        return this
    }

    fun setInputFilter(@StringRes errorMessage: Int, filter: TextFilter): AwesomeTextPromptDialog {
        return setInputFilter(string(errorMessage), filter)
    }

    fun setInputFilter(errorMessage: String, filter: TextFilter): AwesomeTextPromptDialog {
        this.filter = filter
        this.errorMessage!!.text = errorMessage
        return this
    }

    fun setErrorMessageColor(color: Int): AwesomeTextPromptDialog {
        errorMessage!!.setTextColor(color)
        return this
    }

    fun setInputType(inputType: Int): AwesomeTextPromptDialog {
        inputField!!.inputType = inputType
        return this
    }

    fun addTextWatcher(textWatcher: TextWatcher): AwesomeTextPromptDialog {
        inputField!!.addTextChangedListener(textWatcher)
        return this
    }

    fun setInitialInput(@StringRes text: Int): AwesomeTextPromptDialog {
        return setInitialInput(string(text))
    }

    fun setInitialInput(text: String): AwesomeTextPromptDialog {
        inputField!!.setText(text)
        return this
    }

    fun setHint(@StringRes hint: Int): AwesomeTextPromptDialog {
        return setHint(string(hint))
    }

    fun setHint(text: String): AwesomeTextPromptDialog {
        inputField!!.hint = text
        return this
    }

    private fun setError() {
        errorMessage!!.visibility = View.VISIBLE
    }

    private fun hideError() {
        errorMessage!!.visibility = View.GONE
    }

    internal override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_HAS_ERROR, errorMessage!!.visibility == View.VISIBLE)
        outState.putString(KEY_TYPED_TEXT, inputField!!.text.toString())
    }

    internal override fun restoreState(savedState: Bundle) {
        super.restoreState(savedState)
        if (savedState.getBoolean(KEY_HAS_ERROR, false)) {
            setError()
        }
        inputField!!.setText(savedState.getString(KEY_TYPED_TEXT))
    }

    private inner class TextInputListener (private val wrapped: OnTextInputConfirmListener?) : View.OnClickListener {

        override fun onClick(v: View) {
            val text = inputField!!.text.toString()

            if (filter != null) {
                val isWrongInput = !filter!!.check(text)
                if (isWrongInput) {
                    setError()
                    return
                }
            }

            wrapped?.onTextInputConfirmed(text)

            dismiss()
        }
    }

    private inner class HideErrorOnTextChanged : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            hideError()
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    interface OnTextInputConfirmListener {
        fun onTextInputConfirmed(text: String)
    }

    interface TextFilter {
        fun check(text: String): Boolean
    }

    companion object {

        private val KEY_HAS_ERROR = "key_has_error"
        private val KEY_TYPED_TEXT = "key_typed_text"
    }
}

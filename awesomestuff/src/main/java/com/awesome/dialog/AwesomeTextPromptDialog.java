package com.awesome.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.awesome.R;


/**
 * Created by yarolegovich on 16.04.2016.
 */
public class AwesomeTextPromptDialog extends AwesomeBase<AwesomeTextPromptDialog> {

    private static final String KEY_HAS_ERROR = "key_has_error";
    private static final String KEY_TYPED_TEXT = "key_typed_text";

    private EditText inputField;
    private TextView errorMessage;
    private TextView confirmButton, negativeButton;

    private TextFilter filter;

    public AwesomeTextPromptDialog(Context context) {
        super(context);
    }

    public AwesomeTextPromptDialog(Context context, int theme) {
        super(context, theme);
    }

    {
        confirmButton = findView(R.id.ld_btn_confirm);
        negativeButton = findView(R.id.ld_btn_negative);
        inputField = findView(R.id.ld_text_input);
        errorMessage = findView(R.id.ld_error_message);
        inputField.addTextChangedListener(new HideErrorOnTextChanged());
    }

    public AwesomeTextPromptDialog setConfirmButton(@StringRes int text, OnTextInputConfirmListener listener) {
        return setConfirmButton(string(text), listener);
    }

    public AwesomeTextPromptDialog setConfirmButton(String text, OnTextInputConfirmListener listener) {
        confirmButton.setText(text);
        confirmButton.setOnClickListener(new TextInputListener(listener));
        return this;
    }

    public AwesomeTextPromptDialog setConfirmButtonColor(int color) {
        confirmButton.setTextColor(color);
        return this;
    }

    public AwesomeTextPromptDialog setNegativeButton(@StringRes int text, View.OnClickListener listener){
        return setNegativeButton(string(text), listener);
    }

    public AwesomeTextPromptDialog setNegativeButton(String text, View.OnClickListener listener){
        negativeButton.setVisibility(View.VISIBLE);
        negativeButton.setText(text);
        negativeButton.setOnClickListener(new ClickListenerDecorator(listener, true));
        return this;
    }

    public AwesomeTextPromptDialog setNegativeButtonColor(int color) {
        negativeButton.setTextColor(color);
        return this;
    }

    public AwesomeTextPromptDialog setInputFilter(@StringRes int errorMessage, TextFilter filter) {
        return setInputFilter(string(errorMessage), filter);
    }

    public AwesomeTextPromptDialog setInputFilter(String errorMessage, TextFilter filter) {
        this.filter = filter;
        this.errorMessage.setText(errorMessage);
        return this;
    }

    public AwesomeTextPromptDialog setErrorMessageColor(int color) {
        errorMessage.setTextColor(color);
        return this;
    }

    public AwesomeTextPromptDialog setInputType(int inputType) {
        inputField.setInputType(inputType);
        return this;
    }

    public AwesomeTextPromptDialog addTextWatcher(TextWatcher textWatcher) {
        inputField.addTextChangedListener(textWatcher);
        return this;
    }

    public AwesomeTextPromptDialog setInitialInput(@StringRes int text) {
        return setInitialInput(string(text));
    }

    public AwesomeTextPromptDialog setInitialInput(String text) {
        inputField.setText(text);
        return this;
    }

    public AwesomeTextPromptDialog setHint(@StringRes int hint) {
        return setHint(string(hint));
    }

    public AwesomeTextPromptDialog setHint(String text) {
        inputField.setHint(text);
        return this;
    }

    private void setError() {
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        errorMessage.setVisibility(View.GONE);
    }

    @Override
    void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_HAS_ERROR, errorMessage.getVisibility() == View.VISIBLE);
        outState.putString(KEY_TYPED_TEXT, inputField.getText().toString());
    }

    @Override
    void restoreState(Bundle savedState) {
        super.restoreState(savedState);
        if (savedState.getBoolean(KEY_HAS_ERROR, false)) {
            setError();
        }
        inputField.setText(savedState.getString(KEY_TYPED_TEXT));
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_text_input;
    }

    private class TextInputListener implements View.OnClickListener {

        private OnTextInputConfirmListener wrapped;

        private TextInputListener(OnTextInputConfirmListener wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void onClick(View v) {
            String text = inputField.getText().toString();

            if (filter != null) {
                boolean isWrongInput = !filter.check(text);
                if (isWrongInput) {
                    setError();
                    return;
                }
            }

            if (wrapped != null) {
                wrapped.onTextInputConfirmed(text);
            }

            dismiss();
        }
    }

    private class HideErrorOnTextChanged implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            hideError();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public interface OnTextInputConfirmListener {
        void onTextInputConfirmed(String text);
    }

    public interface TextFilter {
        boolean check(String text);
    }
}

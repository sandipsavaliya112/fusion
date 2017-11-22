package com.awesome.dialog;

import android.content.Context;

import com.awesome.R;

/**
 * Created by yarolegovich on 16.04.2016.
 */
public class AwesomeProgressDialog extends AwesomeBase<AwesomeProgressDialog> {

    public AwesomeProgressDialog(Context context) {
        super(context);
    }

    public AwesomeProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    {
        setCancelable(false);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_progress;
    }
}

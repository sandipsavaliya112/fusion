package com.awesome.dialog

import android.content.Context

import com.awesome.R

/**
 * Created by Sandip Savaliya on 16.04.2016.
 */
class AwesomeProgressDialog : AwesomeBase<AwesomeProgressDialog> {

    override val layout: Int
        get() = R.layout.dialog_progress

    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)

    init {
        setCancelable(false)
    }
}

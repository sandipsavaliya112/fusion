package com.awesome.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.awesome.R

class AwesomeSwitchableDialog : AwesomeBase<AwesomeSwitchableDialog> {

    private var addedViewTop: View? = null
    private var addedViewBottom: View? = null
    private val instanceStateManager: InstanceStateManager? = null

    protected override val layout: Int
        get() = R.layout.dialog_top_bottom

    constructor(context: Context) : super(context) {}

    constructor(context: Context, theme: Int) : super(context, theme) {}

    fun setTopView(@LayoutRes layout: Int): AwesomeSwitchableDialog {
        val inflater = LayoutInflater.from(context)
        val parent = findView<ViewGroup>(R.id.ld_custom_top_container)
        addedViewTop = inflater.inflate(layout, parent, true)
        return this
    }

    fun setTopView(customView: View): AwesomeSwitchableDialog {
        val topcontainer = findView<ViewGroup>(R.id.ld_custom_top_container)
        topcontainer.addView(customView)
        addedViewTop = customView

        return this
    }

    fun configureTopView(configurator: topViewConfigurator): AwesomeSwitchableDialog {
        if (addedViewTop == null) {
            throw IllegalStateException(string(R.string.ex_msg_dialog_top_not_set))
        }
        configurator.configureViewTop(addedViewTop!!)
        return this
    }


    fun setBottomView(@LayoutRes layout: Int): AwesomeSwitchableDialog {
        val inflater = LayoutInflater.from(context)
        val parent = findView<ViewGroup>(R.id.ld_custom_bottom_container)
        addedViewBottom = inflater.inflate(layout, parent, true)
        return this
    }

    fun setBottomView(customView: View): AwesomeSwitchableDialog {
        val bottomcontainer = findView<ViewGroup>(R.id.ld_custom_bottom_container)
        bottomcontainer.addView(customView)
        addedViewBottom = customView
        return this
    }


    fun configureBottomView(configurator: bottomViewConfigurator): AwesomeSwitchableDialog {
        if (addedViewBottom == null) {
            throw IllegalStateException(string(R.string.ex_msg_dialog_bottom_not_set))
        }
        configurator.configureViewBottom(addedViewBottom!!)
        return this
    }

    internal override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateManager!!.saveInstanceState(outState)
    }

    internal override fun restoreState(savedState: Bundle) {
        super.restoreState(savedState)
        instanceStateManager!!.restoreInstanceState(savedState)
    }

    interface topViewConfigurator {
        fun configureViewTop(v: View)
    }

    interface bottomViewConfigurator {
        fun configureViewBottom(v: View)
    }


    interface InstanceStateManager {
        fun saveInstanceState(outState: Bundle)
        fun restoreInstanceState(savedState: Bundle)
    }
}

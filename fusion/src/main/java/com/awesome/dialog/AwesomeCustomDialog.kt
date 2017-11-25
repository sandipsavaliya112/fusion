package com.awesome.dialog

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.awesome.R

class AwesomeCustomDialog : AwesomeBase<AwesomeCustomDialog> {

    private var addedView: View? = null
    private var instanceStateManager: InstanceStateManager? = null

    override val layout: Int
        get() = R.layout.dialog_custom

    constructor(context: Context) : super(context) {}

    constructor(context: Context, theme: Int) : super(context, theme) {}

    fun setView(@LayoutRes layout: Int): AwesomeCustomDialog {
        val inflater = LayoutInflater.from(context)
        val parent = findView<ViewGroup>(R.id.ld_custom_view_container)
        addedView = inflater.inflate(layout, parent, true)
        return this
    }

    fun setView(customView: View): AwesomeCustomDialog {
        val container = findView<ViewGroup>(R.id.ld_custom_view_container)
        container.addView(customView)
        addedView = customView
        return this
    }

    fun configureView(configurator: ViewConfigurator): AwesomeCustomDialog {
        if (addedView == null) {
            throw IllegalStateException(string(R.string.ex_msg_dialog_view_not_set))
        }
        configurator.configureView(addedView!!)
        return this
    }

    fun setListener(viewId: Int, listener: View.OnClickListener): AwesomeCustomDialog {
        return setListener(viewId, false, listener)
    }

    fun setListener(viewId: Int, dismissOnClick: Boolean, listener: View.OnClickListener): AwesomeCustomDialog {
        if (addedView == null) {
            throw IllegalStateException(string(R.string.ex_msg_dialog_view_not_set))
        }
        val clickListener = AwesomeBase.ClickListenerDecorator(listener, dismissOnClick, dialog!!)
        findView<View>(viewId).setOnClickListener(clickListener)
        return this
    }

    fun setInstanceStateManager(instanceStateManager: InstanceStateManager): AwesomeCustomDialog {
        this.instanceStateManager = instanceStateManager
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

    interface ViewConfigurator {
        fun configureView(v: View)
    }

    interface InstanceStateManager {
        fun saveInstanceState(outState: Bundle)

        fun restoreInstanceState(savedState: Bundle)
    }
}

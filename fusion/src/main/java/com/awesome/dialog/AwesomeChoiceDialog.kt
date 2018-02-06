package com.awesome.dialog

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

import com.awesome.R

import java.util.ArrayList
import java.util.Arrays


class AwesomeChoiceDialog : AwesomeBase<AwesomeChoiceDialog> {

    private var choicesList: ListView? = null
    private var confirmButton: TextView? = null

    override val layout: Int
        get() = R.layout.dialog_choice

    private val isMultiChoiceList: Boolean
        get() = choicesList!!.choiceMode == AbsListView.CHOICE_MODE_MULTIPLE

    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)

    init {
        choicesList = findView(R.id.ld_choices)
    }

    fun <T> setItems(items: Array<T>, itemSelectedListener: OnItemSelectedListener<T>): AwesomeChoiceDialog {
        return setItems(Arrays.asList(*items), itemSelectedListener)
    }

    fun <T> setItems(items: List<T>, itemSelectedListener: OnItemSelectedListener<T>): AwesomeChoiceDialog {
        val adapter = ArrayAdapter(context,
                R.layout.item_simple_text, android.R.id.text1,
                items)
        return setItems(adapter, itemSelectedListener)
    }

    fun <T> setItems(adapter: ArrayAdapter<T>, itemSelectedListener: OnItemSelectedListener<T>): AwesomeChoiceDialog {
        choicesList!!.onItemClickListener = ItemSelectedAdapter(itemSelectedListener)
        choicesList!!.adapter = adapter
        return this
    }

    fun <T> setItemsMultiChoice(items: Array<T>, itemsSelectedListener: OnItemsSelectedListener<T>): AwesomeChoiceDialog {
        return setItemsMultiChoice(items, null, itemsSelectedListener)
    }

    fun <T> setItemsMultiChoice(items: Array<T>, selectionState: BooleanArray?, itemsSelectedListener: OnItemsSelectedListener<T>): AwesomeChoiceDialog {
        return setItemsMultiChoice(Arrays.asList(*items), selectionState, itemsSelectedListener)
    }

    fun <T> setItemsMultiChoice(items: List<T>, itemsSelectedListener: OnItemsSelectedListener<T>): AwesomeChoiceDialog {
        return setItemsMultiChoice(items, null, itemsSelectedListener)
    }

    fun <T> setItemsMultiChoice(items: List<T>, selectionState: BooleanArray?, itemsSelectedListener: OnItemsSelectedListener<T>): AwesomeChoiceDialog {
        val adapter = ArrayAdapter(context,
                R.layout.item_simple_text_multichoice, android.R.id.text1,
                items)
        return setItemsMultiChoice(adapter, selectionState, itemsSelectedListener)
    }

    fun <T> setItemsMultiChoice(adapter: ArrayAdapter<T>, itemsSelectedListener: OnItemsSelectedListener<T>): AwesomeChoiceDialog {
        return setItemsMultiChoice(adapter, null, itemsSelectedListener)
    }

    fun <T> setItemsMultiChoice(adapter: ArrayAdapter<T>, selectionState: BooleanArray?, itemsSelectedListener: OnItemsSelectedListener<T>): AwesomeChoiceDialog {
        val inflater = LayoutInflater.from(context)
        val confirmBtnContainer = inflater.inflate(R.layout.item_footer_confirm, null)
        confirmButton = confirmBtnContainer.findViewById<View>(R.id.ld_btn_confirm) as TextView
        confirmButton!!.setOnClickListener(ItemsSelectedAdapter(itemsSelectedListener))
        choicesList!!.addFooterView(confirmBtnContainer)

        val choicesList = findView<ListView>(R.id.ld_choices)
        choicesList.choiceMode = AbsListView.CHOICE_MODE_MULTIPLE
        choicesList.adapter = adapter

        if (selectionState != null) {
            for (i in selectionState.indices) {
                choicesList.setItemChecked(i, selectionState[i])
            }
        }

        return this
    }

    fun setConfirmButtonText(@StringRes text: Int): AwesomeChoiceDialog {
        return setConfirmButtonText(string(text))
    }

    fun setConfirmButtonText(text: String): AwesomeChoiceDialog {
        if (confirmButton == null) {
            throw IllegalStateException(string(R.string.ex_msg_dialog_choice_confirm))
        }
        confirmButton!!.text = text
        return this
    }

    fun setConfirmButtonColor(color: Int): AwesomeChoiceDialog {
        if (confirmButton == null) {
            throw IllegalStateException(string(R.string.ex_msg_dialog_choice_confirm))
        }
        confirmButton!!.setTextColor(color)
        return this
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isMultiChoiceList) {
            val adapter = choicesList!!.adapter
            val checkedStates = BooleanArray(adapter.count)
            val checkedPositions = choicesList!!.checkedItemPositions
            for (i in 0 until checkedPositions.size()) {
                if (checkedPositions.valueAt(i)) {
                    checkedStates[checkedPositions.keyAt(i)] = true
                }
            }
            outState.putBooleanArray(KEY_ITEM_CHECKED_STATES, checkedStates)
        }
    }

    override fun restoreState(savedState: Bundle) {
        super.restoreState(savedState)
        if (isMultiChoiceList) {
            val checkedStates = savedState.getBooleanArray(KEY_ITEM_CHECKED_STATES) ?: return
            for (index in checkedStates.indices) {
                choicesList!!.setItemChecked(index, checkedStates[index])
            }
        }
    }

    private inner class ItemSelectedAdapter<T> (private val adaptee: OnItemSelectedListener<T>?) : AdapterView.OnItemClickListener {

        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            adaptee?.onItemSelected(position, parent.getItemAtPosition(position) as T)
            dismiss()
        }
    }

    interface OnItemSelectedListener<T> {
        fun onItemSelected(position: Int, item: T)
    }

    private inner class ItemsSelectedAdapter<T> (private val adaptee: OnItemsSelectedListener<T>?) : View.OnClickListener {

        override fun onClick(v: View) {
            if (adaptee != null) {
                val checkedItemPositions = choicesList!!.checkedItemPositions
                val selectedItems = ArrayList<T>(checkedItemPositions.size())
                val selectedPositions = ArrayList<Int>(checkedItemPositions.size())
                val adapter = choicesList!!.adapter
                for (index in 0 until adapter.count) {
                    if (checkedItemPositions.get(index)) {
                        selectedPositions.add(index)
                        selectedItems.add(adapter.getItem(index) as T)
                    }
                }
                adaptee.onItemsSelected(selectedPositions, selectedItems)
            }
            dismiss()
        }
    }

    interface OnItemsSelectedListener<T> {
        fun onItemsSelected(positions: List<Int>, items: List<T>)
    }

    companion object {

        private val KEY_ITEM_CHECKED_STATES = "key_item_checked_states"
    }
}

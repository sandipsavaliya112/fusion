package com.awesome.dialog

import android.os.Bundle
import android.util.SparseArray

import java.lang.ref.WeakReference

/**
 * Created by Sandip Savaliya on 17.04.2016.
 */
class AwesomeSaveStateHandler {

    private val handledDialogs: SparseArray<WeakReference<AwesomeBase<*>>>

    init {
        handledDialogs = SparseArray()
    }

    fun saveInstanceState(outState: Bundle) {
        for (index in handledDialogs.size() - 1 downTo 0) {
            val dialogRef = handledDialogs.valueAt(index)
            if (dialogRef.get() == null) {
                handledDialogs.remove(index)
                continue
            }
            val dialog = dialogRef.get()
            if (dialog!!.isShowing) {
                dialog.onSaveInstanceState(outState)
                outState.putInt(KEY_DIALOG_ID, handledDialogs.keyAt(index))
                return
            }
        }
    }

    internal fun handleDialogStateSave(id: Int, dialog: AwesomeBase<*>) {
        handledDialogs.put(id, WeakReference(dialog))
    }

    companion object {

        private val KEY_DIALOG_ID = "id"

        fun wasDialogOnScreen(savedInstanceState: Bundle): Boolean {
            return savedInstanceState.keySet().contains(KEY_DIALOG_ID)
        }

        fun getSavedDialogId(savedInstanceState: Bundle): Int {
            return savedInstanceState.getInt(KEY_DIALOG_ID, -1)
        }
    }
}

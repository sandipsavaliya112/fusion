package com.awesome.layouts

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class ExpandableSavedState : View.BaseSavedState {
    var size: Int = 0
    var weight: Float = 0.toFloat()

    internal constructor(superState: Parcelable) : super(superState) {}

    private constructor(`in`: Parcel) : super(`in`) {
        this.size = `in`.readInt()
        this.weight = `in`.readFloat()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(this.size)
        out.writeFloat(this.weight)
    }

    companion object {

        val CREATOR: Parcelable.Creator<ExpandableSavedState> = object : Parcelable.Creator<ExpandableSavedState> {
            override fun createFromParcel(`in`: Parcel): ExpandableSavedState {
                return ExpandableSavedState(`in`)
            }

            override fun newArray(size: Int): Array<ExpandableSavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}

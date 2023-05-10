package com.example.feedback_ui.model

import android.os.Parcel
import android.os.Parcelable


data class FeedbackData(

    val rating: Float = 0f,

    val feedback: String? ="",

    val companyID: String? = ""

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(rating)
        parcel.writeString(feedback)
        parcel.writeString(companyID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeedbackData> {
        override fun createFromParcel(parcel: Parcel): FeedbackData {
            return FeedbackData(parcel)
        }

        override fun newArray(size: Int): Array<FeedbackData?> {
            return arrayOfNulls(size)
        }
    }


}
package com.example.citysound

import android.os.Parcel
import android.os.Parcelable

data class Tour(
    val id: Int,
    val tourName: String,
    val description: String,
    // val imageTour: Int, // Si deseas incluir una imagen, puedes usar un recurso de imagen
    // val pointsOfInterest: List<String> // Si deseas incluir una lista de puntos de interés
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(tourName)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tour> {
        override fun createFromParcel(parcel: Parcel): Tour {
            return Tour(parcel)
        }

        override fun newArray(size: Int): Array<Tour?> {
            return arrayOfNulls(size)
        }
    }
}
package com.example.citysound

import android.os.Parcel
import android.os.Parcelable

//Creamos dataclass para pasar sus datos entre activities
data class Tour(
    val id: Int,
    val tourName: String,
    val description: String,
    val tourImage: String,
    val guideId: Int,
    val duration: Int

    //Implementamos interfaz Parcelable para empaquetar datos entre activities
) : Parcelable {


    //Contructor para crear tour para la serialización y deserialización
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ,
        parcel.readInt() ,

    )

    //sobreescribimos los metodos necesarios
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(tourName)
        parcel.writeString(description)
        parcel.writeString(tourImage)
        parcel.writeInt(guideId)

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
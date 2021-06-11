package com.example.submission2bfaa

import android.os.Parcel
import android.os.Parcelable

data class User(
    var name: String? = null,
    var detail:String? = null,
    var photo: String? = null,
    var following: String? = null,
    var followers: String? = null,
    var repository: String? = null,
    var perusahaan: String? = null,
    var lokasi: String? = null,
    var isFav: Boolean = false
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(detail)
        parcel.writeString(photo)
        parcel.writeString(following)
        parcel.writeString(followers)
        parcel.writeString(repository)
        parcel.writeString(perusahaan)
        parcel.writeString(lokasi)
        parcel.writeByte(if (isFav) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
package com.topgithub.demo.models

import android.os.Parcel
import android.os.Parcelable

class Repo() : Parcelable {

    lateinit var name: String;
    lateinit var description: String;
    lateinit var url: String;

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()!!
        description = parcel.readString()!!
        url = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Repo> {
        override fun createFromParcel(parcel: Parcel): Repo {
            return Repo(parcel)
        }

        override fun newArray(size: Int): Array<Repo?> {
            return arrayOfNulls(size)
        }
    }

}
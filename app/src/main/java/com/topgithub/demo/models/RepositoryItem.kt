package com.topgithub.demo.models

import android.os.Parcel
import android.os.Parcelable

open class RepositoryItem() : Parcelable {
    lateinit var username: String;
    lateinit var name: String;
    lateinit var type: String;
    lateinit var url: String;
    lateinit var avatar: String;
    lateinit var repo: Repo;

    constructor(parcel: Parcel) : this() {
        username = parcel.readString()!!
        name = parcel.readString()!!
        type = parcel.readString()!!
        url = parcel.readString()!!
        avatar = parcel.readString()!!
        repo = parcel.readParcelable(Repo::class.java.classLoader)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(url)
        parcel.writeString(avatar)
        parcel.writeParcelable(repo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RepositoryItem> {
        override fun createFromParcel(parcel: Parcel): RepositoryItem {
            return RepositoryItem(parcel)
        }

        override fun newArray(size: Int): Array<RepositoryItem?> {
            return arrayOfNulls(size)
        }
    }
}
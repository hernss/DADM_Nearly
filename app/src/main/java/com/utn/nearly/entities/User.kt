package com.utn.nearly.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(var uuid: String = "", var name:String ="", var surname:String ="", var email:String = "", var address :String = "", var lat: Double = 0.0, var lon: Double = 0.0, var type:String = "", var fcmToken:String = "", var createdAt : Long = 0, var lastLogin : Long = 0) :
    Parcelable {

}
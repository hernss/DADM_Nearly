package com.utn.nearly.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.utn.nearly.entities.Shop
import com.utn.nearly.entities.User
import com.utn.nearly.utils.calculateDistance
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CustomerMainViewModel : ViewModel() {
    val maxDistance = 1500 //maxima distancia en metros
    // TODO: Implement the ViewModel
    var shops = MutableLiveData<MutableList<Shop>>()

    var userLat : Double = 0.0
    var userLon : Double = 0.0
    init {
        shops.value = mutableListOf()

        val db = Firebase.firestore

        val auth = Firebase.auth

        if(auth.currentUser != null) {
            viewModelScope.launch {
                val userId = auth.currentUser!!.uid

                val user = db.collection("users").document(userId).get().await().toObject(User::class.java)
                userLat = user!!.lat
                userLon = user.lon

                val shopsCollection = db.collection("shops").get().await().toObjects(Shop::class.java)

                var listRemove : MutableList<Shop> = mutableListOf()
                shopsCollection.forEach {  shop ->
                    val distance = calculateDistance(userLat, userLon, shop.lat, shop.lon)
                    if(distance > maxDistance){
                        listRemove.add(shop)
                    }
                }

                listRemove.forEach {
                    shopsCollection.remove(it)
                }

                shops.value = shopsCollection
            }
        }
        //products.value!!.add(Producto("Aceite", 12.54, "Aceite Marolio x 1Lt", "https://www.marolio.com.ar/sites/default/files/styles/blog_image/public/AceiteGirasol3000.jpg?itok=-DfGcPAh",true))
    }
}
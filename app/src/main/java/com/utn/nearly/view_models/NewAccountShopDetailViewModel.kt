package com.utn.nearly.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.utn.nearly.entities.Shop
import com.utn.nearly.entities.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NewAccountShopDetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var isLoading = MutableLiveData<Boolean>()
    var isShopCreated = MutableLiveData<Boolean>()

    init {
        isLoading.value = false
        isShopCreated.value = false
    }

    fun createShop(shopName : String, shopAddress : String){
        isLoading.value = true

        viewModelScope.launch {
            val db = Firebase.firestore
            val auth = Firebase.auth

            if(auth.currentUser == null) {
                return@launch
            }

            val ownerID = auth.currentUser!!.uid
            val user = db.collection("users").document(ownerID).get().await().toObject(User::class.java)

            //copio la localizacion del usuario aunque tecnicamente deberia cargar la localizacion nuevamente
            val shop = Shop(ownerID, "", shopName, shopAddress,user!!.fcmToken, user.lat, user.lon, mutableListOf())

            shop.shopId = db.collection("shops").add(shop).await().id



            db.collection("shops").document(shop.shopId).update("shopId", shop.shopId).await()

            isLoading.value = false
            isShopCreated.value = true
        }

    }
}
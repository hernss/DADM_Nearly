package com.utn.nearly.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.utn.nearly.entities.Producto
import com.utn.nearly.entities.Shop
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShopMainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var products = MutableLiveData<MutableList<Producto>>()
    var isLoading = MutableLiveData<Boolean>()

    init {

        products.value = mutableListOf()

        val db = Firebase.firestore

        val auth = Firebase.auth

        if(auth.currentUser != null) {
            viewModelScope.launch {
                isLoading.value = true
                val userId = auth.currentUser!!.uid

                val shops = db.collection("shops").whereEqualTo("ownerUID", userId).get().await().toObjects(Shop::class.java)

                products.value = shops.first().productos
                isLoading.value = false
            }
        }
        //products.value!!.add(Producto("Aceite", 12.54, "Aceite Marolio x 1Lt", "https://www.marolio.com.ar/sites/default/files/styles/blog_image/public/AceiteGirasol3000.jpg?itok=-DfGcPAh",true))
    }

    fun updateProductState(productID :Int, state : Boolean){
        viewModelScope.launch {
            isLoading.value = true
            val db = Firebase.firestore
            val auth = Firebase.auth

            if(auth.currentUser != null) {
                val userId = auth.currentUser!!.uid


                var shop = db.collection("shops").whereEqualTo("ownerUID", userId).get().await()
                    .toObjects(Shop::class.java).first()

                shop.productos[productID].isActive = state

                db.collection("shops").document(shop.shopId).update("productos", shop.productos).await()
                isLoading.value = false
            }
        }
    }

    fun reloadProducts(){
        val db = Firebase.firestore

        val auth = Firebase.auth

        if(auth.currentUser != null) {
            viewModelScope.launch {
                isLoading.value = true
                val userId = auth.currentUser!!.uid

                val shops = db.collection("shops").whereEqualTo("ownerUID", userId).get().await().toObjects(Shop::class.java)

                products.value = shops.first().productos
                isLoading.value = false
            }
        }
    }
}
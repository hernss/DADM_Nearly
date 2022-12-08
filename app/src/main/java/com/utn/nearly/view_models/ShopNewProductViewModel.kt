package com.utn.nearly.view_models

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.utn.nearly.entities.Producto
import com.utn.nearly.entities.Shop
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


class ShopNewProductViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var isLoading = MutableLiveData<Boolean>()
    var productUploaded = MutableLiveData<Boolean>()

    init {
        isLoading.value = false
        productUploaded.value = false
    }

    fun uploadNewProduct(name:String, description:String, price : String, state : Boolean, imageUri : Uri){

        isLoading.value = true

        viewModelScope.launch {
            //Creo mi producto base
            var producto = Producto(name, 0.0, description,"",state)

            //Asigno el precio
            producto.precio = price.toDouble()

            val db = Firebase.firestore
            val storage = Firebase.storage
            val auth = Firebase.auth

            if(auth.currentUser == null){
                return@launch
            }

            //Generate random filename
            val filename = UUID.randomUUID().toString()

            //Upload file
            val strRef = storage.reference
            strRef.child("shop-images").child(auth.currentUser!!.uid).child(filename).putFile(imageUri).await()

            //Get download url from uploaded file
            val downloadUrl = strRef.child("shop-images").child(auth.currentUser!!.uid).child(filename).downloadUrl.await().toString()

            //Update product url image
            producto.urlImage = downloadUrl

            //Get user's shop
            val shops = db.collection("shops").whereEqualTo("ownerUID", auth.currentUser!!.uid).get().await().toObjects(Shop::class.java)

            //Add product to products list
            shops.elementAt(0).productos.add(producto)

            //Update shop with new products list
            db.collection("shops").document(shops.elementAt(0).shopId).update("productos", shops.elementAt(0).productos)

            isLoading.value = false
            productUploaded.value = true
        }

    }
}
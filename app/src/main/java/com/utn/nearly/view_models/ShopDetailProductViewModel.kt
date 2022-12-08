package com.utn.nearly.view_models

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.utn.nearly.entities.Shop
import com.utn.nearly.fragments.ShopDetailProductFragmentArgs
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShopDetailProductViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var productUpdated = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()

    var productName = MutableLiveData<String>()
    var productDescription = MutableLiveData<String>()
    var productPrice = MutableLiveData<String>()
    var productUrlImage = MutableLiveData<String>()
    var productState = MutableLiveData<Boolean>()

    init{
        productUpdated.value = false
        isLoading.value = false
    }

    fun loadProductDetail(productID : Int){
        isLoading.value = true
        val db = Firebase.firestore

        val auth = Firebase.auth

        if (auth.currentUser != null) {
            viewModelScope.launch {
                val userId = auth.currentUser!!.uid

                val shops =
                    db.collection("shops").whereEqualTo("ownerUID", userId).get().await().toObjects(
                        Shop::class.java
                    )

                var product = shops.first().productos.elementAt(productID)

                productName.value = product.name
                productDescription.value = product.descripcion
                productPrice.value = product.precio.toString()
                productUrlImage.value = product.urlImage
                productState.value = product.isActive

                isLoading.value = false
            }
        }
    }

    fun updateProduct(productID: Int, name : String, description : String, price : String, active : Boolean){
        isLoading.value = true
        val db = Firebase.firestore

        val auth = Firebase.auth

        if (auth.currentUser != null) {
            viewModelScope.launch {
                val userId = auth.currentUser!!.uid

                val shops =
                    db.collection("shops").whereEqualTo("ownerUID", userId).get().await().toObjects(
                        Shop::class.java
                    )

                shops.first().productos.elementAt(productID).name = name
                shops.first().productos.elementAt(productID).descripcion = description
                shops.first().productos.elementAt(productID).precio = price.toDouble()
                shops.first().productos.elementAt(productID).isActive = active


                db.collection("shops").document(shops.first().shopId).update("productos", shops.first().productos).await()

                isLoading.value = false
                productUpdated.value = true
            }
        }
    }

}



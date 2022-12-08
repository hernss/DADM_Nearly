package com.utn.nearly.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.utn.nearly.entities.Pedido
import com.utn.nearly.entities.Producto
import com.utn.nearly.entities.Shop
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CustomerSelectedShopArticlesViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var productsList = MutableLiveData<MutableList<Producto>>()
    var isLoading = MutableLiveData<Boolean>()
    var shopName = MutableLiveData<String>()

    private var shopID : String = ""
    fun setShopID(_shopID: String){
        val db = Firebase.firestore
        isLoading.value = true
        viewModelScope.launch{
            val shop = db.collection("shops").document(_shopID).get().await().toObject(Shop::class.java)

            //Remuevo los que no estan activos
            var tempList : MutableList<Producto> = mutableListOf()
            shop?.productos?.forEach {
                if(it.isActive)
                    tempList.add(it)
            }

            productsList.value = tempList
            shopName.value = shop?.name
            isLoading.value = false
            shopID = _shopID
        }
    }

    fun getBasicPedido(): Pedido? {

        if (Firebase.auth.currentUser == null) {
            return null
        }
        val userID = Firebase.auth.currentUser!!.uid

        return Pedido(
            "",
            userID,
            null,
            shopID,
            mutableListOf(),
            "PENDING",
            0.0,
            System.currentTimeMillis(),
            0,
            0
        )
    }
}
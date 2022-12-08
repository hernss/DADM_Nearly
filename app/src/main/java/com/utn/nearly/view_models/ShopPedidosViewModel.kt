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
import com.utn.nearly.entities.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShopPedidosViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var pedidos = MutableLiveData<MutableList<Pedido>>()
    var isLoading = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            isLoading.value = true

            getPedidosFromDB()
            isLoading.value = false
        }
    }

    fun updatePedidoState(pedidoNro : Int){
        viewModelScope.launch {
            isLoading.value = true

            val db = Firebase.firestore
            val auth = Firebase.auth

            var pedidoID = pedidos.value?.elementAt(pedidoNro)?.pedidoId

            if(pedidoID == null) pedidoID = ""

            db.collection("pedidos").document(pedidoID!!).update("state", "PREPARED", "preparedAt", System.currentTimeMillis()).await()

            getPedidosFromDB()

            isLoading.value = false
        }
    }

    private suspend fun getPedidosFromDB() {
        val db = Firebase.firestore
        val auth = Firebase.auth

        if (auth.currentUser != null) {
            val userId = auth.currentUser!!.uid

            val shops = db.collection("shops").whereEqualTo("ownerUID", userId).get().await()
                .toObjects(Shop::class.java)

            val shopId = shops.first().shopId

            val pedidosDB = db.collection("pedidos").whereEqualTo("shopId", shopId).get().await()
                .toObjects(Pedido::class.java)

            pedidosDB.forEach { pedido ->
                val user = db.collection("users").document(pedido.userId).get().await()
                    .toObject(User::class.java)
                pedido.user = user
            }

            pedidos.value = pedidosDB
        }
    }
}
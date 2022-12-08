package com.utn.nearly.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.utn.nearly.entities.Pedido
import com.utn.nearly.entities.Producto
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CustomerShopBagViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var isLoading = MutableLiveData<Boolean>()
    var totalPedido = MutableLiveData<Double>()
    var products = MutableLiveData<MutableList<Producto>>()
    var pedidoAccepted = MutableLiveData<Boolean>()

    private lateinit var pedido : Pedido

    init {
        isLoading.value = false
        pedidoAccepted.value = false
    }

    fun setPedido(_pedido : Pedido){
        products.value = _pedido.products
        var total : Double = 0.0
        _pedido.products.forEach {
            total += it.precio
        }
        _pedido.total = total
        totalPedido.value = total
        pedido = _pedido
    }

    fun acceptPedido(){
        val db = Firebase.firestore
        isLoading.value = true
        viewModelScope.launch {
            pedido.state = "ACCEPTED"

            val pedidoID = db.collection("pedidos").add(pedido).await().id

            db.collection("pedidos").document(pedidoID).update("pedidoId", pedidoID)

            isLoading.value=false
            pedidoAccepted.value = true
        }
    }

}
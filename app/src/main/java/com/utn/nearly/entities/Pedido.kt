package com.utn.nearly.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
class Pedido(var pedidoId : String = "", var userId : String = "", var user : User? = null, var shopId : String = "", var products : @RawValue  MutableList<Producto> = mutableListOf(), var state : String = "", var total : Double = 0.0, var createdAt : Long = 0, var preparedAt : Long = 0, var deliveryAt : Long = 0) : Parcelable{
}
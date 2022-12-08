package com.utn.nearly.entities

data class Shop( var ownerUID: String = "", var shopId : String = "", var name : String = "", var direccion : String = "", var fcmToken: String = "", var lat : Double = 0.0, var lon : Double = 0.0, var productos : MutableList<Producto> = mutableListOf())



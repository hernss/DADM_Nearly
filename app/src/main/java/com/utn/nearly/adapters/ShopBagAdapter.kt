package com.utn.nearly.adapters

import android.view.View
import com.utn.nearly.entities.Producto


class ShopBagAdapter(private var productsList : MutableList<Producto>) : BaseProductAdapter(productsList,{ }) {

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.setName(productsList[position].name)
        holder.setDescription(productsList[position].descripcion)
        holder.setImage(productsList[position].urlImage)
        holder.setPrice(productsList[position].precio)
        holder.setState(false) //By default all off


        //Hide switch
        holder.getSwitch().visibility = View.GONE
    }

}
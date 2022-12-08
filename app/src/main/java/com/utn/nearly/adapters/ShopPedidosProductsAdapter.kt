package com.utn.nearly.adapters

import android.view.View
import com.utn.nearly.entities.Producto

class ShopPedidosProductsAdapter (private var productsList : MutableList<Producto>,
                                 private var onClick: (Int)->Unit) : BaseProductAdapter(productsList, onClick) {

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.setName(productsList[position].name)
        holder.setDescription(productsList[position].descripcion)
        holder.setImage(productsList[position].urlImage)
        holder.setPrice(productsList[position].precio)
        holder.setState(false)
        holder.getSwitch().visibility = View.GONE
        holder.getCard().setOnClickListener{
            onClick(position)
        }
    }
}
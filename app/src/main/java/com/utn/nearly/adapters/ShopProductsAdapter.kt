package com.utn.nearly.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.utn.nearly.R
import com.utn.nearly.entities.Producto

open class ShopProductsAdapter(
private var productsList : MutableList<Producto>,
private var onClick: (Int)->Unit,
private var onSwitchChange : (Int, Boolean) -> Unit): BaseProductAdapter(productsList,onClick)
{

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.setName(productsList[position].name)
        holder.setDescription(productsList[position].descripcion)
        holder.setImage(productsList[position].urlImage)
        holder.setPrice(productsList[position].precio)
        holder.setState(productsList[position].isActive)
        holder.getCard().setOnClickListener{
            onClick(position)
        }
        holder.getSwitch().setOnCheckedChangeListener { compoundButton, b ->
            onSwitchChange(position, compoundButton.isChecked)
        }
    }
}
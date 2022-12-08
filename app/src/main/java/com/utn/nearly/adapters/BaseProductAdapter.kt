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

open class BaseProductAdapter (
    private var productsList : MutableList<Producto>,
    private var onClick: (Int)->Unit): RecyclerView.Adapter<BaseProductAdapter.ProductHolder>()
{
    class ProductHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View
        init{
            this.view = v
        }

        fun setName(name : String){
            var txtName : TextView = view.findViewById(R.id.txtProductDetailName)
            txtName.text = name
        }

        fun setDescription(description : String){
            var txtDesc : TextView = view.findViewById(R.id.txtProductDetailDescription)
            txtDesc.text = description
        }

        fun setImage(url : String){
            Glide.with(view)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(view.findViewById(R.id.imgProductDetailImage))
        }
        fun getCard() : CardView {
            return view.findViewById(R.id.cardViewItem)
        }

        fun getSwitch() : Switch {
            return view.findViewById(R.id.swProductDetailActive)
        }

        fun setPrice(price : Double){
            var txtPrice : TextView = view.findViewById(R.id.txtProductDetailPrice)
            txtPrice.text = "$ $price"
        }

        fun setState(state : Boolean){
            var swState : Switch = view.findViewById(R.id.swProductDetailActive)
            swState.isChecked = state
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return BaseProductAdapter.ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.setName(productsList[position].name)
        holder.setDescription(productsList[position].descripcion)
        holder.setImage(productsList[position].urlImage)
        holder.setPrice(productsList[position].precio)
        holder.setState(productsList[position].isActive)
        holder.getCard().setOnClickListener{
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}
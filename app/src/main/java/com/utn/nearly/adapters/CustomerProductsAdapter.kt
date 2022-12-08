package com.utn.nearly.adapters

import com.utn.nearly.entities.Producto

class CustomerProductsAdapter(private var productsList : MutableList<Producto>,
                              private var onClick: (Int)->Unit) : BaseProductAdapter(productsList, onClick) {

    private var selectedProducts : MutableList<Producto> = mutableListOf()

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.setName(productsList[position].name)
        holder.setDescription(productsList[position].descripcion)
        holder.setImage(productsList[position].urlImage)
        holder.setPrice(productsList[position].precio)
        holder.setState(false) //By default all off
        holder.getCard().setOnClickListener{
            onClick(position)
        }

        //On checked change update selectedProducts list
        holder.getSwitch().setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked){
                selectedProducts.add(productsList[position])
            }else{
                selectedProducts.remove(productsList[position])
            }
        }
    }

    fun getSelectedProducts() : MutableList<Producto>{
        return selectedProducts
    }
}
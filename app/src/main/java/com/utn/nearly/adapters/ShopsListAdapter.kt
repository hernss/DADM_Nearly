package com.utn.nearly.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.utn.nearly.R
import com.utn.nearly.entities.Shop
import com.utn.nearly.utils.calculateDistance
import kotlin.math.roundToInt

class ShopsListAdapter (
    private var shopsList : MutableList<Shop>,
    private var onClick: (String)->Unit): RecyclerView.Adapter<ShopsListAdapter.ShopHolder>()
{
    class ShopHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View
        init{
            this.view = v
        }

        fun setName(name : String){
            var txtName : TextView = view.findViewById(R.id.txtShopItemDetailName)
            txtName.text = name
        }

        fun setAddress(address : String){
            var txtAddress : TextView = view.findViewById(R.id.txtShopItemDetailAddress)
            txtAddress.text = address
        }

        fun setDistance(distance : Double){
            var txtDistance : TextView = view.findViewById(R.id.txtShopItemDetailDistance)
            txtDistance.text = "${distance.roundToInt()} mts"
        }
        fun getCard() : CardView {
            return view.findViewById(R.id.shopItemCardView)
        }
    }
    var lat : Double = 0.0
    var lon : Double = 0.0

    fun setHomeLocalization(_lat:Double, _lon:Double){
        lat = _lat
        lon = _lon

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop_item, parent, false)
        return ShopsListAdapter.ShopHolder(view)
    }

    override fun onBindViewHolder(holder: ShopsListAdapter.ShopHolder, position: Int) {

        holder.setAddress(shopsList[position].direccion)
        holder.setDistance(calculateDistance(lat, lon,shopsList[position].lat, shopsList[position].lon))
        holder.setName(shopsList[position].name)
        holder.getCard().setOnClickListener{
            onClick(shopsList[position].shopId)
        }
    }

    override fun getItemCount(): Int {
        return shopsList.size
    }
}
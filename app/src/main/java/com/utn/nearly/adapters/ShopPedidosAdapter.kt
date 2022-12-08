package com.utn.nearly.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.utn.nearly.R
import com.utn.nearly.entities.Pedido

class ShopPedidosAdapter (
    private var pedidosList : MutableList<Pedido>,
    private var onClick: (Int)->Unit,
    private var onStateChange: (Int)->Unit): RecyclerView.Adapter<ShopPedidosAdapter.PedidoHolder>()
    {
        class PedidoHolder(v: View) : RecyclerView.ViewHolder(v) {
            private var view: View
            init{
                this.view = v
            }

            fun setCustomerName(name : String){
                var txtName : TextView = view.findViewById(R.id.txtItemPedidoCustomerName)
                txtName.text = "Customer: $name"
            }

            fun setCustomerAddress(address : String){
                var txtAddress : TextView = view.findViewById(R.id.txtItemPedidoCustomerAddress)
                txtAddress.text = "Address: $address"
            }

            fun setIdPedido(id : String){
                var txtIdPedido : TextView = view.findViewById(R.id.txtItemPedidoId)
                txtIdPedido.text = id
            }

            fun getCard() : CardView {
                return view.findViewById(R.id.itemPedidoCardView)
            }

            fun getButton() : Button {
                return view.findViewById(R.id.btnItemPedidoReady)
            }

            fun setState(state : String){
                if(state == "ACCEPTED"){
                    getButton().visibility = View.VISIBLE
                }else {
                    getButton().visibility = View.GONE
                }
            }

            fun setTotalPrice(price : Double){
                var txtTotal : TextView = view.findViewById(R.id.txtItemPedidototal)
                txtTotal.text = "Total: $ $price"
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.pedido_item, parent, false)
            return ShopPedidosAdapter.PedidoHolder(view)
        }

        override fun onBindViewHolder(holder: PedidoHolder, position: Int) {
            pedidosList[position].user?.name?.let { holder.setCustomerName(it) }
            pedidosList[position].user?.address?.let { holder.setCustomerAddress(it) }

            holder.setIdPedido(pedidosList[position].pedidoId)
            holder.setTotalPrice(pedidosList[position].total)
            holder.setState(pedidosList[position].state)
            holder.getCard().setOnClickListener{
                onClick(position)
            }
            holder.getButton().setOnClickListener {
                onStateChange(position)
            }
        }

        override fun getItemCount(): Int {
            return pedidosList.size
        }
    }
package com.utn.nearly.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.utn.nearly.R
import com.utn.nearly.adapters.CustomerProductsAdapter
import com.utn.nearly.adapters.ShopPedidosProductsAdapter
import com.utn.nearly.databinding.FragmentCustomerMainBinding
import com.utn.nearly.databinding.FragmentShopPedidosDetailBinding
import com.utn.nearly.view_models.ShopPedidosDetailViewModel

class ShopPedidosDetailFragment : Fragment() {

    private lateinit var binding : FragmentShopPedidosDetailBinding
    private val args : ShopPedidosDetailFragmentArgs by navArgs()

    companion object {
        fun newInstance() = ShopPedidosDetailFragment()
    }

    private lateinit var viewModel: ShopPedidosDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopPedidosDetailBinding.inflate(inflater,
            container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.pedidoDetailProductsReciclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ShopPedidosProductsAdapter(args.pedido.products){ productID ->
            //val action = ShopMainFragmentDirections.actionShopMainFragmentToShopDetailProductFragment(productID)
            //binding.root.findNavController().navigate(action)
            Toast.makeText(requireContext(), "Click en $productID", Toast.LENGTH_LONG).show()
        }

        binding.pedidoDetailProductsReciclerView.adapter = adapter

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShopPedidosDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
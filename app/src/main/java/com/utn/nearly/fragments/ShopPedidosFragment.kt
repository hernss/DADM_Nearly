package com.utn.nearly.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.utn.nearly.R
import com.utn.nearly.adapters.ShopPedidosAdapter
import com.utn.nearly.adapters.ShopProductsAdapter
import com.utn.nearly.databinding.FragmentShopMainBinding
import com.utn.nearly.databinding.FragmentShopPedidosBinding
import com.utn.nearly.view_models.ShopPedidosViewModel

class ShopPedidosFragment : Fragment() {
    private lateinit var binding : FragmentShopPedidosBinding

    companion object {
        fun newInstance() = ShopPedidosFragment()
    }

    private lateinit var viewModel: ShopPedidosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopPedidosBinding.inflate(inflater,
            container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.shopPedidosReciclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.pedidos.observe(viewLifecycleOwner, Observer { pedidos ->
            var adapter = ShopPedidosAdapter(pedidos, { pedidoID ->
                val action = ShopPedidosFragmentDirections.actionShopPedidosFragmentToShopPedidosDetailFragment(pedidos[pedidoID])
                binding.root.findNavController().navigate(action)
            }, { pedidoID ->
                viewModel.updatePedidoState(pedidoID)
            })


            binding.shopPedidosReciclerView.adapter = adapter
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.shopPedidosProgressBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.shopPedidosProgressBar.visibility = View.GONE
            }
        })
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShopPedidosViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
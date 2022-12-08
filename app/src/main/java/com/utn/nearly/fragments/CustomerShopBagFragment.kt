package com.utn.nearly.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.utn.nearly.R
import com.utn.nearly.adapters.ShopBagAdapter
import com.utn.nearly.adapters.ShopsListAdapter
import com.utn.nearly.databinding.FragmentCustomerMainBinding
import com.utn.nearly.databinding.FragmentCustomerShopBagBinding
import com.utn.nearly.view_models.CustomerShopBagViewModel

class CustomerShopBagFragment : Fragment() {

    companion object {
        fun newInstance() = CustomerShopBagFragment()
    }
    private lateinit var binding : FragmentCustomerShopBagBinding
    private lateinit var viewModel: CustomerShopBagViewModel
    private val args : CustomerShopBagFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerShopBagBinding.inflate(inflater,
            container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.shopBagProductsReciclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.products.observe(viewLifecycleOwner, Observer { products ->
            var adapter = ShopBagAdapter(products)

            binding.shopBagProductsReciclerView.adapter = adapter
        })

        viewModel.totalPedido.observe(viewLifecycleOwner, Observer { totalPedido ->
            binding.txtShopBagTotalPedido.text = "Total: $ $totalPedido"
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.shopBagProgressBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.shopBagProgressBar.visibility = View.GONE
            }
        })

        viewModel.pedidoAccepted.observe(viewLifecycleOwner, Observer { isAccepted ->
            if(isAccepted){
                binding.root.findNavController().navigateUp()
            }
        })

        binding.btnShopBagAccept.setOnClickListener {
            viewModel.acceptPedido()
        }


        viewModel.setPedido(args.pedido)
        (activity as AppCompatActivity).supportActionBar?.title = "Shop Bag"
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomerShopBagViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
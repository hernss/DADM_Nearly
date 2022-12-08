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
import com.utn.nearly.adapters.ShopProductsAdapter
import com.utn.nearly.databinding.FragmentShopMainBinding
import com.utn.nearly.view_models.ShopMainViewModel

class ShopMainFragment : Fragment() {
    private lateinit var binding : FragmentShopMainBinding
    private val PREF_NAME = "myPreferences"

    companion object {
        fun newInstance() = ShopMainFragment()
    }

    private lateinit var viewModel: ShopMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopMainBinding.inflate(inflater,
            container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.shopProductsReciclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.products.observe(viewLifecycleOwner, Observer { products ->
            var adapter = ShopProductsAdapter(products,{ productID ->
                val action = ShopMainFragmentDirections.actionShopMainFragmentToShopDetailProductFragment(productID)
                binding.root.findNavController().navigate(action)
            },{productID, state ->
                viewModel.updateProductState(productID, state)
            })

            binding.shopProductsReciclerView.adapter = adapter
        })

        binding.btnShopMainAddProduct.setOnClickListener {
            val action = ShopMainFragmentDirections.actionShopMainFragmentToShopNewProductFragment()
            binding.root.findNavController().navigate(action)
        }

        viewModel.reloadProducts()

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.shopMainProgressBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.shopMainProgressBar.visibility = View.GONE
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShopMainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
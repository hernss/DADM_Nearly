package com.utn.nearly.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.utn.nearly.R
import com.utn.nearly.adapters.CustomerProductsAdapter
import com.utn.nearly.adapters.ShopProductsAdapter
import com.utn.nearly.databinding.FragmentCustomerMainBinding
import com.utn.nearly.databinding.FragmentCustomerSelectedShopArticlesBinding
import com.utn.nearly.entities.Pedido
import com.utn.nearly.view_models.CustomerSelectedShopArticlesViewModel

class CustomerSelectedShopArticlesFragment : Fragment() {
private lateinit var binding : FragmentCustomerSelectedShopArticlesBinding
    companion object {
        fun newInstance() = CustomerSelectedShopArticlesFragment()
    }

    private lateinit var adapter : CustomerProductsAdapter
    private val args : CustomerSelectedShopArticlesFragmentArgs by navArgs()
    private lateinit var viewModel: CustomerSelectedShopArticlesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerSelectedShopArticlesBinding.inflate(inflater,
            container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_customer_carrito, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = when(item.itemId) {

            R.id.shopCartActionBarButton -> {
                if(adapter.getSelectedProducts().isNotEmpty()) {
                    val pedido = viewModel.getBasicPedido() ?: return true

                    pedido.products = adapter.getSelectedProducts()
                    val action =
                        CustomerSelectedShopArticlesFragmentDirections.actionCustomerSelectedShopArticlesFragmentToCustomerShopBagFragment(pedido)
                    binding.root.findNavController().navigate(action)
                    return true
                }else {
                    Toast.makeText(requireContext(), "Select at least one product", Toast.LENGTH_LONG).show()
                    return true
                }
            }

            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.customerShopProductsProgressBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.customerShopProductsProgressBar.visibility = View.GONE
            }
        })

        binding.customerShopArticlesReciclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.productsList.observe(viewLifecycleOwner, Observer { products ->
            adapter = CustomerProductsAdapter(products){ productID ->
                //val action = ShopMainFragmentDirections.actionShopMainFragmentToShopDetailProductFragment(productID)
                //binding.root.findNavController().navigate(action)
                Toast.makeText(requireContext(), "Click en $productID", Toast.LENGTH_LONG).show()
            }

            binding.customerShopArticlesReciclerView.adapter = adapter




        })

        viewModel.setShopID(args.shopID)

        viewModel.shopName.observe(viewLifecycleOwner, Observer { shopName ->
            (activity as AppCompatActivity).supportActionBar?.title = shopName
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomerSelectedShopArticlesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
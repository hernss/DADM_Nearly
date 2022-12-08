package com.utn.nearly.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.utn.nearly.R
import com.utn.nearly.adapters.ShopsListAdapter
import com.utn.nearly.databinding.FragmentCustomerMainBinding
import com.utn.nearly.view_models.CustomerMainViewModel

class CustomerMainFragment : Fragment() {

    companion object {
        fun newInstance() = CustomerMainFragment()
    }

    private lateinit var binding : FragmentCustomerMainBinding
    private lateinit var viewModel: CustomerMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerMainBinding.inflate(inflater,
            container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }



    override fun onStart() {
        super.onStart()
        binding.customerShopsReciclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.shops.observe(viewLifecycleOwner, Observer { shops ->
            var adapter = ShopsListAdapter(shops){ shopID ->
                val action = CustomerMainFragmentDirections.actionCustomerMainFragmentToCustomerSelectedShopArticlesFragment(shopID)
                binding.root.findNavController().navigate(action)
                //Toast.makeText(requireContext(), "Click en $shopID", Toast.LENGTH_LONG).show()
            }
            adapter.setHomeLocalization(viewModel.userLat, viewModel.userLon)
            binding.customerShopsReciclerView.adapter = adapter
            if(shops.isEmpty()){
                binding.txtCustomerMainNoShops.visibility = View.VISIBLE
            } else {
                binding.txtCustomerMainNoShops.visibility = View.GONE
            }
        })
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.app_name)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomerMainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
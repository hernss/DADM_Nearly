package com.utn.nearly.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.utn.nearly.databinding.FragmentNewAccountShopDetailBinding
import com.utn.nearly.view_models.NewAccountShopDetailViewModel

class NewAccountShopDetailFragment : Fragment() {

    companion object {
        fun newInstance() = NewAccountShopDetailFragment()
    }

    lateinit var binding: FragmentNewAccountShopDetailBinding
    private lateinit var viewModel: NewAccountShopDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewAccountShopDetailBinding.inflate(inflater,
            container, false)

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.shopDetailProgressBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.shopDetailProgressBar.visibility = View.GONE
            }
        })

        viewModel.isShopCreated.observe(viewLifecycleOwner, Observer { isShopCreated ->
            if(isShopCreated){
                //Show Spinner
                val action = NewAccountShopDetailFragmentDirections.actionNewAccountShopDetailFragmentToShopMainActivity()
                binding.root.findNavController().navigate(action)
                activity?.finish()
            }
        })

        binding.btnShopDetailNext.setOnClickListener {
            viewModel.createShop(binding.txtNewAccountShopName.text.toString(), binding.txtNewAccountShopAddress.text.toString())
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewAccountShopDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
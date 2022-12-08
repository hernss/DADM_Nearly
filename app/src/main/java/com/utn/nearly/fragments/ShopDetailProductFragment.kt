package com.utn.nearly.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.utn.nearly.R
import com.utn.nearly.databinding.FragmentLoginBinding
import com.utn.nearly.databinding.FragmentShopDetailProductBinding
import com.utn.nearly.view_models.ShopDetailProductViewModel

class ShopDetailProductFragment : Fragment() {
    private lateinit var binding : FragmentShopDetailProductBinding

    companion object {
        fun newInstance() = ShopDetailProductFragment()
    }

    val args : ShopDetailProductFragmentArgs by navArgs()

    private lateinit var viewModel: ShopDetailProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopDetailProductBinding.inflate(inflater,
            container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.btnDetailProductUpdateButton.setOnClickListener {
            //Update product
            val name = binding.txtDetailProductName.text.toString()
            val description = binding.txtDetailProductDescription.text.toString()
            val price = binding.txtDetailProductPrice.text.toString()
            val active = binding.swDetailProductStateSwitch.isChecked
            viewModel.updateProduct(args.productID, name, description, price, active)
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.detailProductProgressBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.detailProductProgressBar.visibility = View.GONE
            }
        })

        viewModel.productName.observe(viewLifecycleOwner, Observer {
            binding.txtDetailProductName.setText(it)
        })

        viewModel.productDescription.observe(viewLifecycleOwner, Observer {
            binding.txtDetailProductDescription.setText(it)
        })

        viewModel.productPrice.observe(viewLifecycleOwner, Observer {
            binding.txtDetailProductPrice.setText(it)
        })

        viewModel.productState.observe(viewLifecycleOwner, Observer {
            binding.swDetailProductStateSwitch.isChecked = it
        })

        viewModel.productUrlImage.observe(viewLifecycleOwner, Observer {
            Glide.with(requireView())
                .load(it)
                .fitCenter()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.imgDetailProductImage)
        })

        viewModel.loadProductDetail(args.productID)

        viewModel.productUpdated.observe(viewLifecycleOwner, Observer { productUpdated ->
            if (productUpdated) {
                binding.root.findNavController().navigateUp()
            }
        })
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShopDetailProductViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
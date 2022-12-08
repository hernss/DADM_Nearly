package com.utn.nearly.fragments

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.utn.nearly.R
import com.utn.nearly.databinding.FragmentNewAccountBinding
import com.utn.nearly.entities.User
import com.utn.nearly.utils.*
import com.utn.nearly.view_models.NewAccountViewModel
import java.security.Timestamp

class NewAccountFragment : Fragment() {
    lateinit var binding : FragmentNewAccountBinding
    companion object {
        fun newInstance() = NewAccountFragment()
    }

    private lateinit var viewModel: NewAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewAccountBinding.inflate(inflater,
            container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        //Check if password is correct
        binding.txtNewAccountPassword.doOnTextChanged { text, start, before, count ->
            val pass : String = binding.txtNewAccountPassword.text.toString()
            if(isValidPassword(pass)){
                binding.txtNewAccountPassword.setError(null)
            }else{
                binding.txtNewAccountPassword.setError(resources.getString(R.string.newAccountFragment_invalidPasswordText))
            }
        }

        //Click on next button
        binding.btnNewAccountNext.setOnClickListener {
            //Log.d("NEWACC", binding.radioGroup.checkedRadioButtonId.toString())

            val email = binding.txtNewAccountEmail.text.toString()
            if (!isEmailValid(email)){
                Toast.makeText(requireContext(), resources.getString(R.string.newAccountFragment_invalidEmailText),Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val pass : String = binding.txtNewAccountPassword.text.toString()
            if(!isValidPassword(pass)){
                Toast.makeText(requireContext(), resources.getString(R.string.newAccountFragment_invalidPasswordText),Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var userType = "CUSTOMER"
            if(binding.radioGroup.checkedRadioButtonId == R.id.btnNewAccountDeliveryType){
                userType = "DELIVERY"
            }
            if(binding.radioGroup.checkedRadioButtonId == R.id.btnNewAccountShopType){
                userType = "SHOP"
            }

            val address = binding.txtNewAccountAddress.text.toString()
            val name = binding.txtNewAccountName.text.toString()
            val surname = binding.txtNewAccountLastname.text.toString()

            if((address == "") || (name == "") || (surname == "")){
                Toast.makeText(requireContext(), resources.getString(R.string.newAccountFragment_fieldsEmpty),Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val user = User("", name, surname, email, address,0.0, 0.0, userType, "", System.currentTimeMillis(), System.currentTimeMillis())

            viewModel.createAccount(user, pass)
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.newAccountLoadingBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.newAccountLoadingBar.visibility = View.GONE
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            if (message != "" )
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        })

        viewModel.accountCreated.observe(viewLifecycleOwner, Observer { accountCreated ->
            if(accountCreated){
                //Account created succesfull
                val action = NewAccountFragmentDirections.actionNewAccountFragmentToNewAccountGeolocFragment()
                binding.root.findNavController().navigate(action)
            }
        })

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewAccountViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
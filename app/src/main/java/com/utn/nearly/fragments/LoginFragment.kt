package com.utn.nearly.fragments

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.utn.nearly.R
import com.utn.nearly.databinding.FragmentLoginBinding
import com.utn.nearly.view_models.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val PREF_NAME = "myPreferences"

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,
            container, false)

         return binding.root
    }

    override fun onStart() {
        super.onStart()

        //Apreto el texto para crear nueva cuenta
        binding.txtNotUserYet.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToNewAccountFragment()
            binding.root.findNavController().navigate(action)
        }

        //Apreto el boton de login
        binding.btnLogin.setOnClickListener {
            val email = binding.txtLoginUsername.text.toString()
            val password = binding.txtLoginPassword.text.toString()
            viewModel.doLogin(email, password)
        }

        //Declaro los observers
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if(it != "")
                Snackbar.make(requireView(),resources.getString(R.string.loginFragment_loginErrorText), Snackbar.LENGTH_LONG).show()
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.loginFragmentProgressBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.loginFragmentProgressBar.visibility = View.GONE
            }
        })

        viewModel.isUserLoggedIn.observe(viewLifecycleOwner, Observer { isUserLoggedIn ->
            if(isUserLoggedIn){
                //Si el usuario esta logueado me voy al activity del usuario

                val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("userId", viewModel.userId)
                editor.apply()

                if(viewModel.userType == "SHOP"){
                    val action = LoginFragmentDirections.actionLoginFragmentToShopMainActivity()
                    binding.root.findNavController().navigate(action)
                    activity?.finish()
                }else if(viewModel.userType == "DELIVERY"){
                    val action = LoginFragmentDirections.actionLoginFragmentToDeliveryMainActivity()
                    binding.root.findNavController().navigate(action)
                    activity?.finish()
                }else if(viewModel.userType == "CUSTOMER"){
                    val action = LoginFragmentDirections.actionLoginFragmentToCustomerMainActivity()
                    binding.root.findNavController().navigate(action)
                    activity?.finish()
                }

            }
        })

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
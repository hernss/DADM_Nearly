package com.utn.nearly.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.utn.nearly.R
import com.utn.nearly.entities.User
import kotlinx.coroutines.tasks.await

class SplashFragment : Fragment() {

    private val PREF_NAME = "myPreferences"
    lateinit var v : View

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_splash, container, false)
        return v
    }

    override fun onStart() {
        super.onStart()
        //Acceso a las shared preferences
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        //Me fijo si me tengo que saltar el splash, por si vengo de un logout
        val skipSplash = sharedPref.getBoolean("skipSplash",false)

        val auth = Firebase.auth
        var userId  = ""
        if(auth.currentUser != null) {
            userId = auth.currentUser!!.uid
            Log.d("SPLASH", "User already logged: $userId")
        }

        if(skipSplash){
            //Si me lo  tengo que saltar, bajo el flag para que la proxima no me lo salte
            editor.putBoolean("skipSplash",false)
            editor.apply()

            val skipAction = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            v.findNavController().navigate(skipAction)
            return
        }

        //Creo el temporizador del splash
        val timer = object: CountDownTimer(3000, 3000) {
            override fun onTick(p0: Long) {
                return
            }

            override fun onFinish() {
                if(userId != "") {
                    //Tengo un usuario logueado, asi que me salto la pantalla de login
                    //Guardo el user_id en las shared preference para poder levantarlos en el activity
                    editor.putString("userId", userId)
                    editor.apply()

                    val db = Firebase.firestore

                    db.collection("users").document(userId).update("lastLogin", System.currentTimeMillis()).addOnCompleteListener {
                        Log.d("SPLASH","User updated at firestore")
                        db.collection("users").document(userId).get().addOnCompleteListener {
                            Log.d("SPLASH", "Get user listener complete")
                            if (it.isSuccessful){
                                val user : User? = it.result.toObject(User::class.java)

                                if(user == null){
                                    Toast.makeText(requireContext(), "LOGIN ERROR - User Null", Toast.LENGTH_LONG).show()
                                    return@addOnCompleteListener
                                }
                                if(user?.type == "SHOP"){
                                    val action = SplashFragmentDirections.actionSplashFragmentToShopMainActivity()
                                    v.findNavController().navigate(action)
                                    activity?.finish()
                                }else if(user?.type == "DELIVERY"){
                                    val action = SplashFragmentDirections.actionSplashFragmentToDeliveryMainActivity()
                                    v.findNavController().navigate(action)
                                    activity?.finish()
                                }else{
                                    val action = SplashFragmentDirections.actionSplashFragmentToCustomerMainActivity()
                                    v.findNavController().navigate(action)
                                    activity?.finish()
                                }
                            }else{
                                Toast.makeText(requireContext(), "LOGIN ERROR", Toast.LENGTH_LONG).show()
                            }
                        }
                            .addOnFailureListener {
                                Log.d("SPLASH", "DB Update failed")
                            }
                    }
                        .addOnFailureListener {
                            Log.d("SPLASH", "DB Update failed")
                        }

                }else {
                    val actionLogin = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    //val action = SplashFragmentDirections.actionSplashFragmentToNewUserFragment()
                    v.findNavController().navigate(actionLogin)
                }
            }
        }
        timer.start()
    }

}
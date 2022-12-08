package com.utn.nearly.view_models

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.internal.FallbackServiceBroker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.utn.nearly.entities.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    private val TAG = "LoginViewModel"

    //Variables de interaccion con la UI
    var errorMessage = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()
    var isUserLoggedIn = MutableLiveData<Boolean>()
    var userId : String = ""
    var userType : String = "DEFAULT"

    init {
        //Inicializo las variable
        errorMessage.value = ""
        isLoading.value = false
        isUserLoggedIn.value = false
    }

    fun doLogin(email : String, password : String){
        //Recupero la instancia de auth
        val auth = Firebase.auth
        val db = Firebase.firestore

        isLoading.value = true
        viewModelScope.launch {
            var uid : String = ""
            try {
                uid = auth.signInWithEmailAndPassword(email, password).await().user?.uid!!
            }catch (e : Exception){
                Log.w(TAG, "signInWithEmail:failure $e")
                errorMessage.value = e.toString()
                isLoading.value = false
                return@launch
            }

            db.collection("users").document(uid).update("lastLogin", System.currentTimeMillis()).await()
            val user : User? = db.collection("users").document(uid).get().await().toObject(User::class.java)

            if (user == null){
                Log.w(TAG, "signInWithEmail:failure User Null")
                errorMessage.value = "User Null"
                isLoading.value = false
                return@launch
            }

            userType = user.type
            isUserLoggedIn.value = true
            isLoading.value = false
        }
        /*auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    userId = task.result.user?.uid.toString()

                    db.collection("users").document(userId).update("lastLogin", System.currentTimeMillis())




                    isUserLoggedIn.value = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    errorMessage.value = task.exception.toString()
                }
                isLoading.value = false
            }*/
    }
}
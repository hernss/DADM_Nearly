package com.utn.nearly.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.utn.nearly.entities.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.concurrent.timerTask

class NewAccountGeolocViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var userType :String = ""

    var isLoading = MutableLiveData<Boolean>()
    var userUpdated = MutableLiveData<Boolean>()

    var latitud : Double = 0.0
    var longitud : Double = 0.0

    init{
        isLoading.value = false
        userUpdated.value = false

        val auth = Firebase.auth
        if (auth.currentUser != null) {
            val db = Firebase.firestore

            db.collection("users").document(auth.currentUser!!.uid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    userType = it.result.toObject(User::class.java)?.type!!
                }else{
                    userType = ""
                }
            }
        }
    }

    fun updateUser(lat : Double, lon : Double){
        isLoading.value = true

        val auth = Firebase.auth
        if(auth.currentUser == null){
            return
        }
        val userId = auth.currentUser!!.uid

        val db = Firebase.firestore

        db.collection("users").document(userId).update("lat",lat, "lon", lon).addOnCompleteListener {
            userUpdated.value = it.isSuccessful
            isLoading.value = false
        }
    }

    fun updateUser() {
        updateUser(latitud, longitud)
    }
}
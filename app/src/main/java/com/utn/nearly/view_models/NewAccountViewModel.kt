package com.utn.nearly.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.utn.nearly.entities.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NewAccountViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    //Flag que se pone en true una vez que la cuenta este creada
    var accountCreated = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()

    init {
        accountCreated.value = false
        isLoading.value = false
        errorMessage.value = ""
    }

    fun createAccount(user : User, password : String){

        //Recupero la instancia de auth
        val auth = Firebase.auth
        val db = Firebase.firestore

        //show UI login spinner
        isLoading.value = true


        viewModelScope.launch {

            //Chequeo que el email este disponible
            try {
                val empty = db.collection("users").whereEqualTo("email", user.email).get().await().isEmpty
                if(!empty){
                    errorMessage.value = "Email already used"
                    Log.w("NewAccountVM", "signInWithEmail:failure Email Used")
                    isLoading.value = false
                    return@launch
                }
            }catch (e :Exception){
                Log.w("NewAccountVM", "signInWithEmail:failure $e")
                errorMessage.value = e.toString()
                isLoading.value = false
                return@launch
            }

            //Agrego el usuario a auth
            try {
                auth.signOut()
                auth.createUserWithEmailAndPassword(user.email, password).await()
            } catch (e : Exception){
                Log.w("NewAccountVM", "signInWithEmail:failure $e")
                errorMessage.value = e.toString()
                isLoading.value = false
                return@launch
            }

            val fbUser: FirebaseUser? = auth.currentUser

            if( fbUser == null){
                errorMessage.value = "Error creating account"
                isLoading.value = false
                return@launch
            }

            user.uuid = fbUser.uid

            //Actualizo el token de FCM
            user.fcmToken = FirebaseMessaging.getInstance().token.await().toString()

            //Agrego el usuario a la firestore
            try {
                db.collection("users").document(user.uuid).set(user).await()
            }catch (e :Exception){
                Log.w("NewAccountVM", "signInWithEmail:failure $e")
                errorMessage.value = e.toString()
                isLoading.value = false
                return@launch
            }

            isLoading.value = false
            accountCreated.value = true
        }
    }

}



















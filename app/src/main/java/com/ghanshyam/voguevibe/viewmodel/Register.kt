package com.ghanshyam.voguevibe.viewmodel

import androidx.lifecycle.ViewModel
import com.ghanshyam.voguevibe.data.User
import com.ghanshyam.voguevibe.util.Constants.USER_COLLECTION
import com.ghanshyam.voguevibe.util.RegisterFieldState
import com.ghanshyam.voguevibe.util.RegisterValidation
import com.ghanshyam.voguevibe.util.Resource
import com.ghanshyam.voguevibe.util.validateEmail
import com.ghanshyam.voguevibe.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class Register @Inject constructor(

    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore

) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()


    fun createAccount(user: User, password: String) {
        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserData(it.uid, user)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val registerFieldState =
                RegisterFieldState(validateEmail(user.email), validatePassword(password))
            runBlocking { _validation.send(registerFieldState) }
        }
    }

    private fun saveUserData(userId: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        return shouldRegister
    }
}
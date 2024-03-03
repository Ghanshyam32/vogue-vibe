package com.ghanshyam.voguevibe.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ghanshyam.voguevibe.R
import com.ghanshyam.voguevibe.data.User
import com.ghanshyam.voguevibe.databinding.FragmentRegisterBinding
import com.ghanshyam.voguevibe.util.RegisterValidation
import com.ghanshyam.voguevibe.util.Resource
import com.ghanshyam.voguevibe.viewmodel.Register
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var bindings: FragmentRegisterBinding
    private val viewModel by viewModels<Register>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentRegisterBinding.inflate(inflater)
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindings.apply {
            signup.setOnClickListener {
                val user = User(
                    inputFirstName.text.toString().trim(),
                    inputLastName.text.toString().trim(),
                    inputEmail.text.toString().trim(),
                )
                val password = inputPassword.text.toString()
                viewModel.createAccount(user, password)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.register.collect() {
                when (it) {
                    is Resource.Loading -> {
                        bindings.signup.startAnimation()
                    }

                    is Resource.Success -> {
                        Log.d("text", it.message.toString())
                        bindings.signup.revertAnimation()
                    }

                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        bindings.signup.revertAnimation()
                    }

                    else ->
                        Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        bindings.inputEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        bindings.inputPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
}
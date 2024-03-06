package com.ghanshyam.voguevibe.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ghanshyam.voguevibe.R
import com.ghanshyam.voguevibe.activities.ShoppingActivity
import com.ghanshyam.voguevibe.databinding.FragmentLoginBinding
import com.ghanshyam.voguevibe.dialogs.setupBottomSheetDialog
import com.ghanshyam.voguevibe.util.Resource
import com.ghanshyam.voguevibe.viewmodel.Login
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<Login>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            binding.createAcc.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            login.setOnClickListener {
                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString()
                viewModel.login(email, password)
            }
        }

        binding.forgotPassword.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.resetPassword(email)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                       Snackbar.make(
                            requireView(),
                            "Reset link was sent to your email",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }


                    is Resource.Error -> {
                        Snackbar.make(
                            requireView(),
                            "Error: ${it.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.login.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.login.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.login.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

    }

}
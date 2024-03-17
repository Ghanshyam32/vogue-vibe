package com.ghanshyam.voguevibe.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ghanshyam.voguevibe.R
import com.ghanshyam.voguevibe.activities.ShoppingActivity
import com.ghanshyam.voguevibe.databinding.FragmentIntroBinding
import com.google.firebase.auth.FirebaseAuth

class IntroFragment : Fragment(R.layout.fragment_intro) {

    private lateinit var binding: FragmentIntroBinding
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.discover.setOnClickListener {

            if (mAuth.currentUser != null) {
                // User is already authenticated, navigate to ShoppingActivity
                startActivity(Intent(requireActivity(), ShoppingActivity::class.java))
            } else {
                // User is not authenticated, navigate to LoginFragment
                findNavController().navigate(R.id.action_introFragment_to_accountOptionsFragment)

            }
        }

    }
}
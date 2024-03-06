package com.ghanshyam.voguevibe.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ghanshyam.voguevibe.R
import com.ghanshyam.voguevibe.databinding.ActivityShoppingBinding

class ShoppingActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.shoppingFragment)
        binding.bottomNav.setupWithNavController(navController)

    }
}
package com.ghanshyam.voguevibe.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghanshyam.voguevibe.R
import com.ghanshyam.voguevibe.adapters.BestDealsAdapter
import com.ghanshyam.voguevibe.adapters.BestProductAdapter
import com.ghanshyam.voguevibe.adapters.SpecialProductsAdapter
import com.ghanshyam.voguevibe.databinding.ActivityMainBinding
import com.ghanshyam.voguevibe.databinding.FragmentMainCategoryBinding
import com.ghanshyam.voguevibe.util.Resource
import com.ghanshyam.voguevibe.viewmodel.MainCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = ""

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductAdapter: SpecialProductsAdapter
    private val viewModel by viewModels<MainCategory>()
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpecialProduct()
        setupBestDeal()
        setUpBestProduct()
        lifecycleScope.launchWhenStarted {
            viewModel.specialProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        specialProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else ->
                        Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        bestProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else ->
                        Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestDealsProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else ->
                        Unit
                }
            }
        }
    }

    private fun setUpBestProduct() {
        bestProductAdapter = BestProductAdapter()
        binding.bestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun setupBestDeal() {
        bestDealsAdapter = BestDealsAdapter()
        binding.bestDeals.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setUpSpecialProduct() {
        specialProductAdapter = SpecialProductsAdapter()
        binding.specialProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter
        }
    }

}
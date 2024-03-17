package com.ghanshyam.voguevibe.fragments.shoppingActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ghanshyam.voguevibe.R
import com.ghanshyam.voguevibe.adapters.HomeViewpagerAdapter
import com.ghanshyam.voguevibe.databinding.HomeFragmentBinding
import com.ghanshyam.voguevibe.fragments.categories.BeautyCategoryFragment
import com.ghanshyam.voguevibe.fragments.categories.HomeLivingCategoryFragment
import com.ghanshyam.voguevibe.fragments.categories.KidsCategoryFragment
import com.ghanshyam.voguevibe.fragments.categories.MainCategoryFragment
import com.ghanshyam.voguevibe.fragments.categories.MenCategoryFragment
import com.ghanshyam.voguevibe.fragments.categories.WomenCategoryFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Home : Fragment(R.layout.home_fragment) {

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            MenCategoryFragment(),
            WomenCategoryFragment(),
            KidsCategoryFragment(),
            HomeLivingCategoryFragment(),
            BeautyCategoryFragment()
        )

        binding.viewpager.isUserInputEnabled = false

        val viewPager2Adapter =
            HomeViewpagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewpager.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Men"
                2 -> tab.text = "Women"
                3 -> tab.text = "Kids"
                4 -> tab.text = "Home & Living"
                5 -> tab.text = "Beauty"
            }
        }.attach()

    }

}
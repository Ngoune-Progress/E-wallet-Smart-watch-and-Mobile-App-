package com.se3.payme.bubbleNavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.se3.payme.Adapter.ViewPagerAdapter
import com.se3.payme.R
import com.se3.payme.bubbleNavigation.FragmentTabs.GenerateCodeFragment
import com.se3.payme.bubbleNavigation.FragmentTabs.ScanCodeFragment
import com.se3.payme.croudFunding.StatisticsFragment
import com.se3.payme.croudFunding.croundFundingViewFragment

class BottomBarActivity :  Fragment() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        supportActionBar?.hide()
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bottom_bar)
//        val viewPager = findViewById<ViewPager>(R.id.viewPager)
//        val tabs = findViewById<TabLayout>(R.id.tabs)
//        val adapter = ViewPagerAdapter(supportFragmentManager)
//        adapter.addFragment(GenerateCodeFragment(), "My Code")
//        adapter.addFragment(ScanCodeFragment(), "Scan Code")
//        viewPager.adapter = adapter
//        tabs.setupWithViewPager(viewPager)
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_bottom_bar, container, false)
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        val tabs =view.findViewById<TabLayout>(R.id.tabs)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(GenerateCodeFragment(), "My Code")
        adapter.addFragment(ScanCodeFragment(), "Scan ")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        return  view
    }

}
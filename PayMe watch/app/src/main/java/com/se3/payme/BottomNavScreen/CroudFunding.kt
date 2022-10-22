package com.se3.payme.BottomNavScreen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.android.material.tabs.TabLayout
import com.se3.payme.Adapter.ViewPagerAdapter
import com.se3.payme.R
import com.se3.payme.bubbleNavigation.FragmentTabs.GenerateCodeFragment
import com.se3.payme.bubbleNavigation.FragmentTabs.ScanCodeFragment
import com.se3.payme.croudFunding.StatisticsFragment
import com.se3.payme.croudFunding.croundFundingViewFragment

class CroudFunding : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_croud_funding, container, false)
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager1)
        val tabs =view.findViewById<TabLayout>(R.id.tabs1)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(StatisticsFragment(), "Stat")
        adapter.addFragment(croundFundingViewFragment(), "Crowd Funding")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        return  view
    }

}
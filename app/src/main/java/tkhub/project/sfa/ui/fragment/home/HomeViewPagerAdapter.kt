package tkhub.project.sfa.ui.fragment.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tkhub.project.sfa.ui.fragment.home.chart_one.ChartOneFragment
import tkhub.project.sfa.ui.fragment.home.chart_two.ChartTwoFragment

class HomeViewPagerAdapter  (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment = when (position) {

        CHART_ONE_PAGE_INDEX -> ChartOneFragment()
        CHART_TWO_PAGE_INDEX -> ChartTwoFragment()
        else -> throw IllegalStateException("Invalid adapter position")

    }
    override fun getItemCount(): Int = 2

    companion object {

        internal const val TRANSACTION_SCREEN_OFFSCREEN_LIMIT = 1
        internal const val TRANSACTION_SCREENS_NUMBER = 2

        internal const val CHART_ONE_PAGE_INDEX = 0
        internal const val CHART_TWO_PAGE_INDEX = 1

    }

}
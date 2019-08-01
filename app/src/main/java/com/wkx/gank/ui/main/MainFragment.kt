package com.wkx.gank.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.wkx.common.adapter.ViewPagerAdapter
import com.wkx.gank.R
import com.wkx.gank.ui.MainActivity
import com.wkx.gank.ui.category.TabPageFragment
import com.wkx.gank.ui.home.HomeFragment
import com.wkx.gank.ui.fragment.NotificationsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager.adapter = ViewPagerAdapter(
            childFragmentManager,
            listOf(HomeFragment(), TabPageFragment(), NotificationsFragment())
        )
        view_pager.offscreenPageLimit = 2
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageSelected(position: Int) {
                (activity as? MainActivity)?.toolbar?.title = when (position) {
                    0 -> "最新"
                    1 -> "分类"
                    else -> ""
                }
                nav_view.menu.getItem(position).isChecked = true
            }
        })
        nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> view_pager.currentItem = 0
                R.id.navigation_dashboard -> view_pager.currentItem = 1
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

}
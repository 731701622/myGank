package com.wkx.gank.ui.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wkx.common.adapter.ViewPagerAdapter
import com.wkx.common.base.BaseFragment
import com.wkx.gank.R
import kotlinx.android.synthetic.main.fragment_tab_page.*

class TabPageFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_tab_page

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleList: ArrayList<String> = arrayListOf("all", "Android", "iOS", "App", "前端", "瞎推荐", "拓展资源", "福利", "休息视频")
        val fragmentList: ArrayList<Fragment> = ArrayList()
        for (i in titleList) {
            fragmentList.add(CategoryFragment.newInstance(i))
        }
        val adapter = ViewPagerAdapter(childFragmentManager, fragmentList, titleList.also { it[0] = "全部" })
        view_pager.adapter = adapter
        view_pager.currentItem = 0
        view_pager.offscreenPageLimit = fragmentList.size - 1
        tab_layout.setupWithViewPager(view_pager)
    }

}
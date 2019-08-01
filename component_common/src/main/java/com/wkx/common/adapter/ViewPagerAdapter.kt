package com.wkx.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

open class ViewPagerAdapter constructor(
    fragmentManager: FragmentManager,
    private val fragments: List<Fragment>,
    private val titles: List<String>? = arrayListOf()
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = titles?.get(position)

}
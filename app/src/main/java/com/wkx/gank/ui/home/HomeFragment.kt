package com.wkx.gank.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wkx.common.adapter.OnItemClickListener
import com.wkx.common.base.BaseFragment
import com.wkx.common.utils.Code
import com.wkx.common.utils.toast
import com.wkx.gank.R
import com.wkx.gank.entity.Gank
import com.wkx.gank.ui.GankDetailsActivity
import com.wkx.gank.ui.home.adapter.HomeAdapter
import com.wkx.gank.ui.home.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.layout_refresh_recycler.*

class HomeFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.layout_refresh_recycler

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this)[HomeViewModel::class.java]
    }

    private val adapter: HomeAdapter  by lazy { HomeAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            statusLiveData.observe(this@HomeFragment, Observer {
                when (it.code) {
                    Code.SUCCESS -> showContent()
                    Code.ING -> {
                        if (adapter.getItems().isEmpty()) {
                            showLoading()
                        }
                    }
                    Code.EMPTY -> showEmpty()
                    Code.ERROR -> {
                        if (adapter.getItems().isEmpty()) {
                            showError()
                        } else {
                            toast(it.msg)
                        }
                    }
                    Code.END -> swipe_refresh_layout.isRefreshing = false
                    else -> Unit
                }
            })
            todayLiveData.observe(this@HomeFragment, Observer {
                adapter.setNewItem(it)
            })
        }

        swipe_refresh_layout.setOnRefreshListener { viewModel.refresh() }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).also {
                it.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_recycler_divider)!!)
            })
            adapter = this@HomeFragment.adapter
        }
        adapter.setOnItemClickListener(object : OnItemClickListener<Gank> {
            override fun onItemClick(view: View, item: Gank, position: Int) {
                GankDetailsActivity.startActivity(context, item)
            }
        })
    }

    override fun onNetReload() {
        super.onNetReload()
        viewModel.refresh()
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//      }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_history -> {
//                findNavController().navigate(R.id.action_mainFragment_to_historyFragment)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}
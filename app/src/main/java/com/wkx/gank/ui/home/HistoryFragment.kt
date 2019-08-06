package com.wkx.gank.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wkx.common.adapter.OnItemClickListener
import com.wkx.common.base.BaseFragment
import com.wkx.gank.ui.home.adapter.HomeAdapter
import com.wkx.gank.ui.home.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.layout_refresh_recycler.*
import com.wkx.common.utils.Code
import com.wkx.common.utils.logd
import com.wkx.common.utils.toast
import com.wkx.gank.R
import com.wkx.gank.entity.Gank
import com.wkx.gank.ui.GankDetailsActivity
import com.wkx.gank.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

class HistoryFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.layout_refresh_recycler

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProviders.of(activity!!)[HistoryViewModel::class.java]
    }

    private lateinit var adapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomeAdapter()
        viewModel.apply {
            statusLiveData.observe(this@HistoryFragment, Observer {
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
            currentHistoryLiveData.observe(this@HistoryFragment, Observer {
                (activity as MainActivity).toolbar.title = it.title
            })
            gankLiveData.observe(this@HistoryFragment, Observer {
                this@HistoryFragment.adapter.setNewItem(it)
            })
        }
        swipe_refresh_layout.setOnRefreshListener { viewModel.refresh() }
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            recycler_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            recycler_view.adapter = this@HistoryFragment.adapter
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.history, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_date -> {
                findNavController().navigate(R.id.action_historyFragment_to_historicalDateFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
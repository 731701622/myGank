package com.wkx.gank.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wkx.common.adapter.OnItemClickListener
import com.wkx.common.adapter.OnLoadMoreListener
import com.wkx.common.base.BaseFragment
import com.wkx.common.utils.Code
import com.wkx.common.utils.logd
import com.wkx.gank.R
import com.wkx.gank.entity.History
import com.wkx.gank.ui.MainActivity
import com.wkx.gank.ui.home.adapter.HistoryAdapter
import com.wkx.gank.ui.home.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_refresh_recycler.*

class HistoricalDateFragment : BaseFragment() {

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProviders.of(activity!!)[HistoryViewModel::class.java]
    }

    private val adapter = HistoryAdapter()

    override fun getLayoutId(): Int = R.layout.layout_refresh_recycler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            statusLiveData.observe(this@HistoricalDateFragment, Observer {
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
                            adapter.loadMoreFail()
                        }
                    }
                    Code.END -> swipe_refresh_layout.isRefreshing = false
                    else -> Unit
                }
            })
            currentHistoryLiveData.observe(this@HistoricalDateFragment, Observer {
                (activity as MainActivity).toolbar.title = it.title
            })
            historyLiveData.observe(this@HistoricalDateFragment, Observer {
                adapter.setItem(it)
            })
        }
        adapter.setOnItemClickListener(object :OnItemClickListener<History>{
            override fun onItemClick(view: View, item: History, position: Int) {
                viewModel.getGank(item)
                findNavController().popBackStack()
            }
        })
        adapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                viewModel.loadMore()
            }

            override fun onRetry() {
                viewModel.retry()
            }
        })
        swipe_refresh_layout.isEnabled = false
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            recycler_view.adapter = this@HistoricalDateFragment.adapter
        }
    }

}
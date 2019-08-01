package com.wkx.gank.ui.category

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wkx.common.adapter.OnItemClickListener
import com.wkx.common.adapter.OnLoadMoreListener
import com.wkx.common.base.BaseLazyFragment
import com.wkx.common.utils.Code
import com.wkx.gank.R
import com.wkx.gank.entity.Gank
import com.wkx.gank.ui.GankDetailsActivity
import com.wkx.gank.ui.home.adapter.HomeAdapter
import kotlinx.android.synthetic.main.layout_refresh_recycler.*

class CategoryFragment : BaseLazyFragment() {

    private val viewModel: CategoryViewModel by lazy {
        val category = arguments?.getString(KEY_CATEGORY)!!
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CategoryViewModel(category) as T
            }
        })[CategoryViewModel::class.java]
    }

    private val adapter: HomeAdapter by lazy { HomeAdapter() }

    override fun getLayoutId(): Int = R.layout.layout_refresh_recycler

    override fun onFirstUserVisible(isVisibleToUser: Boolean) {
        if (!isVisibleToUser) return

        viewModel.apply {
            refreshGankLiveData.observe(this@CategoryFragment, Observer {
                adapter.setNewItem(it)
            })
            loadMoreGankLiveData.observe(this@CategoryFragment, Observer {
                adapter.setItem(it)
            })
            statusLiveData.observe(this@CategoryFragment, Observer {
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
        }
        swipe_refresh_layout.setOnRefreshListener { viewModel.refresh() }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).also {
                it.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_recycler_divider)!!)
            })
            adapter = this@CategoryFragment.adapter
        }

        adapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                viewModel.loadMore()
            }

            override fun onRetry() {
                viewModel.retry()
            }
        })
        adapter.setOnItemClickListener(object : OnItemClickListener<Gank> {
            override fun onItemClick(view: View, item: Gank, position: Int) {
                GankDetailsActivity.startActivity(context, item)
            }
        })
    }

    override fun onUserVisible(isVisibleToUser: Boolean) {

    }

    override fun onNetReload() {
        super.onNetReload()
        viewModel.retry()
    }

    companion object {
        private const val KEY_CATEGORY = "key_category"
        fun newInstance(category: String): CategoryFragment = CategoryFragment().also {
            it.arguments = Bundle().also { bundle ->
                bundle.putString(KEY_CATEGORY, category)
            }
        }
    }

}
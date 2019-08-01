package com.wkx.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.wkx.common.loadsir.EmptyCallback
import com.wkx.common.loadsir.ErrorCallback
import com.wkx.common.loadsir.LoadingCallback
import com.wkx.common.loadsir.TimeoutCallback

abstract class BaseFragment : Fragment() {

    private lateinit var loadService: LoadService<Any>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val rootView = inflater.inflate(getLayoutId(), container, false)
        loadService = LoadSir.getDefault().register(rootView) { onNetReload() }
        return loadService.loadLayout
    }

    open fun onNetReload() = showLoading()

    fun showLoading() = loadService.showCallback(LoadingCallback::class.java)

    fun showEmpty() = loadService.showCallback(EmptyCallback::class.java)

    fun showError() = loadService.showCallback(ErrorCallback::class.java)

    fun showTimeout() = loadService.showCallback(TimeoutCallback::class.java)

    fun showContent() = loadService.showSuccess()

    abstract fun getLayoutId(): Int

}
package com.wkx.common.loadsir

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.wkx.common.R

open class EmptyCallback : Callback() {

    override fun onCreateView(): Int = R.layout.status_empty
}

open class ErrorCallback : Callback() {

    override fun onCreateView(): Int = R.layout.status_error
}

open class TimeoutCallback : Callback() {

    override fun onCreateView(): Int = R.layout.status_timeout

    override fun onReloadEvent(context: Context, view: View?): Boolean {
//        Toast.makeText(context.applicationContext, "再次连接到网络！", Toast.LENGTH_SHORT).show()
        return super.onReloadEvent(context, view)
    }
}

open class LoadingCallback : Callback() {

    override fun onCreateView(): Int = R.layout.status_loading

    override fun getSuccessVisible(): Boolean = super.getSuccessVisible()

    override fun onReloadEvent(context: Context?, view: View?): Boolean = true
}


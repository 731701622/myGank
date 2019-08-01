package com.wkx.common

import android.app.Application
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import kotlin.properties.Delegates
import com.wkx.common.loadsir.*

abstract class BaseApplication : Application() {

    companion object {
        var instance: BaseApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())
            .addCallback(EmptyCallback())
            .addCallback(LoadingCallback())
            .addCallback(TimeoutCallback())
//            .addCallback(CustomCallback())
            .setDefaultCallback(SuccessCallback::class.java)
            .commit()
    }

}
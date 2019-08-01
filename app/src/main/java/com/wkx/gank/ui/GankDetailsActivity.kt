package com.wkx.gank.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.wkx.common.utils.toast
import com.wkx.gank.R
import com.wkx.gank.entity.Gank
import kotlinx.android.synthetic.main.activity_gank_details.*

class GankDetailsActivity : AppCompatActivity() {

    companion object {
        private const val KEY_GANK = "gank"
        fun startActivity(context: Context?, gank: Gank) = run {
            val intent = Intent(context, GankDetailsActivity::class.java).also {
                it.putExtra(KEY_GANK, gank)
            }
            context?.startActivity(intent)
        }
    }

    private val gank: Gank by lazy { intent.getParcelableExtra(KEY_GANK) as Gank }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.enableSlowWholeDocumentDraw()
        setContentView(R.layout.activity_gank_details)

        toolbar.title = gank.desc
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { finish() }

        initWebSettings()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && web_view.canGoBack()) {
            web_view.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        if (web_view != null) {
            web_view.settings.javaScriptEnabled = true
            web_view.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (web_view != null) {
            web_view.settings.javaScriptEnabled = false
            web_view.onPause()
        }
    }


    private fun initWebSettings() {
        with(web_view.settings) {
            useWideViewPort = true               // 将图片调整到适合webView的大小
            loadWithOverviewMode = true          // 缩放至屏幕的大小
            setSupportZoom(true)                 // 支持缩放，默认为true 是下面那个的前提
            builtInZoomControls = true           // 设置内置的缩放控件。若为false，则该WebView不可缩放
            displayZoomControls = false          // 隐藏原生的缩放控件
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK // 关闭webView中缓存
            allowFileAccess = true                          // 设置可以访问文件
            javaScriptCanOpenWindowsAutomatically = true    // 支持通过JS打开新窗口
            loadsImagesAutomatically = true                 // 支持自动加载图片
            defaultTextEncodingName = "UTF-8"               // 设置编码格式
        }

        with(web_view) {
            loadUrl(gank.url)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    view.loadUrl(request.url.toString())
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    progress_bar.progress = newProgress
                    progress_bar.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
                }

                override fun onReceivedTitle(view: WebView, title: String) {
                    super.onReceivedTitle(view, title)
                    toolbar.title = title
                }
            }
        }
        with(progress_bar) {
            progress = 0
            max = 100
            visibility = View.VISIBLE
        }
    }
}
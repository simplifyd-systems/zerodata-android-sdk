package com.simplifydvpn.android.ui.main.webview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.core.view.isVisible
import com.simplifydvpn.android.R
import kotlinx.android.synthetic.main.fragment_webview.*

class WebViewFragment : Fragment(R.layout.fragment_webview) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        setUpWebView()
        showLoading()
        loadWebPage()
    }

    private fun setUpWebView() {
        web_view.webViewClient = ChangeLogWebViewClient()
        web_view.webChromeClient = WebChromeClient()
        web_view.settings.javaScriptCanOpenWindowsAutomatically = true
        web_view.settings.setSupportMultipleWindows(true)
    }

    inner class ChangeLogWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            if (this@WebViewFragment.view == null) {
                return
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (this@WebViewFragment.view == null) {
                return
            }

            loading_error_state.isVisible = false

            web_view.isVisible = true
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?,
        ) {
            super.onReceivedError(view, request, error)
            if (this@WebViewFragment.view == null) {
                return
            }

            loading_error_state.isVisible = false
        }
    }

    private fun loadWebPage() {
        arguments?.getString(WEB_URL)?.let{
            web_view.loadUrl(it)
        }
    }

    private fun showLoading() {
        loading_error_state.isVisible = true
    }

    companion object {
        private val TAG: String = WebViewFragment::class.java.name
        private const val WEB_URL = "webUrl"
    }
}
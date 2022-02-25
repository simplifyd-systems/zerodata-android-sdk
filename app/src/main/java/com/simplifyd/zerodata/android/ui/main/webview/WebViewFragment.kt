package com.simplifyd.zerodata.android.ui.main.webview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.simplifyd.zerodata.android.R
import kotlinx.android.synthetic.main.fragment_webview.*

class WebViewFragment : DialogFragment(R.layout.fragment_webview) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Simplifyd_FullScreenDialog_Normal)
    }

    private fun disableBackClick() {
        view?.let {
            it.isFocusableInTouchMode = true
            it.requestFocus()
            it.setOnKeyListener { _, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_BACK) {
                        dismiss()
                    }
                }
                return@setOnKeyListener false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.run {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            disableBackClick()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        setUpWebView()
        showLoading()
        loadWebPage()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    private fun setUpWebView() {
        web_view.webViewClient = CustomWebViewClient()
        web_view.webChromeClient = WebChromeClient()
        web_view.settings.javaScriptCanOpenWindowsAutomatically = true
        web_view.settings.setSupportMultipleWindows(true)
    }

    inner class CustomWebViewClient : WebViewClient() {
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
        arguments?.getString(WEB_URL)?.let {
            web_view.loadUrl(it)
        }
    }

    private fun showLoading() {
        loading_error_state.isVisible = true
    }

    companion object {
        private val TAG: String = WebViewFragment::class.java.name
        private const val WEB_URL = "webUrl"

        fun display(
            fragmentManager: FragmentManager,
            webUrl: String
        ): WebViewFragment {
            val dialogFragment = WebViewFragment()
            dialogFragment.arguments = Bundle().apply {
                putString(WEB_URL, webUrl)
            }
            dialogFragment.show(fragmentManager, TAG)
            return dialogFragment
        }
    }
}
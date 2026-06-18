package com.desktop.browser

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

class DesktopWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : WebView(context, attrs) {

    init {
        setBackgroundColor(Color.BLACK)
        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false
        overScrollMode = View.OVER_SCROLL_NEVER

        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            supportZoom = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            userAgentString = BrowserSettings.DESKTOP_USER_AGENT
        }

        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(this@DesktopWebView, true)
        }

        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url.isNullOrBlank()) return false
                view?.loadUrl(url)
                return true
            }
        }

        webChromeClient = WebChromeClient()
    }

    fun loadHome(url: String) {
        loadUrl(url)
    }

    fun injectDesktopHints() {
        evaluateJavascript(
            """
            (function() {
              try {
                const style = document.createElement('style');
                style.innerHTML = `
                  html, body { cursor: default !important; }
                  * { -webkit-tap-highlight-color: rgba(0,0,0,0) !important; }
                `;
                document.head && document.head.appendChild(style);
              } catch (e) {}
            })();
            """.trimIndent(),
            null
        )
    }
}

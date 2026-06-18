package com.desktop.browser

import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: DesktopWebView
    private lateinit var cursorOverlay: CursorOverlayView
    private lateinit var addressBar: EditText

    private var desktopMode = true
    private var mouseMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFullscreen()
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        cursorOverlay = findViewById(R.id.cursorOverlay)
        addressBar = findViewById(R.id.addressBar)

        val openBtn: Button = findViewById(R.id.openBtn)
        val desktopBtn: Button = findViewById(R.id.desktopBtn)
        val mouseBtn: Button = findViewById(R.id.mouseBtn)
        val fullscreenBtn: Button = findViewById(R.id.fullscreenBtn)
        val root: FrameLayout = findViewById(R.id.root)

        openBtn.setOnClickListener {
            val url = addressBar.text?.toString().orEmpty()
            webView.loadHome(normalizeUrl(url))
        }

        desktopBtn.setOnClickListener {
            desktopMode = !desktopMode
            applyMode()
        }

        mouseBtn.setOnClickListener {
            mouseMode = !mouseMode
            cursorOverlay.setCursorEnabled(mouseMode)
        }

        fullscreenBtn.setOnClickListener {
            setupFullscreen()
        }

        root.setOnTouchListener { _, event ->
            if (mouseMode) {
                cursorOverlay.handleTouch(event)
                true
            } else {
                false
            }
        }

        webView.loadHome(BrowserSettings.DEFAULT_HOME)
        applyMode()
    }

    override fun onResume() {
        super.onResume()
        setupFullscreen()
    }

    private fun applyMode() {
        webView.settings.userAgentString = BrowserSettings.DESKTOP_USER_AGENT
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.textZoom = if (desktopMode) 100 else 92
        webView.injectDesktopHints()
    }

    private fun setupFullscreen() {
        window.setDecorFitsSystemWindows(false)
        val controller = window.insetsController
        controller?.hide(
            WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
        )
        controller?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun normalizeUrl(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return BrowserSettings.DEFAULT_HOME
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) return trimmed
        if (trimmed.contains('.')) return "https://$trimmed"
        return "https://www.google.com/search?q=${
            java.net.URLEncoder.encode(trimmed, "UTF-8")
        }"
    }
}

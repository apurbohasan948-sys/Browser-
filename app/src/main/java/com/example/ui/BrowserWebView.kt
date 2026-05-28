package com.example.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.viewmodel.BrowserTab
import com.example.viewmodel.BrowserViewModel
import java.io.ByteArrayInputStream

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserWebView(
    tab: BrowserTab,
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier,
    onWebViewCreated: (WebView) -> Unit = {}
) {
    val adBlockEnabled by viewModel.adBlockEnabled.collectAsState()
    val dataSavingEnabled by viewModel.dataSavingEnabled.collectAsState()
    val websiteDarkMode by viewModel.websiteDarkMode.collectAsState()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                onWebViewCreated(this)
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )

                // High-performance custom hardware configurations & optimizations
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    allowFileAccess = true
                    allowContentAccess = true
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        url?.let {
                            viewModel.updateTabUrl(tab.id, it)
                        }
                        viewModel.updateTabProgress(tab.id, 10)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        url?.let {
                            viewModel.updateTabUrl(tab.id, it)
                        }
                        view?.title?.let {
                            viewModel.updateTabTitle(tab.id, it)
                        }
                        viewModel.updateTabProgress(tab.id, 100)
                        viewModel.updateTabNavigationState(
                            tab.id,
                            canGoBack = view?.canGoBack() ?: false,
                            canGoForward = view?.canGoForward() ?: false
                        )

                        // Website Auto Dark Mode injection
                        if (websiteDarkMode) {
                            val darkCssJs = """
                                (function() {
                                    if (document.getElementById('auramini-dark-mode')) return;
                                    var style = document.createElement('style');
                                    style.id = 'auramini-dark-mode';
                                    style.type = 'text/css';
                                    style.innerHTML = 'html, body { background-color: #12141C !important; color: #E2E8F0 !important; } ' +
                                                      'a { color: #818CF8 !important; } ' +
                                                      'p, span, h1, h2, h3, h4, h5, h6 { color: #E2E8F0 !important; }';
                                    document.head.appendChild(style);
                                })();
                            """.trimIndent()
                            view?.evaluateJavascript(darkCssJs, null)
                        }
                    }

                    // Ad Blocker inside WebView intercepts tracking / ad networks
                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): WebResourceResponse? {
                        val requestUrl = request?.url?.toString() ?: ""

                        if (adBlockEnabled) {
                            val isAd = requestUrl.contains("ads") || 
                                       requestUrl.contains("doubleclick") || 
                                       requestUrl.contains("googleads") || 
                                       requestUrl.contains("adservice") ||
                                       requestUrl.contains("telemetry") || 
                                       requestUrl.contains("analytics") ||
                                       requestUrl.contains("adnxs") ||
                                       requestUrl.contains("popads")

                            if (isAd) {
                                return WebResourceResponse(
                                    "text/javascript",
                                    "UTF-8",
                                    ByteArrayInputStream("".toByteArray())
                                )
                            }
                        }

                        // Extreme Data Saving mode (Lite mode) strips heavy image downloads
                        if (dataSavingEnabled) {
                            val isImage = requestUrl.endsWith(".png") ||
                                          requestUrl.endsWith(".jpg") ||
                                          requestUrl.endsWith(".jpeg") ||
                                          requestUrl.endsWith(".gif") ||
                                          requestUrl.endsWith(".webp") ||
                                          requestUrl.contains("images?")

                            if (isImage) {
                                return WebResourceResponse(
                                    "image/png",
                                    "UTF-8",
                                    ByteArrayInputStream("".toByteArray())
                                )
                            }
                        }

                        return super.shouldInterceptRequest(view, request)
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        viewModel.updateTabProgress(tab.id, newProgress)
                    }
                }
            }
        },
        update = { webView ->
            // Update User Agent if Desktop mode toggled
            val mobileUA = "Mozilla/5.0 (Linux; Android 12; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Mobile Safari/537.36"
            val desktopUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Safari/537.36"
            
            webView.settings.userAgentString = if (tab.desktopMode) desktopUA else mobileUA
            
            // Navigate if URL changed outside the webview
            if (webView.url != tab.url) {
                webView.loadUrl(tab.url)
            }
        },
        modifier = modifier.fillMaxSize()
    )
}

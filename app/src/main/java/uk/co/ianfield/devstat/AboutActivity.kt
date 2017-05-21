package uk.co.ianfield.devstat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView

import butterknife.BindView
import butterknife.ButterKnife

class AboutActivity : AppCompatActivity() {
    @BindView(R.id.webView)
    internal var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        ButterKnife.bind(this)
        webView!!.loadUrl("file:///android_asset/about.html")
    }
}

package uk.co.ianfield.devstat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.co.ianfield.devstat.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webView.loadUrl("file:///android_asset/about.html")
    }
}

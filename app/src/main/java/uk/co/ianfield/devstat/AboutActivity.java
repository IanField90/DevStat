package uk.co.ianfield.devstat;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.webView) WebView webView;

    @Override public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        webView.loadUrl("file:///android_asset/about.html");
    }
}

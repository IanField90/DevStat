package uk.co.ianfield.devstat;

import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ianfield on 21/02/2014.
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends ActionBarActivity {

    @ViewById(R.id.webView)
    WebView webView;

    @AfterViews
    void initAbout() {
        webView.loadUrl("file:///android_asset/about.html");
    }
}

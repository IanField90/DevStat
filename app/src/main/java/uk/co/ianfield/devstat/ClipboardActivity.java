package uk.co.ianfield.devstat;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.ianfield.devstat.model.StatItem;

/**
 * Created by ianfield on 29/01/2017.
 */

public class ClipboardActivity extends AppCompatActivity {

    @SuppressLint("NewApi") // Always going to be 25+
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StatHelper statHelper = new StatHelper(this);
        String clipboardContents = "";
        ArrayList<StatItem> list;

        list = statHelper.getHardwareList();
        clipboardContents += String.format("%s\n", getString(R.string.title_hardware));
        for (StatItem item : list) {
            clipboardContents += item.toString();
            clipboardContents += "\n";
        }
        list = statHelper.getScreenList();
        clipboardContents += String.format("\n%s\n", getString(R.string.title_screen_metrics));
        for (StatItem item : list) {
            clipboardContents += item.toString();
            clipboardContents += "\n";
        }
        list = statHelper.getSoftwareList();
        clipboardContents += String.format("\n%s\n", getString(R.string.title_software));
        for (StatItem item : list) {
            clipboardContents += item.toString();
            clipboardContents += "\n";
        }
        list = statHelper.getFeatureList();
        clipboardContents += String.format("\n%s\n", getString(R.string.title_crypto));
        for (StatItem item : list) {
            clipboardContents += item.getTitle();
            clipboardContents += ":\n";
            clipboardContents += item.getInfo();
            clipboardContents += "\n";
        }

        list = statHelper.getCryptoList();
        clipboardContents += String.format("\n%s\n", getString(R.string.title_crypto));
        for (StatItem item : list) {
            clipboardContents += item.getTitle();
            clipboardContents += ":\n";
            clipboardContents += item.getInfo();
            clipboardContents += "\n";
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text label", clipboardContents);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
        finish();
    }
}

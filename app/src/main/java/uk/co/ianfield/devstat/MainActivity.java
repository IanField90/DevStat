package uk.co.ianfield.devstat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.ianfield.devstat.model.StatItem;
import uk.co.ianfield.devstat.widget.InformationPagerAdapter;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @Inject
    StatHelper helper;
    private ArrayList<StatItem> hardwareStats;
    private ArrayList<StatItem> screenStats;
    private ArrayList<StatItem> softwareStats;
    private ArrayList<StatItem> featureStats;
    private ArrayList<StatItem> cryptoStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((DevStatApplication) getApplication()).component().inject(this);
        ButterKnife.bind(this);

        hardwareStats = helper.getHardwareList();
        screenStats = helper.getScreenList();
        softwareStats = helper.getSoftwareList();
        featureStats = helper.getFeatureList();
        cryptoStats = helper.getCryptoList();

        // This could probably be done better
        ArrayList<ArrayList<StatItem>> statGroups = new ArrayList<>();
        statGroups.addAll(
                Arrays.asList(screenStats, softwareStats, hardwareStats, featureStats, cryptoStats)
        );

        viewPager.setAdapter(new InformationPagerAdapter(getSupportFragmentManager(), this,
                new int[]{R.string.title_screen_metrics, R.string.title_software, R.string.title_hardware,
                        R.string.title_features, R.string.title_crypto
                },
                statGroups));

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_developer:
                startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.send_email)
    protected void emailClick() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("%s\n", getString(R.string.title_screen_metrics)));
        for (StatItem item : screenStats) {
            stringBuilder.append(item.toString());
            stringBuilder.append("\n");
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_software)));
        for (StatItem item : softwareStats) {
            stringBuilder.append(item.toString());
            stringBuilder.append("\n");
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_hardware)));
        for (StatItem item : hardwareStats) {
            stringBuilder.append(item.toString());
            stringBuilder.append("\n");
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_features)));
        for (StatItem item : featureStats) {
            stringBuilder.append(item.getTitle());
            stringBuilder.append(":\n");
            stringBuilder.append(item.getInfo());
            stringBuilder.append("\n");
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_crypto)));
        for (StatItem item : cryptoStats) {
            stringBuilder.append(item.getTitle());
            stringBuilder.append(":\n");
            stringBuilder.append(item.getInfo());
            stringBuilder.append("\n");
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
    }

}

package uk.co.ianfield.devstat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((DevStatApplication) getApplication()).component().inject(this);
        ButterKnife.bind(this);

        hardwareStats = new ArrayList<>();
        screenStats = new ArrayList<>();
        softwareStats = new ArrayList<>();
        featureStats = new ArrayList<>();

        // Screen
        screenStats.add(helper.getStatItem(StatHelper.Screen.WIDTH));
        screenStats.add(helper.getStatItem(StatHelper.Screen.HEIGHT));
        screenStats.add(helper.getStatItem(StatHelper.Screen.DISPLAY_DENSITY));
        screenStats.add(helper.getStatItem(StatHelper.Screen.DRAWABLE_DENSITY));
        screenStats.add(helper.getStatItem(StatHelper.Screen.SCREEN_SIZE));

        // Software
        softwareStats.add(helper.getStatItem(StatHelper.Software.ANDROID_VERSION));
        softwareStats.add(helper.getStatItem(StatHelper.Software.SDK_INT));
        softwareStats.add(helper.getStatItem(StatHelper.Software.OPEN_GL_ES));
        softwareStats.add(helper.getStatItem(StatHelper.Software.GOOGLE_PLAY_SERVICES_VERSION));

        // Hardware
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.MANUFACTURER));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.MODEL));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.DEVICE));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.BRAND));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.BOARD));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.HOST));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.PRODUCT));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.MEMORY_CLASS));
        if (Build.VERSION.SDK_INT >= 11) { // This is also checked for within
            hardwareStats.add(helper.getStatItem(StatHelper.Hardware.LARGE_MEMORY_CLASS));
        }
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.MAX_MEMORY));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.FREE_SPACE));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.TELEPHONY));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.SD_CARD));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.ARCHITECTURE));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.PROCESSORS));

        // Features (some will dupe for now)
        featureStats = helper.getFeatureList();

        // This could probably be done better
        ArrayList<ArrayList<StatItem>> statGroups = new ArrayList<>();
        statGroups.addAll(Arrays.asList(screenStats, softwareStats, hardwareStats, featureStats));

        viewPager.setAdapter(new InformationPagerAdapter(getSupportFragmentManager(), this,
                new int[]{R.string.title_screen_metrics, R.string.title_software, R.string.title_hardware, R.string.title_features},
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

        emailIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
    }

}

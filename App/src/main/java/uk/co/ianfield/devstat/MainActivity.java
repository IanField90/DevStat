package uk.co.ianfield.devstat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import uk.co.ianfield.devstat.model.StatItem;
import uk.co.ianfield.devstat.widget.InformationPagerAdapter;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.tabs)
    TabLayout tabLayout;

    @ViewById(R.id.viewpager)
    ViewPager viewPager;

    ArrayList<StatItem> hardwareStats;
    ArrayList<StatItem> screenStats;
    ArrayList<StatItem> softwareStats;
    ArrayList<StatItem> featureStats;

    @AfterViews
    void initContent() {
        StatHelper helper = new StatHelper(this);
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

        // Features (some will dupe for now)
        featureStats = helper.getFeatureList();


        ArrayList[] statSets = new ArrayList[] {screenStats, softwareStats, hardwareStats, featureStats};

        viewPager.setAdapter(new InformationPagerAdapter(getSupportFragmentManager(), this, statSets));

        tabLayout.setupWithViewPager(viewPager);
    }

    @OptionsItem(R.id.action_about)
    void actionAboutClick() {
        Intent intent = new Intent(this, AboutActivity_.class);
        startActivity(intent);
    }

    @OptionsItem(R.id.action_developer)
    void actionDeveloperClick() {
        startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
    }

    @Click(R.id.btnSendEmail)
    void emailClick() {
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

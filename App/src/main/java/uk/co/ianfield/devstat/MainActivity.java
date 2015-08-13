package uk.co.ianfield.devstat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import uk.co.ianfield.devstat.model.StatItem;

@EActivity(R.layout.activity_main)
//@OptionsMenu(R.menu.main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.llScreenMetricsContainer)
    LinearLayout screenMetricsContainer;

    @ViewById(R.id.llSoftwareContainer)
    LinearLayout softwareContainer;

    @ViewById(R.id.llHardwareContainer)
    LinearLayout hardwareContainer;

    @ViewById(R.id.llFeaturesContainer)
    LinearLayout featuresContainer;

    ArrayList<StatItem> hardwareStats = new ArrayList<>();
    ArrayList<StatItem> screenStats = new ArrayList<>();
    ArrayList<StatItem> softwareStats = new ArrayList<>();
    ArrayList<StatItem> featureStats = new ArrayList<>();

    @AfterViews
    void initContent() {
        StatHelper helper = new StatHelper(this);
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
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.MEMORY_CLASS));
        if (Build.VERSION.SDK_INT >= 11) { // This is also checked for within
            hardwareStats.add(helper.getStatItem(StatHelper.Hardware.LARGE_MEMORY_CLASS));
        }
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.MAX_MEMORY));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.FREE_SPACE));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.VIBRATOR));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.TELEPHONY));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.CAMERA));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.AUTO_FOCUS));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.COMPASS));
        hardwareStats.add(helper.getStatItem(StatHelper.Hardware.ACCELEROMETER));

        // Features (some will dupe for now)
        featureStats = helper.getFeatureList();

        loadDataIntoContainers(screenStats, screenMetricsContainer);
        loadDataIntoContainers(hardwareStats, hardwareContainer);
        loadDataIntoContainers(softwareStats, softwareContainer);
        loadDataIntoContainers(featureStats, featuresContainer);
    }


    private void loadDataIntoContainers(ArrayList<StatItem> list, LinearLayout container) {
        for (StatItem stat : list) {
            LinearLayout row = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.stat_item, null);
            TextView txtInfo = (TextView) row.findViewById(R.id.txtInfo);
            TextView txtTitle = (TextView) row.findViewById(R.id.txtTitle);

            if (txtInfo != null) {
                txtInfo.setText(stat.getInfo());
            }

            if (txtTitle != null) {
                txtTitle.setText(stat.getTitle());
            }
            container.addView(row);

        }
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

    @OptionsItem(R.id.action_email)
    @Click(R.id.btnSendEmail)
    void emailClick() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(String.format("%s\n", getString(R.string.title_screen_metrics)));
        for (StatItem item : screenStats) {
            stringBuffer.append(item.toString());
            stringBuffer.append("\n");
        }

        stringBuffer.append(String.format("\n%s\n", getString(R.string.title_software)));
        for (StatItem item : softwareStats) {
            stringBuffer.append(item.toString());
            stringBuffer.append("\n");
        }

        stringBuffer.append(String.format("\n%s\n", getString(R.string.title_hardware)));
        for (StatItem item : hardwareStats) {
            stringBuffer.append(item.toString());
            stringBuffer.append("\n");
        }

        stringBuffer.append(String.format("\n%s\n", getString(R.string.title_features)));
        for (StatItem item : featureStats) {
            stringBuffer.append(item.toString());
            stringBuffer.append("\n");
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, stringBuffer.toString());
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
    }

}

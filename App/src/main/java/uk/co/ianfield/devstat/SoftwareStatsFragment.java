package uk.co.ianfield.devstat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import uk.co.ianfield.devstat.model.StatItem;
import uk.co.ianfield.devstat.widget.StatItemArrayAdapter;

/**
 * Created by ianfield on 20/02/2014.
 */
@EFragment (R.layout.stats_list)
public class SoftwareStatsFragment extends Fragment {
    @ViewById
    ListView statsList;

    ArrayList<StatItem> stats = new ArrayList<StatItem>();

    @AfterViews
    void initSoftwareStats() {
        StatItem stat;

        stat = new StatItem();
        stat.setTitle(getString(R.string.android_version));
        stat.setInfo(Build.VERSION.RELEASE);
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.sdk_int));
        stat.setInfo(String.format("%d", Build.VERSION.SDK_INT));
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.opengl_version));
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        if (configurationInfo != null) {
            stat.setInfo(configurationInfo.getGlEsVersion());
        }
        else {
            stat.setInfo(getString(R.string.unknown));
        }
        stats.add(stat);

        statsList.setAdapter(new StatItemArrayAdapter(getActivity(), R.layout.stat_item, stats));
    }
}

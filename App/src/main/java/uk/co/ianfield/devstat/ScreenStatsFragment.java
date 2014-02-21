package uk.co.ianfield.devstat;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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
public class ScreenStatsFragment extends Fragment {
    @ViewById
    ListView statsList;

    ArrayList<StatItem> stats = new ArrayList<StatItem>();
    DisplayMetrics metrics;

    @AfterViews
    void initScreenStats() {
        metrics = getResources().getDisplayMetrics();

        StatItem stat;

        stat = new StatItem();
        stat.setTitle(getString(R.string.screen_width));
        stat.setInfo(String.format("%d px", metrics.widthPixels));
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.screen_height));
        stat.setInfo(String.format("%d px", metrics.heightPixels));
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.display_density));
        stat.setInfo(String.format("%d dpi", metrics.densityDpi));
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.drawable_density));
        if (metrics.density == 0.75) {
            stat.setInfo("ldpi (.75x)");
        }
        else if (metrics.density == 1.0) {
            stat.setInfo("mdpi (1x)");
        }
        else if (metrics.density == 1.5) {
            stat.setInfo("hdpi (1.5x)");
        }
        else if (metrics.density == 2.0) {
            stat.setInfo("xhdpi (2x)");
        }
        else if (metrics.density == 3.0) {
            stat.setInfo("xxhdpi (3x)");
        }
        else if (metrics.density == 4.0) {
            stat.setInfo("xxxhdpi (4x)");
        }
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.screen_size));
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                stat.setInfo(getString(R.string.screen_size_large));
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                stat.setInfo(getString(R.string.screen_size_normal));
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                stat.setInfo(getString(R.string.screen_size_small));
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                stat.setInfo(getString(R.string.screen_size_xlarge));
                break;
            default:
                stat.setInfo(getString(R.string.screen_size_undefined));
        }
        stats.add(stat);

        statsList.setAdapter(new StatItemArrayAdapter(getActivity(), R.layout.stat_item, stats));
    }

}

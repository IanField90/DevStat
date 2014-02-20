package uk.co.ianfield.devstat;

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
    ListView screenStatsList;

    ArrayList<StatItem> stats = new ArrayList<StatItem>();
    DisplayMetrics metrics;

    @AfterViews
    void initScreenStats() {
        metrics = getResources().getDisplayMetrics();

        StatItem stat;

        stat = new StatItem();
        stat.setTitle(getString(R.string.screen_width));
        stat.setInfo(String.format("%dpx", metrics.widthPixels));
        stats.add(stat);

        stat = new StatItem();
        stat.setTitle(getString(R.string.screen_height));
        stat.setInfo(String.format("%dpx", metrics.heightPixels));
        stats.add(stat);

        screenStatsList.setAdapter(new StatItemArrayAdapter(getActivity(), R.layout.stat_item, stats));
    }




}

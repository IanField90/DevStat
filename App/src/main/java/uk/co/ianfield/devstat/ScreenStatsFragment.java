package uk.co.ianfield.devstat;

import android.support.v4.app.Fragment;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import uk.co.ianfield.devstatcore.StatHelper;
import uk.co.ianfield.devstatcore.widget.StatItemArrayAdapter;
import uk.co.ianfield.devstatcore.model.StatItem;

/**
 * Created by ianfield on 20/02/2014.
 */
@EFragment (R.layout.stats_list)
public class ScreenStatsFragment extends Fragment {
    @ViewById
    ListView statsList;

    ArrayList<StatItem> stats = new ArrayList<StatItem>();

    @AfterViews
    void initScreenStats() {
        StatHelper statHelper = new StatHelper(getActivity());
        stats.add(statHelper.getStatItem(StatHelper.Screen.WIDTH));
        stats.add(statHelper.getStatItem(StatHelper.Screen.HEIGHT));
        stats.add(statHelper.getStatItem(StatHelper.Screen.DISPLAY_DENSITY));
        stats.add(statHelper.getStatItem(StatHelper.Screen.DRAWABLE_DENSITY));
        stats.add(statHelper.getStatItem(StatHelper.Screen.SCREEN_SIZE));

        statsList.setAdapter(new StatItemArrayAdapter(getActivity(), R.layout.stat_item, stats));
    }

}

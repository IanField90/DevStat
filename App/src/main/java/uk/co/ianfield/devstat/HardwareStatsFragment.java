package uk.co.ianfield.devstat;

import android.os.Build;
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
public class HardwareStatsFragment extends Fragment {
    @ViewById
    ListView statsList;

    ArrayList<StatItem> stats = new ArrayList<StatItem>();

    @AfterViews
    void initHardwareStats() {
        StatHelper helper = new StatHelper(getActivity());
        stats.add(helper.getStatItem(StatHelper.Hardware.MANUFACTURER));
        stats.add(helper.getStatItem(StatHelper.Hardware.MODEL));
        if(Build.VERSION.SDK_INT >= 11) { // This is also checked for within
            stats.add(helper.getStatItem(StatHelper.Hardware.LARGE_MEMORY_CLASS));
        }
        stats.add(helper.getStatItem(StatHelper.Hardware.MAX_MEMORY));
        stats.add(helper.getStatItem(StatHelper.Hardware.FREE_SPACE));
        stats.add(helper.getStatItem(StatHelper.Hardware.VIBRATOR));
        stats.add(helper.getStatItem(StatHelper.Hardware.TELEPHONY));
        stats.add(helper.getStatItem(StatHelper.Hardware.AUTO_FOCUS));

        statsList.setAdapter(new StatItemArrayAdapter(getActivity(), R.layout.stat_item, stats));
    }
}

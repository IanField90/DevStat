package uk.co.ianfield.devstat;

import android.app.ActivityManager;
import android.content.Context;
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
public class HardwareStatsFragment extends Fragment {
    @ViewById
    ListView statsList;

    ArrayList<StatItem> stats = new ArrayList<StatItem>();

    @AfterViews
    void initHardwareStats() {
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        StatItem stat;

        stat = new StatItem();
        stat.setTitle(getString(R.string.memory_class));
        int memoryClass = am.getMemoryClass();
        stat.setInfo(String.format("%d mb", memoryClass));
        stats.add(stat);

        if(Build.VERSION.SDK_INT >= 11) {
            stat = new StatItem();
            stat.setTitle(getString(R.string.large_memory_class));
            int largeMemoryClass = am.getLargeMemoryClass();
            stat.setInfo(String.format("%d mb", largeMemoryClass));
            stats.add(stat);
        }

        stat = new StatItem();
        stat.setTitle(getString(R.string.max_memory));
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        stat.setInfo(String.format("%d bytes", maxMemory));
        stats.add(stat);

        statsList.setAdapter(new StatItemArrayAdapter(getActivity(), R.layout.stat_item, stats));
    }
}

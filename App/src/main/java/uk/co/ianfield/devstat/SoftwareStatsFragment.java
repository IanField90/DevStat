package uk.co.ianfield.devstat;

import android.support.v4.app.Fragment;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import uk.co.ianfield.devstatcore.StatHelper;
import uk.co.ianfield.devstatcore.model.StatItem;
import uk.co.ianfield.devstatcore.widget.StatItemArrayAdapter;

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
        StatHelper statHelper = new StatHelper(getActivity());
        stats.add(statHelper.getStatItem(StatHelper.Software.ANDROID_VERSION));
        stats.add(statHelper.getStatItem(StatHelper.Software.SDK_INT));
        stats.add(statHelper.getStatItem(StatHelper.Software.OPEN_GL_ES));
        statsList.setAdapter(new StatItemArrayAdapter(getActivity(), R.layout.stat_item, stats));
    }
}

package uk.co.ianfield.devstat.widget;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import uk.co.ianfield.devstat.R;
import uk.co.ianfield.devstat.StatHelper;
import uk.co.ianfield.devstat.model.StatItem;

/**
 * Created by Ian Field on 14/08/15.
 */
@EFragment(R.layout.fragment_information_page)
public class InformationPageFragment extends Fragment {
    @FragmentArg
    int page;

    @ViewById(R.id.llContainer)
    LinearLayout container;

    ArrayList<StatItem> items;

    @AfterViews
    void init() {
        StatHelper helper = new StatHelper(getActivity());
        items = new ArrayList<>();
        switch (page) {
            case 0:
                items.add(helper.getStatItem(StatHelper.Screen.WIDTH));
                items.add(helper.getStatItem(StatHelper.Screen.HEIGHT));
                items.add(helper.getStatItem(StatHelper.Screen.DISPLAY_DENSITY));
                items.add(helper.getStatItem(StatHelper.Screen.DRAWABLE_DENSITY));
                items.add(helper.getStatItem(StatHelper.Screen.SCREEN_SIZE));
                break;

            case 1:
                items.add(helper.getStatItem(StatHelper.Software.ANDROID_VERSION));
                items.add(helper.getStatItem(StatHelper.Software.SDK_INT));
                items.add(helper.getStatItem(StatHelper.Software.OPEN_GL_ES));
                break;

            case 2:
                items.add(helper.getStatItem(StatHelper.Hardware.MANUFACTURER));
                items.add(helper.getStatItem(StatHelper.Hardware.MODEL));
                items.add(helper.getStatItem(StatHelper.Hardware.DEVICE));
                items.add(helper.getStatItem(StatHelper.Hardware.BRAND));
                items.add(helper.getStatItem(StatHelper.Hardware.BOARD));
                items.add(helper.getStatItem(StatHelper.Hardware.HOST));
                items.add(helper.getStatItem(StatHelper.Hardware.PRODUCT));

                items.add(helper.getStatItem(StatHelper.Hardware.MEMORY_CLASS));
                if (Build.VERSION.SDK_INT >= 11) { // This is also checked for within
                    items.add(helper.getStatItem(StatHelper.Hardware.LARGE_MEMORY_CLASS));
                }
                items.add(helper.getStatItem(StatHelper.Hardware.MAX_MEMORY));
                items.add(helper.getStatItem(StatHelper.Hardware.FREE_SPACE));
                items.add(helper.getStatItem(StatHelper.Hardware.VIBRATOR));
                items.add(helper.getStatItem(StatHelper.Hardware.TELEPHONY));
                break;
            case 3:
                items = helper.getFeatureList();
                break;
        }

        loadDataIntoContainer(items, container);
    }


    private void loadDataIntoContainer(ArrayList<StatItem> list, LinearLayout container) {
        container.removeAllViews();
        for (StatItem stat : list) {
            LinearLayout row = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.stat_item, null);
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
}
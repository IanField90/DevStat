package uk.co.ianfield.devstat.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import uk.co.ianfield.devstat.R;
import uk.co.ianfield.devstat.model.StatItem;

/**
 * Created by Ian Field on 14/08/15.
 */
public class InformationPagerAdapter extends FragmentPagerAdapter {
    private int tabTitles[] = { R.string.title_screen_metrics, R.string.title_software, R.string.title_hardware, R.string.title_features };
    private Context context;

    private ArrayList<StatItem>[] statSets;

    public InformationPagerAdapter(FragmentManager fm, Context context, ArrayList<StatItem>[] statSets) {
        super(fm);
        this.context = context;
        this.statSets = statSets;
    }


    @Override
    public Fragment getItem(int i) {
        InformationPageFragment fragment = InformationPageFragment_.builder().page(i).build();
        fragment.setItems(statSets[i]);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return context.getText(tabTitles[position]);
    }
}

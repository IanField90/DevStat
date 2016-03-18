package uk.co.ianfield.devstat.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import uk.co.ianfield.devstat.model.StatItem;

/**
 * Created by Ian Field on 14/08/15.
 */
public class InformationPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private int[] tabTitles;
    private ArrayList<ArrayList<StatItem>> statSets;

    public InformationPagerAdapter(FragmentManager fm, Context context, int[] tabTitles, ArrayList<ArrayList<StatItem>> statSets) {
        super(fm);
        this.tabTitles = tabTitles;
        this.context = context;
        this.statSets = statSets;
    }

    @Override public Fragment getItem(int i) {
        InformationPageFragment fragment = new InformationPageFragment();
        fragment.setItems(statSets.get(i));
        return fragment;
    }

    @Override public int getCount() {
        return tabTitles.length;
    }

    @Override public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return context.getText(tabTitles[position]);
    }
}

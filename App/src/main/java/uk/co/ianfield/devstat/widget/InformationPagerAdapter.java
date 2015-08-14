package uk.co.ianfield.devstat.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import uk.co.ianfield.devstat.R;

/**
 * Created by Ian Field on 14/08/15.
 */
public class InformationPagerAdapter extends FragmentPagerAdapter {
    private int tabTitles[] = { R.string.title_screen_metrics, R.string.title_software, R.string.title_hardware, R.string.title_features };
    private Context context;


    public InformationPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        return InformationPageFragment_.builder().page(i).build();
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

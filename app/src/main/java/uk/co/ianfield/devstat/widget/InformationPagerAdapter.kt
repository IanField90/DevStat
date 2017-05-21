package uk.co.ianfield.devstat.widget

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import java.util.ArrayList

import uk.co.ianfield.devstat.model.StatItem

/**
 * Created by Ian Field on 14/08/15.
 */
class InformationPagerAdapter(fm: FragmentManager, private val context: Context, private val tabTitles: IntArray, private val statSets: ArrayList<ArrayList<StatItem>>) : FragmentPagerAdapter(fm) {

    override fun getItem(i: Int): Fragment {
        val fragment = InformationPageFragment()
        fragment.setItems(statSets[i])
        return fragment
    }

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        // Generate title based on item position
        return context.getText(tabTitles[position])
    }
}

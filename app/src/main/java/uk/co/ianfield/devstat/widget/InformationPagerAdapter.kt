package uk.co.ianfield.devstat.widget

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.ianfield.devstat.model.StatItem
import java.util.*

/**
 * Created by Ian Field on 14/08/15.
 */
class InformationPagerAdapter(fm: androidx.fragment.app.FragmentManager, private val context: Context,
                              private val tabTitles: IntArray,
                              private val statSets: ArrayList<ArrayList<StatItem>>)
    : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(i: Int): androidx.fragment.app.Fragment {
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

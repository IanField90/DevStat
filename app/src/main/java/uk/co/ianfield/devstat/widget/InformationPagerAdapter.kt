package uk.co.ianfield.devstat.widget

import android.content.Context
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import uk.co.ianfield.devstat.model.StatItem
import java.util.*

/**
 * Created by Ian Field on 14/08/15.
 */
class InformationPagerAdapter(fa: FragmentActivity,
                              private val tabTitles: IntArray,
                              private val statSets: ArrayList<ArrayList<StatItem>>)
    : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        val fragment = InformationPageFragment()
        fragment.setItems(statSets[position])
        return fragment
    }

    override fun getItemCount(): Int {
        return tabTitles.size
    }
}

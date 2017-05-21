package uk.co.ianfield.devstat.widget

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.Unbinder
import uk.co.ianfield.devstat.R
import uk.co.ianfield.devstat.StatItemAdapter
import uk.co.ianfield.devstat.model.StatItem
import java.util.*

class InformationPageFragment : Fragment() {

    internal var recyclerView: RecyclerView? = null
    private var items: ArrayList<StatItem>? = null

    lateinit private var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_information_page, container, false)
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        retainInstance = true
        recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView!!.layoutManager = layoutManager

        val adapter = StatItemAdapter(activity, items) { position: Int ->

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
                clipboard.text = items!![position].toString()
            } else {
                val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("text label", items!![position].toString())
                clipboard.primaryClip = clip
            }
            Snackbar.make(view, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show()
        }
        recyclerView!!.adapter = adapter
        return view
    }

    fun setItems(items: ArrayList<StatItem>) {
        this.items = items
    }
}

package uk.co.ianfield.devstat.widget

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_information_page.*
import uk.co.ianfield.devstat.R
import uk.co.ianfield.devstat.StatItemAdapter
import uk.co.ianfield.devstat.model.StatItem
import java.util.*

class InformationPageFragment : Fragment() {

    private var items: ArrayList<StatItem>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_information_page, container, false)
        retainInstance = true
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        val adapter = StatItemAdapter(activity, items) { position: Int ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("text label", items!![position].toString())
            clipboard.primaryClip = clip
            Snackbar.make(recyclerView, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }

    fun setItems(items: ArrayList<StatItem>) {
        this.items = items
    }
}

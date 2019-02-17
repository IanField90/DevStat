package uk.co.ianfield.devstat

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uk.co.ianfield.devstat.model.StatItem
import java.util.*

/**
 * Created by Ian on 18/08/2015.
 */
class StatItemAdapter(context: Context, private val dataSet: ArrayList<StatItem>?,
                      private val listener: (Int) -> Unit) :
        androidx.recyclerview.widget.RecyclerView.Adapter<StatItemAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = inflater.inflate(R.layout.stat_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet!![position].title
        holder.info.text = dataSet[position].info

        val viewListener: View.OnLongClickListener = View.OnLongClickListener {
            listener.invoke(holder.adapterPosition)
            true
        }
        holder.title.setOnLongClickListener(viewListener)
        holder.info.setOnLongClickListener(viewListener)
    }

    override fun getItemCount(): Int {
        return dataSet!!.size
    }

    interface OnItemLongClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(container: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(container) {
        val title: TextView
        val info: TextView

        init {
            title = container.findViewById<TextView>(R.id.txtTitle)
            info = container.findViewById<TextView>(R.id.txtInfo)
        }
    }
}

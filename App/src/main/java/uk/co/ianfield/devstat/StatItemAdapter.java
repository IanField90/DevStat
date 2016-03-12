package uk.co.ianfield.devstat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.ianfield.devstat.model.StatItem;

/**
 * Created by Ian on 18/08/2015.
 */
public class StatItemAdapter extends  RecyclerView.Adapter<StatItemAdapter.ViewHolder> {
    private ArrayList<StatItem> dataSet;

    private final OnItemLongClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView info;
        public ViewHolder(View container) {
            super(container);
            title = (TextView) container.findViewById(R.id.txtTitle);
            info = (TextView) container.findViewById(R.id.txtInfo);
        }
    }

    public StatItemAdapter(ArrayList<StatItem> statItems, OnItemLongClickListener listener) {
        dataSet = statItems;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(dataSet.get(position).getTitle());
        holder.info.setText(dataSet.get(position).getInfo());

        if (listener != null) {
            View.OnLongClickListener viewListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(position);
                    return true;
                }
            };
            holder.title.setOnLongClickListener(viewListener);
            holder.info.setOnLongClickListener(viewListener);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface OnItemLongClickListener {
        void onItemClick(int position);
    }
}

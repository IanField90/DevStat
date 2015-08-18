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
    private ArrayList<StatItem> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mInfo;
        public ViewHolder(View container) {
            super(container);
            mTitle = (TextView) container.findViewById(R.id.txtTitle);
            mInfo = (TextView) container.findViewById(R.id.txtInfo);
        }
    }

    public StatItemAdapter(ArrayList<StatItem> statItems) {
        mDataset = statItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(mDataset.get(position).getTitle());
        holder.mInfo.setText(mDataset.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

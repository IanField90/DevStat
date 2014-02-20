package uk.co.ianfield.devstat.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.ianfield.devstat.R;
import uk.co.ianfield.devstat.model.StatItem;

/**
 * Created by ianfield on 20/02/2014.
 */
public class StatItemArrayAdapter extends ArrayAdapter<StatItem> {

    public StatItemArrayAdapter (Context context, int resource, ArrayList<StatItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stat_item, null);
        }

        if(convertView != null) {
            StatItem item = getItem(position);
            TextView txtInfo = (TextView) convertView.findViewById(R.id.txtInfo);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

            if (txtInfo != null) {
                txtInfo.setText(item.getInfo());
            }

            if (txtTitle != null) {
                txtTitle.setText(item.getTitle());
            }
        }
        return convertView;
    }
}

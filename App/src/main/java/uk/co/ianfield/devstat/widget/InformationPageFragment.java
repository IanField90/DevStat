package uk.co.ianfield.devstat.widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import uk.co.ianfield.devstat.R;
import uk.co.ianfield.devstat.StatItemAdapter;
import uk.co.ianfield.devstat.model.StatItem;

/**
 * Created by Ian Field on 14/08/15.
 */
@EFragment(R.layout.fragment_information_page)
public class InformationPageFragment extends Fragment {
    @ViewById(R.id.recycler_view)
    RecyclerView mRecyclerView;

    ArrayList<StatItem> items;

    @AfterViews
    void init() {
        setRetainInstance(true);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new StatItemAdapter(items, new StatItemAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(int position) {

                if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(items.get(position).toString());
                } else {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = android.content.ClipData.newPlainText("text label", items.get(position).toString());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(getActivity(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    public void setItems(ArrayList<StatItem> items) {
        this.items = items;
    }
}

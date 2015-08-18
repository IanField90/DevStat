package uk.co.ianfield.devstat.widget;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
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
    @FragmentArg
    int page;

    @ViewById(R.id.recycler_view)
    RecyclerView mRecyclerView;

    ArrayList<StatItem> items;

    @AfterViews
    void init() {
        setRetainInstance(true);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new StatItemAdapter(items);
        mRecyclerView.setAdapter(adapter);
    }

    public void setItems(ArrayList<StatItem> items) {
        this.items = items;
    }
}

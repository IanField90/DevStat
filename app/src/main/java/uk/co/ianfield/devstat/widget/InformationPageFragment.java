package uk.co.ianfield.devstat.widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.ianfield.devstat.R;
import uk.co.ianfield.devstat.StatItemAdapter;
import uk.co.ianfield.devstat.model.StatItem;

public class InformationPageFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ArrayList<StatItem> items;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        setRetainInstance(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new StatItemAdapter(getActivity(), items, position -> {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(items.get(position).toString());
            } else {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text label", items.get(position).toString());
                clipboard.setPrimaryClip(clip);
            }
            Snackbar.make(view, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void setItems(ArrayList<StatItem> items) {
        this.items = items;
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }
}

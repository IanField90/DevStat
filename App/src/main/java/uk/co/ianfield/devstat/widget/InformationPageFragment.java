package uk.co.ianfield.devstat.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import uk.co.ianfield.devstat.R;

/**
 * Created by Ian Field on 14/08/15.
 */
@EFragment(R.layout.fragment_information_page)
public class InformationPageFragment extends Fragment {
    @FragmentArg
    int page;


//    public static final String ARG_PAGE = "ARG_PAGE";
//
//    private int mPage;
//
//    public static InformationPageFragment newInstance(int page) {
//        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, page);
//        InformationPageFragment fragment = new InformationPageFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPage = getArguments().getInt(ARG_PAGE);
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_page, container, false);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPage);
//        return view;
//    }
}

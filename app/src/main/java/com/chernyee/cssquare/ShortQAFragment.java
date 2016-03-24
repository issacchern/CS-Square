package com.chernyee.cssquare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;


public class ShortQAFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private BottomBar mBottomBar;

    private OnFragmentInteractionListener mListener;

    public ShortQAFragment() {
        // Required empty public constructor
    }

    public static ShortQAFragment newInstance(String param1, String param2) {
        ShortQAFragment fragment = new ShortQAFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_short_qa, container, false);

        mBottomBar = BottomBar.attach(v.findViewById(R.id.fragmentContainer), savedInstanceState);
        mBottomBar.setFragmentItems(getFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(ShortQATabFragment.newInstance("Code"), R.drawable.code_tags, "Code"),
                new BottomBarFragment(ShortQATabFragment.newInstance("Algorithm"), R.drawable.sitemap, "Algorithm"),
                new BottomBarFragment(ShortQATabFragment.newInstance("Java"), R.drawable.coffee, "Java"),
                new BottomBarFragment(ShortQATabFragment.newInstance("Android"), R.drawable.android, "Android")
        );

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

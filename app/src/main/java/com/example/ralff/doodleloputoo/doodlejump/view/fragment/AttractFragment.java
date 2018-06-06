package com.example.ralff.doodleloputoo.doodlejump.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ralff.doodleloputoo.R;
import com.example.ralff.doodleloputoo.doodlejump.listener.UiListener;



public class AttractFragment extends Fragment {
    public static final String TAG = "AttractFragment";

    private UiListener mUiListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.attract_fragment, container, false);

        fragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUiListener != null)
                    mUiListener.onMainMenu();
            }
        });
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        
        try {
            mUiListener = (UiListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement UiListener");
        }
    }
}

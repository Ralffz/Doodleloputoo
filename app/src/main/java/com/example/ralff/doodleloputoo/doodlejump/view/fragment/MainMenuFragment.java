package com.example.ralff.doodleloputoo.doodlejump.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ralff.doodleloputoo.R;
import com.example.ralff.doodleloputoo.doodlejump.listener.UiListener;
import com.example.ralff.doodleloputoo.doodlejump.view.AboutMeActivity;




public class MainMenuFragment extends Fragment {
    public static final String TAG = "MainMenuFragment";

    private UiListener mUiListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.main_menu_fragment, container, false);

        Button newGameButton = (Button) fragmentView.findViewById(R.id.new_game_button);
        Button aboutUsButton = (Button) fragmentView.findViewById(R.id.about_me_button);
        Button scoresButton = (Button) fragmentView.findViewById(R.id.scores_button);


        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUiListener != null)
                    mUiListener.onStartGame();
            }
        });

        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), AboutMeActivity.class);

                startActivity(intent);
            }
        });

        scoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUiListener != null)
                    mUiListener.onShowScoreboard();
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

package com.mazzdev.pacman.android;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.mazzdev.pacman.ActionResolver;
import com.mazzdev.pacman.PacmanMainClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matteo on 30/12/2014.
 */
public class GameFragment extends AndroidFragmentApplication
{


    final static String TAG = "GameFragment";

    private ActionResolver actionResolver;
    private int nPlayer;
    private String playerName;
    private List<String> pNames = new ArrayList();

    public View gameView;
    public PacmanMainClass game;

    public GameFragment(ActionResolver actionResolver) {
        this(actionResolver, 1, "Pacman", null);
    }

    public GameFragment(ActionResolver actionResolver, int nPlayer, String playerName, List<String> pNames) {
        this.actionResolver = actionResolver;
        this.nPlayer = nPlayer;
        this.playerName = playerName;
        if (pNames == null || pNames.isEmpty()) {
            this.pNames.add("P1");
        } else {
            this.pNames = pNames;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);
        game = new PacmanMainClass(actionResolver, nPlayer, playerName, pNames);
        gameView =  initializeForView(game);

        gameView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        gameView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            new CountDownTimer(3000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    gameView.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                                }
                            }.start();
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                        }
                    }
                });

        return gameView;
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getMultiPlayerHelper().leaveRoom();
        super.onPause();
    }
}
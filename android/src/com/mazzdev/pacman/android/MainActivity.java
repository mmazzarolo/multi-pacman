package com.mazzdev.pacman.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.mazzdev.pacman.ActionResolver;
import com.mazzdev.pacman.actors.BaseActor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements
        ActionResolver, AndroidFragmentApplication.Callbacks,
        MultiPlayerHelper.MultiPlayerUi {

    private final static String TAG = "MainActivity";

    public MultiPlayerHelper multiPlayerHelper;

    private static final int RC_SIGN_IN = 9001;

    private MainFragment mainFragment;

    // Game variables
    private GameFragment gameFragment;
    private String myId = null;
    private String myChar = null;
    private boolean isMultiplayer = false;
    private int numParticipants = 0;
    private int numReadyParticipants = 0;
    private ArrayList<Participant> participantList = null;
    private Map mapCharToId = new HashMap();
    private Map mapIdToChar = new HashMap();
    private final static String[] CHARACTERS = {"Pacman", "Blinky", "Clyde"};
    private List<String> pNames = new ArrayList();

//*=================================================================================================
//* ANDROID LIFECYCLE AND LISTENERS.
//*=================================================================================================
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        multiPlayerHelper = new MultiPlayerHelper(this);
        multiPlayerHelper.onCreate();

        mainFragment = new MainFragment();


        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(android.R.id.content, mainFragment);
        trans.addToBackStack(null);
        trans.commit();

//        setContentView(R.layout.fragment_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
//        setSupportActionBar(toolbar);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "**** got onStop");
        multiPlayerHelper.onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        multiPlayerHelper.onStart(this, this);
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        multiPlayerHelper.onActivityResult(requestCode, responseCode, intent);

        switch (requestCode) {
            case RC_SIGN_IN:
                GoogleApiClient googleApiClient = multiPlayerHelper.getGameHelper().getApiClient();
                Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                        + responseCode + ", intent=" + intent);
                if (responseCode == RESULT_OK) {
                    googleApiClient.connect();
                } else {
                    BaseGameUtils.showActivityResultError(this,requestCode,responseCode,
                            R.string.signin_failure, R.string.signin_other_error);
                }
                break;
        }
    }

//*=================================================================================================
//* UI SECTION. Methods that implement the game's UI.
//*=================================================================================================

//    private void changeFragmentToConnected() {
//
//
//        if (currentFragment instanceof SettingsFragment) {
//            SettingsFragment settingsFragment = (SettingsFragment) currentFragment;
//            settingsFragment.onConnectionChanged();
//        }
//
//        if (currentFragment instanceof MultiPlayerFragment) {
//            MultiPlayerFragment multiPlayerFragment = (MultiPlayerFragment) currentFragment;
//            multiPlayerFragment.onConnectionChanged();
//        }
//   }

//*=================================================================================================
//* GAME SECTION. Game management.
//*=================================================================================================
    private void resetGameVars() {
        myChar = null;
        isMultiplayer = false;
        numParticipants = 0;
        numReadyParticipants = 0;
        mapCharToId.clear();
        mapIdToChar.clear();
        pNames.clear();
    }

    public void startGame(boolean isMultiplayer) {
        resetGameVars();

        this.isMultiplayer = isMultiplayer;

        if (isMultiplayer) {
            managePlayers();
            gameFragment = new GameFragment(this, numParticipants, myChar, pNames);
        } else {
            numParticipants = 1;
            gameFragment = new GameFragment(this);
        }

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(android.R.id.content, gameFragment);
        trans.addToBackStack(null);
        trans.commit();
    }

    private void managePlayers() {

        numParticipants = participantList.size();

        Collections.sort(participantList, new Comparator<Participant>() {
            public int compare(Participant one, Participant other) {
                return one.getParticipantId().compareTo(other.getParticipantId());
            }
        });

        Log.d(TAG, "managePlayers(): numParticipants = " + numParticipants);

        mapIdToChar.clear();
        mapCharToId.clear();
        for (int i = 0; i < participantList.size(); ++i) {
            String id = participantList.get(i).getParticipantId();
            String character = CHARACTERS[i];
            mapCharToId.put(character, id);
            mapIdToChar.put(id, character);
            Log.d(TAG, "managePlayers() - PLAYER -> " + id + ", " + character);
            pNames.add(participantList.get(i).getDisplayName());
        }

        myChar = (String) mapIdToChar.get(myId);

    }

    public void manageMsg(Participant participant, byte[] data) {
        String senderId = participant.getParticipantId();

        if (data[0] == 'M') {
            String senderChar = (String) mapIdToChar.get(senderId);
            BaseActor actor = gameFragment.game.gameStage.actorsGroup.findActor(senderChar);
            actor.setPositionFromMsg(data);
        }

        if (data[0] == 'B') {
            showToast("CIAPPAAAA");
        }

        if (data[0] == 'R') {
            numReadyParticipants++;
            checkIfReady();
        }
    }

    private void checkIfReady(){
        if (numReadyParticipants >= numParticipants) {
            gameFragment.game.gameStage.startGame();
        }
    }

//*=================================================================================================
//* LIBGDX Section
//*=================================================================================================
    public void showToast(final CharSequence text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
        Log.v(TAG, "text=" + text);
    }

    public void broadcastMsg(byte[] msg) {
        if (isMultiplayer) {
            multiPlayerHelper.sendUpdate(msg, false);
        }
    }

    public void onGameEnded() {
        Log.d(TAG, "onGameEnded()");
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        trans.remove(gameFragment);
        trans.commit();
    }

    @Override
    public void setMeAsReady(){
        numReadyParticipants++;
        checkIfReady();
    }

//*=================================================================================================
//* MultiplayerHelper.MultiplayerUI implementation.
//*=================================================================================================
    @Override
    public void showScreen(int screen) {}

    @Override
    public void onUpdateReceived(Participant participant, byte[] data) {
        manageMsg(participant, data);
    }

    @Override
    public void onParticipantsChanged (ArrayList<Participant> participants) {
        Log.d(TAG, "onParticipantsChanged() -> " + participants.size());
        participantList = participants;
    }

    @Override
    public void onSignInStatusChanged(boolean isSignedIn) {
        Log.d(TAG, "onSignInStatusChanged() -> " + isSignedIn);
        mainFragment.onConnectionChanged();
    }

    @Override
    public void onGameReady(){
        myId = multiPlayerHelper.getUserId();
        startGame(true);
    }

    @Override
    public void removePlayers(List<String> peersWhoLeft) {
        Log.d(TAG, "removePlayers!");

        for (String id : peersWhoLeft) {
            Log.d(TAG, "id = " + id);
            if (mapIdToChar.get(id) != null) {
                String character = (String) mapIdToChar.get(id);

                mapIdToChar.remove(id);
                mapCharToId.remove(character);

                numParticipants--;
                numReadyParticipants--;

                showToast("UN MERDONE HA QUITTATO :(");
                gameFragment.game.gameStage.removeActor(character);
            }
        }
    }

    public MultiPlayerHelper getMultiPlayerHelper() {
        return multiPlayerHelper;
    }

//*=================================================================================================
//* AndroidFragmentApplication OVERRIDE
//*=================================================================================================
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void exit() {}

}
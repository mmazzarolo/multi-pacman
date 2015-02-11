package com.mazzdev.pacman.android;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

/**
 * Created by Matteo on 08/01/2015.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private View view;

    private MainActivity mainActivity;

    private final static int[] CLICKABLES = {
            R.id.btn_single_player,
            R.id.btn_invite_players,
            R.id.btn_quick_game,
            R.id.btn_see_invitations,
            R.id.btn_sign_out,
            R.id.btn_sign_in
    };

    private View imageViewBottomAnim;
    private AnimationDrawable animationDrawableBottomAnim;
    private TranslateAnimation animation1;
    private TranslateAnimation animation2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_main, container, false);

        setFullScreen();

        mainActivity = (MainActivity) getActivity();

        Typeface font = Typeface.createFromAsset(mainActivity.getApplicationContext().getAssets(),
                "fonts/prstartk.ttf");

        for (int id : CLICKABLES) {
            Button btn = (Button) view.findViewById(id);
            btn.setTypeface(font);
            btn.setOnClickListener(this);
        }

        imageViewBottomAnim = view.findViewById(R.id.iv_bottom_anim);

        setBottomAnimation();

        onConnectionChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setFullScreen();
    }

    //*=================================================================================================
    //* Setting the bottom animation.
    //*=================================================================================================
    private void setBottomAnimation() {

        setAnimation1();

        WindowManager wm = (WindowManager) mainActivity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int width40 = size.x / 100 * 40;
        int width50 = size.x / 100 * 50;
        width += width50;

        MyAnimationListener myAnimationListener = new MyAnimationListener();

        animation1 = new TranslateAnimation(-width40, (float) width, 0.0f, 0.0f);
        animation1.setDuration(6000);
        animation1.setRepeatCount(0);
        animation1.setAnimationListener(myAnimationListener);


        animation2 = new TranslateAnimation((float) width, -width50, 0.0f, 0.0f);
        animation2.setDuration(6001);
        animation2.setRepeatCount(0);
        animation2.setAnimationListener(myAnimationListener);

        imageViewBottomAnim.startAnimation(animation1);
    }

    private void setAnimation1(){
        imageViewBottomAnim.setBackgroundResource(R.drawable.bottom_left_to_right);
        animationDrawableBottomAnim = (AnimationDrawable) imageViewBottomAnim.getBackground();
        animationDrawableBottomAnim.start();
    }

    private void setAnimation2(){
        imageViewBottomAnim.setBackgroundResource(R.drawable.bottom_right_to_left);
        animationDrawableBottomAnim = (AnimationDrawable) imageViewBottomAnim.getBackground();
        animationDrawableBottomAnim.start();
    }

    class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animation.getDuration() == 6000) {
                setAnimation2();
                imageViewBottomAnim.startAnimation(animation2);
            } else {
                setAnimation1();
                imageViewBottomAnim.startAnimation(animation1);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    }

    //*=================================================================================================
    //* Setting the fragment to fullscreen.
    //*=================================================================================================
    private void setFullScreen() {
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        view.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            new CountDownTimer(3000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    view.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                                }
                            }.start();
                        }
                    }
                });
    }


    //*=================================================================================================
    //* Setting the onClick to every button.
    //*=================================================================================================
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_single_player:
                mainActivity.startGame(false);
                break;
            case R.id.btn_invite_players:
                mainActivity.multiPlayerHelper.startGameWithFriends(1, 3);
                break;
            case R.id.btn_see_invitations:
                mainActivity.multiPlayerHelper.sendInvitationInbox();
                break;
            case R.id.btn_quick_game:
                mainActivity.multiPlayerHelper.startQuickGame(1, 1);
                break;
            case R.id.btn_sign_out:
                mainActivity.multiPlayerHelper.signOut();
                onConnectionChanged();
                break;
            case R.id.btn_sign_in:
                mainActivity.multiPlayerHelper.signIn();
                break;
        }

    }

    //*=================================================================================================
    //* Hiding/Showing the correct views when there is a connection/disconnection event.
    //*=================================================================================================
    public void onConnectionChanged() {

        boolean isConnected = mainActivity.multiPlayerHelper.isSignedIn();

        if (view != null) {
            if (isConnected) {
                view.findViewById(R.id.btn_sign_out).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btn_sign_in).setVisibility(View.GONE);
                view.findViewById(R.id.btn_invite_players).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btn_see_invitations).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btn_quick_game).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.btn_sign_out).setVisibility(View.GONE);
                view.findViewById(R.id.btn_sign_in).setVisibility(View.VISIBLE);
                view.findViewById(R.id.btn_invite_players).setVisibility(View.GONE);
                view.findViewById(R.id.btn_see_invitations).setVisibility(View.GONE);
                view.findViewById(R.id.btn_quick_game).setVisibility(View.GONE);
            }
        }
    }
}
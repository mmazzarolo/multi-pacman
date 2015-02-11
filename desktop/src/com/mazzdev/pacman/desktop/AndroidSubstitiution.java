package com.mazzdev.pacman.desktop;

import com.badlogic.gdx.Gdx;
import com.mazzdev.pacman.ActionResolver;

public class AndroidSubstitiution implements ActionResolver{

    @Override
    public void showToast(CharSequence text)
    {
        Gdx.app.log("AndroidSubstitution", text.toString());
    }

    @Override
    public void broadcastMsg(byte[] msg) {

    }

    @Override
    public void onGameEnded() {

    }

    @Override
    public void setMeAsReady() {
    }
}

package com.mazzdev.pacman;

/**
 * Created by Matteo on 19/12/2014.
 */
public interface ActionResolver {
    public void showToast(CharSequence text);
    public void broadcastMsg(byte[] msg);
    public void onGameEnded();
    public void setMeAsReady();
}
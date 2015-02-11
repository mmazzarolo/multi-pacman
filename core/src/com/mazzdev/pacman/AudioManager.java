package com.mazzdev.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Matteo on 09/02/2015.
 */
public final class AudioManager implements Music.OnCompletionListener {


    public void playSiren1() {
        Music soundBG = Gdx.audio.newMusic(Gdx.files.internal("audio/siren_1.ogg"));
        soundBG.setLooping(true);
        soundBG.play();
    }

    public void playStart() {
        Music soundStarting = Gdx.audio.newMusic(Gdx.files.internal("audio/start.ogg"));
        soundStarting.play();
    }

    public void playEating() {
        Music eatopen = Gdx.audio.newMusic(Gdx.files.internal("audio/eatclose.ogg"));
        eatopen.setOnCompletionListener(this);
        eatopen.play();
    }

    public void onCompletion(Music music) {
        Music eatclose = Gdx.audio.newMusic(Gdx.files.internal("audio/eatopen.ogg"));
        eatclose.play();
    }

}

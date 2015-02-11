package com.mazzdev.pacman;

/**
 * Created by Matteo on 10/12/2014.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.mazzdev.pacman.enums.Direction;

public class MyGestureListener implements GestureListener {

    private final String TAG = "InputProcessor";

    GameStage stage;

    public MyGestureListener(GameStage stage) {
        this.stage = stage;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0) {
                Gdx.app.log(TAG, "fling: SWYPE RIGHT");
                stage.getUser().setInputDirection(Direction.RIGHT);
            } else {
                Gdx.app.log(TAG, "fling: SWYPE LEFT");
                stage.getUser().setInputDirection(Direction.LEFT);
            }
        } else {
            if (velocityY > 0) {
                Gdx.app.log(TAG, "fling: SWYPE DOWN");
                stage.getUser().setInputDirection(Direction.DOWN);
            } else {
                Gdx.app.log(TAG, "fling: SWYPE UP");
                stage.getUser().setInputDirection(Direction.UP);
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) { return false; }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) { return false; }

    @Override
    public boolean zoom(float initialDistance, float distance) { return false; }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) { return false; }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) { return false; }

    @Override
    public boolean longPress(float x, float y) { return false; }

    @Override
    public boolean tap(float x, float y, int count, int button) { return false; }
}

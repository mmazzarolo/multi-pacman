package com.mazzdev.pacman.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.mazzdev.pacman.MovementManager;
import com.mazzdev.pacman.GameStage;
import com.mazzdev.pacman.enums.Direction;
import com.mazzdev.pacman.enums.Status;

import java.math.BigDecimal;

/**
 * Created by Matteo on 24/12/2014.
 */
public class BaseActor extends Actor {

    private float SINGLE_STEP = 0.1f;
    protected float speed;
    protected Animation animation;
    protected Direction movingDirection = Direction.NEUTRAL;
    protected Direction inputDirection = Direction.NEUTRAL;
    protected float stateTime = 0;
    private TextureRegion region;

    public GameStage gameStage;
    private MovementManager movementManager;

    public boolean isThePlayer = false;

    public float remainingDistance;

    public BaseActor(Animation animation, float x, float y, float w, float h, float speed) {
        this.animation = animation;
        this.speed = speed;
        setPosition(x, y);
        setWidth(w);
        setHeight(h);
    }

    @Override
    public void act(float delta) {

        this.gameStage = (GameStage) getStage();
        this.movementManager = gameStage.getMovementManager();

        super.act(delta);
        if (movingDirection != Direction.NEUTRAL) {
            stateTime += delta;
        }

        if (gameStage.gameStatus == Status.PLAYING) {
            movementManager.moveActor(this);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        region = animation.getKeyFrame(stateTime, true);

        batch.draw(region,
                getX(),
                getY(),
                0.5f,
                0.5f,
                getWidth(),
                getHeight(),
                1.75f,
                1.75f,
                getRotation());
    }

    public static Float round(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public Rectangle getNextBounds(Direction direction) {
        float stepX = direction.getX() * SINGLE_STEP;
        float stepY = direction.getY() * SINGLE_STEP;
        return new Rectangle(getX() + stepX, getY() + stepY, getWidth(), getHeight());
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(round(x), round(y));
    }



    public void moveBy (Direction direction) {
        float stepX = direction.getX() * SINGLE_STEP;
        float stepY = direction.getY() * SINGLE_STEP;
        moveBy(stepX, stepY);
        super.setPosition(round(getX()), round(getY()));
    }

    public boolean isInputDirectionNeutral() {
        return (inputDirection == Direction.NEUTRAL);
    }

    public boolean isMovingDirectionNeutral() {
        return (movingDirection == Direction.NEUTRAL);
    }

    public Direction getDirectionFromChar(char c) {
        switch (c) {
            case 'U': return Direction.UP;
            case 'D': return Direction.DOWN;
            case 'L': return Direction.LEFT;
            case 'R': return Direction.RIGHT;
            case 'N': return Direction.NEUTRAL;
        }
        return null;
    }

    public void setPositionFromMsg(byte[] msg) {
        Direction msgDirection = getDirectionFromChar((char) msg[1]);

        int newX = ((msg[2] & 0xff) << 8) | (msg[3] & 0xff);
        int newY = ((msg[4] & 0xff) << 8) | (msg[5] & 0xff);

        float msgX = (float) newX / 10;
        float msgY = (float) newY / 10;

        setMovingDirection(msgDirection);

        MoveToAction action = Actions.action(MoveToAction.class);
        action.setPosition(msgX, msgY);
        addAction(action);
    }

    //==============================================================================================
    // Standard getters and setters
    //==============================================================================================
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void setMovingDirection(Direction movingDirection) {
        this.movingDirection = movingDirection;
    }

    public float getSpeed() { return speed; }

    public void setAnimation(Animation animation) { this.animation = animation; }

    public Direction getMovingDirection() { return movingDirection; }

    public Direction getInputDirection() { return inputDirection; }

    public void setInputDirection(Direction inputDirection) { this.inputDirection = inputDirection; }



}

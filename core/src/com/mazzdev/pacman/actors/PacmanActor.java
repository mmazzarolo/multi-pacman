package com.mazzdev.pacman.actors;

import com.mazzdev.pacman.enums.Direction;

import static com.mazzdev.pacman.AssetsUtilty.createPacmanAnimation;

/**
 * Created by Matteo on 24/12/2014.
 */
public class PacmanActor extends BaseActor {

    public PacmanActor() {
        super(null, 13.5f, 7f, 1f, 1f, 0.065f);
        super.setAnimation(createPacmanAnimation());
        super.setName("Pacman");
        super.setInputDirection(Direction.LEFT);
    }

    @Override
    public void setMovingDirection(Direction movingDirection) {
        if (this.movingDirection != movingDirection) {
            setRotation(movingDirection);
        }
        this.movingDirection = movingDirection;
    }

    public void setRotation (Direction direction) {
        switch (direction) {
            case UP: setRotation(270); break;
            case DOWN: setRotation(90); break;
            case LEFT: setRotation(0); break;
            case RIGHT: setRotation(180); break;
        }
    }
}

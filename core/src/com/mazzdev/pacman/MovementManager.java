package com.mazzdev.pacman;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.mazzdev.pacman.actors.BaseActor;
import com.mazzdev.pacman.actors.PacmanActor;
import com.mazzdev.pacman.enums.Direction;

/**
 * Created by Matteo on 31/01/2015.
 */
public class MovementManager {

    GameStage gameStage;
    WorldMap worldMap;
    Array<Actor> actorList;

    public MovementManager(GameStage gameStage) {
        this.gameStage = gameStage;
        worldMap = gameStage.worldMap;
        actorList = gameStage.getActorList();
    }

    public void moveActor(BaseActor actor) {

            float distanceToMove = actor.getSpeed() + actor.remainingDistance;

            while (distanceToMove >= 0.1f) {

                if (!actor.isInputDirectionNeutral())
                    tryChangingDirection(actor);

                if (!actor.isMovingDirectionNeutral())
                    tryMovingDirection(actor);

                distanceToMove = distanceToMove - 0.1f;
            }
            actor.remainingDistance = distanceToMove;
    }

    public void tryChangingDirection(BaseActor actor) {
        Rectangle nextBounds = actor.getNextBounds(actor.getInputDirection());

        if (!willCollide(nextBounds)) {
            actor.setMovingDirection(actor.getInputDirection());
            actor.setInputDirection(Direction.NEUTRAL);
        }
    }

    public void tryMovingDirection(BaseActor actor) {
        Rectangle nextBounds = actor.getNextBounds(actor.getMovingDirection());

        if (willCollide(nextBounds)) {
            actor.setMovingDirection(Direction.NEUTRAL);
        } else {
            actor.moveBy(actor.getMovingDirection());
        }
    }

    private boolean willCollide(Rectangle rect) {
        for (Rectangle tile : worldMap.getWallTiles()) {
            if (rect.overlaps(tile)) return true;
        }
        return false;
    }

    public void checkCoinsCollision(PacmanActor pacmanActor) {
        for (Rectangle tile : worldMap.getCollectableTiles()) {
            if (pacmanActor.getBounds().overlaps(tile)) {

                TiledMapTile tileWithCollectable = worldMap.getTile((int) tile.x, (int) tile.y, "Collectables");

                if (tileWithCollectable.getProperties().containsKey("Coins") && tileWithCollectable.getProperties().get("Coins", String.class).equals("1")) {
                    gameStage.onPacmanScore();
                    worldMap.setTileAsEmpy((int) tile.x, (int) tile.y, "Collectables");
                }
                break;
            }
        }
    }

    public boolean checkGhostsCollisions(PacmanActor pacmanActor) {
        Rectangle rect = pacmanActor.getBounds();
        for (int i = 0; i < actorList.size; i++) {
            BaseActor actor = (BaseActor) actorList.get(i);
            if (!(actor.getName().equals("Pacman")) && rect.overlaps(actor.getBounds())) {
                return true;
            }
        }
        return false;
    }
}

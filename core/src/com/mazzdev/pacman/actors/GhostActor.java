package com.mazzdev.pacman.actors;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mazzdev.pacman.enums.Direction;
import com.mazzdev.pacman.enums.Status;

import static com.mazzdev.pacman.AssetsUtilty.createGhostAnimation;

/**
 * Created by Matteo on 05/01/2015.
 */
public class GhostActor extends BaseActor {

    char name;

    Pixmap fogCirclePixmap;
    Texture fogCircleTexture;

    Animation[] ghostAnimation;

    int radius = 64;
    int radiusOfVision = 4;

    public GhostActor(float x, float y, float w, float h, float speed, char name) {
        super(null, x, y, w, h, speed);

        this.ghostAnimation = createGhostAnimation(name);
        super.setAnimation(ghostAnimation[0]);
        this.name = name;

        Pixmap.setBlending(Pixmap.Blending.None);
        fogCirclePixmap = new Pixmap(64, 64, Pixmap.Format.LuminanceAlpha);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (isThePlayer) {
            updateFogCircle();
            batch.draw(fogCircleTexture,
                    getX() - 32,
                    getY() - 32);
        }
    }

    @Override
    public void setMovingDirection(Direction movingDirection) {
        if (this.movingDirection != movingDirection) {
            setRotation(movingDirection);
        }
        this.movingDirection = movingDirection;
    }

    private void updateFogCircle (){
        if (gameStage.gameStatus == Status.ENDING)
            radius++;
        if (gameStage.gameStatus == Status.PLAYING && radius > radiusOfVision)
            radius--;
        fogCirclePixmap.setColor(0, 0, 0, 1);
        fogCirclePixmap.fill();
        fogCirclePixmap.fillCircle(32, 32, radius);
        fogCircleTexture = new Texture(fogCirclePixmap, Pixmap.Format.LuminanceAlpha, false);
    }

    public void setRotation (Direction direction) {
        switch (direction) {
            case UP: super.setAnimation(ghostAnimation[1]); break;
            case DOWN: super.setAnimation(ghostAnimation[0]); break;
            case LEFT: super.setAnimation(ghostAnimation[2]); break;
            case RIGHT: super.setAnimation(ghostAnimation[3]); break;
        }
    }
}

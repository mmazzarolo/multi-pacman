package com.mazzdev.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Matteo on 05/01/2015.
 */
public final class AssetsUtilty {

    public static Animation[] createGhostAnimation(char name) {

        Texture walkSheet;

        switch (name) {
            case 'C':
                walkSheet = new Texture(Gdx.files.internal("sprites/clyde.png"));
                break;
            case 'B':
                walkSheet = new Texture(Gdx.files.internal("sprites/blinky.png"));
                break;
            default:
                return null;
        }


        TextureRegion[] walkFramesDown;
        TextureRegion[] walkFramesUp;
        TextureRegion[] walkFramesLeft;
        TextureRegion[] walkFramesRight;

        TextureRegion[][] tmp;

        Animation[] animation;

        int frameRows = 2;
        int frameCols = 4;

        walkSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / frameCols, walkSheet.getHeight() / frameRows);

        walkFramesDown = new TextureRegion[2];
        walkFramesUp = new TextureRegion[2];
        walkFramesLeft = new TextureRegion[2];
        walkFramesRight = new TextureRegion[2];

        // Down
        walkFramesDown[0] = tmp[0][0];
        walkFramesDown[1] = tmp[0][1];

        // Up
        walkFramesUp[0] = tmp[0][2];
        walkFramesUp[1] = tmp[0][3];

        // Left
        walkFramesLeft[0] = tmp[1][0];
        walkFramesLeft[1] = tmp[1][1];

        // Right
        walkFramesRight[0] = tmp[1][2];
        walkFramesRight[1] = tmp[1][3];

        animation = new Animation[4];
        animation[0] = new Animation(0.1f, walkFramesDown);
        animation[1] = new Animation(0.1f, walkFramesUp);
        animation[2] = new Animation(0.1f, walkFramesLeft);
        animation[3] = new Animation(0.1f, walkFramesRight);

        return animation;

    }

    public static Animation createPacmanAnimation() {

        Texture walkSheet;
        TextureRegion[] walkFrames;
        TextureRegion[][] tmp;

        int frameRows = 1;
        int frameCols = 4;

        walkSheet = new Texture(Gdx.files.internal("sprites/pacman16.png"));

        walkSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / frameCols, walkSheet.getHeight() / frameRows);
        walkFrames = new TextureRegion[4];
        int index = 0;
        for (int i = 0; i < frameCols; i++) {
            walkFrames[index++] = tmp[0][i];
        }
        return new Animation(0.05f, walkFrames);
    }
}

package com.mazzdev.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.List;

/**
 * Created by Matteo on 10/12/2014.
 */
public class UIStage extends Stage {

    public TextUI textUI;
    public ScoreUI scoreUI;

    private static float WIDTH = 280f;
    private static float HEIGHT = 310f;
    private static int MAX_SCORE = 160;

    public UIStage(List<String> pNames) {
        super(new FitViewport(WIDTH, HEIGHT));

        textUI = new TextUI();
        addActor(textUI);

        scoreUI = new ScoreUI();
        addActor(scoreUI);

//        int i = 0;
//        for (String pName : pNames) {
//            i++;
//            if (i == 1) addActor(new PlayerName(pName, i));
//            if (i == 2) addActor(new PlayerName(pName, i));
//            if (i == 3) addActor(new PlayerName(pName, i));
//        }

    }

/*================================================================================================*/
// TextUI Actor
/*================================================================================================*/
    public class TextUI extends Actor {

        private BitmapFont font;
        private String text;
        private Vector2 position;

        public TextUI() {
            font = new BitmapFont(Gdx.files.internal("fonts/font2.fnt"));
            font.setScale(.7f, .7f);
            text = "wait";
            position = new Vector2(113, 142);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            font.draw(batch, text, position.x, position.y);
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setPosition(float x, float y) {
            position.x = x;
            position.y = y;
        }
    }

/*================================================================================================*/
// ScoreUI Actor
/*================================================================================================*/
    public class ScoreUI extends Actor {

        private BitmapFont fontScoreText;
        private String textScoreText;
        private Vector2 positionScoreText;

        private Pixmap pixmapScoreLine;
        private Texture textureScoreLine;
        private int scoreLineWidthMax = 230;
        private float scoreLineWidth;
        private float scoreSegmentWidth = (float) scoreLineWidthMax / (float) MAX_SCORE;

        public ScoreUI() {

            fontScoreText = new BitmapFont(Gdx.files.internal("fonts/font2.fnt"));
            fontScoreText.setScale(.6f, .6f);
            fontScoreText.setColor(74/255f,176/255f,255/255f,1);
            textScoreText = String.valueOf(MAX_SCORE);
            positionScoreText = new Vector2(245, 401);

            Pixmap.setBlending(Pixmap.Blending.None);

            scoreLineWidth = scoreLineWidthMax;
            pixmapScoreLine = new Pixmap((int)scoreLineWidth, 10, Pixmap.Format.RGB888);
            prepareScoreLine();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(textureScoreLine, 10, 390);
            fontScoreText.draw(batch, textScoreText, positionScoreText.x, positionScoreText.y);
        }

        public void onScored(int score) {
            scoreLineWidth = scoreLineWidthMax - (scoreSegmentWidth * score);
            prepareScoreLine();
            textScoreText = String.valueOf(MAX_SCORE - score);
            positionScoreText.x = (int) scoreLineWidth + 15;
        }

        private void prepareScoreLine() {
            pixmapScoreLine.setColor(Color.BLACK);
            pixmapScoreLine.fill();
            pixmapScoreLine.setColor(74/255f,176/255f,255/255f,1);
            pixmapScoreLine.fillRectangle(0, 4, (int) scoreLineWidth, 2);
            pixmapScoreLine.fillRectangle(0, 0, 2, 10);
            pixmapScoreLine.fillRectangle((int) scoreLineWidth - 2, 0, 2, 10);
            textureScoreLine = new Texture(pixmapScoreLine, Pixmap.Format.RGB888, false);
        }


    }

/*================================================================================================*/
// PlayerName Actor
/*================================================================================================*/
    public class PlayerName extends Actor {

        private BitmapFont font;
        private String text;
        private Vector2 position;

        public PlayerName(String pName, int i) {

            font = new BitmapFont(Gdx.files.internal("fonts/font2.fnt"));
            font.setScale(.6f, .6f);

            switch (i) {
                case 1:
                    position = new Vector2(10, 70);
                    font.setColor(255/255f, 252/255f, 0/255f,1);
                    break;
                case 2:
                    position = new Vector2(150, 70);
                    font.setColor(255/255f, 0/255f, 0/255f,1);
                    break;
                case 3:
                    position = new Vector2(10, 40);
                    font.setColor(255/255f, 153/255f, 0/255f,1);
                    break;
            }
            if (pName.length() >= 12) {
                text = pName.substring(0, 12);
            } else {
                text = pName;
            }

        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            font.draw(batch, text, position.x, position.y);
        }
    }
}
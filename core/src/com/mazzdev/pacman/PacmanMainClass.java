package com.mazzdev.pacman;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

public class PacmanMainClass implements ApplicationListener {

    private static int VIRTUAL_WIDTH = 28;
    private static int VIRTUAL_HEIGHT = 31;
    public static final float SCALE = 1 / 8f;

    private int nPlayers;
    private String playerName;
    private List<String> pNames;

    public GameStage gameStage;
    public UIStage uiStage;

    private ActionResolver actionResolver;
    private SpriteBatch spriteBatch;
    private Viewport viewport;

    private OrthogonalTiledMapRenderer render;

    FPSLogger fpsLogger;

    public PacmanMainClass(ActionResolver actionResolver, int nPlayers, String playerName,
                           List<String> pNames) {
        this.actionResolver = actionResolver;
        this.nPlayers = nPlayers;
        this.playerName = playerName;
        this.pNames = pNames;

        fpsLogger = new FPSLogger();
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        uiStage = new UIStage(pNames);

        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        gameStage = new GameStage(viewport, actionResolver, uiStage, nPlayers, playerName);
        render = new OrthogonalTiledMapRenderer(gameStage.getMap(), SCALE);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(((OrthographicCamera) viewport.getCamera()).combined);

        render.setView((OrthographicCamera) viewport.getCamera());

        render.render();

        gameStage.act();
        gameStage.draw();

        uiStage.act();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

}
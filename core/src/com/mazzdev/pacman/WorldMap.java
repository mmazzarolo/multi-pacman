package com.mazzdev.pacman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Iterator;

/**
 * Created by Matteo on 24/12/2014.
 */
public class WorldMap {
    private TiledMap map;
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };
    private Array<Rectangle> wallTiles = new Array<Rectangle>();
    private Array<Rectangle> collectableTiles = new Array<Rectangle>();
    private StaticTiledMapTile emptyTile;

    private int nPlayers;

    public WorldMap(int nPlayers) {

        TmxMapLoader loader = new TmxMapLoader();
        TmxMapLoader.Parameters par = new TmxMapLoader.Parameters();
//        par.textureMinFilter = Texture.TextureFilter.Linear;
//        par.textureMagFilter = Texture.TextureFilter.Linear;
        map = loader.load("maps/mapg.tmx", par);
        this.nPlayers = nPlayers;

        extractEmptyTile();
        extractWalls();
        extractCollectables();
//        setPlayersInfo();
    }

    private void setPlayersInfo() {
        map.getLayers().get("Player1").setVisible(true);
        if (nPlayers >= 2) {
            map.getLayers().get("Player2").setVisible(true);
        } else {
            map.getLayers().get("Player2").setVisible(false);
        }

        if (nPlayers >= 3){
            map.getLayers().get("Player3").setVisible(true);
        } else {
            map.getLayers().get("Player3").setVisible(false);
        }
    }

    private void extractEmptyTile() {
        Iterator<TiledMapTile> tileset = map.getTileSets().getTileSet("maptileset").iterator();
        emptyTile = null;
        while (tileset.hasNext()) {
            TiledMapTile tilee = tileset.next();
            if (tilee.getProperties().containsKey("Empty") && tilee.getProperties().get("Empty", String.class).equals("true")) {
                emptyTile = (StaticTiledMapTile) tilee;
            }
        }
    }

    private void extractWalls() {
        TiledMapTileLayer wallsLayer = (TiledMapTileLayer) map.getLayers().get("Walls");
        rectPool.freeAll(wallTiles);
        wallTiles.clear();

        for (int y = 0; y < wallsLayer.getHeight(); y++) {
            for (int x = 0; x < wallsLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = wallsLayer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    wallTiles.add(rect);
                }
            }
        }
    }

    private void extractCollectables() {
        TiledMapTileLayer collectableLayer = (TiledMapTileLayer) map.getLayers().get("Collectables");
        rectPool.freeAll(collectableTiles);
        collectableTiles.clear();

        for (int y = 0; y < collectableLayer.getHeight(); y++) {
            for (int x = 0; x < collectableLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = collectableLayer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    collectableTiles.add(rect);
                }
            }
        }
    }

    public TiledMapTile getTile(int x, int y, String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        return layer.getCell(x, y).getTile();
    }

    public void setTileAsEmpy(int x, int y, String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        layer.getCell(x, y).setTile(emptyTile);
    }

    /* Generated getters and setters */

    public Array<Rectangle> getWallTiles() { return wallTiles; }

    public Array<Rectangle> getCollectableTiles() { return collectableTiles; }

    public TiledMap getMap() { return map; }
}

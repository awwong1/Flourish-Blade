package com.spiritatlas.flourishblade.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FringeLayer {

    private class FringeTile implements Comparable<FringeTile> {
        TiledMapTileLayer.Cell cell;
        int x, y;
        float yOffset;

        FringeTile(TiledMapTileLayer.Cell cell, int x, int y, float yoffset) {
            this.cell = cell;
            this.x = x;
            this.y = y;
            this.yOffset = yoffset;
        }

        float height() {
            return y + yOffset;
        }

        @Override
        public int compareTo(FringeTile o) {
            if (o.height() > height()) {
                return 1;
            } else if (o.height() < height()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private final TiledMap map;
    private final TiledMapTileLayer layer;
    private final List<FringeTile> tiles;

    private int currentInd;

    public FringeLayer(TiledMap map, TiledMapTileLayer fringeLayer) {
        this.map = map;
        this.layer = fringeLayer;
        this.tiles = new ArrayList<FringeTile>();

        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null) continue;
                String yoffsetStr = cell.getTile().getProperties().get("yOffset", String.class);

                if (yoffsetStr == null || yoffsetStr.isEmpty()) {
                    // System.out.println("Warning - Fringe tile at [" + x + ", " + y + "] missing property <yOffset>");
                } else {
                    try {
                        tiles.add(new FringeTile(
                                cell,
                                x, y,
                                Float.parseFloat(yoffsetStr)));
                    } catch (NumberFormatException e) {
                        System.err.println("Can't parse \"" + yoffsetStr + "\" as float: " + e);
                    }
                }
            }
        }
        Collections.sort(tiles);
    }

    public void begin() {
        currentInd = 0;
    }

    public void renderTill(SpriteBatch batch, float y) {
        for (int i = currentInd; i < tiles.size(); i++) {
            FringeTile tile = tiles.get(i);
            currentInd = i;
            if (tile.height() < y) {
                return;
            }
            batch.draw(tile.cell.getTile().getTextureRegion(), tile.x, tile.y, 1f, 1f);
        }
        // Only if all tiles were drawn (see return; above)
        currentInd++;
    }

    public void end(SpriteBatch batch) {
        for (int i = currentInd; i < tiles.size(); i++) {
            FringeTile tile = tiles.get(i);
            batch.draw(tile.cell.getTile().getTextureRegion(), tile.x, tile.y, 1f, 1f);
        }
    }

}



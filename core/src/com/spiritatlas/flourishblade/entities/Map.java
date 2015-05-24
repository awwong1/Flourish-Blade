package com.spiritatlas.flourishblade.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.spiritatlas.flourishblade.util.MapCollisionBuilder;
import com.spiritatlas.flourishblade.util.Physics;

public class Map implements Disposable {

    // Disposable
    private boolean disposed = false;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final int fringeLayerIndex;
    private final int[] belowEntities;
    private final int[] aboveEntities;
    private final FringeLayer fringeLayer;

    public Map(String mapfilePath) {
        this(mapfilePath, null);
    }

    public Map(String mapfilePath, Physics physics) {
        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        map = tmxMapLoader.load(mapfilePath);
        this.renderer = new OrthogonalTiledMapRenderer(map,
                1f / Float.parseFloat(map.getProperties().get("tilewidth").toString()));

        fringeLayerIndex = computeEntityLayer();
        if (fringeLayerIndex == 999) {
            belowEntities = new int[map.getLayers().getCount()];
            aboveEntities = new int[0];
            fillCounting(0, belowEntities);
            fringeLayer = null;
        } else {
            belowEntities = new int[fringeLayerIndex];
            aboveEntities = new int[map.getLayers().getCount() - fringeLayerIndex - 1];
            fillCounting(0, belowEntities);
            fillCounting(fringeLayerIndex + 1, aboveEntities);
            MapLayer mapLayer = map.getLayers().get(fringeLayerIndex);
            if (mapLayer instanceof TiledMapTileLayer) {
                fringeLayer = new FringeLayer(map, (TiledMapTileLayer) mapLayer);
            } else {
                fringeLayer = null;
            }
        }
        MapCollisionBuilder.buildShapes(map, 32, physics.getWorld());
    }

    private void fillCounting(int start, int[] array) {
        for (int i = 0, val = start; i < array.length; i++, val++) {
            array[i] = val;
        }
    }

    private int computeEntityLayer() {
        for (int i = 0; i < map.getLayers().getCount(); i++) {
            if (map.getLayers().get(i).getName().equalsIgnoreCase("fringe")) {
                return i;
            }
        }
        return 999;
    }

    public FringeLayer getFringeLayer() {
        return fringeLayer;
    }

    public void renderBelowEntities(OrthographicCamera cam) {
        renderer.setView(cam);
        renderer.render(belowEntities);
    }

    public void renderAboveEntities(OrthographicCamera cam) {
        renderer.setView(cam);
        renderer.render(aboveEntities);
    }

    /* (non-Javadoc)
     * @see com.badlogic.gdx.utils.Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (!disposed) {
            renderer.dispose();
            map.dispose();
            disposed = true;
        }
    }
}

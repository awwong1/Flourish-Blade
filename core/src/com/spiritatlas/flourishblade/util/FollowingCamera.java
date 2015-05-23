package com.spiritatlas.flourishblade.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.spiritatlas.flourishblade.FlourishBlade;
import com.spiritatlas.flourishblade.entities.Entity;

public class FollowingCamera {
    private final OrthographicCamera camera;
    private final float pixPerMeter;

    public Entity following;

    public FollowingCamera(float pixPerMeter) {
        float screenw = FlourishBlade.V_WIDTH;
        float screenh = FlourishBlade.V_HEIGHT;
        this.camera = new OrthographicCamera(screenw / pixPerMeter, screenh / pixPerMeter);
        this.pixPerMeter = pixPerMeter;
    }

    public void update() {
        if (following != null) {
            Vector2 position = following.getPosition();
            camera.position.set(position.x, position.y, 0);
        }
        camera.update();
    }

    public void resize(float width, float height) {
        camera.viewportWidth = width / pixPerMeter;
        camera.viewportHeight = height / pixPerMeter;
    }

    public void loadToBatch(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}

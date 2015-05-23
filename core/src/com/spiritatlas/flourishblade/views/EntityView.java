package com.spiritatlas.flourishblade.views;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.spiritatlas.flourishblade.entities.Entity;

public abstract class EntityView {

    public abstract void draw(SpriteBatch batch, Entity e, float delta);

    public void draw(SpriteBatch batch, Entity e, Sprite sprite, float width, float xoffset, float yoffset) {
        Body body = e.getBody();
        // Super-Duper important Box2D-Magic code:
        // This should be / is in almost every Box2D project
        // It takes the Body and the associated sprite and
        // renders the sprite properly, using the body's
        // position, rotation and origin.
        final float worldToSprite = sprite.getWidth() / width;
        final float spriteToWorld = width / sprite.getWidth();
        // Get body position:
        final float bodyX = e.getX();
        final float bodyY = e.getY();
        // Get body center:
        final Vector2 center = body.getLocalCenter();
        final Vector2 massCenter = body.getMassData().center;
        center.sub(massCenter).add(xoffset, yoffset);
        // Compute sprite-space center:
        final Vector2 spriteCenter = new Vector2(sprite.getWidth() / 2,
                sprite.getHeight() / 2).sub((center.cpy().scl(worldToSprite)));
        // Upload to sprite:
        sprite.setScale(1f * spriteToWorld);
        sprite.setRotation(e.getRotation() * MathUtils.radiansToDegrees);
        sprite.setOrigin(spriteCenter.x, spriteCenter.y);
        sprite.setPosition(
                bodyX - spriteCenter.x,
                bodyY - spriteCenter.y);
        // Draw Sprite:
        sprite.draw(batch);
    }

}

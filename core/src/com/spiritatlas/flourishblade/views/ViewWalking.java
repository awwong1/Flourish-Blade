package com.spiritatlas.flourishblade.views;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.spiritatlas.flourishblade.entities.Entity;
import com.spiritatlas.flourishblade.util.AnimationHelper;
import com.spiritatlas.flourishblade.util.Direction;
import com.spiritatlas.flourishblade.util.SpriteAnimation;

public class ViewWalking extends EntityView {
    private final Sprite sprite;
    private final float width;
    private final float xoffset;
    private final float yoffset;
    private final AnimationHelper anims;

    private int direction;
    private boolean moving;

    public ViewWalking(float width, float xoffset, float yoffset, SpriteAnimation[] spriteAnimations) {
        this.width = width;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
        this.anims = new AnimationHelper();

        anims.set(spriteAnimations);
        this.sprite = new Sprite(anims.getKeyframe());
    }

    @Override
    public void draw(SpriteBatch batch, Entity e, float delta) {
        Vector2 linVel = e.getBody().getLinearVelocity();

        if (linVel.len() > 0.3f)
            direction = Direction.getDirection(linVel.x, linVel.y);
        moving = linVel.len() > 0.1f;

        anims.setDirection(direction);
        anims.setMoving(moving);
        anims.setDeltaSpeed(delta);
        anims.apply(e);

        sprite.setRegion(anims.getKeyframe());
        draw(batch, e, sprite, width, xoffset, yoffset);
    }
}
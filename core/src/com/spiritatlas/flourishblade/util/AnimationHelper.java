package com.spiritatlas.flourishblade.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.spiritatlas.flourishblade.entities.Entity;

public class AnimationHelper extends Component {
    private SpriteAnimation[] animations;
    private boolean moving;
    private int direction;
    private float deltaSpeed;

    public void set(SpriteAnimation[] animations) {
        if (animations.length != 4) {
            throw new IllegalArgumentException("Need four animations. Got " + animations.length + " instead.");
        }
        this.animations = animations;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setDirection(int direction) {
        Direction.validCheck(direction);
        this.direction = direction;
    }

    public void setDeltaSpeed(float deltaSpeed) {
        this.deltaSpeed = deltaSpeed;
    }

    public TextureRegion getKeyframe() {
        return animations[direction].getCurrentKeyframe();
    }

    @Override
    public void apply(Entity entity) {
        if (moving) {
            animations[direction].tick(deltaSpeed);
        } else {
            for (SpriteAnimation anim : animations) {
                anim.reset();
            }
        }
    }
}


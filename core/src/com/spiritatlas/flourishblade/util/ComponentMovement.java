package com.spiritatlas.flourishblade.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.spiritatlas.flourishblade.entities.Entity;

public class ComponentMovement extends Component {

    private boolean moving;
    private int direction;

    private float strength;
    private float maxspeed;
    private float friction;
    private float xsteer;
    private float ysteer;

    public ComponentMovement(int startDir) {
        this.direction = startDir;
    }

    public void set(float strength, float maxspeed, float friction, float xsteer, float ysteer) {
        this.strength = strength;
        this.maxspeed = maxspeed;
        this.friction = friction;
        this.xsteer = xsteer;
        this.ysteer = ysteer;
    }

    public void set(float strength, float maxspeed, float friction) {
        this.strength = strength;
        this.maxspeed = maxspeed;
        this.friction = friction;
    }

    public void setSteer(float xsteer, float ysteer) {
        this.xsteer = xsteer;
        this.ysteer = ysteer;
    }

    public void setSteer(float strength, float xsteer, float ysteer) {
        this.strength = strength;
        this.xsteer = xsteer;
        this.ysteer = ysteer;
    }

    public void setMaxspeed(float maxspeed) {
        this.maxspeed = maxspeed;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    @Override
    public void apply(Entity entity) {
        Body body = entity.getBody();
        moving = false;

        // If trying to move (pressing buttons on Keyboard, steering with Gamepad)
        if (xsteer != 0f || ysteer != 0f) {
            moving = true;

            if (Math.abs(xsteer) > Math.abs(ysteer)) {
                if (xsteer < 0) {
                    direction = Direction.LEFT;
                } else {
                    direction = Direction.RIGHT;
                }
            } else {
                if (ysteer < 0) {
                    direction = Direction.DOWN;
                } else {
                    direction = Direction.UP;
                }
            }
        }

        Vector2 linVel = body.getLinearVelocity();
        if (linVel.len() > maxspeed) {
            body.setLinearVelocity(linVel.cpy().nor().scl(maxspeed));
        }

        body.applyForceToCenter(strength * xsteer, strength * ysteer, true);

        if (friction > 1f && !moving) {
            body.setLinearVelocity(xsteer * strength, ysteer * strength);
            //body.setLinearVelocity(linVel.div(friction));
        }
    }

    public boolean isMoving() {
        return moving;
    }

    public int getDirection() {
        return direction;
    }

}

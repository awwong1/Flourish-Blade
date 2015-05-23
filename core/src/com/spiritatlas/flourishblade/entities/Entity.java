package com.spiritatlas.flourishblade.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Entity implements Comparable<Entity> {

    protected final Body body;

    public Entity(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public float getRotation() {
        return body.getAngle();
    }


    @Override
    public int compareTo(Entity that) {
        return (int) (that.getY() * 1000f - this.getY() * 1000f);
    }

    public String toString() {
        return String.format(
                "[Entity]:\n" +
                        "\tpos: %s\n" +
                        "\trot: %G\n" +
                        "\tvel: %s\n" +
                        "\trot-vel: %G",
                getPosition().toString(),
                getRotation(),
                getBody().getLinearVelocity().toString(),
                getBody().getAngularVelocity()
        );
    }
}
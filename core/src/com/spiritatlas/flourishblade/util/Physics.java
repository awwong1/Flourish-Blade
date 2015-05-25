package com.spiritatlas.flourishblade.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

public class Physics implements ContactListener, Disposable {

    protected final World world;
    public float timescale = 1f;
    private boolean haveEvent = false;
    private MapProperties mapProperties;

    public Physics(Vector2 gravity, boolean sleep) {
        world = new World(gravity, sleep);
        world.setContactListener(this);
    }

    public World getWorld() {
        return world;
    }

    public void step(float delta) {
        world.step(Math.min(delta, 0.02f) * timescale, 4, 4);
    }

    public void step() {
        step(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    /* (non-Javadoc)
     * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
     */
    @Override
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if (fa == null || fb == null) {
            return;
        }
        Gdx.app.log("Physics", fa.toString() + ", " + fb.toString());
        if (fa.getUserData() != null) {
            haveEvent = true;
            mapProperties = (MapProperties) fa.getUserData();
        }
        if (fb.getUserData() != null) {
            haveEvent = true;
            mapProperties = (MapProperties) fb.getUserData();
        }
    }

    /* (non-Javadoc)
     * @see com.badlogic.gdx.physics.box2d.ContactListener#endContact(com.badlogic.gdx.physics.box2d.Contact)
     */
    @Override
    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if (fa == null || fb == null) {
            return;
        }
        if (fa.getUserData() != null) {
            haveEvent = false;
            mapProperties = null;
        }
        if (fb.getUserData() != null) {
            haveEvent = false;
            mapProperties = null;
        }
    }

    /* (non-Javadoc)
     * @see com.badlogic.gdx.physics.box2d.ContactListener#postSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.ContactImpulse)
     */
    @Override
    public void postSolve(Contact c, ContactImpulse ci) {
    }

    /* (non-Javadoc)
     * @see com.badlogic.gdx.physics.box2d.ContactListener#preSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.Manifold)
     */
    @Override
    public void preSolve(Contact c, Manifold m) {
        Fixture fixtureA = c.getFixtureA();
        Fixture fixtureB = c.getFixtureB();

        handleFixture(fixtureA, fixtureB, c, m);
        handleFixture(fixtureB, fixtureA, c, m);
    }

    public void handleFixture(Fixture fixture, Fixture other, Contact c, Manifold m) {
        Object obj = fixture.getUserData();
        if (obj instanceof Collidable) {
            ((Collidable) obj).collide(other, c, m);
        }
    }

    public boolean isHaveEvent() {
        return haveEvent;
    }

    public MapProperties getMapProperties() {
        return mapProperties;
    }

}

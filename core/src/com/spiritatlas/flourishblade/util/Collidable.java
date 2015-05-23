package com.spiritatlas.flourishblade.util;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface Collidable {

    public void collide(Fixture other, Contact contact, Manifold manifold);

}

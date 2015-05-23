package com.spiritatlas.flourishblade.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.spiritatlas.flourishblade.util.SpriteAnimation;
import com.spiritatlas.flourishblade.views.EntityView;
import com.spiritatlas.flourishblade.views.ViewWalking;

public class PlayerEntity extends Entity {
    protected final EntityView view;

    public static final String cSpritesheetKey = "CharacterSpritesheet";

    private boolean disposed = false;

    public PlayerEntity(Body body, EntityView view) {
        super(body);
        this.view = view;
    }

    public EntityView getView() {
        return view;
    }

    public static EntityView createView(Texture tex) {
        SpriteAnimation walkingDown = new SpriteAnimation(tex,
                new float[]{0.08f, 0.08f, 0.08f, 0.08f}, 0.08f * 4.0f, new TextureRegion[]{
                new TextureRegion(tex, 32, 0, 32, 32), new TextureRegion(tex, 64, 0, 32, 32),
                new TextureRegion(tex, 32, 0, 32, 32), new TextureRegion(tex, 0, 0, 32, 32)}, "walkingDown"
        );
        SpriteAnimation walkingLeft = new SpriteAnimation(tex,
                new float[]{0.08f, 0.08f, 0.08f, 0.08f}, 0.08f * 4.0f, new TextureRegion[]{
                new TextureRegion(tex, 32, 32, 32, 32), new TextureRegion(tex, 64, 32, 32, 32),
                new TextureRegion(tex, 0, 32, 32, 32), new TextureRegion(tex, 32, 32, 32, 32)}, "walkingLeft"
        );
        SpriteAnimation walkingRight = new SpriteAnimation(tex,
                new float[]{0.08f, 0.08f, 0.08f, 0.08f}, 0.08f * 4.0f, new TextureRegion[]{
                new TextureRegion(tex, 32, 64, 32, 32), new TextureRegion(tex, 64, 64, 32, 32),
                new TextureRegion(tex, 32, 64, 32, 32), new TextureRegion(tex, 0, 64, 32, 32)}, "walkingRight"
        );

        SpriteAnimation walkingUp = new SpriteAnimation(tex,
                new float[]{0.08f, 0.08f, 0.08f, 0.08f}, 0.08f * 4.0f, new TextureRegion[]{
                new TextureRegion(tex, 32, 96, 32, 32), new TextureRegion(tex, 64, 96, 32, 32),
                new TextureRegion(tex, 32, 96, 32, 32), new TextureRegion(tex, 0, 96, 32, 32)}, "walkingUp"
        );
        return new ViewWalking(1f, 0f, 0.2f, new SpriteAnimation[]{walkingRight, walkingUp, walkingLeft, walkingDown});
    }
}

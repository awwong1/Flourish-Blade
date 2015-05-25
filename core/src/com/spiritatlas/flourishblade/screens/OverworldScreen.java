package com.spiritatlas.flourishblade.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spiritatlas.flourishblade.FlourishBlade;
import com.spiritatlas.flourishblade.entities.FringeLayer;
import com.spiritatlas.flourishblade.entities.Map;
import com.spiritatlas.flourishblade.entities.PlayerEntity;
import com.spiritatlas.flourishblade.handlers.GameState;
import com.spiritatlas.flourishblade.util.*;

public class OverworldScreen extends AbstractScreen {
    private final ResourceLoader resourceLoader;
    private final Physics physics;
    private final Map map;
    private final PlayerEntity player;
    private final OrthographicCamera hudCamera;

    private final Touchpad touchpad;
    private final ComponentMovement movement;

    // TODO: Remove this from the final game
    public final Box2DDebugRenderer debugRenderer;

    public OverworldScreen(final ResourceLoader resources, final FlourishBlade game, final GameState gameState) {
        super(new Stage(new FitViewport(FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT)), game);
        this.resourceLoader = resources;
        resourceLoader.loadTexture("spritesheets/character.png", ResourceLoader.PLAYER_SPRITESHEET);
        physics = new Physics(new Vector2(0, 0), true);
        map = new Map(gameState.getMapFilePath(), physics);
        player = createPlayer(gameState.getCharacterPosition());
        game.getCamera().following = player;

        touchpad = createTouchpad();
        stage.addActor(touchpad);
        createUIButtons();

        movement = createMovement();

        this.hudCamera = new OrthographicCamera(stage.getWidth(), stage.getHeight());
        hudCamera.viewportWidth = Gdx.graphics.getWidth();
        hudCamera.viewportHeight = Gdx.graphics.getHeight();

        debugRenderer = new Box2DDebugRenderer();
    }

    private PlayerEntity createPlayer(Vector2 characterPosition) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(characterPosition);
        bdef.angle = 0;
        bdef.linearVelocity.set(0, 0);
        bdef.angularVelocity = 0;
        bdef.linearDamping = 0.99f;
        bdef.angularDamping = 0;
        bdef.allowSleep = true;
        bdef.awake = true;
        bdef.fixedRotation = true;
        bdef.bullet = false;
        bdef.active = true;
        bdef.gravityScale = 1;

        FixtureDef fdef = new FixtureDef();
        fdef.shape = new CircleShape();
        fdef.shape.setRadius(0.3f);
        fdef.friction = 0.2f;
        fdef.restitution = 0.1f;
        fdef.density = 1;
        fdef.isSensor = false;

        Body body = physics.getWorld().createBody(bdef);
        body.createFixture(fdef);

        MassData md = body.getMassData();
        md.mass = 1;
        body.setMassData(md);

        Texture tex = resourceLoader.getTexture(ResourceLoader.PLAYER_SPRITESHEET);
        return new PlayerEntity(body, PlayerEntity.createView(tex));
    }

    private Touchpad createTouchpad() {
        String touchKnobId = "touchKnob";
        String touchBackgroundId = "touchBackground";

        Skin touchpadSkin = new Skin();

        touchpadSkin.add(touchBackgroundId,
                new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 335, 38, 35, 38));
        touchpadSkin.add(touchKnobId,
                new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 365, 483, 17, 17));

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        Drawable touchBackground = touchpadSkin.getDrawable(touchBackgroundId);
        Drawable touchKnob = touchpadSkin.getDrawable(touchKnobId);
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        Touchpad tpad = new Touchpad(10, touchpadStyle);
        tpad.setBounds(15, 15, 150, 150);
        return tpad;
    }

    private void createUIButtons() {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(
                new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 290, 0, 45, 49)
        );
        style.imageDown = new TextureRegionDrawable(
                new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 290, 139, 45, 45)
        );
        ImageButton actionButton = new ImageButton(style);
        actionButton.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return false;
            }
        });
        actionButton.setPosition(stage.getWidth() - actionButton.getWidth() - 50, 50);
        stage.addActor(actionButton);
    }

    private ComponentMovement createMovement() {
        ComponentMovement movement = new ComponentMovement(Direction.DOWN);
        movement.set(16, 6, 1.5f);
        return movement;
    }

    @Override
    public void tick(float delta) {
        movement.setSteer(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
        movement.apply(player);
        physics.step(delta);
        stage.act(delta);
        game.getCamera().update();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        game.getCamera().loadToBatch(spriteBatch);
        map.renderBelowEntities(game.getCamera().getCamera());
        spriteBatch.begin();

        FringeLayer fringeLayer = map.getFringeLayer();
        fringeLayer.begin();
        fringeLayer.renderTill(spriteBatch, player.getY());
        player.getView().draw(spriteBatch, player, Gdx.graphics.getDeltaTime());
        fringeLayer.end(spriteBatch);

        spriteBatch.end();
        map.renderAboveEntities(game.getCamera().getCamera());
        drawHUD(spriteBatch);

        debugRenderer.render(physics.getWorld(), game.getCamera().getCamera().combined);
    }

    private void drawHUD(SpriteBatch spriteBatch) {
        hudCamera.update();
        stage.draw();
        spriteBatch.setProjectionMatrix(hudCamera.projection);
        spriteBatch.setTransformMatrix(hudCamera.view);
        float x = -Gdx.graphics.getWidth() / 2f + 5f;
        float y = Gdx.graphics.getHeight() / 2f - 5f;
        spriteBatch.begin();
        resourceLoader.getFont(ResourceLoader.FONT).draw(
                spriteBatch, "fps: " + Gdx.graphics.getFramesPerSecond(), x, y);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        hudCamera.viewportWidth = Gdx.graphics.getWidth();
        hudCamera.viewportHeight = Gdx.graphics.getHeight();
        touchpad.setBounds(15, 15, 150, 150);
    }

    @Override
    public boolean isParentVisible() {
        return false;
    }

    @Override
    public boolean darkenScreen() {
        return true;
    }

    @Override
    public void dispose() {
        map.dispose();
        resourceLoader.removeTexture(ResourceLoader.PLAYER_SPRITESHEET);
        resourceLoader.removeTexture(ResourceLoader.PLAYER_SWORD);
    }
}

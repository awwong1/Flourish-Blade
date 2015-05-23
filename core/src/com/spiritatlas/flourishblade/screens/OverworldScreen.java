package com.spiritatlas.flourishblade.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spiritatlas.flourishblade.FlourishBlade;
import com.spiritatlas.flourishblade.entities.PlayerEntity;
import com.spiritatlas.flourishblade.handlers.GameState;
import com.spiritatlas.flourishblade.util.Physics;
import com.spiritatlas.flourishblade.util.ResourceLoader;

public class OverworldScreen extends AbstractScreen {
    private final ResourceLoader resourceLoader;
    private final Physics physics;
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    private final PlayerEntity player;
    private final OrthographicCamera hudCamera;

    public OverworldScreen(final ResourceLoader resources, final FlourishBlade game, final GameState gameState) {
        super(new Stage(new FitViewport(FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT)), game);
        this.resourceLoader = resources;
        resourceLoader.loadTexture("spritesheets/character.png", ResourceLoader.PLAYER_SPRITESHEET);
        physics = new Physics(new Vector2(0, 0), true);

        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load(gameState.getMapFilePath());

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,
                1f / Float.parseFloat(tiledMap.getProperties().get("tilewidth").toString()));
        player = createPlayer(gameState.getCharacterPosition());
        game.getCamera().following = player;

        this.hudCamera = new OrthographicCamera(stage.getWidth(), stage.getHeight());
        hudCamera.viewportWidth = Gdx.graphics.getWidth();
        hudCamera.viewportHeight = Gdx.graphics.getHeight();
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

    @Override
    public void tick(float delta) {
        physics.step(delta);
        stage.act(delta);
        game.getCamera().update();
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        game.getCamera().loadToBatch(spriteBatch);
        tiledMapRenderer.setView(game.getCamera().getCamera());
        tiledMapRenderer.render();
        spriteBatch.begin();
        player.getView().draw(spriteBatch, player, Gdx.graphics.getDeltaTime());
        spriteBatch.end();
        drawHUD(spriteBatch);
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
        tiledMap.dispose();
    }
}

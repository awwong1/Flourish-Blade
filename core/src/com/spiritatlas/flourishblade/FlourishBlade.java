package com.spiritatlas.flourishblade;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.spiritatlas.flourishblade.handlers.GameSettings;
import com.spiritatlas.flourishblade.screens.AbstractScreen;
import com.spiritatlas.flourishblade.screens.SplashScreen;
import com.spiritatlas.flourishblade.util.FollowingCamera;
import com.spiritatlas.flourishblade.util.ResourceLoader;

import java.util.Stack;

public class FlourishBlade extends Game implements InputProcessor {
    public static final String TITLE = "Flourish Blade";
    public static final int V_WIDTH = 640;
    public static final int V_HEIGHT = 480;

    public static final float PIX_PER_METER = 32f / 1f;

    /* Resource Loader Strings */
    private final Color darkenColor = new Color(0.1f, 0.1f, 0.1f, 0.8f);

    private SpriteBatch spriteBatch;
    private FollowingCamera camera;

    private ResourceLoader resourceLoader;
    private Stack<AbstractScreen> screens;
    private Stack<AbstractScreen> drawStack;
    private GameSettings gameSettings;
    private Texture whitePixelTexture;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        screens = new Stack<AbstractScreen>();
        drawStack = new Stack<AbstractScreen>();
        gameSettings = GameSettings.loadSettings();
        createWhitePixelTexture();
        setupCameras();
        handleResources();
        pushScreen(new SplashScreen(resourceLoader, this));
        Gdx.input.setInputProcessor(this);
    }

    private void handleResources() {
        resourceLoader = new ResourceLoader();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Munro.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        resourceLoader.setFont(font, ResourceLoader.FONT);
        resourceLoader.loadTexture("spritesheets/ui.png", ResourceLoader.UI_TEX);
        resourceLoader.loadSound("sfx/select.wav", ResourceLoader.CLICK);
    }

    private void createWhitePixelTexture() {
        Pixmap whitePixel = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePixel.setColor(Color.WHITE);
        whitePixel.fillRectangle(0, 0, 1, 1);
        whitePixelTexture = new Texture(whitePixel, Pixmap.Format.RGB888, false);
    }

    private void setupCameras() {
        camera = new FollowingCamera(PIX_PER_METER);
    }

    @Override
    public void render() {
        Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
        Gdx.gl20.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        prepareDrawStack();
        updateDrawStack(Gdx.graphics.getDeltaTime());
    }

    private void prepareDrawStack() {
        for (int i = screens.size() - 1; i >= 0; i--) {
            AbstractScreen screen = screens.get(i);
            drawStack.push(screen);
            if (!screen.isParentVisible()) {
                break;
            }
        }
    }

    private void updateDrawStack(float delta) {
        while (drawStack.size() > 1) {
            AbstractScreen screen = drawStack.pop();
            Stage stage = screen.getStage();
            // If it is the last element, it is the topmost screen
            stage.getBatch().setColor(Color.WHITE);
            screen.update(delta);
        }
        AbstractScreen topScreen = drawStack.pop();
        if (topScreen.darkenScreen()) {
            Stage stage = topScreen.getStage();
            stage.getBatch().enableBlending();
            stage.getBatch().setColor(darkenColor);
            stage.getBatch().begin();
            stage.getBatch().draw(whitePixelTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.getBatch().end();
            stage.getBatch().setColor(Color.WHITE);
        }
        topScreen.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.gl20.glViewport(0, 0, width, height);
        for (AbstractScreen screen : screens) {
            screen.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        resourceLoader.removeAll();
    }

    public AbstractScreen popScreen() {
        AbstractScreen popped = screens.pop();
        popped.dispose();
        return popped;
    }

    public void pushScreen(AbstractScreen screen) {
        screens.add(screen);
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public FollowingCamera getCamera() {
        return camera;
    }

    public boolean shouldHaveInputFocus(AbstractScreen screen) {
        return screens.peek() == screen;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    /* Input methods, refer to the top screen in the screens stack. */

    @Override
    public boolean keyDown(int keycode) {
        return screens.peek().keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return screens.peek().keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return screens.peek().keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return screens.peek().touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return screens.peek().touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return screens.peek().touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return screens.peek().mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return screens.peek().scrolled(amount);
    }
}

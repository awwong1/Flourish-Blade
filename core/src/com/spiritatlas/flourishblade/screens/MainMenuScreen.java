package com.spiritatlas.flourishblade.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spiritatlas.flourishblade.FlourishBlade;
import com.spiritatlas.flourishblade.util.ResourceLoader;

public class MainMenuScreen extends AbstractScreen {
    private final ResourceLoader resourceLoader;

    private Image flourishBladeLogo;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    public MainMenuScreen(final ResourceLoader resources, final FlourishBlade game) {
        super(new Stage(new FitViewport(FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT)), game);
        this.resourceLoader = resources;
        resourceLoader.loadTexture("images/flourishblade.png", ResourceLoader.FLOURISH_BLADE_LOGO);
        resourceLoader.loadMusic("bgm/main_theme.wav", ResourceLoader.MUSIC_MAIN_THEME);
        handleMusic();
        handleMainMenuUI();
        handleBackgroundMap();
    }

    private void handleMusic() {
        Music mainTheme = resourceLoader.getMusic(ResourceLoader.MUSIC_MAIN_THEME);
        mainTheme.setLooping(true);
        mainTheme.setVolume(game.getGameSettings().getBgmVolume());
        mainTheme.play();
    }

    private void handleMainMenuUI() {
        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle();
        buttonStyle.font = resourceLoader.getFont(ResourceLoader.FONT);
        TextureRegion buttonUp = new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 0, 282, 190, 49);
        TextureRegion buttonDown = new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 0, 237, 190, 46);

        buttonStyle.imageUp = new TextureRegionDrawable(buttonUp);
        buttonStyle.imageDown = new TextureRegionDrawable(buttonDown);
        buttonStyle.pressedOffsetY = -3;

        ImageTextButton continueGameButton = new ImageTextButton("Continue", buttonStyle);
        continueGameButton.clearChildren();
        Stack stack = new Stack();
        stack.add(continueGameButton.getImage());
        stack.add(continueGameButton.getLabel());
        continueGameButton.add(stack);
        continueGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                //resourceLoader.getMusic(ResourceKeys.MUSIC_MAIN_THEME).stop();
                //game.popScreen();
                //game.pushScreen(new TextOverlayScreen(resourceLoader, game));
            }
        });

        ImageTextButton newGameButton = new ImageTextButton("New Game", buttonStyle);
        newGameButton.clearChildren();
        stack = new Stack();
        stack.add(newGameButton.getImage());
        stack.add(newGameButton.getLabel());
        newGameButton.add(stack);
        newGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                // resourceLoader.getMusic(ResourceLoader.MUSIC_MAIN_THEME).stop();
                // game.popScreen();
                //game.pushScreen(new OverworldScreen(resourceLoader, game, new GameState(resourceLoader, game)));
            }
        });

        ImageTextButton settingsButton = new ImageTextButton("Settings", buttonStyle);
        settingsButton.clearChildren();
        stack = new Stack();
        stack.add(settingsButton.getImage());
        stack.add(settingsButton.getLabel());
        settingsButton.add(stack);
        settingsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                game.pushScreen(new SettingsScreen(resourceLoader, game));
            }
        });

        Table uiTable = new Table();
        uiTable.align(Align.right);
        uiTable.row().height(50);
        uiTable.add(continueGameButton).space(20);
        uiTable.row().height(50);
        uiTable.add(newGameButton).space(20);
        uiTable.row().height(50);
        uiTable.add(settingsButton).space(20);
        uiTable.setPosition(stage.getWidth(), stage.getHeight() / 2);
        uiTable.drawDebug(new ShapeRenderer());
        stage.addActor(uiTable);

        flourishBladeLogo = new Image(resourceLoader.getTexture(ResourceLoader.FLOURISH_BLADE_LOGO));
        resizeLogo();
        stage.addActor(flourishBladeLogo);
        //stage.setDebugAll(true);
    }

    private void handleBackgroundMap() {
        world = new World(new Vector2(0, 0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void tick(float delta) {
        stage.act(delta);
    }

    @Override
    public void draw(SpriteBatch batch) {
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        resizeLogo();
    }

    private void resizeLogo() {
        float windroseLogoRatio = flourishBladeLogo.getHeight() / flourishBladeLogo.getWidth();
        flourishBladeLogo.setSize(stage.getWidth(), windroseLogoRatio * stage.getWidth());
        flourishBladeLogo.setPosition(0, stage.getHeight() - flourishBladeLogo.getHeight());
    }

    @Override
    public void dispose() {
        stage.dispose();
        resourceLoader.removeTexture(ResourceLoader.FLOURISH_BLADE_LOGO);
        resourceLoader.removeMusic(ResourceLoader.MUSIC_MAIN_THEME);
    }

    @Override
    public boolean isParentVisible() {
        return false;
    }

    @Override
    public boolean darkenScreen() {
        return true;
    }

}

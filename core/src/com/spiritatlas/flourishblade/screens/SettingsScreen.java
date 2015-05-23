package com.spiritatlas.flourishblade.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spiritatlas.flourishblade.FlourishBlade;
import com.spiritatlas.flourishblade.util.ResourceLoader;

public class SettingsScreen extends AbstractScreen {
    private final ResourceLoader resourceLoader;

    private TextField bgmValue;
    private TextField sfxValue;

    public SettingsScreen(final ResourceLoader resources, final FlourishBlade game) {
        super(new Stage(new FitViewport(FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT)), game);
        this.resourceLoader = resources;

        handleSettingsUI();
    }

    private void handleSettingsUI() {
        // Set up the back button
        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle();
        buttonStyle.font = resourceLoader.getFont(ResourceLoader.FONT);
        TextureRegion buttonUp = new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 0, 282, 190, 49);
        buttonUp.setRegionHeight(49);
        TextureRegion buttonDown = new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 0, 237, 190, 46);
        buttonDown.setRegionHeight(46);

        buttonStyle.imageUp = new TextureRegionDrawable(buttonUp);
        buttonStyle.imageDown = new TextureRegionDrawable(buttonDown);
        buttonStyle.pressedOffsetY = -3;

        ImageTextButton backButton = new ImageTextButton("Back", buttonStyle);
        backButton.clearChildren();
        Stack stack = new Stack();
        stack.add(backButton.getImage());
        stack.add(backButton.getLabel());
        backButton.add(stack);
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                game.getGameSettings().saveSettings();
                game.popScreen();
            }
        });

        // Set up the music and sound volume sliders text style and slider style
        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = resourceLoader.getFont(ResourceLoader.FONT);
        textStyle.fontColor = Color.WHITE;
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = new TextureRegionDrawable(
                new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 338, 386, 18, 18)
        );
        style.knob = new TextureRegionDrawable(
                new TextureRegion(resourceLoader.getTexture(ResourceLoader.UI_TEX), 335, 152, 34, 37)
        );

        // Set up the music (bgm) slider and value
        bgmValue = new TextField(
                String.valueOf(MathUtils.round(game.getGameSettings().getBgmVolume() * 100)), textStyle
        );
        final Slider bgmSlider = new Slider(0.0f, 1.0f, 0.02f, false, style);
        bgmSlider.setValue(game.getGameSettings().getBgmVolume());
        bgmSlider.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                bgmValue.setText(String.valueOf(MathUtils.round(bgmSlider.getValue() * 100)));
                resourceLoader.setMusicVolume(bgmSlider.getValue());
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bgmValue.setText(String.valueOf(MathUtils.round(bgmSlider.getValue() * 100)));
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                game.getGameSettings().setBgmVolume(bgmSlider.getValue());
                resourceLoader.setMusicVolume(bgmSlider.getValue());
            }
        });

        // Set up the sound (sfx) slider and value
        sfxValue = new TextField(
                String.valueOf(MathUtils.round(game.getGameSettings().getSfxVolume() * 100)), textStyle
        );
        final Slider sfxSlider = new Slider(0.0f, 1.0f, 0.02f, false, style);
        sfxSlider.setValue(game.getGameSettings().getSfxVolume());
        sfxSlider.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                sfxValue.setText(String.valueOf(MathUtils.round(sfxSlider.getValue() * 100)));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sfxValue.setText(String.valueOf(MathUtils.round(sfxSlider.getValue() * 100)));
                game.getGameSettings().setSfxVolume(sfxSlider.getValue());
                resourceLoader.getSound(ResourceLoader.CLICK).play(game.getGameSettings().getSfxVolume());
            }
        });

        // Setup the table
        Table uiTable = new Table();
        uiTable.setFillParent(true);
        uiTable.row().colspan(2);
        uiTable.add(new TextField("Music", textStyle));
        uiTable.row().height(40);
        uiTable.add(bgmValue);
        uiTable.add(bgmSlider);
        uiTable.row().colspan(2);
        uiTable.add(new TextField("Sound", textStyle));
        uiTable.row().height(40);
        uiTable.add(sfxValue);
        uiTable.add(sfxSlider);
        uiTable.row().colspan(2).height(50);
        uiTable.add(backButton).space(8);
        uiTable.drawDebug(new ShapeRenderer());
        stage.addActor(uiTable);
    }

    @Override
    public void tick(float delta) {
        stage.act(delta);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public boolean isParentVisible() {
        return true;
    }

    @Override
    public boolean darkenScreen() {
        return true;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

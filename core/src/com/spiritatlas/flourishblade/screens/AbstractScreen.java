package com.spiritatlas.flourishblade.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.spiritatlas.flourishblade.FlourishBlade;

public abstract class AbstractScreen implements Disposable, InputProcessor {
    protected final Stage stage;
    protected final FlourishBlade game;

    public AbstractScreen(Stage stage, FlourishBlade game) {
        this.stage = stage;
        this.game = game;
    }

    public void update(float delta) {
        if (game.shouldHaveInputFocus(this)) {
            tick(delta);
        }
        draw(game.getSpriteBatch());
    }

    public Stage getStage() {
        return stage;
    }

    public abstract void tick(float delta);

    public abstract void draw(SpriteBatch spriteBatch);

    public abstract void resize(int width, int height);

    public abstract boolean isParentVisible();

    public abstract boolean darkenScreen();

    @Override
    public abstract void dispose();

    /* Input Handling methods */

    @Override
    public boolean keyDown(int keycode) {
        return stage.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return stage.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return stage.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return stage.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return stage.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return stage.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return stage.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return stage.scrolled(amount);
    }
}

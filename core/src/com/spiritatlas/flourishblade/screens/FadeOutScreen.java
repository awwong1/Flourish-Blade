package com.spiritatlas.flourishblade.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spiritatlas.flourishblade.FlourishBlade;

public class FadeOutScreen extends AbstractScreen {

    private Actor black;
    private float holdSeconds;
    private Texture blackPixelTexture;

    private boolean beginFadeIn = true;
    private AbstractScreen previousScreen;
    private AbstractScreen nextScreen;

    public FadeOutScreen(final FlourishBlade game, AbstractScreen nextScreen) {
        super(new Stage(new FitViewport(FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT)), game);
        this.nextScreen = nextScreen;

        Pixmap blackPixel = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        blackPixel.setColor(Color.BLACK);
        blackPixel.fillRectangle(0, 0, 1, 1);
        blackPixelTexture = new Texture(blackPixel, Pixmap.Format.RGB888, false);
        TextureRegion bpTextureRegion = new TextureRegion(blackPixelTexture, 0, 0,
                FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT);
        holdSeconds = 1.0f;
        stage.addActor(new Image(bpTextureRegion));
        black = stage.getActors().peek();
        black.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                Color color = black.getColor();
                if (beginFadeIn) {
                    color.a = 0;
                    beginFadeIn = false;
                } else {
                    color.a += delta;
                    if (color.a >= 1.0f) {
                        color.a = 1.0f;
                        if (holdSeconds >= 0.0f) {
                            holdSeconds -= delta;
                        }
                    }
                }
                black.setColor(color);
                return false;
            }
        });
    }

    @Override
    public void tick(float delta) {
        stage.act(delta);
        if (!beginFadeIn && holdSeconds <= 0.0f) {
            if (previousScreen == null) {
                previousScreen = game.removeSecondPoppedScreen();
            } else {
                game.popScreen();
                game.pushScreen(nextScreen);
            }
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public boolean isParentVisible() {
        return true;
    }

    @Override
    public boolean darkenScreen() {
        return false;
    }

    @Override
    public void dispose() {
        stage.dispose();
        blackPixelTexture.dispose();
    }
}

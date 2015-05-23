package com.spiritatlas.flourishblade.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spiritatlas.flourishblade.FlourishBlade;
import com.spiritatlas.flourishblade.util.ResourceLoader;

public class SplashScreen extends AbstractScreen {
    private final ResourceLoader resourceLoader;

    private Image saLogoImage;
    private Actor logo;
    private float holdSeconds;
    private boolean beginFadeIn = true;
    private boolean beginFadeOut = false;

    public SplashScreen(final ResourceLoader resourceLoader, final FlourishBlade game) {
        super(new Stage(new FitViewport(FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT)), game);
        this.resourceLoader = resourceLoader;
        resourceLoader.loadTexture("images/spiritatlas.png", ResourceLoader.SPIRIT_ATLAS_LOGO);
        TextureRegion saLogoTextureRegion = new TextureRegion(
                resourceLoader.getTexture(ResourceLoader.SPIRIT_ATLAS_LOGO));
        saLogoImage = new Image(saLogoTextureRegion);

        holdSeconds = 2.0f;
        stage.addActor(saLogoImage);
        logo = stage.getActors().peek();
        logo.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                Color c = logo.getColor();
                if (beginFadeIn) {
                    c.a = 0;
                    beginFadeIn = false;
                } else if (!beginFadeOut) {
                    c.a += delta;
                    if (c.a >= 1.0f) {
                        c.a = 1.0f;
                        if (holdSeconds >= 0.0f) {
                            holdSeconds -= delta;
                        } else {
                            beginFadeOut = true;
                        }
                    }
                } else {
                    c.a -= delta;
                    if (c.a <= 0.0f) {
                        c.a = 0.0f;
                    }
                }
                logo.setColor(c);
                return false;
            }
        });
    }

    @Override
    public void tick(float delta) {
        stage.act(delta);
        if (logo.getColor().a == 0.0f && beginFadeOut) {
            game.popScreen();
            game.pushScreen(new MainMenuScreen(resourceLoader, game));
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        float saLogoRatio = saLogoImage.getHeight() / saLogoImage.getWidth();
        saLogoImage.setSize(stage.getWidth(), saLogoRatio * stage.getWidth());
        saLogoImage.setPosition(
                stage.getWidth() / 2 - saLogoImage.getWidth() / 2,
                stage.getHeight() / 2 - saLogoImage.getHeight() / 2);
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
        stage.dispose();
        resourceLoader.removeTexture(ResourceLoader.SPIRIT_ATLAS_LOGO);
    }
}

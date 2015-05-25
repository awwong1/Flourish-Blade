package com.spiritatlas.flourishblade.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spiritatlas.flourishblade.FlourishBlade;
import com.spiritatlas.flourishblade.util.ResourceLoader;

public class DialogScreen extends AbstractScreen {
    private final ResourceLoader resourceLoader;
    private Texture blackPixelTexture;

    public DialogScreen(final ResourceLoader resources, final FlourishBlade game, final String dialog) {
        super(new Stage(new FitViewport(FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT)), game);
        this.resourceLoader = resources;

        Pixmap blackPixel = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        blackPixel.setColor(new Color(0, 0, 0, 0.7f));
        blackPixel.fillRectangle(0, 0, 1, 1);
        blackPixelTexture = new Texture(blackPixel, Pixmap.Format.RGB888, false);
        TextureRegion bpTextureRegion = new TextureRegion(blackPixelTexture, 0, 0,
                FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT / 2);
        Stack stackText = new Stack();
        stackText.add(new Image(bpTextureRegion));
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = resourceLoader.getFont(ResourceLoader.FONT);
        style.fontColor = Color.WHITE;
        stackText.add(new TextArea("\n" + dialog, style));
        stackText.setPosition(0, FlourishBlade.V_HEIGHT / 2);
        stackText.setSize(FlourishBlade.V_WIDTH, FlourishBlade.V_HEIGHT / 2);
        stage.addActor(stackText);
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.popScreen();
        return false;
    }
}

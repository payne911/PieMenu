package com.payne.games.piemenu.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public abstract class BaseScreen extends ScreenAdapter {

    protected float screenColorRed = 0.4f;
    protected float screenColorGreen = 0.4f;
    protected float screenColorBlue = 0.4f;
    protected float screenColorAlpha = 1f;

    protected TestsMenu game;
    protected boolean autoDispose = true;
    protected Array<Texture> autoDisposableTextures = new Array<>();

    public BaseScreen(TestsMenu game) {
        this.game = game;
    }

    protected Texture getTextureAutoDisposable(String path) {
        return getTextureAutoDisposable(Gdx.files.internal(path));
    }

    protected Texture getTextureAutoDisposable(FileHandle file) {
        Texture localTexture = new Texture(file);
        autoDisposableTextures.add(localTexture);
        return localTexture;
    }

    protected void setScreenColor(float r, float g, float b, float a) {
        screenColorRed = r;
        screenColorGreen = g;
        screenColorBlue = b;
        screenColorAlpha = a;
    }

    public void updateInputPre(float delta) { // todo: remove?
    }

    public void updateInputPost(float delta) { // todo: remove?
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(screenColorRed, screenColorGreen, screenColorBlue, screenColorAlpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateInputPre(delta); // todo: remove?

        game.stage.act(delta);
        game.stage.draw();

        updateInputPost(delta); // todo: remove?
    }

    @Override
    public void hide() {
        game.stage.clear();
        game.disableDebug();
        if (autoDispose) {
            dispose();
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < autoDisposableTextures.size; i++) {
            autoDisposableTextures.get(i).dispose();
        }
        Gdx.app.log(getClass().getSimpleName(), "Disposed");
    }
}

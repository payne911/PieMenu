package com.payne.games.piemenu.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public abstract class BaseScreen extends ScreenAdapter {

    protected float screenColorRed = 0.4f;
    protected float screenColorGreen = 0.4f;
    protected float screenColorBlue = 0.4f;
    protected float screenColorAlpha = 1f;

    protected BaseGame game;

    public BaseScreen(BaseGame game) {
        this.game = game;
    }

    protected void setScreenColor(float r, float g, float b, float a) {
        screenColorRed = r;
        screenColorGreen = g;
        screenColorBlue = b;
        screenColorAlpha = a;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(screenColorRed, screenColorGreen, screenColorBlue, screenColorAlpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateInputPre(delta);

        game.stage.act(delta);
        game.stage.draw();

        updateInputPost(delta);
    }

    public void updateInputPre(float delta) {
    }

    public void updateInputPost(float delta) {
    }

    @Override
    public void hide() {
        game.stage.clear();
        dispose();
    }

    @Override
    public void dispose() {
    }
}

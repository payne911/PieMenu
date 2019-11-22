package com.payne.games.piemenu.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BaseGame extends Game {

    private static final String SKIN_PATH = "skin.json";

    public boolean isDebug;
    public Batch batch;
    public Stage stage;
    public Skin skin;
    public MainScreen mainScreen;

    @Override
    public void create() {
        isDebug = false;
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        skin = new Skin(Gdx.files.internal(SKIN_PATH));
        mainScreen = new MainScreen(this);
        Gdx.input.setInputProcessor(stage);
        switchToMainScreen();
    }

    public void switchToMainScreen() {
        if (getScreen() != mainScreen) {
            setScreen(mainScreen);
        }
    }

    public void toggleDebug() {
        isDebug = !isDebug;
        stage.setDebugAll(isDebug);
    }

    public void enableDebug() {
        isDebug = true;
        stage.setDebugAll(isDebug);
    }

    public void disableDebug() {
        isDebug = false;
        stage.setDebugAll(isDebug);
    }

    private void updateInput() {
        if (UIUtils.ctrl()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                toggleDebug();
                Gdx.app.log("Debug mode", isDebug ? "On" : "Off");
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            switchToMainScreen();
        }
    }

    @Override
    public void render() {
        updateInput();
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}

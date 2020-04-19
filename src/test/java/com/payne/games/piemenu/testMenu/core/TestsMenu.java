package com.payne.games.piemenu.testMenu.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TestsMenu extends Game {

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

    public void refreshDebug() {
        stage.setDebugAll(isDebug);
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
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                if (getScreen() != mainScreen) {
                    try {
                        Constructor localConstructor = ClassReflection.getConstructor(getScreen().getClass(), TestsMenu.class);
                        setScreen((Screen) localConstructor.newInstance(this));
                    } catch (ReflectionException e) {
                        e.printStackTrace();
                    }
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
                if (getScreen() != mainScreen) {
                    int localIndexCurrent = MainScreen.registeredTests.indexOf(getScreen().getClass());
                    int localIndexNext = localIndexCurrent - 1;
                    if ((localIndexCurrent >= 0 && localIndexCurrent < MainScreen.registeredTests.size()) && localIndexNext >= 0) {
                        try {
                            Constructor localConstructor = ClassReflection.getConstructor(MainScreen.registeredTests.get(localIndexNext), TestsMenu.class);
                            setScreen((Screen) localConstructor.newInstance(this));
                        } catch (ReflectionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
                if (getScreen() != mainScreen) {
                    int localIndexCurrent = MainScreen.registeredTests.indexOf(getScreen().getClass());
                    int localIndexNext = localIndexCurrent + 1;
                    if ((localIndexCurrent >= 0 && localIndexCurrent < MainScreen.registeredTests.size()) && localIndexNext < MainScreen.registeredTests.size()) {
                        try {
                            Constructor localConstructor = ClassReflection.getConstructor(MainScreen.registeredTests.get(localIndexNext), TestsMenu.class);
                            setScreen((Screen) localConstructor.newInstance(this));
                        } catch (ReflectionException e) {
                            e.printStackTrace();
                        }
                    }
                }
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
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();
        if (getScreen() != mainScreen) {
            mainScreen.dispose();
        }
    }
}

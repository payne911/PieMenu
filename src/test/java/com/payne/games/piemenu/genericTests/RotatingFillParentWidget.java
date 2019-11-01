package com.payne.games.piemenu.genericTests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;


public class RotatingFillParentWidget extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private Batch batch;
    private PieMenu menu;

    @Override
    public void create () {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(true);

        /* Setting up the WhitePixel. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1,1,1);
        pixmap.fill();
        tmpTex = new Texture(pixmap);
        pixmap.dispose();
        TextureRegion whitePixel = new TextureRegion(tmpTex);





        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.hoverColor = Color.RED;
        style.selectedColor = Color.BLUE;
        style.background = new TextureRegionDrawable(new Texture(Gdx.files.internal("rael_pie.png")));
        menu = new PieMenu(batch, whitePixel, style, 80, 24f/80, 30);
        menu.setGlobalAlphaMultiplier(.5f);

        for(int i=0 ; i<6 ; i++)
            menu.addActor(new Label("menu " + i, skin));

        menu.addListener(new PieMenu.PieMenuCallbacks() {
            @Override
            public void onHoverChange(int hoveredIndex) {
                System.out.println("hovered: " + hoveredIndex);
            }
        });


        stage.addActor(menu);
        menu.setFillParent(true);
        menu.drawRudimentaryDebug();
    }

    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(.4f,.4f,.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();

        if(Gdx.input.isKeyPressed(Keys.SPACE)) {
            menu.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(menu.getRotation());
        }
        if(Gdx.input.isKeyPressed(Keys.M)) {
            menu.setPosition(10,100);
        }

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose () {

        /* Disposing is good practice! */
        skin.dispose();
        stage.dispose();
        batch.dispose();
        tmpTex.dispose();
    }
}
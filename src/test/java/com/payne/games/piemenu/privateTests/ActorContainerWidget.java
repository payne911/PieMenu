package com.payne.games.piemenu.privateTests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class ActorContainerWidget extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private PolygonSpriteBatch batch;
    private ShapeDrawer shape;

    private PieMenu menu;

    @Override
    public void create () {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        /* Setting up the ShapeDrawer. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1,1,1);
        pixmap.fill();
        tmpTex = new Texture(pixmap);
        pixmap.dispose();
        shape = new ShapeDrawer(batch, new TextureRegion(tmpTex));

        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.hoveredSliceColor = Color.RED;
        style.highlightedSliceColor = Color.BLUE;
        style.selectedSliceColor = Color.BLUE;
        style.backgroundColor = Color.ORANGE;
        menu = new PieMenu(shape, style, 400);

        for(int i=0 ; i<5 ; i++)
            menu.addActor(new TextButton("menu " + i, skin));

        menu.drawRudimentaryDebug();
		menu.setPosition(stage.getWidth()/2, stage.getHeight()/2, Align.center);

        stage.addActor(menu);
    }

    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();

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
        tmpTex.dispose();
    }
}
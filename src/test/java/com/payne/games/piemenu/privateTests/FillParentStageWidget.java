package com.payne.games.piemenu.privateTests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class FillParentStageWidget extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private PolygonSpriteBatch batch;
    private ShapeDrawer shape;

    private PieMenu menu1;

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
        PieMenu.PieMenuStyle style1 = new PieMenu.PieMenuStyle();
        style1.hoveredSliceColor = Color.RED;
        style1.highlightedSliceColor = Color.BLUE;
        style1.selectedSliceColor = Color.BLUE;
        style1.backgroundColor = Color.ORANGE;
        menu1 = new PieMenu(shape, style1, 80);

        for(int i=0 ; i<5 ; i++)
            menu1.addActor(new Label("menu " + i, skin));

        menu1.addListener(new PieMenu.PieMenuCallbacks() {
            @Override
            public void onHoverChange(int hoveredIndex) {
                System.out.println("hov: " + hoveredIndex);
            }
        });


        stage.addActor(menu1);
//        menu1.centerOnScreen();
        menu1.drawRudimentaryDebug();
        menu1.setFillParent(true);
    }

    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(.4f,.4f,.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            menu1.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(menu1.getRotation());
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
        tmpTex.dispose();
    }
}
package com.payne.games.piemenu.genericTests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.PieWidget;


public class SelfContainedPieMenu extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private Batch batch;
    private PieWidget menu;

    @Override
    public void create() {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(true);

        /* Setting up the WhitePixel. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        tmpTex = new Texture(pixmap);
        pixmap.dispose();
        TextureRegion whitePixel = new TextureRegion(tmpTex);





        /* Adding the demo widgets. */
        PieWidget.PieWidgetStyle style1 = new PieWidget.PieWidgetStyle();
        style1.sliceColor = Color.ORANGE;
        style1.alternateSliceColor = new Color(.8f, .5f, .2f, 1);
        style1.circumferenceWidth = 1;
        style1.circumferenceColor = Color.BLACK;
        menu = new PieWidget(whitePixel, style1, 500);

        PieMenu.PieMenuStyle style2 = new PieMenu.PieMenuStyle();
        style2.hoverColor = Color.GREEN;
        style2.selectedColor = Color.MAGENTA;
        style2.backgroundColor = Color.BROWN;
        for(int i=0 ; i<5 ; i++) {
            PieMenu tmp = new PieMenu(whitePixel, style2, 110);
            menu.addActor(tmp);
            for(int j=0 ; j<5 ; j++)
                tmp.addActor(new Label(i + " " + j, skin));
//            tmp.setWidth(50);
//            tmp.setHeight(50);

        }

        stage.addActor(menu);
        menu.setFillParent(true);
    }

    @Override
    public void render() {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();

        menu.centerOnScreen();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            menu.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(menu.getRotation());
        }

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {

        /* Disposing is good practice! */
        skin.dispose();
        stage.dispose();
        batch.dispose();
        tmpTex.dispose();
    }
}
package com.payne.games.piemenu.genericTests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;


public class TableWidgets extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Batch batch;
    private Texture tmpTex;

    @Override
    public void create () {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);
		Table root = new Table();
		root.debugAll();
		root.setFillParent(true);
		stage.addActor(root);

		/* Top label. */
		root.add(new Label("R: restart\n" +
				"S: toggle the permanent pie\n" +
				"Middle-click / Right-click: try it out!", skin)).colspan(2).padTop(25).top();

		/* New row. */
		root.row().padBottom(20).uniform();

        /* Setting up the WhitePixel. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1,1,1);
        pixmap.fill();
        tmpTex = new Texture(pixmap);
        pixmap.dispose();
        TextureRegion whitePixel = new TextureRegion(tmpTex);





        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style1 = new PieMenu.PieMenuStyle();
        style1.hoverColor = Color.RED;
        style1.selectedColor = Color.BLUE;
        style1.backgroundColor = Color.ORANGE;
        PieMenu menu1 = new PieMenu(batch, whitePixel, style1, 80);

        for(int i=0 ; i<5 ; i++)
            menu1.addActor(new Label("menu " + i, skin));

//        menu1.setPosition(stage.getWidth()/2 + 100, stage.getHeight()/2, Align.center);
//        stage.addActor(menu1);
        menu1.drawRudimentaryDebug();
        root.add(menu1).fill();

        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style2 = new PieMenu.PieMenuStyle();
        style2.hoverColor = Color.RED;
        style2.selectedColor = Color.BLUE;
        style2.backgroundColor = Color.ORANGE;
        PieMenu menu2 = new PieMenu(batch, whitePixel, style2, 120);

        for(int i=0 ; i<5 ; i++)
            menu2.addActor(new Label("menu " + i, skin));

//        menu2.setPosition(0, 0, Align.bottomLeft);
//        stage.addActor(menu2);
        menu2.drawRudimentaryDebug();
        root.add(menu2);

        /* New row. */
        root.row().colspan(2);

        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style3 = new PieMenu.PieMenuStyle();
        style3.hoverColor = Color.RED;
        style3.selectedColor = Color.BLUE;
        style3.backgroundColor = Color.ORANGE;
        PieMenu menu3 = new PieMenu(batch, whitePixel, style3, 40);

        for(int i=0 ; i<5 ; i++)
            menu3.addActor(new Label("menu " + i, skin));

//        menu3.setPosition(stage.getWidth(), stage.getHeight(), Align.topRight);
//        stage.addActor(menu3);
        menu3.drawRudimentaryDebug();
        root.add(menu3);



        /* Some buttons, for comparison. */
        root.row().padLeft(50);
        Button b1 = new Button(skin);
        Button b2 = new TextButton("textBtn", skin);
        Button b3 = new ImageTextButton("imgTxt", skin);

        root.add(b1).fill();
        root.add(b2,b3);
    }

    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(.2f,.2f,.7f, 1);
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
        batch.dispose();
        stage.dispose();
        tmpTex.dispose();
    }
}
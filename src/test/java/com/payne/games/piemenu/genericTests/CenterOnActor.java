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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieWidget;


public class CenterOnActor extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private Batch batch;
    private Label testLabel;
    private PieWidget widget;

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
//        style1.sliceColor = Color.ORANGE;
//        style1.alternateSliceColor = new Color(.8f, .5f, .2f, 1);
        style1.circumferenceWidth = 2;
        style1.circumferenceColor = Color.BLACK;
        style1.separatorWidth = 2;
        style1.separatorColor = Color.BLACK;
        widget = new PieWidget(whitePixel, style1, 200, 0.9f);

        for(int i=0 ; i<4 ; i++) {
            TextButton tmp = new TextButton("XXX", skin);
            widget.addActor(tmp);
        }

        stage.addActor(widget);


        /* The Group to test coordinates with. */
        Group group = new Group();
        Table table = new Table(skin);
        table.add(new Label("Bobbbbbbbbbbbb", skin));
        testLabel = new Label("Test", skin);
        table.add(testLabel);
        group.addActor(table);
        stage.addActor(group);
        group.setPosition(250,250);
    }

    @Override
    public void render() {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(.4f, .4f, .4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();

        widget.centerOnActor(testLabel);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            widget.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(widget.getRotation());
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
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;


public class DifferentActorsWidget extends ApplicationAdapter {
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
        style.backgroundColor = Color.ORANGE;
        menu = new PieMenu(batch, whitePixel, style, 400) {
            @Override
            public float getActorDistanceFromCenter(Actor actor) {
                invalidate();
                return getAmountOfChildren() > 1
                        ? getMaxRadius() - getChild(0).getWidth()
                        : 0;
            }

            @Override
            public void modifyActor(Actor actor, float degreesPerChild, float actorDistanceFromCenter) {
                float size = getEstimatedRadiusAt(degreesPerChild, actorDistanceFromCenter);
                size *= 1.45f;
                if(! (actor instanceof List))
                    actor.setSize(size, size);
                else
                    actor.setSize(size*3f, size*1.4f);
            }
        };

        /* Populating with a bunch of different Actor types. */
        menu.addActor(new TextButton("txtBtn", skin));
        menu.addActor(new Image(new Texture(Gdx.files.internal("heart-drop.png"))));
        menu.addActor(new Button(skin));
        menu.addActor(new Label("label", skin));
        menu.addActor(new ImageTextButton("imgTxtBtn", skin));
        menu.addActor(new TextField("txtField", skin));
        menu.addActor(new ImageButton(new Image(new Texture(Gdx.files.internal("coffee-mug.png"))).getDrawable()));
        menu.addActor(new CheckBox("chkBox", skin));
        menu.addActor(new TextArea("txtArea", skin));
        List<Label> myList = new List<>(skin);
        myList.setItems(new Label("item1", skin), new Label("item2", skin));
        menu.addActor(myList);
        SelectBox<Label> myBox = new SelectBox<>(skin);
        myBox.setItems(new Label("item1", skin), new Label("item2", skin));
        menu.addActor(myBox);
        menu.addActor(new ProgressBar(0,10,5, false, skin));
        menu.addActor(new Slider(0,10,5, false, skin));
        menu.addActor(new Window("window", skin));
        menu.addActor(new Touchpad(50, skin));
        menu.addActor(new Container<>(new Label("containedLbl", skin)));
        menu.addActor(new Stack(new Image(new Texture(Gdx.files.internal("jeweled-chalice.png"))), new Label("stackLbl", skin)));


        menu.drawRudimentaryDebug();
        menu.setFillParent(true);

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
        batch.dispose();
        stage.dispose();
        tmpTex.dispose();
    }
}
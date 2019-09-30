package com.payne.games.piemenu.individuals;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class ClickToggle extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private PolygonSpriteBatch batch;
    private PieMenu menu;


    @Override
    public void create () {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        /* Ideally, you would extract such a pixel from your Atlas instead. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1,1,1);
        pixmap.fill();
        tmpTex = new Texture(pixmap);
        pixmap.dispose();

        /* Setting up the ShapeDrawer. */
        ShapeDrawer shape = new ShapeDrawer(batch, new TextureRegion(tmpTex));



        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.radius = 80;
        style.selectedChildRegionColor = new Color(.7f,.1f,.7f,1);
        style.childRegionColor = new Color(0,.7f,0,1);
        style.alternateChildRegionColor = new Color(.7f,0,0,1);
        menu = new PieMenu(shape, style);

        /* No need to customize the behavior: we use the default one. */
        // default

        /* Adding the listeners. */
        menu.addListener(menu.getSuggestedClickListener());
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - newly selected index: " + menu.getSelectedIndex());
            }
        });

        /* Populating the widget. */
        int dragPieAmount = 0;
        for (int i = 0; i < 4; i++) {
            Label label = new Label(Integer.toString(dragPieAmount++), skin);
            menu.addActor(label);
        }

        /* Including the Widget in the Stage. */
        stage.addActor(menu);
    }


    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();



        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            menu.centerOnMouse();
            menu.resetSelection();
            menu.setVisible(true);
        }
    }


    @Override
    public void dispose () {

        /* Disposing is good practice! */
        batch.dispose();
        tmpTex.dispose();
        skin.dispose();
    }
}

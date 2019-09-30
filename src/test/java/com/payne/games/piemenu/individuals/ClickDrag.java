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


public class ClickDrag extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private PolygonSpriteBatch batch;
    private PieMenu menu;

    /* For the demonstration's purposes. Not actually necessary. */
    private float red, green, blue;


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
        style.separatorWidth = 2;
        style.backgroundColor = new Color(1,1,1,.1f);
        style.separatorColor = new Color(.1f,.1f,.1f,1);
        style.selectedChildRegionColor = new Color(.5f,.5f,.5f,1);
        style.childRegionColor = new Color(.33f,.33f,.33f,1);
        menu = new PieMenu(shape, style);

        /* Customizing the behavior. */
        menu.setHighlightIsSelection(true); // because we want instant feedback
        menu.setSelectionButton(Input.Buttons.RIGHT); // right-click for interactions with the widget

        /* Setting up listeners. */
        menu.addListener(menu.getSuggestedClickListener());
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch(menu.getSelectedIndex()) {
                    case 0:
                        red   = .25f;
                        blue  = .75f;
                        green = .25f;
                        break;
                    case 1:
                        red   = .75f;
                        blue  = .25f;
                        green = .25f;
                        break;
                    case 2:
                        red   = .25f;
                        blue  = .25f;
                        green = .75f;
                        break;
                    default: // good practice
                        red   = .75f;
                        blue  = .75f;
                        green = .75f;
                        break;
                }
            }
        });

        /* Populating the widget. */
        Label blue = new Label("blue", skin);
        menu.addActor(blue);
        Label red = new Label("red", skin);
        menu.addActor(red);
        Label green = new Label("green", skin);
        menu.addActor(green);

        /* Including the Widget in the Stage. */
        menu.selectIndex(0); // selecting an initial value
        stage.addActor(menu);
    }


    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(red, green, blue, 1); // updated with the menu
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();



        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        if (Gdx.input.isButtonJustPressed(menu.getSelectionButton())) {
            menu.centerOnMouse();
            menu.triggerDefaultListenerTouchDown(); // Programmatically sends the user into the `touchDragged` Event
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

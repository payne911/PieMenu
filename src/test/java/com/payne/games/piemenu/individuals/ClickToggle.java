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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.PieMenuSuggestedClickListener;
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
        style.background = new Image(new Texture(Gdx.files.internal("rael_pie.png"))).getDrawable(); // image background!
        style.innerRadius = 24;
        style.startDegreesOffset = 30;
        style.selectedChildRegionColor = new Color(1,.5f,.5f,.5f);
        style.childRegionColor = new Color(.73f,.33f,.33f,.1f);
        menu = new PieMenu(shape, style);

        /* Customizing the behavior. */
        menu.setInfiniteSelectionRange(true);

        /* Adding the listeners. */
        menu.addListener(new PieMenuSuggestedClickListener());
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - newly selected index: " + menu.getSelectedIndex());
                menu.setVisible(false);
                menu.remove();
            }
        });

        /* Populating the widget. */
        Array<Image> imgs = new Array<>();
        imgs.add(new Image(new Texture(Gdx.files.internal("heart-drop.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("beer-stein.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("coffee-mug.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("gooey-daemon.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("jeweled-chalice.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("coffee-mug.png"))));
        for (int i = 0; i < imgs.size; i++)
            menu.addActor(imgs.get(i));
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

        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            stage.addActor(menu);
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

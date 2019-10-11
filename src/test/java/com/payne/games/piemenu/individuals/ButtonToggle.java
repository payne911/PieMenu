package com.payne.games.piemenu.individuals;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.AnimatedRadialGroup;
import com.payne.games.piemenu.RadialGroup;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class ButtonToggle extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private PolygonSpriteBatch batch;
    private AnimatedRadialGroup radGroup;


    @Override
    public void create () {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        /* Adding a Table. */
        Table root = new Table();
        root.setFillParent(true);
        root.defaults().padBottom(150);
        stage.addActor(root);

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
        RadialGroup.RadialGroupStyle style = new RadialGroup.RadialGroupStyle();
        style.sliceColor = new Color(1,1,1,.2f);
        style.separatorWidth = 2;
        style.circumferenceWidth = 2;
        style.circumferenceColor = Color.BLACK;
        style.separatorColor = style.circumferenceColor;
        radGroup = new AnimatedRadialGroup(shape, style, 110, 50, 315, 270);

        /* Populating the widget. */
        for (int i = 0; i < 8; i++) {
            Label label = new Label(Integer.toString(i), skin);
            radGroup.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Toggle",  skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                radGroup.toggleVisibility(.9f); // 0.9 seconds animation
                radGroup.setPosition(textButton.getX() + textButton.getWidth()/2,
                        textButton.getY() + textButton.getHeight()/2 - 5, Align.center);
            }
        });
        root.add(textButton).expand().bottom();

        /* Including the Widget in the Stage. */
        stage.addActor(radGroup);
        radGroup.setVisible(false);
    }


    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(.2f, .2f, .8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();
    }


    @Override
    public void dispose () {

        /* Disposing is good practice! */
        batch.dispose();
        tmpTex.dispose();
        skin.dispose();
    }
}

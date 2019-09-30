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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class ButtonBound extends ApplicationAdapter {
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
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.radius = 130;
        style.innerRadius = 50;
        style.startDegreesOffset = 180;
        style.totalDegreesDrawn = 320;
        style.backgroundColor = new Color(1,1,1,.3f);
        style.selectedChildRegionColor = new Color(.7f,.3f,.5f,1);
        style.childRegionColor = new Color(0,.7f,0,1);
        style.alternateChildRegionColor = new Color(.7f,0,0,1);
        menu = new PieMenu(shape, style);

        /* Populating the widget. */
        for (int i = 0; i < 5; i++) {
            Label label = new Label(Integer.toString(i), skin);
            menu.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Drag Pie",  skin);
        textButton.addListener(new ClickListener() {
            /*
            If this was a ChangeListener, the `dragPit.suggestedClickListener`
            would get called before the `changed` method would get called.
            `changed` only gets called after the user releases the click
            directly within the boundaries of the Button, whereas the
            SuggestedClickListener is summoned as soon as the click happens.
            */
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menu.resetSelection();
                menu.centerOnActor(textButton);
                return true;
            }
        });
        textButton.addListener(menu.getSuggestedClickListener());
        root.add(textButton).expand().bottom();

        /* Adding the listener. */
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int index = menu.getSelectedIndex();
                if(menu.isValidIndex(index)) {
                    textButton.setText("Drag Pie");
                    return;
                }
                Actor child = menu.getChild(index);
                textButton.setText(((Label)child).getText().toString());
            }
        });

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
    }


    @Override
    public void dispose () {

        /* Disposing is good practice! */
        batch.dispose();
        tmpTex.dispose();
        skin.dispose();
    }
}

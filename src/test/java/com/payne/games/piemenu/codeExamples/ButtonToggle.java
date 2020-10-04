package com.payne.games.piemenu.codeExamples;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.AnimatedPieWidget;
import com.payne.games.piemenu.PieWidget;


public class ButtonToggle extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private Batch batch;
    private AnimatedPieWidget radGroup;


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
        TextureRegion whitePixel = new TextureRegion(tmpTex);



        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Setting up and creating the widget. */
        PieWidget.PieWidgetStyle style = new PieWidget.PieWidgetStyle();
        style.sliceColor = new Color(1,1,1,.2f);
        style.separatorWidth = 2;
        style.circumferenceWidth = 2;
        style.separatorColor = style.circumferenceColor;
        radGroup = new AnimatedPieWidget(whitePixel, style, 110, 50f/110, 315, 270);

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
                radGroup.setPosition(textButton.getX(Align.center),
                        textButton.getY(Align.center) - 5, Align.center);
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
        stage.dispose();
        batch.dispose();
        tmpTex.dispose();
        skin.dispose();
    }
}
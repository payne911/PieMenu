package com.payne.games.piemenu.codeExamples;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.RadialGroup;


public class RadialButtons extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Batch batch;
    private RadialGroup menu;

    /* For the demonstration's purposes. Not actually necessary. */
    private float red   = .25f;
    private float blue  = .75f;
    private float green = .25f;


    @Override
    public void create () {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);



        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Creating the widget. */
        menu = new RadialGroup(200, .7f);

        /* Populating the widget. */
        for (int i = 0; i < 9; i++) {
            final int tmp = i;
            String name;
            switch(i) {
                case 0:
                    name = "-BLUE-";
                    break;
                case 3:
                    name = "-RED-";
                    break;
                case 6:
                    name = "-GREEN-";
                    break;
                default:
                    name = "white";
                    break;
            }
            TextButton btn = new TextButton(name, skin);
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    switch(tmp) {
                        case 0:
                            red   = .25f;
                            blue  = .75f;
                            green = .25f;
                            break;
                        case 3:
                            red   = .75f;
                            blue  = .25f;
                            green = .25f;
                            break;
                        case 6:
                            red   = .25f;
                            blue  = .25f;
                            green = .75f;
                            break;
                        default:
                            red   = .75f;
                            blue  = .75f;
                            green = .75f;
                            break;
                    }
                }
            });
            menu.addActor(btn);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Show buttons",  skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menu.setVisible(!menu.isVisible());
            }
        });
        textButton.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Align.center);
        stage.addActor(textButton);

        /* Including the widget in the Stage. */
        stage.addActor(menu);
        menu.setVisible(false);
        menu.centerOnActor(textButton);
    }


    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(red, green, blue, 1); // updated with the menu
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Drawing the Stage (no need to update with `Stage#act()`). */
        stage.draw();
    }


    @Override
    public void dispose () {

        /* Disposing is good practice! */
        stage.dispose();
        batch.dispose();
        skin.dispose();
    }
}
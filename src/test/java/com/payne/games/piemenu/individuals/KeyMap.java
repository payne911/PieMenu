package com.payne.games.piemenu.individuals;

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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;

import java.util.HashSet;


public class KeyMap extends ApplicationAdapter {
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
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.background = new TextureRegionDrawable(new Texture(Gdx.files.internal("rael_pie.png"))); // image background!
        style.selectedColor = new Color(1,.5f,.5f,.5f);
        style.downColor = new Color(1,.8f,.8f,.5f);
        menu = new PieMenu(batch, whitePixel, style, 80, 24f/80, 30) {
            /* Since we are using Images, we want to resize them to fit within each sector. */
            @Override
            public void modifyActor(Actor actor, float degreesPerChild, float actorDistanceFromCenter) {
                float size = getEstimatedRadiusAt(degreesPerChild, actorDistanceFromCenter);
                size *= 1.26f; // adjusting the returned value to our likes
                actor.setSize(size, size);
            }
        };

        /* Customizing the behavior. */
        menu.setMiddleCancel(true);
        menu.setDefaultIndex(2);
        menu.setInfiniteSelectionRange(true);
        menu.setPieMenuListener(new PieMenu.PieMenuClickListener() {

            private HashSet<Integer> pressed = new HashSet<>();
            private int currentKey;

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(!(event.getListenerActor() instanceof PieMenu))
                    return false;
                PieMenu pie = (PieMenu)event.getListenerActor();

                boolean numPressed = keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9;
                boolean padPressed = keycode >= Input.Keys.NUMPAD_0 && keycode <= Input.Keys.NUMPAD_9;
                boolean valid = numPressed || padPressed;
                if(valid) {
                    if(numPressed)
                        currentKey = keycode - Input.Keys.NUM_0;
                    else
                        currentKey = keycode - Input.Keys.NUMPAD_0;
                    pie.highlightIndex(currentKey);
                    pressed.add(currentKey);
                }

                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(!(event.getListenerActor() instanceof PieMenu))
                    return false;
                PieMenu pie = (PieMenu)event.getListenerActor();

                boolean numPressed = keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9;
                boolean padPressed = keycode >= Input.Keys.NUMPAD_0 && keycode <= Input.Keys.NUMPAD_9;
                boolean valid = numPressed || padPressed;

                if(valid) {
                    if(numPressed)
                        currentKey = keycode - Input.Keys.NUM_0;
                    else
                        currentKey = keycode - Input.Keys.NUMPAD_0;
                    pressed.remove(currentKey);

                    if(pressed.isEmpty())
                        pie.selectIndex(currentKey);
                    else
                        pie.highlightIndex(pressed.iterator().next());
                }

                return true;
            }
        });

        /* Adding a selection-listener. */
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - newly selected index: " + menu.getSelectedIndex());
                menu.setVisible(false);
                stage.setKeyboardFocus(null);
                menu.remove();
            }
        });

        /* Populating the widget. */
        int key = 0;
        Array<Stack> imgs = new Array<>();
        imgs.add(getNewStack("heart-drop.png", Integer.toString(key++)));
        imgs.add(getNewStack("beer-stein.png", Integer.toString(key++)));
        imgs.add(getNewStack("coffee-mug.png", Integer.toString(key++)));
        imgs.add(getNewStack("gooey-daemon.png", Integer.toString(key++)));
        imgs.add(getNewStack("jeweled-chalice.png", Integer.toString(key++)));
        imgs.add(getNewStack("coffee-mug.png", Integer.toString(key++)));
        for (int i = 0; i < imgs.size; i++)
            menu.addActor(imgs.get(i));


        menu.selectIndex(menu.getDefaultIndex()); // because we would like to trigger that index
    }

    private Stack getNewStack(String img, String key) {
        Stack s = new Stack();
        s.add(new Image(new Texture(Gdx.files.internal(img))));
        Label.LabelStyle lbs = new Label.LabelStyle(skin.get("red", Label.LabelStyle.class));
        s.add(new Label(" " + key, lbs));
        return s;
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
            stage.setKeyboardFocus(menu);
            menu.setVisible(true);
        }
    }


    @Override
    public void dispose () {

        /* Disposing is good practice! */
        batch.dispose();
        tmpTex.dispose();
        stage.dispose();
        skin.dispose();
    }
}

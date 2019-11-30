package com.payne.games.piemenu.individuals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.core.BaseGame;
import com.payne.games.piemenu.core.BaseScreen;

import java.util.HashSet;


public class KeyMap extends BaseScreen {
    private PieMenu menu;

    public KeyMap(BaseGame game) {
        super(game);
    }


    @Override
    public void show() {
        setScreenColor(.2f, .2f, .2f, 1);

        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.background = new TextureRegionDrawable(getTextureAutoDisposable("rael_pie.png")); // image background!
        style.selectedColor = new Color(1, .5f, .5f, .5f);
        style.downColor = new Color(1, .8f, .8f, .5f);
        menu = new PieMenu(game.skin.getRegion("white"), style, 80, 24f / 80, 30) {
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
        menu.setPieMenuListener(new PieMenu.PieMenuListener(menu) {

            private HashSet<Integer> pressed = new HashSet<>();
            private int currentKey;

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (event.getListenerActor() != menu)
                    return false;

                boolean numPressed = keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9;
                boolean padPressed = keycode >= Input.Keys.NUMPAD_0 && keycode <= Input.Keys.NUMPAD_9;
                boolean valid = numPressed || padPressed;
                if (valid) {
                    if (numPressed)
                        currentKey = keycode - Input.Keys.NUM_0;
                    else
                        currentKey = keycode - Input.Keys.NUMPAD_0;
                    menu.highlightIndex(currentKey);
                    pressed.add(currentKey);
                }

                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (event.getListenerActor() != menu)
                    return false;

                boolean numPressed = keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9;
                boolean padPressed = keycode >= Input.Keys.NUMPAD_0 && keycode <= Input.Keys.NUMPAD_9;
                boolean valid = numPressed || padPressed;

                if (valid) {
                    if (numPressed)
                        currentKey = keycode - Input.Keys.NUM_0;
                    else
                        currentKey = keycode - Input.Keys.NUMPAD_0;
                    pressed.remove(currentKey);

                    if (pressed.isEmpty())
                        menu.selectIndex(currentKey);
                    else
                        menu.highlightIndex(pressed.iterator().next());
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
                game.stage.setKeyboardFocus(null);
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
        s.add(new Image(getTextureAutoDisposable(img)));
        Label.LabelStyle lbs = new Label.LabelStyle(game.skin.get("red", Label.LabelStyle.class));
        Container<Label> c = new Container<>(new Label(key, lbs));
        c.align(Align.center);
        s.add(c);
        return s;
    }


    @Override
    public void updateInputPost(float delta) {
        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            game.stage.addActor(menu);
            menu.centerOnMouse();
            game.stage.setKeyboardFocus(menu);
            menu.setVisible(true);
        }
    }
}

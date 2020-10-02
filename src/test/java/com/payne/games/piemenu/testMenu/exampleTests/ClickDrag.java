package com.payne.games.piemenu.testMenu.exampleTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;


public class ClickDrag extends BaseScreen {
    private PieMenu menu;

    public ClickDrag(TestsMenu game) {
        super(game);
    }


    @Override
    public void show() {
        setScreenColor(.25f, .25f, .75f, 1f);

        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.separatorWidth = 2;
        style.circumferenceWidth = 2;
        style.backgroundColor = new Color(1, 1, 1, .1f);
        style.separatorColor = new Color(.1f, .1f, .1f, 1);
        style.downColor = new Color(.5f, .5f, .5f, 1);
        style.sliceColor = new Color(.33f, .33f, .33f, 1);
        menu = new PieMenu(game.skin.getRegion("white"), style, 80, 0, 90);

        /* Customizing the behavior. */
        menu.setInfiniteSelectionRange(true);
        menu.setSelectionButton(Input.Buttons.RIGHT); // right-click for interactions with the widget

        /* Setting up listeners. */
        menu.addListener(new PieMenu.PieMenuCallbacks() {
            @Override
            public void onHighlightChange(int highlightedIndex) {
                switch (highlightedIndex) {
                    case 0:
                        setScreenColor(.75f, .25f, .25f, 1); // Red
                        break;
                    case 1:
                        setScreenColor(.25f, .75f, .25f, 1); // Green
                        break;
                    case 2:
                        setScreenColor(.25f, .25f, .75f, 1); // Blue
                        break;
                    default: // Just in case...
                        setScreenColor(.75f, .75f, .75f, 1);
                        break;
                }
            }
        });
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - selected index: " + menu.getSelectedIndex());
                menu.setVisible(false);
                menu.remove();
            }
        });

        /* Populating the widget. */
        Label red = new Label("red", game.skin);
        menu.addActor(red);
        Label green = new Label("green", game.skin);
        menu.addActor(green);
        Label blue = new Label("blue", game.skin);
        menu.addActor(blue);
    }


    @Override
    public void updateInputPost(float delta) {
        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        if (Gdx.input.isButtonJustPressed(menu.getSelectionButton())) {
            game.stage.addActor(menu);
            menu.centerOnMouse();
            menu.setVisible(true);
            transferInteraction(game.stage, menu);
        }
    }

    /**
     * To be used to get the user to transition directly into
     * {@link InputListener#touchDragged(InputEvent, float, float, int)}
     * as if he had triggered
     * {@link InputListener#touchDown(InputEvent, float, float, int, int)}.<br>
     * I am not certain this is the recommended way of doing this, but for the
     * purposes of this demonstration, it works!
     *
     * @param stage  the stage.
     * @param widget the PieMenu on which to transfer the interaction.
     */
    private void transferInteraction(Stage stage, PieMenu widget) {
        if (widget == null) throw new IllegalArgumentException("widget cannot be null.");
        if (widget.getPieMenuListener() == null) throw new IllegalArgumentException("inputListener cannot be null.");
        stage.addTouchFocus(widget.getPieMenuListener(), widget, widget, 0, widget.getSelectionButton());
    }
}

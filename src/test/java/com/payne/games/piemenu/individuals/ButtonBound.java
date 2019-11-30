package com.payne.games.piemenu.individuals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.payne.games.piemenu.AnimatedPieMenu;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.core.BaseGame;
import com.payne.games.piemenu.core.BaseScreen;


public class ButtonBound extends BaseScreen {
    private AnimatedPieMenu menu;

    public ButtonBound(BaseGame game) {
        super(game);
    }


    @Override
    public void show() {
        setScreenColor(.2f, .2f, .2f, 1);

        /* Adding a Table. */
        Table root = new Table();
        root.setFillParent(true);
        root.defaults().padBottom(150);
        game.stage.addActor(root);

        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.backgroundColor = new Color(1, 1, 1, .3f);
        style.selectedColor = new Color(.7f, .3f, .5f, 1);
        style.sliceColor = new Color(0, .7f, 0, 1);
        style.alternateSliceColor = new Color(.7f, 0, 0, 1);
        menu = new AnimatedPieMenu(game.skin.getRegion("white"), style, 130, 50f / 130, 180, 320);

        /* Customizing the behavior. */
        menu.setInfiniteSelectionRange(true);

        /* Populating the widget. */
        for (int i = 0; i < 5; i++) {
            Label label = new Label(Integer.toString(i), game.skin);
            menu.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Drag Pie", game.skin);
        textButton.addListener(new ClickListener() {
            /*
            In our particular case, we want to NOT use a ChangeListener because
            else the user would have to release his mouse-click before seeing
            the menu, which goes against our current goal of obtaining a
            "drag-selection" menu.
            */
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menu.resetSelection();
                menu.centerOnActor(textButton);
                menu.animateOpening(.4f);
                transferInteraction(game.stage, menu);
                return true;
            }
        });
        root.add(textButton).expand().bottom();

        /* Adding a selection-listener. */
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menu.transitionToClosing(.4f);
                int index = menu.getSelectedIndex();
                if (!menu.isValidIndex(index)) {
                    textButton.setText("Drag Pie");
                    return;
                }
                Actor child = menu.getChild(index);
                textButton.setText(((Label) child).getText().toString());
            }
        });

        /* Including the Widget in the Stage. */
        game.stage.addActor(menu);
        menu.setVisible(false);
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

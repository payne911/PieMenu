package com.payne.games.piemenu.testMenu.exampleTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;


public class Permanent extends BaseScreen {
    private PieMenu menu;

    /* For the demonstration's purposes. */
    private Color backgroundColor = new Color(1, 1, 1, .2f);

    public Permanent(TestsMenu game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.8f, .4f, 0, 1);

        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.circumferenceWidth = 2;
        style.backgroundColor = backgroundColor;
        style.downColor = new Color(.5f, .5f, .5f, 1);
        style.sliceColor = new Color(.33f, .33f, .33f, 1);
        style.alternateSliceColor = new Color(.25f, .25f, .25f, 1);
        style.circumferenceColor = new Color(0, 0, 0, 1);
        menu = new PieMenu(game.skin.getRegion("white"), style, 100, 30f / 100, 0, 180);

        /* Adding a selection-listener. */
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float alpha = MathUtils.map(0, menu.getAmountOfChildren() - 1, 0, 1, menu.getSelectedIndex());
                menu.getStyle().backgroundColor.set(backgroundColor.r, backgroundColor.g, backgroundColor.b, alpha);
            }
        });

        /* Populating the widget. */
        for (int i = 0; i < 5; i++) {
            Label label = new Label(Integer.toString(i), game.skin);
            menu.addActor(label);
        }

        /* Customizing the behavior. */
        menu.setDefaultIndex(2);

        /* Including the Widget at some absolute coordinate in the World. */
        menu.setPosition(Gdx.graphics.getWidth() / 2f, 0, Align.center); // positioning along the edge
        game.stage.addActor(menu);
    }
}

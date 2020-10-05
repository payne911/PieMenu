package com.payne.games.piemenu.testMenu.exampleTests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.payne.games.piemenu.AnimatedPieWidget;
import com.payne.games.piemenu.PieWidget;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;

public class ButtonToggle extends BaseScreen {
    private AnimatedPieWidget widget;

    public ButtonToggle(TestsMenu game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.2f, .2f, .8f, 1);

        /* Adding a Table. */
        Table root = new Table();
        root.setFillParent(true);
        root.defaults().padBottom(150);
        game.stage.addActor(root);

        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Setting up and creating the widget. */
        PieWidget.PieWidgetStyle style = new PieWidget.PieWidgetStyle();
        style.sliceColor = new Color(1, 1, 1, .35f);
        style.separatorWidth = 2;
        style.circumferenceWidth = 2;
        style.separatorColor = style.circumferenceColor;
        widget = new AnimatedPieWidget(game.skin.getRegion("white"), style, 110, 50f / 110, 315, 270);

        /* Populating the widget. */
        for (int i = 0; i < 8; i++) {
            Label label = new Label(Integer.toString(i), game.skin);
            widget.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Toggle", game.skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                widget.toggleVisibility(.9f); // 0.9 seconds animation
                widget.setPosition(textButton.getX(Align.center),
                        textButton.getY(Align.center) - 5, Align.center);
            }
        });
        root.add(textButton).expand().bottom();

        /* Including the Widget in the Stage. */
        game.stage.addActor(widget);
        widget.setVisible(false);
    }
}

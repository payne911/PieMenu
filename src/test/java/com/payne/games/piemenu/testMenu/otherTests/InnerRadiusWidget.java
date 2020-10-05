package com.payne.games.piemenu.testMenu.otherTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.payne.games.piemenu.PieWidget;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;

public class InnerRadiusWidget extends BaseScreen {
    private PieWidget widget;

    public InnerRadiusWidget(TestsMenu game) {
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
        style.backgroundColor = new Color(.4f, .2f, .6f, .6f);
        style.sliceColor = new Color(1, 1, 1, .35f);
        style.separatorWidth = 2;
        style.circumferenceWidth = 2;
        style.separatorColor = style.circumferenceColor;
        widget = new PieWidget(game.skin.getRegion("white"), style, 110, 50f / 110, 315, 270);

        /* Customizing the behavior. */
        widget.setHitThroughInnerRadius(false);

        /* Populating the widget. */
        for (int i = 0; i < 8; i++) {
            Label label = new Label(Integer.toString(i), game.skin);
            widget.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Toggle Button", game.skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                widget.setPosition(textButton.getX(Align.center),
                        textButton.getY(Align.center) - 5, Align.center);
                widget.setVisible(!widget.isVisible());
            }
        });
        root.add(textButton).expand().bottom();

        /* Including the Widget in the Stage. */
        game.stage.addActor(widget);
        widget.setVisible(false);
    }

    @Override
    public void updateInputPost(float delta) {
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            widget.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(widget.getRotation());
        }
    }
}

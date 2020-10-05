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
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;

public class InnerRadiusMenu extends BaseScreen {
    private PieMenu menu;

    public InnerRadiusMenu(TestsMenu game) {
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
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.separatorWidth = 2;
        style.circumferenceWidth = 2;
        style.backgroundColor = new Color(1, 1, 1, .3f);
        style.separatorColor = new Color(.1f, .1f, .1f, 1);
        style.downColor = new Color(.5f, .5f, .5f, 1);
        style.sliceColor = new Color(.33f, .33f, .33f, 1);
        menu = new PieMenu(game.skin.getRegion("white"), style, 110, 50f / 110, 315, 270);

        /* Customizing the behavior. */
        menu.setInfiniteSelectionRange(true);

        /* Populating the widget. */
        Label red = new Label("red", game.skin);
        menu.addActor(red);
        Label green = new Label("green", game.skin);
        menu.addActor(green);
        Label blue = new Label("blue", game.skin);
        menu.addActor(blue);

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

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Toggle Button", game.skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menu.setPosition(textButton.getX(Align.center),
                        textButton.getY(Align.center) - 5, Align.center);
                menu.setVisible(!menu.isVisible());
            }
        });
        root.add(textButton).expand().bottom();

        /* Including the Widget in the Stage. */
        game.stage.addActor(menu);
        menu.setVisible(false);
    }

    @Override
    public void updateInputPost(float delta) {
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            menu.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(menu.getRotation());
        }
    }
}

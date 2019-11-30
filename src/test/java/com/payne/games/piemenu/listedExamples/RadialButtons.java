package com.payne.games.piemenu.listedExamples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.payne.games.piemenu.RadialGroup;
import com.payne.games.piemenu.core.BaseScreen;
import com.payne.games.piemenu.core.TestsMenu;


public class RadialButtons extends BaseScreen {
    private RadialGroup menu;

    public RadialButtons(TestsMenu game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.25f, .25f, .75f, 1f);

        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Creating the widget. */
        menu = new RadialGroup(200, .7f);

        /* Populating the widget. */
        for (int i = 0; i < 9; i++) {
            final int tmp = i;
            String name;
            switch (i) {
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
            TextButton btn = new TextButton(name, game.skin);
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    switch (tmp) {
                        case 0:
                            screenColorRed = .25f;
                            screenColorBlue = .75f;
                            screenColorGreen = .25f;
                            break;
                        case 3:
                            screenColorRed = .75f;
                            screenColorBlue = .25f;
                            screenColorGreen = .25f;
                            break;
                        case 6:
                            screenColorRed = .25f;
                            screenColorBlue = .25f;
                            screenColorGreen = .75f;
                            break;
                        default:
                            screenColorRed = .75f;
                            screenColorBlue = .75f;
                            screenColorGreen = .75f;
                            break;
                    }
                }
            });
            menu.addActor(btn);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Show buttons", game.skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menu.setVisible(!menu.isVisible());
            }
        });
        textButton.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);
        game.stage.addActor(textButton);

        /* Including the widget in the Stage. */
        game.stage.addActor(menu);
        menu.setVisible(false);
        menu.centerOnActor(textButton);
    }
}

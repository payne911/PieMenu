package com.payne.games.piemenu.listedExamples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.core.BaseScreen;
import com.payne.games.piemenu.core.TestsMenu;


public class ClickToggle extends BaseScreen {
    private PieMenu menu;

    public ClickToggle(TestsMenu game) {
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
        menu.setInfiniteSelectionRange(true);

        /* Adding a selection-listener. */
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - newly selected index: " + menu.getSelectedIndex());
                menu.setVisible(false);
                menu.remove();
            }
        });

        /* Populating the widget. */
        Array<Image> imgs = new Array<>();
        imgs.add(new Image(getTextureAutoDisposable("heart-drop.png")));
        imgs.add(new Image(getTextureAutoDisposable("beer-stein.png")));
        imgs.add(new Image(getTextureAutoDisposable("coffee-mug.png")));
        imgs.add(new Image(getTextureAutoDisposable("gooey-daemon.png")));
        imgs.add(new Image(getTextureAutoDisposable("jeweled-chalice.png")));
        imgs.add(new Image(getTextureAutoDisposable("coffee-mug.png")));
        for (int i = 0; i < imgs.size; i++)
            menu.addActor(imgs.get(i));
    }


    @Override
    public void updateInputPost(float delta) {
        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            game.stage.addActor(menu);
            menu.centerOnMouse();
            menu.resetSelection();
            menu.setVisible(true);
        }
    }
}

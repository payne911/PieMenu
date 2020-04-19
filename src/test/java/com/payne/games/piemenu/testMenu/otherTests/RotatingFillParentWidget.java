package com.payne.games.piemenu.testMenu.otherTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;


public class RotatingFillParentWidget extends BaseScreen {
    private PieMenu menu;

    public RotatingFillParentWidget(TestsMenu game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.4f, .4f, .4f, 1);
        game.enableDebug();

        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.hoverColor = Color.RED;
        style.selectedColor = Color.BLUE;
        style.background = new TextureRegionDrawable(getTextureAutoDisposable("rael_pie.png"));
        menu = new PieMenu(game.skin.getRegion("white"), style, 80, 24f / 80, 30);
        menu.setGlobalAlphaMultiplier(.5f);

        for (int i = 0; i < 6; i++)
            menu.addActor(new Label("menu " + i, game.skin));

        menu.addListener(new PieMenu.PieMenuCallbacks() {
            @Override
            public void onHoverChange(int hoveredIndex) {
                System.out.println("hovered: " + hoveredIndex);
            }
        });


        game.stage.addActor(menu);
        menu.setFillParent(true);
        menu.drawRudimentaryDebug();
    }

    @Override
    public void updateInputPost(float delta) {
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            menu.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(menu.getRotation());
        }
        if (Gdx.input.isKeyPressed(Keys.M)) {
            menu.setPosition(10, 100);
        }
    }
}
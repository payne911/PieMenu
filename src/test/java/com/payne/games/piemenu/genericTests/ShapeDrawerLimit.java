package com.payne.games.piemenu.genericTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.core.BaseGame;
import com.payne.games.piemenu.core.BaseScreen;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class ShapeDrawerLimit extends BaseScreen {
    private PieMenu menu;

    public ShapeDrawerLimit(BaseGame game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.4f, .4f, .4f, 1);
        //game.enableDebug();

        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style1 = new PieMenu.PieMenuStyle();
        style1.hoverColor = Color.RED;
        style1.selectedColor = Color.BLUE;
        style1.backgroundColor = Color.ORANGE;
        menu = new PieMenu(game.skin.getRegion("white"), style1, 250); // at "5092" it crashes

        for (int i = 0; i < 5; i++)
            menu.addActor(new Label("menu " + i, game.skin));


        menu.setShapeDrawer(new ShapeDrawer(game.batch, game.skin.getRegion("white"))); // default "estimateSidesRequired"

        game.stage.addActor(menu);
        menu.setPosition(180, 180);
//        menu.drawRudimentaryDebug();
    }

    @Override
    public void updateInputPost(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            menu.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(menu.getRotation());
        }
    }
}
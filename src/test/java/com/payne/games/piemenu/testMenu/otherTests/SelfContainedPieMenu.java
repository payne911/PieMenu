package com.payne.games.piemenu.testMenu.otherTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.PieWidget;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;


public class SelfContainedPieMenu extends BaseScreen {
    private PieWidget containerWidget;

    public SelfContainedPieMenu(TestsMenu game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.4f, .4f, .4f, 1);
        game.enableDebug();

        /* Adding the demo widgets. */
        PieWidget.PieWidgetStyle style1 = new PieWidget.PieWidgetStyle();
//        style1.sliceColor = Color.ORANGE;
//        style1.alternateSliceColor = new Color(.8f, .5f, .2f, 1);
        style1.circumferenceWidth = 2;
        style1.separatorWidth = 2;
        containerWidget = new PieWidget(game.skin.getRegion("white"), style1, 500, 0.5f);
        containerWidget.setName("MAIN");

        PieMenu.PieMenuStyle style2 = new PieMenu.PieMenuStyle();
        style2.hoverColor = Color.GREEN;
        style2.selectedColor = Color.MAGENTA;
        style2.backgroundColor = Color.BROWN;
        for (int i = 0; i < 5; i++) {
            PieMenu tmp = new PieMenu(game.skin.getRegion("white"), style2, 110);
            tmp.setName("menu " + i);
            containerWidget.addActor(tmp);
            for (int j = 0; j < 5; j++)
                tmp.addActor(new Label(i + " " + j, game.skin));
//            tmp.setWidth(50);
//            tmp.setHeight(50);

        }


        game.stage.addActor(containerWidget);
//        containerWidget.setWidth(1600);
        containerWidget.setFillParent(true);
    }

    @Override
    public void updateInputPost(float delta) {
        containerWidget.centerOnScreen();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            containerWidget.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(containerWidget.getRotation());
        }

    }
}
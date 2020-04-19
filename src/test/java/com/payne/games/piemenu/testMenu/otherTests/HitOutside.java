package com.payne.games.piemenu.testMenu.otherTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.payne.games.piemenu.PieWidget;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;


public class HitOutside extends BaseScreen {
    private PieWidget containerWidget;

    public HitOutside(TestsMenu game) {
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
        style1.circumferenceColor = Color.BLACK;
        style1.separatorWidth = 2;
        style1.separatorColor = Color.BLACK;
        containerWidget = new PieWidget(game.skin.getRegion("white"), style1, 200, 0.9f);
        containerWidget.setName("MAIN");

        for (int i = 0; i < 5; i++) {
            TextButton tmp = new TextButton("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", game.skin);
            containerWidget.addActor(tmp);
        }


        game.stage.addActor(containerWidget);
//        containerWidget.setFillParent(true);
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
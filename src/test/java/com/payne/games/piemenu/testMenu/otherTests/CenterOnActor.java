package com.payne.games.piemenu.testMenu.otherTests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.payne.games.piemenu.PieWidget;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;


public class CenterOnActor extends BaseScreen {
    private Label testLabel;
    private PieWidget widget;

    public CenterOnActor(TestsMenu game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.4f, .4f, .4f, 1);

        /* Setting up the Stage. */
        game.enableDebug();

        /* Adding the demo widgets. */
        PieWidget.PieWidgetStyle style1 = new PieWidget.PieWidgetStyle();
//        style1.sliceColor = Color.ORANGE;
//        style1.alternateSliceColor = new Color(.8f, .5f, .2f, 1);
        style1.circumferenceWidth = 2;
        style1.separatorWidth = 2;
        widget = new PieWidget(game.skin.getRegion("white"), style1, 200, 0.9f);

        for (int i = 0; i < 4; i++) {
            TextButton tmp = new TextButton("XXX", game.skin);
            widget.addActor(tmp);
        }

        game.stage.addActor(widget);


        /* The Group to test coordinates with. */
        Group group = new Group();
        Table table = new Table(game.skin);
        table.add(new Label("Bobbbbbbbbbbbb", game.skin));
        testLabel = new Label("Test", game.skin);
        table.add(testLabel);
        group.addActor(table);
        game.stage.addActor(group);
        group.setPosition(250, 250);
    }

    @Override
    public void updateInputPost(float delta) {
        widget.centerOnActor(testLabel);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            widget.rotateBy(Gdx.graphics.getDeltaTime() * 100);
            System.out.println(widget.getRotation());
        }
    }
}
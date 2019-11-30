package com.payne.games.piemenu.genericTests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.core.BaseGame;
import com.payne.games.piemenu.core.BaseScreen;


public class TableWidgets extends BaseScreen {

    public TableWidgets(BaseGame game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.2f, .2f, .7f, 1);

        Table root = new Table();
        root.debugAll();
        root.setFillParent(true);
        game.stage.addActor(root);

        /* Top label. */
        root.add(new Label("R: restart\n" +
                "S: toggle the permanent pie\n" +
                "Middle-click / Right-click: try it out!", game.skin)).colspan(2).padTop(25).top();

        /* New row. */
        root.row().padBottom(20).uniform();

        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style1 = new PieMenu.PieMenuStyle();
        style1.hoverColor = Color.RED;
        style1.selectedColor = Color.BLUE;
        style1.backgroundColor = Color.ORANGE;
        PieMenu menu1 = new PieMenu(game.skin.getRegion("white"), style1, 80);
        menu1.setName("left widget");

        for (int i = 0; i < 5; i++)
            menu1.addActor(new Label("menu " + i, game.skin));

//        menu1.setPosition(stage.getWidth()/2 + 100, stage.getHeight()/2, Align.center);
//        stage.addActor(menu1);
        menu1.drawRudimentaryDebug();
        root.add(menu1).fill();

        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style2 = new PieMenu.PieMenuStyle();
        style2.hoverColor = Color.RED;
        style2.selectedColor = Color.BLUE;
        style2.backgroundColor = Color.ORANGE;
        PieMenu menu2 = new PieMenu(game.skin.getRegion("white"), style2, 120);
        menu2.setName("right widget");

        for (int i = 0; i < 5; i++)
            menu2.addActor(new Label("menu " + i, game.skin));

//        menu2.setPosition(0, 0, Align.bottomLeft);
//        stage.addActor(menu2);
        menu2.drawRudimentaryDebug();
        root.add(menu2);

        /* New row. */
        root.row().colspan(2);

        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style3 = new PieMenu.PieMenuStyle();
        style3.hoverColor = Color.RED;
        style3.selectedColor = Color.BLUE;
        style3.backgroundColor = Color.ORANGE;
        PieMenu menu3 = new PieMenu(game.skin.getRegion("white"), style3, 40);

        for (int i = 0; i < 5; i++)
            menu3.addActor(new Label("menu " + i, game.skin));

//        menu3.setPosition(stage.getWidth(), stage.getHeight(), Align.topRight);
//        stage.addActor(menu3);
        menu3.drawRudimentaryDebug();
        root.add(menu3);



        /* Some buttons, for comparison. */
        root.row().padLeft(50);
        Button b1 = new Button(game.skin);
        Button b2 = new TextButton("textBtn", game.skin);
        Button b3 = new ImageTextButton("imgTxt", game.skin);

        root.add(b1).fill();
        root.add(b2, b3);
    }
}
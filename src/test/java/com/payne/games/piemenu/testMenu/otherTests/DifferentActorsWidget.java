package com.payne.games.piemenu.testMenu.otherTests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;


public class DifferentActorsWidget extends BaseScreen {
    private PieMenu menu;

    public DifferentActorsWidget(TestsMenu game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(0, 0, 0, 1);

        /* Adding the demo widgets. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.hoverColor = Color.RED;
        style.selectedColor = Color.BLUE;
        style.backgroundColor = Color.ORANGE;
        menu = new PieMenu(game.skin.getRegion("white"), style, 400) {
            @Override
            public float getActorDistanceFromCenter(Actor actor) {
                invalidate();
                return getAmountOfChildren() > 1
                        ? getCurrentRadius() - getChild(0).getWidth()
                        : 0;
            }

            @Override
            public void modifyActor(Actor actor, float degreesPerChild, float actorDistanceFromCenter) {
                float size = getEstimatedRadiusAt(degreesPerChild, actorDistanceFromCenter);
                size *= 1.45f;
                if (!(actor instanceof List))
                    actor.setSize(size, size);
                else
                    actor.setSize(size * 3f, size * 1.4f);
            }
        };

        /* Populating with a bunch of different Actor types. */
        menu.addActor(new TextButton("txtBtn", game.skin));
        menu.addActor(new Image(getTextureAutoDisposable("heart-drop.png")));
        menu.addActor(new Button(game.skin));
        menu.addActor(new Label("label", game.skin));
        menu.addActor(new ImageTextButton("imgTxtBtn", game.skin));
        menu.addActor(new TextField("txtField", game.skin));
        menu.addActor(new ImageButton(new Image(getTextureAutoDisposable("coffee-mug.png")).getDrawable()));
        menu.addActor(new CheckBox("chkBox", game.skin));
        menu.addActor(new TextArea("txtArea", game.skin));
        List<Label> myList = new List<>(game.skin);
        myList.setItems(new Label("item1", game.skin), new Label("item2", game.skin));
        menu.addActor(myList);
        SelectBox<Label> myBox = new SelectBox<>(game.skin);
        myBox.setItems(new Label("item1", game.skin), new Label("item2", game.skin));
        menu.addActor(myBox);
        menu.addActor(new ProgressBar(0, 10, 5, false, game.skin));
        menu.addActor(new Slider(0, 10, 5, false, game.skin));
        menu.addActor(new Window("window", game.skin));
        menu.addActor(new Touchpad(50, game.skin));
        menu.addActor(new Container<>(new Label("containedLbl", game.skin)));
        menu.addActor(new Stack(new Image(getTextureAutoDisposable("jeweled-chalice.png")), new Label("stackLbl", game.skin)));


        menu.drawRudimentaryDebug();
        menu.setFillParent(true);

        game.stage.addActor(menu);
    }
}
package com.payne.games.piemenu.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.payne.games.piemenu.Demonstration;
import com.payne.games.piemenu.listedExamples.ActionMenu;
import com.payne.games.piemenu.listedExamples.ButtonBound;
import com.payne.games.piemenu.listedExamples.ButtonToggle;
import com.payne.games.piemenu.listedExamples.ClickDrag;
import com.payne.games.piemenu.listedExamples.ClickToggle;
import com.payne.games.piemenu.listedExamples.CustomAnimation;
import com.payne.games.piemenu.listedExamples.KeyMap;
import com.payne.games.piemenu.listedExamples.NestedClickDrag;
import com.payne.games.piemenu.listedExamples.Permanent;
import com.payne.games.piemenu.listedExamples.RadialButtons;
import com.payne.games.piemenu.visualTests.CenterOnActor;
import com.payne.games.piemenu.visualTests.DifferentActorsWidget;
import com.payne.games.piemenu.visualTests.HitInfiniteMiddle;
import com.payne.games.piemenu.visualTests.HitOutside;
import com.payne.games.piemenu.visualTests.MgsxTests;
import com.payne.games.piemenu.visualTests.RotatingFillParentWidget;
import com.payne.games.piemenu.visualTests.SelfContainedPieMenu;
import com.payne.games.piemenu.visualTests.ShapeDrawerLimit;
import com.payne.games.piemenu.visualTests.TableWidgets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainScreen extends BaseScreen {

    // todo: this list should be populated automatically (so as to respect the OC principle)
    public static final List<Class<? extends BaseScreen>> registeredTests = new ArrayList<>(
            Arrays.asList(
                    Demonstration.class,

                    CenterOnActor.class,
                    DifferentActorsWidget.class,
                    HitInfiniteMiddle.class,
                    HitOutside.class,
                    MgsxTests.class,
                    RotatingFillParentWidget.class,
                    SelfContainedPieMenu.class,
                    ShapeDrawerLimit.class,
                    TableWidgets.class,

                    ActionMenu.class,
                    ButtonBound.class,
                    ButtonToggle.class,
                    ClickDrag.class,
                    ClickToggle.class,
                    CustomAnimation.class,
                    KeyMap.class,
                    NestedClickDrag.class,
                    Permanent.class,
                    RadialButtons.class
            ));

    private Table tableRoot;

    public MainScreen(TestsMenu game) {
        super(game);

        autoDispose = false;

        tableRoot = new Table(game.skin);
        tableRoot.setFillParent(true);

        Table localTableContent = new Table(game.skin);
        ScrollPane localScrollPane = new ScrollPane(localTableContent, game.skin);
        localScrollPane.setFlickScroll(false);
        localScrollPane.setOverscroll(false, false);
        localScrollPane.setFadeScrollBars(false);

        tableRoot.add("Select A Test")
                .expandX()
                .pad(10)
                .getActor().setFontScale(1.2f);
        tableRoot.row();
        tableRoot.add(localScrollPane).grow();
        tableRoot.row();
        tableRoot.add("Press CTRL + NumPad- to switch to the previous test\n" +
                "Press CTRL + NumPad+ to switch to the next test\n" +
                "Press CTRL + R to restart the current test\n" +
                "Press CTRL + D to toggle debug\n" +
                "Press Esc to exit to main menu")
                .left()
                .growX()
                .pad(10);

        for (int i = 0; i < registeredTests.size(); i++) {
            Class localClass = registeredTests.get(i);
            Label localLabel = new Label("Test : " + localClass.getSimpleName(), game.skin, "white");
            localLabel.setColor(Color.BLACK);
            localTableContent.add(localLabel).left();
            localTableContent.row();
            localLabel.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    game.stage.setScrollFocus(localScrollPane);
                    localLabel.setColor(Color.WHITE);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    if (!isOver()) {
                        localLabel.setColor(Color.BLACK);
                    }
                }

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    try {
                        Constructor localConstructor = ClassReflection.getConstructor(localClass, TestsMenu.class);
                        game.setScreen((Screen) localConstructor.newInstance(game));
                    } catch (ReflectionException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void show() {
        game.stage.addActor(tableRoot);
        game.refreshDebug();
    }
}

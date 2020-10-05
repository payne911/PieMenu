package com.payne.games.piemenu.testMenu.otherTests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.testMenu.core.BaseScreen;
import com.payne.games.piemenu.testMenu.core.TestsMenu;


public class MgsxTests extends BaseScreen {
    final int SIZE = 40;

    public MgsxTests(TestsMenu game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.2f, .2f, .7f, 1);
        game.enableDebug();

        SettingsPOC poc = new SettingsPOC(game.stage, game.skin);
        game.stage.addActor(poc);


//        Group group = new Group();
//        PieMenu menu = new PieMenu(game.skin.getRegion("white"), PieMenuUtils.getStyle(), SIZE, .2f);
//        fillMenu(menu, game.skin, "A", "D", "E");
//        group.addActor(menu);
//        menu.pack();
//        group.setPosition(100, 100);
//        addListeners(menu);
//        game.stage.addActor(group);


//        Group group2 = new Group();
//        PieMenu menu2 = new PieMenu(game.skin.getRegion("white"), PieMenuUtils.getStyle(), SIZE, .2f);
//        fillMenu(menu2, game.skin, "A", "D", "E");
//        group2.addActor(menu2);
//        menu2.setPieMenuListener(new InputListener());
//        menu.pack();
//        game.stage.addActor(group2);


//        PieMenu menu3 = new PieMenu(game.skin.getRegion("white"), PieMenuUtils.getStyle(), SIZE, .2f);
//        fillMenu(menu3, game.skin, "A", "D", "E");
//        menu3.pack();
//        menu3.setPosition(200, 200);
//        addListeners(menu3);
//        game.stage.addActor(menu3);
    }

    private void fillMenu(PieMenu menu, Skin skin, String... labels) {
        for (String label : labels) {
            menu.addActor(new Label(label, skin));
        }
    }

    int name = 0;

    private void addListeners(PieMenu menu) {

        menu.setName(Integer.toString(name++));

        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Selected index: " + menu.getSelectedIndex());
            }
        });

        menu.addListener(new PieMenu.PieMenuCallbacks() {
            @Override
            public void onHighlightChange(int highlightedIndex) {
                System.out.println("Highlighted index: " + highlightedIndex);
            }

            @Override
            public void onHoverChange(int hoveredIndex) {
                System.out.println("Hovered index: " + hoveredIndex);
            }
        });


    }

    public class SettingsPOC extends Group {

        public SettingsPOC(Stage stage, Skin skin) {

            setTransform(false);
            setSize(stage.getWidth(), stage.getHeight());

            PieMenu.PieMenuStyle style = PieMenuUtils.getStyle();
//            PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
//            style.backgroundColor = Color.GRAY;
//            style.hoverColor = Color.GREEN;
//            style.selectedColor = Color.ORANGE;
//            style.separatorWidth = 1;
            PieMenu menu;
            Table table;

            float spaceX = 20;
            float offsetX = spaceX;

            // floating case (OK)
            menu = new PieMenu(skin.getRegion("white"), style, SIZE, .2f);
            fillMenu(menu, skin, "A", "B", "C");
            menu.pack();
            menu.setPosition(offsetX, stage.getHeight() - menu.getHeight());
            addActor(menu);
            offsetX += menu.getWidth() + spaceX;
            addListeners(menu);


            // floating table case. (KO)
            table = new Table(skin);
            table.add("Title very long").row();
            menu = new PieMenu(skin.getRegion("white"), style, SIZE, .2f);
            fillMenu(menu, skin, "A", "D", "E");
            table.add(menu);
            table.pack();
            table.setPosition(offsetX, stage.getHeight() - table.getHeight());
            addActor(table);
            offsetX += table.getWidth() + spaceX;
            addListeners(menu);

            // floating table case 2. (KO)
            table = new Table(skin);
            menu = new PieMenu(skin.getRegion("white"), style, SIZE, .2f);
            fillMenu(menu, skin, "A", "F", "G");
            table.add(menu).row();
            table.add("Title").row();
            table.pack();
            table.setPosition(offsetX, stage.getHeight() - table.getHeight());
            addActor(table);
            offsetX += table.getWidth() + spaceX;
            addListeners(menu);

            // floating table with multiple. (KO)
            table = new Table(skin);
            for (int i = 0; i < 3; i++) {
                menu = new PieMenu(skin.getRegion("white"), style, SIZE, .2f);
                fillMenu(menu, skin, "A", "H", "I");
                table.add(menu);
                addListeners(menu);
            }
            table.pack();
            table.setPosition(offsetX, stage.getHeight() - table.getHeight());
            addActor(table);
            offsetX += table.getWidth() + spaceX;

            // floating table case with grow. (KO)
            table = new Table(skin);
            menu = new PieMenu(skin.getRegion("white"), style, 10, .2f);
            fillMenu(menu, skin, "A", "J", "K");
            table.add(menu).grow().row();
            table.setSize(400, 300);
            table.setPosition(0, stage.getHeight() - table.getHeight() - 300);
            addActor(table);
            addListeners(menu);

            // table case with fill parent. (OK)
            table = new Table(skin);
            menu = new PieMenu(skin.getRegion("white"), style, SIZE, .2f);
            fillMenu(menu, skin, "A", "L", "M");
            table.add(menu).expand().right().bottom().row();
            table.setFillParent(true);
            addActor(table);
            addListeners(menu);

            debugAll();
        }
    }


    public static class PieMenuUtils {
        public static PieMenu.PieMenuStyle getStyle() {
            PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
            style.backgroundColor = Color.BROWN;
            style.selectedColor = Color.ORANGE;
            style.hoverColor = style.backgroundColor.cpy().lerp(style.selectedColor, .5f);
            style.hoverSelectedColor = style.hoverColor.cpy().lerp(style.selectedColor, .5f);
            // XXX style.highlightedChildRegionColor = style.selectedChildRegionColor.cpy().lerp(Color.WHITE, .5f);
            style.circumferenceColor = Color.BROWN.cpy().lerp(Color.BLACK, .5f);
            style.circumferenceWidth = 5f;
            style.separatorColor = style.circumferenceColor;
            style.separatorWidth = style.circumferenceWidth;
            return style;
        }
    }
}
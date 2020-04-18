package com.payne.games.piemenu.listedExamples;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.core.BaseScreen;
import com.payne.games.piemenu.core.TestsMenu;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;


public class CustomAnimation extends BaseScreen {
    private PieMenu pieMenu;
    private ShapeDrawer sd;

    /* Specific to this demonstration's widget. */
    private float time;
    private final float BASE_RADIUS = 180;

    public CustomAnimation(TestsMenu game) {
        super(game);
    }


    @Override
    public void show() {
        setScreenColor(0, 0, 0, 1);

        /* Basic setup. */
        sd = new ShapeDrawer(game.stage.getBatch(), game.skin.getRegion("white")) {
            /* OPTIONAL: Increasing the precision (at the possible cost of performance). */
            @Override
            protected int estimateSidesRequired(float radiusX, float radiusY) {
                return 4 * super.estimateSidesRequired(radiusX, radiusY);
            }
        };

        /* Setting up the Widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.selectedColor = new Color(1, 0, 0, .5f);
        style.downColor = new Color(1, .05f, .05f, .4f);
        style.hoverColor = new Color(.8f, .8f, .8f, .05f);
        style.hoverSelectedColor = new Color(1, .2f, .2f, .55f);
        pieMenu = new PieMenu(game.skin.getRegion("white"), style, BASE_RADIUS) {
            @Override
            public float getActorDistanceFromCenter(Actor actor) {

                /* We want the Labels to be placed closer to the edge than the default value. */
                return getAmountOfChildren() > 1
                        ? getCurrentRadius() - getChild(0).getWidth()
                        : 0;
            }

            @Override
            public void act(float delta) {
                super.act(delta);

                /* Our custom animation! */
                time += delta * 5;
                pieMenu.setStartDegreesOffset((time * 10) % 360);
                pieMenu.setPreferredRadius(MathUtils.sin(time) * 20 + BASE_RADIUS);
//                pieMenu.setInnerRadius(Math.abs(MathUtils.sin(time)/(float)Math.PI +.1f));
                pieMenu.centerOnScreen();
            }
        };

        /* Populating our PieMenu with fancy Labels. */
        for (int i = 0; i < 6; i++) {
            Label.LabelStyle lbs = new Label.LabelStyle(game.skin.get("white", Label.LabelStyle.class));
            lbs.background = new ShapeDrawerDrawable(sd) {
                @Override
                public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
                    float lw = 10;
                    float pad = 4 + lw;
                    shapeDrawer.setColor(Color.GRAY);
                    shapeDrawer.polygon(x + width / 2,
                            y + height / 2,
                            6,
                            width / 2 + pad,
                            width / 2 + pad,
                            x / 100f,
                            lw);
                }
            };
            Label label = new Label("GDX", lbs);
            pieMenu.addActor(label);
        }

        /* Adding listeners. */
        pieMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("selected: " + pieMenu.getSelectedIndex());
            }
        });
        pieMenu.addListener(new PieMenu.PieMenuCallbacks() {
            @Override
            public void onHighlightChange(int highlightedIndex) {
                System.out.println("highlighted: " + highlightedIndex);
            }

            @Override
            public void onHoverChange(int hoveredIndex) {
                System.out.println("hovered: " + hoveredIndex);
            }
        });

        /* Placing the PieMenu. */
        game.stage.addActor(pieMenu);
    }
}
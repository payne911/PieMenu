package com.payne.games.piemenu.individuals;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;


public class CustomAnimation extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private PieMenu pieMenu;
    private ShapeDrawer sd;

    /* Specific to this demonstration's widget. */
    private float time;
    private final float BASE_RADIUS = 180;


    @Override
    public void create() {

        /* Basic setup. */
        PolygonSpriteBatch batch = new PolygonSpriteBatch();
        skin = new Skin(Gdx.files.internal("skin.json"));
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        sd = new ShapeDrawer(batch, skin.getRegion("white")) {
            /* OPTIONAL: Increasing the precision (at the possible cost of performance). */
            @Override
            protected int estimateSidesRequired(float radiusX, float radiusY) {
                return 4*super.estimateSidesRequired(radiusX, radiusY);
            }
        };

        /* Setting up the Widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.selectedChildRegionColor = new Color(1,0,0,.5f);
        style.highlightedChildRegionColor = new Color(1,.05f,.05f,.4f);
        style.hoveredChildRegionColor = new Color(.8f,.8f,.8f,.05f);
        style.hoveredAndSelectedChildRegionColor = new Color(1,.2f,.2f,.55f);
        pieMenu = new PieMenu(sd, style, BASE_RADIUS) {
            @Override
            public float getActorDistanceFromCenter(Actor actor) {

                /* We want the Labels to be placed closer to the edge than the default value. */
                return getAmountOfChildren() > 1
                        ? radius - getChild(0).getWidth()
                        : 0;
            }

            @Override
            public void act(float delta) {
                super.act(delta);

                /* Our custom animation! */
                time += delta*5;
                pieMenu.setStartDegreesOffset((time * 10) % 360);
                pieMenu.setRadius(MathUtils.sin(time) * 20 + BASE_RADIUS);
                pieMenu.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, Align.center);
            }
        };

        /* Populating our PieMenu with fancy Labels. */
        for(int i=0 ; i<6 ; i++){
            Label.LabelStyle lbs = new Label.LabelStyle(skin.get("white", Label.LabelStyle.class));
            lbs.background = new ShapeDrawerDrawable(sd) {
                @Override
                public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
                    float lw = 10;
                    float pad = 4 + lw;
                    shapeDrawer.setColor(Color.GRAY);
                    shapeDrawer.polygon(x+width/2,
                            y+height/2,
                            6,
                            width/2 + pad,
                            width/2 + pad,
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
        pieMenu.addListener(new PieMenu.PieMenuAdditionalChangeListener() {
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
        stage.addActor(pieMenu);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();
    }
}
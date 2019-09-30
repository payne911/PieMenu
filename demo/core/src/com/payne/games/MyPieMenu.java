package com.payne.games;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class MyPieMenu extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private PolygonSpriteBatch batch;
    private ShapeDrawer shape;
    private PieMenu dragPie;
    private PieMenu permaPie;
    private PieMenu rightMousePie;
    private PieMenu middleMousePie;
    private PieMenu.PieMenuStyle midStyle1;
    private PieMenu.PieMenuStyle midStyle2;
    private RadialGroup radial;
    private int dragPieAmount = 0;
    private int permaPieAmount = 0;
    private int radialAmount = 0;
    private float red = .25f;
    private float blue = .75f;
    private float green = .25f;
    private float alpha = 1;
    private final int INITIAL_CHILDREN_AMOUNT = 5;


    @Override
    public void create () {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        Table root = new Table();
        root.setFillParent(true);
        root.defaults().padBottom(150);
        stage.addActor(root);

        /* Setting up the ShapeDrawer. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1,1,1);
        pixmap.fill();
        tmpTex = new Texture(pixmap);
        pixmap.dispose();
        // ideally, you would extract such a pixel from your Atlas instead
        shape = new ShapeDrawer(batch, new TextureRegion(tmpTex));

        /* Adding the demo widgets. */
        setUpDragPieMenu(root);
        setUpRightMousePieMenu();
        setUpMiddleMousePieMenu();
        setUpRadialWidget(root);
        setUpPermaPieMenu();
    }


    private void setUpRadialWidget(Table root) {

        /* Setting up and creating the widget. */
        RadialGroup.RadialGroupStyle style = new RadialGroup.RadialGroupStyle();
        style.radius = 100;
        style.innerRadius = 50;
        style.startDegreesOffset = 0;
        style.totalDegreesDrawn = 180;
        style.backgroundColor = new Color(1,1,1,1);
        style.childRegionColor = new Color(.4f,.4f,.4f,1);
        style.alternateChildRegionColor = new Color(.6f,0,0,1);
        radial = new RadialGroup(shape, style);

        /* Populating the widget. */
        for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
            Label label = new Label(Integer.toString(radialAmount++), skin);
            radial.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Toggle Radial",  skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                radial.setVisible(!radial.isVisible());
                radial.setPosition(textButton.getX() + textButton.getWidth()/2,
                        textButton.getY() + textButton.getHeight() + 15, Align.center);
            }
        });
        root.add(textButton).expand().bottom();

        /* Including the Widget in the Stage. */
        stage.addActor(radial);
        radial.setName("radial");
    }


    private void setUpDragPieMenu(Table root) {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.radius = 130;
        style.innerRadius = 50;
        style.startDegreesOffset = 180;
        style.totalDegreesDrawn = 320;
        style.backgroundColor = new Color(1,1,1,.3f);
        style.selectedChildRegionColor = new Color(.7f,.3f,.5f,1);
        style.childRegionColor = new Color(0,.7f,0,1);
        style.alternateChildRegionColor = new Color(.7f,0,0,1);
        dragPie = new PieMenu(shape, style);

        /* Customizing the behavior. */
        dragPie.setHighlightIsSelection(false);
        dragPie.setInfiniteSelectionRange(true);
        dragPie.setManualControlOfVisibility(false);

        /* Adding some listeners, just 'cuz... */
        dragPie.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - selected index: " + dragPie.getSelectedIndex());
            }
        });
        dragPie.setHighlightChangeListener(new PieMenu.HighlightChangeListener() {
            @Override
            public void onHighlightChange() {
                System.out.println("HighlightChangeListener - highlighted index: " + dragPie.getHighlightedIndex());
            }
        });

        /* Populating the widget. */
        for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
            Label label = new Label(Integer.toString(dragPieAmount++), skin);
            dragPie.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Drag Pie",  skin);
        textButton.addListener(new ClickListener() {
            /*
            If this was a ChangeListener, the `dragPit.suggestedClickListener`
            would get called before the `changed` method would get called.
            `changed` only gets called after the user releases the click
            directly within the boundaries of the Button, whereas the
            SuggestedClickListener is summoned as soon as the click happens.
            */
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dragPie.resetSelection();
                dragPie.setPosition(textButton.getX() + textButton.getWidth()/2,
                        textButton.getY() + textButton.getHeight()/2, Align.center);
                return true;
            }
        });
        textButton.addListener(dragPie.getSuggestedClickListener());
        root.add(textButton).expand().bottom();

        /* Including the Widget in the Stage. */
        stage.addActor(dragPie);
        dragPie.setName("dragPie");
    }

    private void setUpRightMousePieMenu() {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.radius = 80;
        style.separatorWidth = 2;
        style.backgroundColor = new Color(1,1,1,.1f);
        style.separatorColor = new Color(.1f,.1f,.1f,1);
        style.selectedChildRegionColor = new Color(.5f,.5f,.5f,1);
        style.childRegionColor = new Color(.33f,.33f,.33f,1);
        rightMousePie = new PieMenu(shape, style);

        /* Customizing the behavior. */
        rightMousePie.setHighlightIsSelection(true);
        rightMousePie.setInfiniteSelectionRange(true);
        rightMousePie.setManualControlOfVisibility(false);
        rightMousePie.setSelectionButton(Input.Buttons.RIGHT);

        /* Setting up listeners */
        rightMousePie.addListener(rightMousePie.getSuggestedClickListener());
        rightMousePie.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch(rightMousePie.getSelectedIndex()) {
                    case 0:
                        red   = .25f;
                        blue  = .75f;
                        green = .25f;
                        break;
                    case 1:
                        red   = .75f;
                        blue  = .25f;
                        green = .25f;
                        break;
                    case 2:
                        red   = .25f;
                        blue  = .25f;
                        green = .75f;
                        break;
                    default:
                        red   = .75f;
                        blue  = .75f;
                        green = .75f;
                        break;
                }
            }
        });

        /* Populating the widget. */
        Label blue = new Label("blue", skin);
        rightMousePie.addActor(blue);
        Label red = new Label("red", skin);
        rightMousePie.addActor(red);
        Label green = new Label("green", skin);
        rightMousePie.addActor(green);

        /* Including the Widget in the Stage. */
        rightMousePie.selectIndex(0);
        stage.addActor(rightMousePie);
        rightMousePie.setName("rightMousePie");
    }

    private void setUpMiddleMousePieMenu() {
        // todo: eventually should look similar to   https://dribbble.com/shots/647272-Circle-Menu-PSD

        /* Setting up and creating the widget. */
        midStyle1 = new PieMenu.PieMenuStyle();
        midStyle1.radius = 80;
        midStyle1.innerRadius = 24;
        midStyle1.startDegreesOffset = 30;
        midStyle1.selectedChildRegionColor = new Color(1,.5f,.5f,.5f);
        midStyle1.childRegionColor = new Color(.73f,.33f,.33f,.1f);
        midStyle1.background = new Image(new Texture(Gdx.files.internal("rael_pie.png"))).getDrawable();
        middleMousePie = new PieMenu(shape, midStyle1);

        /* Customizing the behavior. */
        middleMousePie.setHighlightIsSelection(false);
        middleMousePie.setInfiniteSelectionRange(true);
        middleMousePie.setManualControlOfVisibility(false);

        /* Adding some listeners. */
        middleMousePie.addListener(middleMousePie.getSuggestedClickListener());
        middleMousePie.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - selected index: " + middleMousePie.getSelectedIndex());
            }
        });
        middleMousePie.setHighlightChangeListener(new PieMenu.HighlightChangeListener() {
            @Override
            public void onHighlightChange() {
                System.out.println("HighlightChangeListener - highlighted index: " + middleMousePie.getHighlightedIndex());
            }
        });

        /* Populating the widget. */
        Array<Image> imgs = new Array<>();
        imgs.add(new Image(new Texture(Gdx.files.internal("heart-drop.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("beer-stein.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("coffee-mug.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("gooey-daemon.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("jeweled-chalice.png"))));
        imgs.add(new Image(new Texture(Gdx.files.internal("coffee-mug.png"))));
        for (int i = 0; i < imgs.size; i++)
            middleMousePie.addActor(imgs.get(i));

        /* Including the Widget in the Stage. */
        stage.addActor(middleMousePie);
        middleMousePie.setName("middleMousePie");

        /* Creating an alternate skin, just for showing off */
        midStyle2 = new PieMenu.PieMenuStyle();
        midStyle2.radius = 80;
        midStyle2.innerRadius = 27;
        midStyle2.separatorWidth = 2;
        midStyle2.selectedChildRegionColor = new Color(1,.5f,.5f,.5f);
        midStyle2.separatorColor = new Color(.1f,.1f,.1f,.5f);
        midStyle2.childRegionColor = new Color(.73f,.33f,.33f,.1f);
        midStyle2.background = new Image(new Texture(Gdx.files.internal("disc.png"))).getDrawable();
    }

    private void setUpPermaPieMenu() {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.radius = 80;
        style.innerRadius = 20;
        style.totalDegreesDrawn = 180;
        style.circumferenceWidth = 1;
        style.backgroundColor = new Color(1,1,1,.2f);
        style.selectedChildRegionColor = new Color(.5f,.5f,.5f,1);
        style.childRegionColor = new Color(.33f,.33f,.33f,1);
        style.alternateChildRegionColor = new Color(.25f,.25f,.25f,1);
        style.circumferenceColor = new Color(0,0,0,1);
        permaPie = new PieMenu(shape, style);

        /* Customizing the behavior. */
        permaPie.setHighlightIsSelection(false);
        permaPie.setInfiniteSelectionRange(false);
        permaPie.setManualControlOfVisibility(true);

        /* Setting up listeners */
        permaPie.addListener(permaPie.getSuggestedClickListener());
        permaPie.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                alpha = MathUtils.map(0,permaPie.getChildren().size-1,0,1,permaPie.getSelectedIndex());
                Color color = radial.getStyle().backgroundColor;
                radial.getStyle().backgroundColor.set(color.r, color.g, color.b, alpha);
            }
        });

        /* Populating the widget. */
        for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
            Label label = new Label(Integer.toString(permaPieAmount++), skin);
            permaPie.addActor(label);
        }

        /* Including the Widget at some absolute coordinate in the World. */
        permaPie.setPosition(Gdx.graphics.getWidth()/2,0, Align.center); // (320,0)
        permaPie.selectIndex(permaPie.getChildren().size-1);
        stage.addActor(permaPie);
        permaPie.setVisible(true);
    }





    @Override
    public void render () {
        Gdx.gl.glClearColor(red, green, blue, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        /* Debugging and interactions. */
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            dragPie.addActor(new Label(Integer.toString(dragPieAmount++), skin));
            permaPie.addActor(new Label(Integer.toString(permaPieAmount++), skin));
            radial.addActor(new Label(Integer.toString(radialAmount++), skin));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            dragPieAmount = 0;
            permaPieAmount = 0;
            radialAmount = 0;
            dispose();
            create();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if(dragPie.getChildren().size == 0)
                return;
            dragPie.removeActor(dragPie.getChild(dragPie.getChildren().size-1));
            permaPie.removeActor(permaPie.getChild(permaPie.getChildren().size-1));
            radial.removeActor(radial.getChild(radial.getChildren().size-1));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            permaPie.setVisible(!permaPie.isVisible());
        }
        if (Gdx.input.isButtonJustPressed(rightMousePie.getSelectionButton())) {
            rightMousePie.centerOnMouse();
            rightMousePie.triggerDefaultListenerTouchDown(); // Programmatically sends the user into the `touchDragged` Event
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            middleMousePie.centerOnMouse();
            middleMousePie.resetSelection();
            middleMousePie.setVisible(true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            middleMousePie.setStyle(middleMousePie.getStyle() == midStyle1 ? midStyle2 : midStyle1);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose () {
        skin.dispose();
        stage.dispose();
        tmpTex.dispose();
    }
}
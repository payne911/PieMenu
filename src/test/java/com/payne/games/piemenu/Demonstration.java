package com.payne.games.piemenu;

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
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class Demonstration extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private PolygonSpriteBatch batch;
    private ShapeDrawer shape;

    private AnimatedPieMenu dragPie;
    private PieMenu permaPie;
    private PieMenu rightMousePie;
    private PieMenu middleMousePie;
    private PieMenu.PieMenuStyle midStyle1;
    private PieMenu.PieMenuStyle midStyle2;
    private AnimatedRadialGroup radial;

    private float midStyle1InnerRadius = 24;
    private float midStyle2InnerRadius = 27;
    private Color backgroundColor = new Color(1,1,1,.2f);
    private int dragPieAmount = 0;
    private int permaPieAmount = 0;
    private int radialAmount = 0;
    private float red = .25f;
    private float green = .25f;
    private float blue = .45f;
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
        stage.addActor(root);

        /* Controls and instructions. */
        root.add(new Label("R: restart\n" +
                "S: toggle the permanent pie\n" +
                "C: change style of Middle-click Pie\n" +
                "L: less items in certain menus\n" +
                "M: more pies in certain menus\n" +
                "Middle-click / Right-click: try it out!", skin)).colspan(2).padTop(25);
        root.row().padBottom(150).uniform();

        /* Setting up the ShapeDrawer. */
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,1,1,1);
        pixmap.fill();
        tmpTex = new Texture(pixmap);
        pixmap.dispose();
        // ideally, you would extract such a pixel from your Atlas instead
        shape = new ShapeDrawer(batch, new TextureRegion(tmpTex));
        /* If you want smoother edges, you can do this instead, but it might affect performances. */
//        shape = new ShapeDrawer(batch, new TextureRegion(tmpTex)) {
//            @Override
//            protected int estimateSidesRequired(float radiusX, float radiusY) {
//                return 2*super.estimateSidesRequired(radiusX, radiusY);
//            }
//        };

        /* Adding the demo widgets. */
        setUpButtonDragPieMenu(root);
        setUpRightMousePieMenu();
        setUpMiddleMousePieMenu();
        setUpRadialWidget(root);
        setUpPermaPieMenu();
    }


    private void setUpRadialWidget(Table root) {

        /* Setting up and creating the widget. */
        RadialGroup.RadialGroupStyle style = new RadialGroup.RadialGroupStyle();
        style.backgroundColor = new Color(1,1,1,1);
        style.sliceColor = new Color(.4f,.4f,.4f,1);
        style.alternateSliceColor = new Color(.6f,0,0,1);
        radial = new AnimatedRadialGroup(shape, style, 100, 50, 0, 180);

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
                radial.toggleVisibility(.9f);
                radial.setPosition(textButton.getX() + textButton.getWidth()/2,
                        textButton.getTop() + 15, Align.center);
            }
        });
        root.add(textButton).expand().bottom();

        /* Including the Widget in the Stage. */
        stage.addActor(radial);
        radial.setVisible(false);
    }


    private void setUpButtonDragPieMenu(Table root) {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.backgroundColor = new Color(1,1,1,.3f);
        style.selectedSliceColor = new Color(.7f,.3f,.5f,1);
        style.highlightedSliceColor = new Color(.7f,.3f,.5f,1);
        style.sliceColor = new Color(0,.7f,0,1);
        style.alternateSliceColor = new Color(.7f,0,0,1);
        dragPie = new AnimatedPieMenu(shape, style, 130, 50, 180, 320);

        /* Customizing the behavior. */
        dragPie.setInfiniteSelectionRange(true);

        /* Populating the widget. */
        for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
            Label label = new Label(Integer.toString(dragPieAmount++), skin);
            dragPie.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Drag Pie",  skin);
        textButton.addListener(new ClickListener() {
            /*
            In our particular case, we want to NOT use a ChangeListener because
            else the user would have to release his mouse-click before seeing
            the menu, which goes against our current goal of obtaining a
            "drag-selection" menu.
            */
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dragPie.resetSelection();
                dragPie.centerOnActor(textButton);
                dragPie.animateOpening(.4f);
                transferInteraction(stage, dragPie);
                return true;
            }
        });
        root.add(textButton).expand().bottom();

        /* Adding a selection-listener. */
        dragPie.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dragPie.transitionToClosing(.4f);
                int index = dragPie.getSelectedIndex();
                if(!dragPie.isValidIndex(index)) {
                    textButton.setText("Drag Pie");
                    return;
                }
                Actor child = dragPie.getChild(index);
                textButton.setText(((Label)child).getText().toString());
            }
        });

        /* Including the Widget in the Stage. */
        stage.addActor(dragPie);
        dragPie.setVisible(false);
    }

    /**
     * To be used to get the user to transition directly into
     * {@link InputListener#touchDragged(InputEvent, float, float, int)}
     * as if he had triggered
     * {@link InputListener#touchDown(InputEvent, float, float, int, int)}.<br>
     * I am not certain this is the recommended way of doing this, but for the
     * purposes of this demonstration, it works!
     *
     * @param stage the stage.
     * @param widget the PieMenu on which to transfer the interaction.
     */
    private void transferInteraction(Stage stage, PieMenu widget) {
        if(widget == null) throw new IllegalArgumentException("widget cannot be null.");
        if(widget.getPieMenuListener() == null) throw new IllegalArgumentException("inputListener cannot be null.");
        stage.addTouchFocus(widget.getPieMenuListener(), widget, widget, 0, widget.getSelectionButton());
    }

    private void setUpRightMousePieMenu() {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.separatorWidth = 2;
        style.backgroundColor = new Color(1,1,1,.1f);
        style.separatorColor = new Color(.1f,.1f,.1f,1);
        style.highlightedSliceColor = new Color(.5f,.5f,.5f,1);
        style.sliceColor = new Color(.33f,.33f,.33f,1);
        rightMousePie = new PieMenu(shape, style, 80);

        /* Customizing the behavior. */
        rightMousePie.setInfiniteSelectionRange(true);
        rightMousePie.setSelectionButton(Input.Buttons.RIGHT);

        /* Setting up listeners. */
        rightMousePie.addListener(new PieMenu.PieMenuCallbacks() {
            @Override
            public void onHighlightChange(int highlightedIndex) {
                switch(highlightedIndex) {
                    case 0:
                        red   = .25f;
                        blue  = .45f;
                        green = .25f;
                        break;
                    case 1:
                        red   = .45f;
                        blue  = .25f;
                        green = .25f;
                        break;
                    case 2:
                        red   = .25f;
                        blue  = .25f;
                        green = .45f;
                        break;
                    default:
                        red   = .75f;
                        blue  = .75f;
                        green = .75f;
                        break;
                }
            }
        });
        rightMousePie.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - selected index: " + rightMousePie.getSelectedIndex());
                rightMousePie.setVisible(false);
                rightMousePie.remove();
            }
        });

        /* Populating the widget. */
        Label blue = new Label("blue", skin);
        rightMousePie.addActor(blue);
        Label red = new Label("red", skin);
        rightMousePie.addActor(red);
        Label green = new Label("green", skin);
        rightMousePie.addActor(green);
    }

    private void setUpMiddleMousePieMenu() {
        // todo: eventually should look similar to   https://dribbble.com/shots/647272-Circle-Menu-PSD

        /* Setting up and creating the widget. */
        midStyle1 = new PieMenu.PieMenuStyle();
        midStyle1.selectedSliceColor = new Color(1,.5f,.5f,.5f);
        midStyle1.highlightedSliceColor = new Color(1,.5f,.5f,.5f);
        midStyle1.background = new Image(new Texture(Gdx.files.internal("rael_pie.png"))).getDrawable();
        middleMousePie = new PieMenu(shape, midStyle1, 80, midStyle1InnerRadius, 30) {
            /* Since we are using Images, we want to resize them to fit within each sector. */
            @Override
            public void modifyActor(Actor actor, float degreesPerChild, float actorDistanceFromCenter) {
                float size = getEstimatedRadiusAt(degreesPerChild, actorDistanceFromCenter);
                size *= 1.26f; // adjusting the returned value to our likes
                actor.setSize(size, size);
            }
        };

        /* Customizing the behavior. */
        middleMousePie.setInfiniteSelectionRange(true);

        /* Adding a selection-listener. */
        middleMousePie.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener - selected index: " + middleMousePie.getSelectedIndex());
                middleMousePie.setVisible(false);
                middleMousePie.remove();
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

        /* Creating an alternate skin, just for showing off */
        midStyle2 = new PieMenu.PieMenuStyle();
        midStyle2.separatorWidth = 2;
        midStyle2.selectedSliceColor = new Color(1,.5f,.5f,.5f);
        midStyle2.highlightedSliceColor = new Color(1,.5f,.5f,.5f);
        midStyle2.separatorColor = new Color(.1f,.1f,.1f,.5f);
        midStyle2.sliceColor = new Color(.73f,.33f,.33f,.1f);
        midStyle2.background = new Image(new Texture(Gdx.files.internal("disc.png"))).getDrawable();
    }

    private void setUpPermaPieMenu() {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.circumferenceWidth = 1;
        style.backgroundColor = backgroundColor;
        style.highlightedSliceColor = new Color(.5f,.5f,.5f,1);
        style.sliceColor = new Color(.33f,.33f,.33f,1);
        style.alternateSliceColor = new Color(.25f,.25f,.25f,1);
        style.circumferenceColor = new Color(0,0,0,1);
        permaPie = new PieMenu(shape, style, 80, 20, 0 ,180);

        /* Adding a selection-listener. */
        permaPie.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float alpha = MathUtils.map(0,permaPie.getAmountOfChildren()-1,0,1,permaPie.getSelectedIndex());
                radial.getStyle().backgroundColor.set(backgroundColor.r, backgroundColor.g, backgroundColor.b, alpha);
            }
        });

        /* Populating the widget. */
        for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
            Label label = new Label(Integer.toString(permaPieAmount++), skin);
            permaPie.addActor(label);
        }

        /* Customizing the behavior. */
        permaPie.setDefaultIndex(2);

        /* Including the Widget at some absolute coordinate in the World. */
        permaPie.setPosition(Gdx.graphics.getWidth()/2f,0, Align.center);
        stage.addActor(permaPie);
    }





    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(red, green, blue, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();

        /* Debugging and interactions. */
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            dragPie.addActor(new Label(Integer.toString(dragPieAmount++), skin));
            permaPie.addActor(new Label(Integer.toString(permaPieAmount++), skin));
            radial.addActor(new Label(Integer.toString(radialAmount++), skin));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            dragPieAmount = 0;
            permaPieAmount = 0;
            radialAmount = 0;
            dispose();
            create();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            if(dragPie.getAmountOfChildren() == 0)
                return;
            dragPie.removeActor(dragPie.getChild(dragPie.getAmountOfChildren()-1));
            permaPie.removeActor(permaPie.getChild(permaPie.getAmountOfChildren()-1));
            radial.removeActor(radial.getChild(radial.getAmountOfChildren()-1));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            permaPie.setVisible(!permaPie.isVisible());
        }
        if (Gdx.input.isButtonJustPressed(rightMousePie.getSelectionButton())) {
            stage.addActor(rightMousePie);
            rightMousePie.centerOnMouse();
            rightMousePie.setVisible(true);
            transferInteraction(stage, rightMousePie);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            stage.addActor(middleMousePie);
            middleMousePie.centerOnMouse();
            middleMousePie.resetSelection();
            middleMousePie.setVisible(true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            middleMousePie.setStyle(
                    middleMousePie.getStyle() == midStyle1
                            ? midStyle2 : midStyle1);
            middleMousePie.setInnerRadius(
                    middleMousePie.getStyle() == midStyle1
                            ? midStyle1InnerRadius : midStyle2InnerRadius);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose () {

        /* Disposing is good practice! */
        skin.dispose();
        stage.dispose();
        tmpTex.dispose();
    }
}
package com.payne.games.piemenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.payne.games.piemenu.actions.RadialGroupActionColorBasic;
import com.payne.games.piemenu.actions.RadialGroupActionVisualAngleClose;
import com.payne.games.piemenu.actions.RadialGroupActionVisualAngleOpen;
import com.payne.games.piemenu.core.BaseGame;
import com.payne.games.piemenu.core.BaseScreen;

public class Demonstration extends BaseScreen {
    private AnimatedPieMenu dragPie;
    private PieMenu dragPieAnimation;
    private PieMenu permaPie;
    private PieMenu rightMousePie;
    private PieMenu middleMousePie;
    private PieMenu.PieMenuStyle midStyle1;
    private PieMenu.PieMenuStyle midStyle2;
    private AnimatedPieWidget radial;

    private float midStyle1InnerRadius = 24f / 80;
    private float midStyle2InnerRadius = 27f / 80;
    private Color backgroundColor = new Color(1, 1, 1, .2f);
    private int dragPieAmount = 0;
    private int dragPieAnimationAmount = 0;
    private int permaPieAmount = 0;
    private int radialAmount = 0;
    private final int INITIAL_CHILDREN_AMOUNT = 5;

    public Demonstration(BaseGame game) {
        super(game);
    }

    @Override
    public void show() {
        setScreenColor(.25f, .25f, .45f, 1);
        /* Setting up the Stage. */
        Table root = new Table();
        root.setFillParent(true);
        game.stage.addActor(root);
//        game.stage.setDebugAll(true);

        /* Controls and instructions. */
        root.add(new Label("R: restart\n" +
                "S: toggle the permanent pie\n" +
                "C: change style of Middle-click Pie\n" +
                "L: less items in certain menus\n" +
                "M: more pies in certain menus\n" +
                "Middle-click / Right-click: try it out!\n" +
                "The Left Pie is an AnimatedPieMenu \n" +
                "which is deprecated\n" +
                "The Middle Pie is using Actions for \n" +
                "animations (using interpolation)", game.skin)).colspan(2).padTop(25);
        root.row().padBottom(150).uniform();

        /* Adding the demo widgets. */
        setUpButtonDragPieMenu(root);
        setUpButtonDragPieAnimationMenu(root);
        setUpRightMousePieMenu();
        setUpMiddleMousePieMenu();
        setUpRadialWidget(root);
        setUpPermaPieMenu();
    }


    private void setUpRadialWidget(Table root) {

        /* Setting up and creating the widget. */
        PieWidget.PieWidgetStyle style = new PieWidget.PieWidgetStyle();
        style.backgroundColor = new Color(1, 1, 1, 1);
        style.sliceColor = new Color(.4f, .4f, .4f, 1);
        style.alternateSliceColor = new Color(.6f, 0, 0, 1);
        radial = new AnimatedPieWidget(game.skin.getRegion("white"), style, 100, .5f, 0, 180);

        /* Populating the widget. */
        for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
            Label label = new Label(Integer.toString(radialAmount++), game.skin);
            radial.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Toggle Radial", game.skin);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                radial.toggleVisibility(.9f);
                radial.setPosition(textButton.getX(Align.center),
                        textButton.getTop() + 15, Align.center);
            }
        });
        root.add(textButton).expand().bottom();

        /* Including the Widget in the Stage. */
        game.stage.addActor(radial);
        radial.setVisible(false);
    }


    private void setUpButtonDragPieMenu(Table root) {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.backgroundColor = new Color(1, 1, 1, .3f);
        style.selectedColor = new Color(.7f, .3f, .5f, 1);
        style.sliceColor = new Color(0, .7f, 0, 1);
        style.alternateSliceColor = new Color(.7f, 0, 0, 1);
        dragPie = new AnimatedPieMenu(game.skin.getRegion("white"), style, 130, 50f / 130, 180, 320);

        /* Customizing the behavior. */
        dragPie.setInfiniteSelectionRange(true);

        /* Populating the widget. */
        for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
            Label label = new Label(Integer.toString(dragPieAmount++), game.skin);
            dragPie.addActor(label);
        }

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Drag Pie", game.skin);
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
                transferInteraction(game.stage, dragPie);
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
                if (!dragPie.isValidIndex(index)) {
                    textButton.setText("Drag Pie");
                    return;
                }
                Actor child = dragPie.getChild(index);
                textButton.setText(((Label) child).getText().toString());
            }
        });

        /* Including the Widget in the Stage. */
        game.stage.addActor(dragPie);
        dragPie.setVisible(false);
    }

    private void setUpButtonDragPieAnimationMenu(Table root) {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.backgroundColor = new Color(1, 1, 1, .3f);
        style.selectedColor = new Color(.7f, .3f, .5f, 1);
        style.sliceColor = new Color(0, .7f, 0, 1);
        style.alternateSliceColor = new Color(.7f, 0, 0, 1);
        dragPieAnimation = new PieMenu(game.skin.getRegion("white"), style, 130, 50f / 130, 180, 320);
        dragPieAnimation.setVisualAngleAutoUpdate(false);

        /* Customizing the behavior. */
        dragPieAnimation.setInfiniteSelectionRange(true);

        /* Populating the widget. */
        for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
            Label label = new Label(Integer.toString(dragPieAnimationAmount++), game.skin);
            dragPieAnimation.addActor(label);
        }

        RadialGroupActionVisualAngleOpen actionVisualAngleOpen = new RadialGroupActionVisualAngleOpen(dragPieAnimation, .1f, Interpolation.linear);
        Action actionOpen = new ParallelAction(actionVisualAngleOpen, new RadialGroupActionColorBasic(dragPieAnimation));
        RadialGroupActionVisualAngleClose actionVisualAngleClose = new RadialGroupActionVisualAngleClose(dragPieAnimation, .1f, Interpolation.linear);
        Action actionClose = new ParallelAction(actionVisualAngleClose, new RadialGroupActionColorBasic(dragPieAnimation));

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Drag Pie", game.skin);
        textButton.addListener(new ClickListener() {
            /*
            In our particular case, we want to NOT use a ChangeListener because
            else the user would have to release his mouse-click before seeing
            the menu, which goes against our current goal of obtaining a
            "drag-selection" menu.
            */
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dragPieAnimation.resetSelection();
                dragPieAnimation.centerOnActor(textButton);

                if (dragPieAnimation.getActions().contains(actionClose, true)) {
                    actionClose.restart();
                    dragPieAnimation.removeAction(actionClose);
                }
                if (!dragPieAnimation.getActions().contains(actionOpen, true)) {
                    actionOpen.restart();
                    dragPieAnimation.addAction(actionOpen);
                }

                transferInteraction(game.stage, dragPieAnimation);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        root.add(textButton).expand().bottom();

        /* Adding a selection-listener. */
        dragPieAnimation.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (dragPieAnimation.getActions().contains(actionOpen, true)) {
                    actionOpen.restart();
                    dragPieAnimation.removeAction(actionOpen);
                }
                if (!dragPieAnimation.getActions().contains(actionClose, true)) {
                    actionClose.restart();
                    dragPieAnimation.addAction(actionClose);
                }

                int index = dragPieAnimation.getSelectedIndex();
                if (!dragPieAnimation.isValidIndex(index)) {
                    textButton.setText("Drag Pie");
                    return;
                }
                Actor child = dragPieAnimation.getChild(index);
                textButton.setText(((Label) child).getText().toString());
            }
        });

        /* Including the Widget in the Stage. */
        game.stage.addActor(dragPieAnimation);
        dragPieAnimation.setVisible(false);
    }

    /**
     * To be used to get the user to transition directly into
     * {@link InputListener#touchDragged(InputEvent, float, float, int)}
     * as if he had triggered
     * {@link InputListener#touchDown(InputEvent, float, float, int, int)}.<br/>
     * I am not certain this is the recommended way of doing this, but for the
     * purposes of this demonstration, it works!
     *
     * @param stage  the stage.
     * @param widget the PieMenu on which to transfer the interaction.
     */
    private void transferInteraction(Stage stage, PieMenu widget) {
        if (widget == null) throw new IllegalArgumentException("widget cannot be null.");
        if (widget.getPieMenuListener() == null) throw new IllegalArgumentException("inputListener cannot be null.");
        game.stage.addTouchFocus(widget.getPieMenuListener(), widget, widget, 0, widget.getSelectionButton());
    }

    private void setUpRightMousePieMenu() {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.separatorWidth = 2;
        style.backgroundColor = new Color(1, 1, 1, .1f);
        style.separatorColor = new Color(.1f, .1f, .1f, 1);
        style.downColor = new Color(.5f, .5f, .5f, 1);
        style.sliceColor = new Color(.33f, .33f, .33f, 1);
        rightMousePie = new PieMenu(game.skin.getRegion("white"), style, 80);

        /* Customizing the behavior. */
        rightMousePie.setInfiniteSelectionRange(true);
        rightMousePie.setSelectionButton(Input.Buttons.RIGHT);

        /* Setting up listeners. */
        rightMousePie.addListener(new PieMenu.PieMenuCallbacks() {
            @Override
            public void onHighlightChange(int highlightedIndex) {
                switch (highlightedIndex) {
                    case 0:
                        screenColorRed = .25f;
                        screenColorBlue = .45f;
                        screenColorGreen = .25f;
                        break;
                    case 1:
                        screenColorRed = .45f;
                        screenColorBlue = .25f;
                        screenColorGreen = .25f;
                        break;
                    case 2:
                        screenColorRed = .25f;
                        screenColorBlue = .25f;
                        screenColorGreen = .45f;
                        break;
                    default:
                        screenColorRed = .75f;
                        screenColorBlue = .75f;
                        screenColorGreen = .75f;
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
        Label blue = new Label("blue", game.skin);
        rightMousePie.addActor(blue);
        Label red = new Label("red", game.skin);
        rightMousePie.addActor(red);
        Label green = new Label("green", game.skin);
        rightMousePie.addActor(green);
    }

    private void setUpMiddleMousePieMenu() {
        // todo: eventually should look similar to   https://dribbble.com/shots/647272-Circle-Menu-PSD

        /* Setting up and creating the widget. */
        midStyle1 = new PieMenu.PieMenuStyle();
        midStyle1.selectedColor = new Color(1, .5f, .5f, .5f);
        midStyle1.background = new TextureRegionDrawable(getTextureAutoDisposable("rael_pie.png"));
        middleMousePie = new PieMenu(game.skin.getRegion("white"), midStyle1, 80, midStyle1InnerRadius, 30) {
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
        imgs.add(new Image(getTextureAutoDisposable("heart-drop.png")));
        imgs.add(new Image(getTextureAutoDisposable("beer-stein.png")));
        imgs.add(new Image(getTextureAutoDisposable("coffee-mug.png")));
        imgs.add(new Image(getTextureAutoDisposable("gooey-daemon.png")));
        imgs.add(new Image(getTextureAutoDisposable("jeweled-chalice.png")));
        imgs.add(new Image(getTextureAutoDisposable("coffee-mug.png")));
        for (int i = 0; i < imgs.size; i++)
            middleMousePie.addActor(imgs.get(i));

        /* Creating an alternate skin, just for showing off */
        midStyle2 = new PieMenu.PieMenuStyle();
        midStyle2.separatorWidth = 2;
        midStyle2.selectedColor = new Color(1, .5f, .5f, .5f);
        midStyle2.separatorColor = new Color(.1f, .1f, .1f, .5f);
        midStyle2.sliceColor = new Color(.73f, .33f, .33f, .1f);
        midStyle2.background = new TextureRegionDrawable(getTextureAutoDisposable("disc.png"));
    }

    private void setUpPermaPieMenu() {

        /* Setting up and creating the widget. */
        PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
        style.circumferenceWidth = 1;
        style.backgroundColor = backgroundColor;
        style.downColor = new Color(.5f, .5f, .5f, 1);
        style.sliceColor = new Color(.33f, .33f, .33f, 1);
        style.alternateSliceColor = new Color(.25f, .25f, .25f, 1);
        style.circumferenceColor = new Color(0, 0, 0, 1);
        permaPie = new PieMenu(game.skin.getRegion("white"), style, 80, 20f / 80, 0, 180);

        /* Adding a selection-listener. */
        permaPie.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float alpha = MathUtils.map(0, permaPie.getAmountOfChildren() - 1, 0, 1, permaPie.getSelectedIndex());
                radial.getStyle().backgroundColor.set(backgroundColor.r, backgroundColor.g, backgroundColor.b, alpha);
            }
        });

        /* Populating the widget. */
        for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
            Label label = new Label(Integer.toString(permaPieAmount++), game.skin);
            permaPie.addActor(label);
        }

        /* Customizing the behavior. */
        permaPie.setDefaultIndex(2);

        /* Including the Widget at some absolute coordinate in the World. */
        permaPie.setPosition(Gdx.graphics.getWidth() / 2f, 0, Align.center);
        game.stage.addActor(permaPie);
    }


    @Override
    public void updateInputPost(float delta) {
        /* Debugging and interactions. */
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            dragPie.addActor(new Label(Integer.toString(dragPieAmount++), game.skin));
            dragPieAnimation.addActor(new Label(Integer.toString(dragPieAnimationAmount++), game.skin));
            permaPie.addActor(new Label(Integer.toString(permaPieAmount++), game.skin));
            radial.addActor(new Label(Integer.toString(radialAmount++), game.skin));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            dragPieAmount = 0;
            dragPieAnimationAmount = 0;
            permaPieAmount = 0;
            radialAmount = 0;
            dispose();
            show();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            if (dragPie.getAmountOfChildren() == 0)
                return;
            dragPie.removeActor(dragPie.getChild(dragPie.getAmountOfChildren() - 1));
            permaPie.removeActor(permaPie.getChild(permaPie.getAmountOfChildren() - 1));
            radial.removeActor(radial.getChild(radial.getAmountOfChildren() - 1));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            permaPie.setVisible(!permaPie.isVisible());
        }
        if (Gdx.input.isButtonJustPressed(rightMousePie.getSelectionButton())) {
            game.stage.addActor(rightMousePie);
            rightMousePie.centerOnMouse();
            rightMousePie.setVisible(true);
            transferInteraction(game.stage, rightMousePie);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            game.stage.addActor(middleMousePie);
            middleMousePie.centerOnMouse();
            middleMousePie.resetSelection();
            middleMousePie.setVisible(true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            middleMousePie.setStyle(
                    middleMousePie.getStyle() == midStyle1
                            ? midStyle2 : midStyle1);
            middleMousePie.setInnerRadiusPercent(
                    middleMousePie.getStyle() == midStyle1
                            ? midStyle1InnerRadius : midStyle2InnerRadius);
        }
    }
}
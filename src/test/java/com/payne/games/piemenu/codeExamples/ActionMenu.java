package com.payne.games.piemenu.codeExamples;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.payne.games.piemenu.PieMenu;
import com.payne.games.piemenu.PieMenu.PieMenuStyle;
import com.payne.games.piemenu.actions.RadialGroupActionColorBasic;
import com.payne.games.piemenu.actions.RadialGroupActionVisualAngleClose;
import com.payne.games.piemenu.actions.RadialGroupActionVisualAngleOpen;


public class ActionMenu extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Texture tmpTex;
    private Batch batch;
    private PieMenu menu;


    @Override
    public void create () {

        /* Setting up the Stage. */
        skin = new Skin(Gdx.files.internal("skin.json"));
        batch = new PolygonSpriteBatch();
        stage = new Stage(new ScreenViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        /* Adding a Table. */
        Table root = new Table();
        root.setFillParent(true);
        root.defaults().padBottom(150);
        stage.addActor(root);

        /* Ideally, you would extract such a pixel from your Atlas instead. */
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(1,1,1,1);
        pixmap.fill();
        tmpTex = new Texture(pixmap);
        pixmap.dispose();
        TextureRegion whitePixel = new TextureRegion(tmpTex);



        /* ====================================================================\
        |                  HERE BEGINS THE MORE SPECIFIC CODE                  |
        \==================================================================== */

        /* Setting up and creating the widget. */
        PieMenuStyle style = new PieMenuStyle();
        style.backgroundColor = new Color(1, 1, 1, .3f);
        style.selectedColor = new Color(.7f, .3f, .5f, 1);
        style.sliceColor = new Color(0, .7f, 0, 1);
        style.alternateSliceColor = new Color(.7f, 0, 0, 1);
        menu = new PieMenu(whitePixel, style, 130, 50f/130, 180, 320);
        menu.setVisualAngleAutoUpdate(false);

        /* Customizing the behavior. */
        menu.setInfiniteSelectionRange(true);

        /* Populating the widget. */
        for (int i = 0; i < 5; i++) {
            Label label = new Label(Integer.toString(i), skin);
            menu.addActor(label);
        }

        /* Preparing the Actions used for our Animations. */
        RadialGroupActionVisualAngleOpen actionVisualAngleOpen = new RadialGroupActionVisualAngleOpen(menu, .1f, Interpolation.linear);
        Action actionOpen = new ParallelAction(actionVisualAngleOpen, new RadialGroupActionColorBasic(menu));
        RadialGroupActionVisualAngleClose actionVisualAngleClose = new RadialGroupActionVisualAngleClose(menu, .1f, Interpolation.linear);
        Action actionClose = new ParallelAction(actionVisualAngleClose, new RadialGroupActionColorBasic(menu));

        /* Setting up the demo-button. */
        final TextButton textButton = new TextButton("Drag Pie", skin);
        textButton.addListener(new ClickListener() {
            /*
            In our particular case, we want to NOT use a ChangeListener because
            else the user would have to release his mouse-click before seeing
            the menu, which goes against our current goal of obtaining a
            "drag-selection" menu.
            */
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                menu.resetSelection();
                menu.centerOnActor(textButton);

                if (menu.getActions().contains(actionClose, true)) {
                    actionClose.restart();
                    menu.removeAction(actionClose);
                }
                if (!menu.getActions().contains(actionOpen, true)) {
                    actionOpen.restart();
                    menu.addAction(actionOpen);
                }

                transferInteraction(stage, menu);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        root.add(textButton).expand().bottom();

        /* Adding a selection-listener. */
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (menu.getActions().contains(actionOpen, true)) {
                    actionOpen.restart();
                    menu.removeAction(actionOpen);
                }
                if (!menu.getActions().contains(actionClose, true)) {
                    actionClose.restart();
                    menu.addAction(actionClose);
                }

                int index = menu.getSelectedIndex();
                if (!menu.isValidIndex(index)) {
                    textButton.setText("Drag Pie");
                    return;
                }
                Actor child = menu.getChild(index);
                textButton.setText(((Label) child).getText().toString());
            }
        });

        /* Including the Widget in the Stage. */
        stage.addActor(menu);
        menu.setVisible(false);
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


    @Override
    public void render () {

        /* Clearing the screen and filling up the background. */
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Updating and drawing the Stage. */
        stage.act();
        stage.draw();
    }


    @Override
    public void dispose () {

        /* Disposing is good practice! */
        stage.dispose();
        batch.dispose();
        tmpTex.dispose();
        skin.dispose();
    }
}
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class MyPieMenu extends ApplicationAdapter {
	private Skin skin;
	private Stage stage;
	private Texture tmpTex;
	private PolygonSpriteBatch batch;
	private ShapeDrawer shape;
	private PieMenu pie;
	private RadialGroup radial;
	private int pieAmount = 0;
	private int radialAmount = 0;


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
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888); // todo: extract from Atlas instead?
		pixmap.setColor(1,1,1,1);
		pixmap.fill();
		tmpTex = new Texture(pixmap);
		pixmap.dispose();
		shape = new ShapeDrawer(batch, new TextureRegion(tmpTex));

		/* Adding the demo buttons. */
		setUpPieMenu(root);
		setUpRadialWidget(root);
	}


	private void setUpRadialWidget(Table root) {

		/* Setting up and creating the widget. */
		RadialGroup.RadialGroupStyle style = new RadialGroup.RadialGroupStyle();
		style.radius = 100;
		style.innerRadius = 20;
		style.startDegreesOffset = 0;
		style.totalDegreesDrawn = 180;
		style.backgroundColor = new Color(1,1,1,1);
		style.childRegionColor = new Color(.4f,.4f,.4f,1);
		style.alternateChildRegionColor = new Color(.6f,0,0,1);
		style.separatorColor = new Color(.5f,1,.3f,1);
		radial = new RadialGroup(shape, style);
		radial.setVisible(false);

		/* Populating the widget. */
		for (int i = 0; i < 5; i++) {
			Label label = new Label(Integer.toString(radialAmount++), skin);
			radial.addActor(label);
		}

		/* Setting up the demo-button. */
		final TextButton textButton = new TextButton("Radial",  skin);
		textButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				radial.setVisible(!radial.isVisible());
			}
		});
		root.add(textButton).expand().bottom();

		/* Including the Widget in the Stage. */
		radial.attachToActor(textButton); // positions the widget
		stage.addActor(radial);
		radial.pack();
	}


	private void setUpPieMenu(Table root) {

		/* Setting up and creating the widget. */
		PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
		style.radius = 130;
		style.innerRadius = 50;
		style.startDegreesOffset = 180;
		style.totalDegreesDrawn = 320;
		style.backgroundColor = new Color(1,1,1,1);
		style.selectedColor = new Color(.7f,.3f,.5f,1);
		style.childRegionColor = new Color(0,.7f,0,1);
		style.alternateChildRegionColor = new Color(.7f,0,0,1);
		style.separatorColor = new Color(1,1,0,1);
		pie = new PieMenu(shape, style);

		/* Customizing the behavior. */
		pie.setHoverIsSelection(false);
		pie.setInfiniteDragRange(true);
		pie.setResetSelectionOnAppear(true);

		/* Adding some listeners, just 'cuz... */
		pie.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("ChangeListener - selected index: " + pie.getSelectedIndex());
			}
		});
		pie.setHoverChangeListener(new PieMenu.HoverChangeListener() {
			@Override
			public void onHoverChange() {
				System.out.println("HoverChangeListener - highlighted index: " + pie.getHighlightedIndex());
			}
		});

		/* Populating the widget. */
		for (int i = 0; i < 5; i++) {
			Label label = new Label(Integer.toString(pieAmount++), skin);
			pie.addActor(label);
		}

		/* Setting up the demo-button. */
		final TextButton textButton = new TextButton("Pie",  skin);
		root.add(textButton).expand().bottom();

		/* Including the Widget in the Stage. */
		pie.attachToActor(textButton); // assigns the listener to the Actor and positions the menu
		stage.addActor(pie);
		pie.pack();
	}







	@Override
	public void render () {
		Gdx.gl.glClearColor(.25f, .25f, .75f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

		/* Debugging. */
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			pie.addActor(new Label(Integer.toString(pieAmount++), skin));
			radial.addActor(new Label(Integer.toString(radialAmount++), skin));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
			pieAmount = 0;
			radialAmount = 0;
			dispose();
			create();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
		    if(pie.getChildren().size == 0)
		        return;
			pie.removeActor(pie.getChild(pie.getChildren().size-1));
			radial.removeActor(radial.getChild(radial.getChildren().size-1));
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
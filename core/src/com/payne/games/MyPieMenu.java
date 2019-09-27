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
	private PieMenu mousePie;
	private PieMenu permaPie;
	private RadialGroup radial;
	private int pieAmount = 0;
	private int mousePieAmount = 0;
	private int permaPieAmount = 0;
	private int radialAmount = 0;
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
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888); // todo: extract from Atlas instead?
		pixmap.setColor(1,1,1,1);
		pixmap.fill();
		tmpTex = new Texture(pixmap);
		pixmap.dispose();
		shape = new ShapeDrawer(batch, new TextureRegion(tmpTex));

		/* Adding the demo widgets. */
		setUpPieMenu(root);
		setUpMousePieMenu();
		setUpPermaPieMenu();
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
		for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
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
		radial.setName("radial");
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
		pie = new PieMenu(shape, style);

		/* Customizing the behavior. */
		pie.setHoverIsSelection(false);
		pie.setInfiniteSelectionRange(true);
		pie.setResetSelectionOnAppear(true);
        pie.setRemainDisplayed(false);

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
		for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
			Label label = new Label(Integer.toString(pieAmount++), skin);
			pie.addActor(label);
		}

		/* Setting up the demo-button. */
		final TextButton textButton = new TextButton("Pie",  skin);
		root.add(textButton).expand().bottom();

		/* Including the Widget in the Stage. */
		pie.attachToActor(textButton); // assigns the listener to the Actor and positions the menu
		stage.addActor(pie);
		pie.setName("pie");
	}

	private void setUpMousePieMenu() {

		/* Setting up and creating the widget. */
		PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
		style.radius = 200;
		style.innerRadius = 20;
		style.backgroundColor = new Color(1,1,1,.1f);
		style.selectedColor = new Color(.5f,.5f,.5f,1);
		style.childRegionColor = new Color(.33f,.33f,.33f,1);
		style.alternateChildRegionColor = new Color(.25f,.25f,.25f,1);
		mousePie = new PieMenu(shape, style);

		/* Customizing the behavior. */
		mousePie.setHoverIsSelection(false);
		mousePie.setInfiniteSelectionRange(true);
		mousePie.setResetSelectionOnAppear(false);
        mousePie.setRemainDisplayed(false);

		/* Adding some listeners, just 'cuz... */
		mousePie.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("ChangeListener - selected index: " + mousePie.getSelectedIndex());
			}
		});
		mousePie.setHoverChangeListener(new PieMenu.HoverChangeListener() {
			@Override
			public void onHoverChange() {
				System.out.println("HoverChangeListener - highlighted index: " + mousePie.getHighlightedIndex());
			}
		});

		/* Populating the widget. */
		for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
			Label label = new Label(Integer.toString(mousePieAmount++), skin);
			mousePie.addActor(label);
		}

		/* Including the Widget in the Stage. */
		stage.addActor(mousePie);
		mousePie.setName("mousePie");
	}

	private void setUpPermaPieMenu() {

		/* Setting up and creating the widget. */
		PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
		style.radius = 50;
		style.innerRadius = 10;
		style.totalDegreesDrawn = 180;
		style.backgroundColor = new Color(1,1,1,.2f);
		style.selectedColor = new Color(.5f,.5f,.5f,1);
		style.childRegionColor = new Color(.33f,.33f,.33f,1);
		style.alternateChildRegionColor = new Color(.25f,.25f,.25f,1);
        style.separatorColor = new Color(.5f,1,.3f,1);
		permaPie = new PieMenu(shape, style);

		/* Customizing the behavior. */
		permaPie.setHoverIsSelection(false);
		permaPie.setInfiniteSelectionRange(false);
		permaPie.setResetSelectionOnAppear(false);
		permaPie.setRemainDisplayed(true);

		/* Adding some listeners, just 'cuz... */
		permaPie.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("ChangeListener - selected index: " + permaPie.getSelectedIndex());
			}
		});
		permaPie.setHoverChangeListener(new PieMenu.HoverChangeListener() {
			@Override
			public void onHoverChange() {
				System.out.println("HoverChangeListener - highlighted index: " + permaPie.getHighlightedIndex());
			}
		});

		/* Populating the widget. */
		for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
			Label label = new Label(Integer.toString(permaPieAmount++), skin);
			permaPie.addActor(label);
		}
//		permaPie.debug();

		/* Including the Widget at some absolute coordinate in the World. */
		permaPie.setPosition(Gdx.graphics.getWidth()/2, 0); // (320,0)
		stage.addActor(permaPie);
		permaPie.attachToActor(permaPie);
//		permaPie.addListener(permaPie.dragListener);
		permaPie.setVisible(true);
		permaPie.setName("permaPie");
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
			mousePie.addActor(new Label(Integer.toString(mousePieAmount++), skin));
			permaPie.addActor(new Label(Integer.toString(permaPieAmount++), skin));
			radial.addActor(new Label(Integer.toString(radialAmount++), skin));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
			pieAmount = 0;
			mousePieAmount = 0;
			permaPieAmount = 0;
			radialAmount = 0;
			dispose();
			create();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
		    if(pie.getChildren().size == 0)
		        return;
			pie.removeActor(pie.getChild(pie.getChildren().size-1));
			mousePie.removeActor(mousePie.getChild(mousePie.getChildren().size-1));
			permaPie.removeActor(permaPie.getChild(permaPie.getChildren().size-1));
			radial.removeActor(radial.getChild(radial.getChildren().size-1));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
			permaPie.setVisible(!permaPie.isVisible());
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
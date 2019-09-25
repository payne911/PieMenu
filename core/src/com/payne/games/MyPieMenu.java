package com.payne.games;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import space.earlygrey.shapedrawer.PolygonShapeDrawer;


public class MyPieMenu extends ApplicationAdapter {
	private Skin skin;
	private Stage stage;
	private Texture tmpTex;
	private PolygonSpriteBatch batch;
	private PolygonShapeDrawer shape;
	private PieMenu pie;
	private int amount = 0;


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

		/* Setting up the ShapeDrawer. */
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(1,1,1,1);
		pixmap.fill();
		tmpTex = new Texture(pixmap);
		pixmap.dispose();
		shape = new PolygonShapeDrawer(batch, new TextureRegion(tmpTex));

		/* Creating and setting up the widget. */
		pie = new PieMenu(shape);
		pie.angleDrawn = 180;
		pie.angleOffset = 0;
		pie.setInnerRadius(30);
		pie.setFullRadius(100);

		/* Populating the widget. */
		for (int i = 0; i < 5; i++) {
			Label label = new Label(Integer.toString(i), skin);
			pie.addItem(label);
		}

		/* Setting up the demo-button. */
		final TextButton textButton = new TextButton("Test",  skin);
		root.defaults().padBottom(150);
		root.add(textButton).expand().bottom();

		/* Including the Widget in the Stage. */
		pie.attachToActor(textButton); // assigns the listener to the Actor
		stage.addActor(pie);
	}







	@Override
	public void render () {
		Gdx.gl.glClearColor(.25f, .25f, .75f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

		/* Debugging. */
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			pie.addItem(new Label(Integer.toString(amount++), skin));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
			dispose();
			create();
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
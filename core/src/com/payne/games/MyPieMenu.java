package com.payne.games;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import space.earlygrey.shapedrawer.PolygonShapeDrawer;


public class MyPieMenu extends ApplicationAdapter {
	private Skin skin;
	private Stage stage;
	private PolygonSpriteBatch batch;
	private PolygonShapeDrawer shape;
	private PieMenu pie;


	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("skin.json"));
		batch = new PolygonSpriteBatch();

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(1,1,1,1);
		pixmap.fill();
		Texture tmp = new Texture(pixmap);
		pixmap.dispose();
		shape = new PolygonShapeDrawer(batch, new TextureRegion(tmp));

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		Table root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		final TextButton textButton = new TextButton("Test",  skin);
		final TextButton textButton2 = new TextButton("Test2",  skin);
		root.add(textButton).expand().bottom();
		root.add(textButton2).expand().bottom();

		final RadialWidget.RadialWidgetStyle style = new RadialWidget.RadialWidgetStyle();
		style.background = skin.getDrawable("round-gray");
		style.radius = 100;

		textButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RadialWidget radialWidget = new RadialWidget(style);
				for (int i = 0; i < 10; i++) {
					TextButton radialButton = new TextButton(Integer.toString(i), skin);
					radialWidget.addActor(radialButton);
				}
				stage.addActor(radialWidget);

				radialWidget.pack();
				radialWidget.setPosition(textButton.getX() + textButton.getWidth() / 2, textButton.getY() + textButton.getHeight() + 25, Align.bottom);
			}
		});







		pie = new PieMenu(shape);
		pie.angleDrawn = 180;
		pie.radius = 100;
		pie.angleOffset = 0;
		pie.setPosition(420,350);

		textButton2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pie.draw = true;
			}
		});
	}







	@Override
	public void render () {
		Gdx.gl.glClearColor(.25f, .25f, .75f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

		batch.begin();
		pie.draw();
		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			pie.addItem(new PieItem());
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
	}









	public static class RadialWidget extends WidgetGroup {
		private RadialWidgetStyle style;
		private static Vector2 vector2 = new Vector2();

		public RadialWidget() {

		}

		public RadialWidget(RadialWidgetStyle style) {
			setStyle(style);
		}

		public RadialWidget(Skin skin) {
			setStyle(skin.get(RadialWidgetStyle.class));
		}

		public RadialWidget(Skin skin,  String style) {
			setStyle(skin.get(style, RadialWidgetStyle.class));
		}

		public RadialWidgetStyle getStyle() {
			return style;
		}

		public void setStyle(RadialWidgetStyle style) {
			this.style = style;
		}

		@Override
		public float getPrefWidth() {
			return style.radius * 2;
		}

		@Override
		public float getPrefHeight() {
			return style.radius;
		}

		@Override
		public float getMinWidth() {
			return style.radius * 2;
		}

		@Override
		public float getMinHeight() {
			return style.radius;
		}

		@Override
		public void addActor(Actor actor) {
			super.addActor(actor);
			invalidate();
		}

		@Override
		public void layout() {
			for (int i = 0; i < getChildren().size; i++) {
				Actor actor = getChildren().get(i);
				vector2.set(style.radius, 0);
				vector2.rotate(i * 180 / (getChildren().size - 1));
				actor.setPosition(style.radius + vector2.x, vector2.y, Align.center);
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
			super.draw(batch, parentAlpha);
		}

		public static class RadialWidgetStyle {
			public Drawable background;
			public float radius;

			public RadialWidgetStyle() {

			}

			public RadialWidgetStyle(RadialWidgetStyle style) {
				this.background = style.background;
				this.radius = style.radius;
			}
		}
	}
}
package com.payne.games;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;


/**
 * @author: raelus
 */
public class RadialWidget extends WidgetGroup {
    private RadialWidget.RadialWidgetStyle style;
    private static Vector2 vector2 = new Vector2();


    public RadialWidget() {

    }

    public RadialWidget(RadialWidget.RadialWidgetStyle style) {
        setStyle(style);
    }

    public RadialWidget(Skin skin) {
        setStyle(skin.get(RadialWidget.RadialWidgetStyle.class));
    }

    public RadialWidget(Skin skin,  String style) {
        setStyle(skin.get(style, RadialWidget.RadialWidgetStyle.class));
    }

    public RadialWidget.RadialWidgetStyle getStyle() {
        return style;
    }

    public void setStyle(RadialWidget.RadialWidgetStyle style) {
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
        float tmp = (float)180 / getChildren().size;
        float half = (float)1 / 2;
        for (int i = 0; i < getChildren().size; i++) {
            Actor actor = getChildren().get(i);
            vector2.set(style.radius, 0);
            vector2.rotate(tmp * (i + half));
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

        public RadialWidgetStyle(RadialWidget.RadialWidgetStyle style) {
            this.background = style.background;
            this.radius = style.radius;
        }
    }
}
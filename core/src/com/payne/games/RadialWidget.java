package com.payne.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class RadialWidget extends WidgetGroup {
    protected Actor attachedTo;
    protected ShapeDrawer sd;
    protected ShapeDrawerDrawable sdd; // todo: can be used to create custom Drawable background... might not be necessary

    private RadialWidget.RadialWidgetStyle style;
    private static Vector2 vector2 = new Vector2();
    private static Vector2 vector22 = new Vector2();



    public RadialWidget(){
    }

    public RadialWidget(final ShapeDrawer sd, RadialWidget.RadialWidgetStyle style) {
        setStyle(style);
        this.sd = sd;
    }

    public RadialWidget(final ShapeDrawer sd, Skin skin) {
        setStyle(skin.get(RadialWidget.RadialWidgetStyle.class));
        this.sd = sd;
    }

    public RadialWidget(final ShapeDrawer sd, Skin skin,  String style) {
        setStyle(skin.get(style, RadialWidget.RadialWidgetStyle.class));
        this.sd = sd;
    }

    public RadialWidget.RadialWidgetStyle getStyle() {
        return style;
    }



    public void setStyle(RadialWidget.RadialWidgetStyle style) {
        checkStyle(style);
        this.style = style;
        invalidate();
    }

    protected void checkStyle(RadialWidget.RadialWidgetStyle style) {
        if(style.startDegreesOffset < 0)
            throw new IllegalArgumentException("startDegreesOffset cannot be negative.");
        if(style.startDegreesOffset == 360)
            style.startDegreesOffset = 0;
        if(style.radius < 0)
            throw new IllegalArgumentException("radius cannot be negative.");
        if(style.totalDegreesDrawn < 0)
            throw new IllegalArgumentException("totalDegreesDrawn cannot be negative.");
        if(style.totalDegreesDrawn > 360)
            throw new IllegalArgumentException("totalDegreesDrawn must be lower or equal to 360.");
        if(style.startDegreesOffset == 0)
            style.startDegreesOffset = 360;
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
    public boolean removeActor(Actor actor) {
        boolean wasRemoved = super.removeActor(actor);
        if(wasRemoved)
            invalidate();
        return wasRemoved;
    }

    @Override
    public void layout() {
        float tmp = style.totalDegreesDrawn / getChildren().size;
        float half = (float)1 / 2;
        for (int i = 0; i < getChildren().size; i++) {
            Actor actor = getChildren().get(i);
            vector2.set(style.radius, 0);
            vector2.rotate(tmp*(i + half) + style.startDegreesOffset);
            actor.setPosition(vector2.x, vector2.y, Align.center);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawWithShapeDrawer(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    protected void drawWithShapeDrawer(Batch batch, float parentAlpha) {

        /* Background image */
        if(style.background != null)
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());

        /* Rest of background */
        sd.setColor(style.backgroundColor);
        float bgRadian = MathUtils.degreesToRadians*style.totalDegreesDrawn;
        float tmpOffset = MathUtils.degreesToRadians*style.startDegreesOffset;
        sd.sector(getX(), getY(), style.radius, tmpOffset, bgRadian);

        /* Children */
        vector2.set(getX(), getY());
        float tmpRad = bgRadian / getChildren().size;
        for(int i=0; i<getChildren().size; i++) {
            float tmp = tmpOffset + i*tmpRad;
            sd.setColor(i%2 == 0 ? Color.CHARTREUSE : Color.FIREBRICK);
            sd.arc(vector2.x, vector2.y, style.radius, tmp, tmpRad, style.radius-style.innerRadius);
            sd.line(pointAtAngle(vector2, style.innerRadius, tmp),
                    pointAtAngle(vector2, style.radius, tmp),
                    Color.PINK, 3);
        }

        /* The remaining last line to be drawn */
        sd.line(pointAtAngle(vector2, style.innerRadius, tmpOffset + getChildren().size*tmpRad),
                pointAtAngle(vector2, style.radius, tmpOffset + getChildren().size*tmpRad),
                Color.PINK, 3);
    }


    /**
     * To get the coordinates of a point along a line traced from the center,
     * along the designated angle, at the designated distance from the center.
     *
     * @param center center point of the Circle
     * @param radius how far from the center the desired point is
     * @param radian the angle along which the line is calculated
     * @return the point associated with those parameters
     */
    protected Vector2 pointAtAngle(Vector2 center, float radius, float radian) {
        vector22.set(center.x + radius * MathUtils.cos(radian), center.y + radius * MathUtils.sin(radian));
        return vector22;
    }

    protected void getStageMiddleCoordinates(Actor actor) {
        vector2.set(actor.getWidth()/2, actor.getHeight()/2);
        actor.localToStageCoordinates(vector2);
    }

    /**
     * Designates an Actor on which the RadialWidget will be centered.
     *
     * @param attachedTo an Actor
     */
    public void attachToActor(Actor attachedTo) { // todo: add boolean for "followActor" ?
        this.attachedTo = attachedTo;
        updatePosition(); // todo: currently doesn't work when called there..?!
    }

    /**
     * Positions the widget right at the middle of its attached Actor.
     */
    protected void updatePosition() { // todo: option for offsets
        getStageMiddleCoordinates(attachedTo);
        setPosition(vector2.x, vector2.y);
    }


    public static class RadialWidgetStyle {
        public Drawable background;
        public Color backgroundColor, separatorColor, childRegionColor, alternateChildRegionColor;
        public float radius, innerRadius, startDegreesOffset, totalDegreesDrawn;

        public RadialWidgetStyle() {
        }

        public RadialWidgetStyle(RadialWidget.RadialWidgetStyle style) {
            this.background = style.background;
            this.radius = style.radius;
            this.innerRadius = style.innerRadius;
            this.startDegreesOffset = style.startDegreesOffset;
            this.totalDegreesDrawn = style.totalDegreesDrawn;
            this.separatorColor = style.separatorColor;
            this.childRegionColor = style.childRegionColor;
            this.alternateChildRegionColor = style.alternateChildRegionColor;
            this.backgroundColor = style.backgroundColor;
        }
    }
}
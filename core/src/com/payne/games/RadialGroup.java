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

// todo: override "hit"
public class RadialGroup extends WidgetGroup {
    protected Actor attachedTo;
    protected ShapeDrawer sd;

    private RadialGroupStyle style;

    /* For internal use (optimization). */
    private static Vector2 vector2 = new Vector2();
    private static Vector2 vector22 = new Vector2();
    private static Vector2 vector23 = new Vector2();



    public RadialGroup(){
    }

    public RadialGroup(final ShapeDrawer sd, RadialGroupStyle style) {
        setStyle(style);
        this.sd = sd;
    }

    public RadialGroup(final ShapeDrawer sd, Skin skin) {
        setStyle(skin.get(RadialGroupStyle.class));
        this.sd = sd;
    }

    public RadialGroup(final ShapeDrawer sd, Skin skin, String style) {
        setStyle(skin.get(style, RadialGroupStyle.class));
        this.sd = sd;
    }

    public RadialGroupStyle getStyle() {
        return style;
    }



    public void setStyle(RadialGroupStyle style) {
        checkStyle(style);
        this.style = style;
        invalidate();
    }

    protected void checkStyle(RadialGroupStyle style) {
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
        if(style.totalDegreesDrawn == 0)
            style.totalDegreesDrawn = 360;

        if(style.innerRadius < 0)
            throw new IllegalArgumentException("innerRadius cannot be negative.");
        if(style.innerRadius >= style.radius)
            throw new IllegalArgumentException("innerRadius must be smaller than the radius.");

        if(style.childRegionColor == null && style.alternateChildRegionColor != null)
            throw new IllegalArgumentException("childRegionColor must also be specified if you are defining alternateChildRegionColor.");
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

        /* Pre-calculating */
        float bgRadian = MathUtils.degreesToRadians*style.totalDegreesDrawn;
        float tmpOffset = MathUtils.degreesToRadians*style.startDegreesOffset;
        int size = getChildren().size;
        float tmpRad = bgRadian / size;

        /* Background image */
        if(style.background != null)
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());

        /* Rest of background */
        if(style.backgroundColor != null) {
            sd.setColor(style.backgroundColor);
            sd.sector(getX(), getY(), style.radius, tmpOffset, bgRadian);
        }

        /* Children */
        vector2.set(getX(), getY());
        for(int i=0; i<size; i++) {
            float tmp = tmpOffset + i*tmpRad;
            drawChildWithoutSelection(vector2, i, tmp, tmpRad);

            /* Separator */
            drawChildSeparator(vector2, tmp);
        }

        /* The remaining last separator to be drawn */
        drawChildSeparator(vector2, tmpOffset + size*tmpRad);
    }

    protected void drawChildSeparator(Vector2 vector2, float drawnRadianAngle) {
        if(getChildren().size > 1 && style.separatorColor != null)
            sd.line(pointAtAngle(vector22, vector2, style.innerRadius, drawnRadianAngle),
                    pointAtAngle(vector23, vector2, style.radius, drawnRadianAngle),
                    style.separatorColor, 3);
    }

    protected void drawChildWithoutSelection(Vector2 vector2, int index, float startAngle, float radian) {
        if(style.childRegionColor != null) {
            if(style.alternateChildRegionColor != null) {
                sd.setColor(index%2 == 0 ? style.childRegionColor : style.alternateChildRegionColor);
                sd.arc(vector2.x, vector2.y, style.radius, startAngle, radian, style.radius-style.innerRadius);
            } else {
                sd.setColor(style.childRegionColor);
                sd.arc(vector2.x, vector2.y, style.radius, startAngle, radian, style.radius-style.innerRadius);
            }
        }
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
    protected Vector2 pointAtAngle(Vector2 output, Vector2 center, float radius, float radian) {
        output.set(center.x + radius * MathUtils.cos(radian), center.y + radius * MathUtils.sin(radian));
        return output;
    }

    protected void getStageMiddleCoordinates(Actor actor) {
        vector2.set(actor.getWidth()/2, actor.getHeight()/2);
        actor.localToStageCoordinates(vector2);
    }

    @Override
    public void act(float delta) {
        updatePosition();
        super.act(delta);
    }

    /**
     * Designates an Actor on which the RadialWidget will be centered.
     * todo: apparently should be a Wrapper class for both the Widget and associated Button
     * @param attachedTo an Actor
     */
    public void attachToActor(Actor attachedTo) {
        this.attachedTo = attachedTo;
    }

    /**
     * Positions the widget right at the middle of its attached Actor.
     */
    protected void updatePosition() { // todo: option for offsets
        if(attachedTo != null) {
            getStageMiddleCoordinates(attachedTo);
            setPosition(vector2.x, vector2.y);
        }
    }


    public static class RadialGroupStyle {
        public Drawable background;
        public Color backgroundColor, separatorColor, childRegionColor, alternateChildRegionColor;
        public float radius, innerRadius, startDegreesOffset, totalDegreesDrawn;

        public RadialGroupStyle() {
        }

        public RadialGroupStyle(RadialGroupStyle style) {
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
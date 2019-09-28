package com.payne.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import space.earlygrey.shapedrawer.ShapeDrawer;


/**
 * A RadialGroup is a container of Actors which will be laid out in a circular
 * fashion.
 *
 * @author Jérémi Grenier-Berthiaume (aka "payne")
 */
public class RadialGroup extends WidgetGroup {

    /**
     * Used to draw on the screen many elements of the style.
     */
    protected ShapeDrawer sd;

    /**
     * Defines the way the widget looks.
     */
    private RadialGroupStyle style;

    /* For internal use (optimization). */
    protected static final float BG_BUFFER = 1;
    private static Vector2 vector2 = new Vector2();
    private static Vector2 vector22 = new Vector2();
    private static Vector2 vector23 = new Vector2();



    public RadialGroup(final ShapeDrawer sd, RadialGroupStyle style) {
        setStyle(style);
        this.sd = sd;
        setVisible(false);
        setTouchable(Touchable.childrenOnly);
    }

    public RadialGroup(final ShapeDrawer sd, Skin skin) {
        setStyle(skin.get(RadialGroupStyle.class));
        this.sd = sd;
        setVisible(false);
        setTouchable(Touchable.childrenOnly);
    }

    public RadialGroup(final ShapeDrawer sd, Skin skin, String style) {
        setStyle(skin.get(style, RadialGroupStyle.class));
        this.sd = sd;
        setVisible(false);
        setTouchable(Touchable.childrenOnly);
    }

    public RadialGroupStyle getStyle() {
        return style;
    }



    public void setStyle(RadialGroupStyle style) {
        checkStyle(style);
        this.style = style;
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
        invalidate();
    }

    protected void checkStyle(RadialGroupStyle style) {
        if(style.startDegreesOffset < 0)
            throw new IllegalArgumentException("startDegreesOffset cannot be negative.");
        if(style.startDegreesOffset == 360)
            style.startDegreesOffset = 0;

        if(style.radius < BG_BUFFER)
            throw new IllegalArgumentException("radius cannot be smaller than " + BG_BUFFER + ".");

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
        return style.radius * 2;
    }

    @Override
    public float getMinWidth() {
        return style.radius * 2;
    }

    @Override
    public float getMinHeight() {
        return style.radius * 2;
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
            vector2.set((style.radius+style.innerRadius)/2, 0);
            vector2.rotate(tmp*(i + half) + style.startDegreesOffset);
            actor.setPosition(vector2.x+style.radius, vector2.y+style.radius, Align.center);
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
            sd.sector(getX()+style.radius, getY()+style.radius, style.radius-BG_BUFFER, tmpOffset, bgRadian);
        }

        /* Children */
        vector2.set(getX()+style.radius, getY()+style.radius);
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
                sd.arc(vector2.x, vector2.y, (style.radius+style.innerRadius)/2, startAngle, radian, style.radius-style.innerRadius);
            } else {
                sd.setColor(style.childRegionColor);
                sd.arc(vector2.x, vector2.y, (style.radius+style.innerRadius)/2, startAngle, radian, style.radius-style.innerRadius);
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

    @Override
    public void act(float delta) {
        // todo: animate here?!
        super.act(delta);
    }


    /**
     * @param x x-coordinate relative to the origin (bottom left) of the widget
     * @param y y-coordinate relative to the origin (bottom left) of the widget
     * @return deepest child's hit at (x,y). Else, the widget itself if it's
     *         the background. Else null.
     */
    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && getTouchable() == Touchable.disabled) return null;
        if (!isVisible()) return null;

        localToStageCoordinates(vector2.set(x,y));
        int childIndex = findChildSectorAtAbsolute(vector2.x,vector2.y);
        if (childIndex < getChildren().size) {
            Actor child = getChildren().get(childIndex);
            if(child.getTouchable() == Touchable.disabled)
                return this;
            child.parentToLocalCoordinates(vector2.set(x, y));
            Actor hit = child.hit(vector2.x, vector2.y, touchable);
            if(hit != null)
                return hit;
            else
                return this;
        }

        return null;
    }

    /**
     * Given a coordinate, find the index of the child (if any).
     *
     * @param x x-coordinate in the Stage.
     * @param y y-coordinate in the Stage.
     * @return The index of the child at that coordinate.
     *         If there are no child there, the amount of children is returned.
     */
    public int findChildSectorAtAbsolute(float x, float y) {
        float angle = angleAtAbsolute(x,y);
        angle = ((angle - style.startDegreesOffset) % 360 + 360) % 360; // normalizing the angle
        int childIndex = MathUtils.floor(angle / style.totalDegreesDrawn * getChildren().size);
        stageToLocalCoordinates(vector2.set(x,y));
        return isWithinRadii(vector2.x - style.radius, vector2.y - style.radius) ? childIndex : getChildren().size; // size is equivalent to "invalid"
    }

    /**
     * Finding the angle, in degrees, compared to the origin (i.e. center) of the Widget.<br>
     * The output is non-normalized. To get a normalized angle, do:
     * <pre>
     * {@code
     * float angle = angleAtAbsolute(x,y);
     * angle = ((angle - style.startDegreesOffset) % 360 + 360) % 360;
     * }
     * </pre>
     *
     * @param x x-coordinate in the Stage.
     * @param y y-coordinate in the Stage.
     * @return a non-normalized angle of the position of the cursor
     *         relative to the origin (i.e. middle) of the widget
     */
    public float angleAtAbsolute(float x, float y) {
        return MathUtils.radiansToDegrees * MathUtils.atan2(y - (getY() + style.radius), x - (getX() + style.radius));
    }

    /**
     * Checks whether or not the input coordinate is in between (inclusively)
     * the innerRadius and the radius of the widget.
     *
     * @param x x-coordinate relative to the center of the widget's
     * @param y y-coordinate relative to the center of the widget's
     * @return 'true' only if the coordinates fall within the widget's radii.
     */
    public boolean isWithinRadii(float x, float y) {
        float distance = pow2(x) + pow2(y);
        float innerRadSquared = pow2(style.innerRadius);
        float radSquared = pow2(style.radius);
        return distance >= innerRadSquared && distance <= radSquared;
    }

    /**
     * @return in * in
     */
    protected float pow2(float in) {
        return in*in;
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
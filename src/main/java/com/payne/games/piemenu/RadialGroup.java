package com.payne.games.piemenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import space.earlygrey.shapedrawer.ShapeDrawer;


/**
 * A RadialGroup aims at providing the user with a simple way to lay out
 * the contained Actors in a circular fashion.
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


    /**
     * Used internally for the shared properties among constructors of RadialWidgets.
     */
    protected RadialGroup(final ShapeDrawer sd) {
        this.sd = sd;
        setVisible(false);
    }

    /**
     * A RadialGroup aims at providing the user with a simple way to lay out
     * the contained Actors in a circular fashion.
     *
     * @param sd used to draw everything but the contained actors.
     * @param style defines the way the widget looks like.
     */
    public RadialGroup(final ShapeDrawer sd, RadialGroupStyle style) {
        this(sd);
        setStyle(style);
        setTouchable(Touchable.childrenOnly);
    }

    /**
     * A RadialGroup aims at providing the user with a simple way to lay out
     * the contained Actors in a circular fashion.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin) {
        this(sd);
        setStyle(skin.get(RadialGroupStyle.class));
        setTouchable(Touchable.childrenOnly);
    }

    /**
     * A RadialGroup aims at providing the user with a simple way to lay out
     * the contained Actors in a circular fashion.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin, String style) {
        this(sd);
        setStyle(skin.get(style, RadialGroupStyle.class));
        setTouchable(Touchable.childrenOnly);
    }

    /**
     * @return the Style that defines this Widget. This style contains information
     * about what is the value of the radius or the width of the separators, for
     * example.
     */
    public RadialGroupStyle getStyle() {
        return style;
    }


    /**
     * Runs checks before assigning the style to the Widget. Only a valid style
     * will pass the test.
     *
     * @param style the style that will be checked before being assigned.
     */
    public void setStyle(RadialGroupStyle style) {
        checkStyle(style);
        this.style = style;
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
        invalidate();
    }

    /**
     * Ensures the input values for the given style are valid.
     *
     * @param style a style class you want to check properties of.
     */
    protected void checkStyle(RadialGroupStyle style) {
        if(style.separatorWidth < 0)
            throw new IllegalArgumentException("separatorWidth cannot be negative.");

        if(style.circumferenceWidth < 0)
            throw new IllegalArgumentException("circumferenceWidth cannot be negative.");

        if(style.startDegreesOffset < 0)
            throw new IllegalArgumentException("startDegreesOffset cannot be negative.");
        if(style.startDegreesOffset >= 360)
            throw new IllegalArgumentException("startDegreesOffset must be lower than 360.");

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
            throw new IllegalArgumentException("childRegionColor must also be specified if you are defining alternateChildRegionColor. " +
                    "You can however only specify the childRegionColor, if you want.");
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
        float degreesPerChild = style.totalDegreesDrawn / getAmountOfChildren();
        float half = 1f / 2;
        for (int i = 0; i < getAmountOfChildren(); i++) {
            Actor actor = getChildren().get(i);
            vector2.set((style.radius+style.innerRadius)/2, 0);
            vector2.rotate(degreesPerChild*(i + half) + style.startDegreesOffset);

            if(actor instanceof Image) {
                /* Adjusting images to fit within their sector. */
                float size = 2*(style.radius*MathUtils.sinDeg(degreesPerChild/2)
                        - (MathUtils.sinDeg(degreesPerChild/2))*(style.radius - style.innerRadius));
                size *= 1.26; // todo: hard-coded and should get tested more thoroughly
                actor.setSize(size, size);
            }
            actor.setPosition(vector2.x+style.radius, vector2.y+style.radius, Align.center);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawWithShapeDrawer(batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    /**
     * Takes care of drawing everything that {@link #layout()} didn't.
     * (Basically everything but the children Actors.)
     *
     * @param batch a Batch used to draw Drawables. The {@link #sd} is used to
     *              draw everything else.
     * @param parentAlpha
     */
    protected void drawWithShapeDrawer(Batch batch, float parentAlpha) {

        /* Pre-calculating */
        float bgRadian = MathUtils.degreesToRadians*style.totalDegreesDrawn;
        float tmpOffset = MathUtils.degreesToRadians*style.startDegreesOffset;
        int size = getAmountOfChildren();
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
        if(getAmountOfChildren() > 1 && style.separatorColor != null)
            sd.line(pointAtAngle(vector22, vector2, style.innerRadius, drawnRadianAngle),
                    pointAtAngle(vector23, vector2, style.radius, drawnRadianAngle),
                    style.separatorColor, style.separatorWidth);
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
        drawChildCircumference(vector2, startAngle, radian, style.radius - style.circumferenceWidth/2);
    }

    protected void drawChildCircumference(Vector2 vector2, float startAngle, float radian, float radius) {
        if(style.circumferenceColor != null && style.circumferenceWidth > 0) {
            sd.setColor(style.circumferenceColor);
            sd.arc(vector2.x, vector2.y, radius, startAngle, radian, style.circumferenceWidth);
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
    public Vector2 pointAtAngle(Vector2 output, Vector2 center, float radius, float radian) {
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
     * @param touchable if {@code true}, hit detection will respect the
     *                  {@link #setTouchable(Touchable) touchability}.
     * @return deepest child's hit at (x,y). Else, the widget itself if it's
     *         the background. Else null.
     */
    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (touchable && getTouchable() == Touchable.disabled) return null;
        if (!isVisible()) return null;

        localToStageCoordinates(vector2.set(x,y));
        int childIndex = findChildSectorAtStage(vector2.x,vector2.y);
        if (childIndex < getAmountOfChildren()) {
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
    public int findChildSectorAtStage(float x, float y) {
        float angle = angleAtStage(x,y);
        angle = ((angle - style.startDegreesOffset) % 360 + 360) % 360; // normalizing the angle
        int childIndex = MathUtils.floor(angle / style.totalDegreesDrawn * getAmountOfChildren());
        stageToLocalCoordinates(vector2.set(x,y));
        return isWithinRadii(vector2.x - style.radius, vector2.y - style.radius) ? childIndex : getAmountOfChildren(); // size is equivalent to "invalid"
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
    public float angleAtStage(float x, float y) {
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
     * Returns the input to the power of 2.
     *
     * @return in * in
     */
    protected float pow2(float in) {
        return in*in;
    }

    /**
     * Centers the Widget on the current position of the mouse.
     */
    public void centerOnMouse() {
        setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), Align.center);
    }

    /**
     * Centers the Widget on the center point of the provided Actor.
     *
     * @param actor the Actor to center on.
     */
    public void centerOnActor(Actor actor) {
        if(actor == null)
            return;
        setPosition(actor.getX() + actor.getWidth()/2,
                actor.getY() + actor.getHeight()/2, Align.center);
    }

    /**
     * Given a child index, find whether or not it would be a valid candidate to
     * highlight or select.
     *
     * @param index an integer that would usually be the output of
     *              {@link #findChildSectorAtStage(float, float)}.
     * @return {@code true} only if the index is linked to a valid child sector.
     */
    public boolean isValidIndex(int index) {
        return !(index <= -1 || index >= getAmountOfChildren());
    }

    /**
     * @return The amount of Actors that are currently contained in the Widget.
     */
    public int getAmountOfChildren() {
        return getChildren().size;
    }


    /**
     * @return the ShapeDrawer used to draw everything but the contained Actors.
     */
    public ShapeDrawer getShapeDrawer() {
        return sd;
    }






    /**
     * Encompasses all the characteristics that define the way the Widget will be drawn.
     */
    public static class RadialGroupStyle {

        /**
         * <i>Recommended. Optional.</i><br>
         * A background that will be drawn behind everything else within the Widget.<br>
         * Be mindful of the fact that this is unaffected by any of the other
         * variables: it will be resized to fit in the whole region that
         * represents the position, width and height of the widget.
         */
        public Drawable background;

        /**
         * <i>Optional.</i><br>
         * A background color that, if provided, will be drawn over the
         * background image and below everything else.<br>
         * It mostly acts as a quick set up option if you do not have an image
         * for you background.
         */
        public Color backgroundColor;

        /**
         * <i>Recommended. Optional.</i><br>
         * The color used by the separating lines between each item.<br>
         * If you do not define a {@link #separatorWidth} along with this value,
         * no lines will be visible.
         */
        public Color separatorColor;

        /**
         * <i>Recommended. Optional.</i><br>
         * The color used to fill the "pie sectors" of each item.<br>
         * Consider using a fairly high alpha value if you are providing a
         * {@link #background} drawable.
         */
        public Color childRegionColor;

        /**
         * <i>Optional.</i><br>
         * If this color is set, the "pie sectors" will alternate between the
         * {@link #childRegionColor} and this one so that their defining region
         * is more easily distinguished.
         */
        public Color alternateChildRegionColor;

        /**
         * <i>Optional.</i><br>
         * The color used for the line that defines the circumference of the
         * Widget. If the Widget is not a complete a circle, this will only be
         * applied along the partial circumference.<br>
         * If you do not define a {@link #circumferenceWidth} along with this
         * value, no circumference will be visible.
         */
        public Color circumferenceColor;

        /**
         * <i>Required.</i><br>
         * The radius that defines how big the Widget will be.<br>
         * It must be bigger than {@value #BG_BUFFER}.
         */
        public float radius;

        /**
         * <i>Optional.</i><br>
         * If provided, the {@link #childRegionColor} will only fill the region
         * defined between the {@link #radius} and this value. A hole will be
         * left into the middle of the Widget, like a doughnut, and if a
         * {@link #background} or a {@link #backgroundColor} was provided, it
         * will be visible in the middle.<br>
         * Actors inserted into the Widget are placed in the middle between the
         * innerRadius and the {@link #radius}.
         */
        public float innerRadius;

        /**
         * <i>Optional.</i><br>
         * Considers that angles start at 0 along the x-axis and increment up
         * to 360 in a counter-clockwise fashion.<br>
         * Defines how far from that origin the {@link #totalDegreesDrawn} will
         * be drawn.<br>
         * For example, if {@code startDegreesOffset = 90} and
         * {@code totalDegreesDrawn = 180}, you would obtain the left half of a
         * circle. All the children would be spread within that half-circle evenly.
         */
        public float startDegreesOffset;

        /**
         * <i>Required.</i><br>
         * If not defined, will be initialized to 360 by default.<br>
         * Determines the total amount of degrees into which the contained
         * Actors will be spread.<br>
         * For example, if {@code startDegreesOffset = 0} and
         * {@code totalDegreesDrawn = 180}, you would obtain the top half of a
         * circle.
         */
        public float totalDegreesDrawn;

        /**
         * <i>Recommended. Optional.</i><br>
         * Determines how wide the lines that separate each region will be.<br>
         * If no {@link #separatorColor} was provided along with this value,
         * no lines will be drawn.
         */
        public float separatorWidth;

        /**
         * <i>Optional.</i><br>
         * Determines how wide the circumference line will be.<br>
         * If no {@link #circumferenceColor} was provided along with this value,
         * no circumference will be drawn.
         */
        public float circumferenceWidth;


        /**
         * Encompasses all the characteristics that define the way the Widget
         * will be drawn.
         */
        public RadialGroupStyle() {
        }

        /**
         * Encompasses all the characteristics that define the way the Widget
         * will be drawn.
         *
         * @param style a Style to copy the parameters from.
         */
        public RadialGroupStyle(RadialGroupStyle style) {
            this.background = style.background;
            this.radius = style.radius;
            this.innerRadius = style.innerRadius;
            this.startDegreesOffset = style.startDegreesOffset;
            this.totalDegreesDrawn = style.totalDegreesDrawn;
            this.separatorColor = new Color(style.separatorColor);
            this.childRegionColor = new Color(style.childRegionColor);
            this.alternateChildRegionColor = new Color(style.alternateChildRegionColor);
            this.backgroundColor = new Color(style.backgroundColor);
        }
    }
}
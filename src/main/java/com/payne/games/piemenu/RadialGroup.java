package com.payne.games.piemenu;

import com.badlogic.gdx.Gdx;
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

    /**
     * <i>Required.</i><br>
     * The radius that defines how big the Widget will be.<br>
     * It must be bigger than {@value #BG_BUFFER}.
     */
    protected float radius;

    /**
     * <i>Optional.</i><br>
     * If provided, the {@link RadialGroupStyle#childRegionColor} will only fill
     * the region defined between the {@link #radius} and this value. A hole
     * will be left into the middle of the Widget, like a doughnut, and if a
     * {@link RadialGroupStyle#background} or a
     * {@link RadialGroupStyle#backgroundColor} was provided, it will be visible
     * in the middle.<br>
     * Actors inserted into the Widget are placed in the middle between the
     * innerRadius and the {@link #radius}.
     */
    protected float innerRadius;

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
    protected float startDegreesOffset;

    /**
     * <i>Required.</i><br>
     * If not defined, will be initialized to 360 by default.<br>
     * Determines the total amount of degrees into which the contained
     * Actors will be spread.<br>
     * For example, if {@code startDegreesOffset = 0} and
     * {@code totalDegreesDrawn = 180}, you would obtain the top half of a
     * circle.
     */
    protected float totalDegreesDrawn = 360;

    /**
     * The alpha value propagated to the whole Widget. It defaults to 1.
     * All the alpha values are multiplied by this value.
     */
    protected float globalAlphaMultiplier = 1;


    /* For internal use (optimization). */
    private float lastRadius = 0;
    protected static final float BG_BUFFER = 1;
    private static Vector2 vector2 = new Vector2();
    private static Vector2 vector22 = new Vector2();
    private static Vector2 vector23 = new Vector2();


    /**
     * Used internally for the shared properties among constructors of RadialWidgets.
     */
    protected RadialGroup(final ShapeDrawer sd, float radius) {
        this.sd = sd;
        setRadius(radius);
    }

    /**
     * Used internally for the shared properties among constructors of RadialWidgets.
     */
    protected RadialGroup(final ShapeDrawer sd, float radius, float innerRadius) {
        this.sd = sd;
        setRadius(radius);
        setInnerRadius(innerRadius);
    }

    /**
     * Used internally for the shared properties among constructors of RadialWidgets.
     */
    protected RadialGroup(final ShapeDrawer sd, float radius, float innerRadius,
                          float startDegreesOffset) {
        this.sd = sd;
        setRadius(radius);
        setInnerRadius(innerRadius);
        setStartDegreesOffset(startDegreesOffset);
    }

    /**
     * Used internally for the shared properties among constructors of RadialWidgets.
     */
    protected RadialGroup(final ShapeDrawer sd, float radius, float innerRadius,
                          float startDegreesOffset, float totalDegreesDrawn) {
        this.sd = sd;
        setRadius(radius);
        setInnerRadius(innerRadius);
        setStartDegreesOffset(startDegreesOffset);
        setTotalDegreesDrawn(totalDegreesDrawn);
    }

    private void constructorsCommon() {
        setTouchable(Touchable.childrenOnly);
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param style defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     */
    public RadialGroup(final ShapeDrawer sd, RadialGroupStyle style, float radius) {
        this(sd, radius);
        setStyle(style);
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param style defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the regions start.
     */
    public RadialGroup(final ShapeDrawer sd, RadialGroupStyle style, float radius,
                       float innerRadius) {
        this(sd, radius, innerRadius);
        setStyle(style);
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param style defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the regions start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public RadialGroup(final ShapeDrawer sd, RadialGroupStyle style, float radius,
                       float innerRadius, float startDegreesOffset) {
        this(sd, radius, innerRadius, startDegreesOffset);
        setStyle(style);
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param style defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the regions start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public RadialGroup(final ShapeDrawer sd, RadialGroupStyle style, float radius,
                       float innerRadius, float startDegreesOffset, float totalDegreesDrawn) {
        this(sd, radius, innerRadius, startDegreesOffset, totalDegreesDrawn);
        setStyle(style);
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin, float radius) {
        this(sd, radius);
        setStyle(skin.get(RadialGroupStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the regions start.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin, float radius,
                       float innerRadius) {
        this(sd, radius, innerRadius);
        setStyle(skin.get(RadialGroupStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the regions start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin, float radius,
                       float innerRadius, float startDegreesOffset) {
        this(sd, radius, innerRadius, startDegreesOffset);
        setStyle(skin.get(RadialGroupStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the regions start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin, float radius,
                       float innerRadius, float startDegreesOffset, float totalDegreesDrawn) {
        this(sd, radius, innerRadius, startDegreesOffset, totalDegreesDrawn);
        setStyle(skin.get(RadialGroupStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param radius the {@link #radius} that defines the size of the widget.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin, String style, float radius) {
        this(sd, radius);
        setStyle(skin.get(style, RadialGroupStyle.class));
        constructorsCommon();
    }


    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the regions start.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin, String style, float radius,
                       float innerRadius) {
        this(sd, radius, innerRadius);
        setStyle(skin.get(style, RadialGroupStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the regions start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin, String style, float radius,
                       float innerRadius, float startDegreesOffset) {
        this(sd, radius, innerRadius, startDegreesOffset);
        setStyle(skin.get(style, RadialGroupStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the regions start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public RadialGroup(final ShapeDrawer sd, Skin skin, String style, float radius,
                       float innerRadius, float startDegreesOffset, float totalDegreesDrawn) {
        this(sd, radius, innerRadius, startDegreesOffset, totalDegreesDrawn);
        setStyle(skin.get(style, RadialGroupStyle.class));
        constructorsCommon();
    }













    /**
     * Returns the label's style. Modifying the returned style may not have an
     * effect until {@link #setStyle(RadialGroupStyle)} is called.<br>
     * It's probable that your code will look like this (to give you an idea):
     * <pre>
     * {@code
     * radialGroup.getStyle().whatYouWantToChange = someNewValue;
     * radialGroup.setStyle(radialGroup.getStyle());
     * }
     * </pre>
     *
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
        invalidate();
    }

    /**
     * Ensures the input values for the given style are valid.
     *
     * @param style a style class you want to check properties of.
     */
    protected void checkStyle(RadialGroupStyle style) {
        if(style == null)
            throw new IllegalArgumentException("style cannot be null.");

        if(style.separatorWidth < 0)
            throw new IllegalArgumentException("separatorWidth cannot be negative.");

        if(style.circumferenceWidth < 0)
            throw new IllegalArgumentException("circumferenceWidth cannot be negative.");

        if(style.childRegionColor == null && style.alternateChildRegionColor != null)
            throw new IllegalArgumentException("childRegionColor must also be specified if you are defining alternateChildRegionColor. " +
                    "You can however only specify the childRegionColor, if you want.");
    }

    @Override
    public float getPrefWidth() {
        return radius * 2;
    }

    @Override
    public float getPrefHeight() {
        return radius * 2;
    }

    @Override
    public float getMinWidth() {
        return radius * 2;
    }

    @Override
    public float getMinHeight() {
        return radius * 2;
    }

    @Override
    public void addActor(Actor actor) {
        if(actor == null) throw new IllegalArgumentException("actor cannot be null.");
        super.addActor(actor);
        invalidate();
    }

    @Override
    public boolean removeActor(Actor actor) {
        if(actor == null) throw new IllegalArgumentException("actor cannot be null.");
        boolean wasRemoved = super.removeActor(actor);
        if(wasRemoved)
            invalidate();
        return wasRemoved;
    }

    /**
     * Determines how far from the center the contained child Actors should be.
     * By default, the value is {@code (style.radius+style.innerRadius)/2}.<br>
     * Override this method when creating your Widget if you want to have control
     * on where the Actors get placed.<br>
     * <b>Do not</b> position the Actor directly in this method: that is handled
     * internally. Just return the desired distance from the center.<br><br>
     * Here is an example:
     * <pre>
     * {@code
     * RadialGroup myWidget = new RadialGroup(shapeDrawer, myStyle) {
     *     public float getActorDistanceFromCenter(Actor actor) {
     *         // We want the Actors to be placed closer to the edge than the default value
     *         return getAmountOfChildren() > 1
     *                 ? getStyle().radius - getChild(0).getWidth()
     *                 : 0;
     *     }
     * };
     * }
     * </pre>
     *
     * @param actor the Actor whose position is to be determined. This instance
     *              is here for convenience so that you can run a check with
     *              {@code instanceof} if you want to.
     * @return distance of this Actor's center from the center of the widget.
     */
    public float getActorDistanceFromCenter(Actor actor) {
        return (radius+innerRadius)/2;
    }

    /**
     * Used to change the size of an Actor according to certain rules. By
     * default, there are no changes applied.<br>
     * Override this method when creating your Widget if you want to have control
     * on how to resize the Actors that get placed within your Widget.<br>
     * <b>Don't forget to actually call {@link #setSize(float, float)} on the
     * Actor</b>, else nothing will happen.<br><br>
     * Here is an example:
     * <pre>
     * {@code
     * RadialGroup myWidget = new RadialGroup(shapeDrawer, myStyle) {
     *     public void adjustActorSize(Actor actor, float degreesPerChild, float actorDistanceFromCenter) {
     *         float size = getEstimatedRadiusAt(degreesPerChild, actorDistanceFromCenter);
     *         size *= 1.26f; // adjusting the returned value to our likes
     *         actor.setSize(size, size);
     *     }
     * };
     * }
     * </pre>
     *
     * @see #getEstimatedRadiusAt(float, float)
     * @param actor the Actor whose size is to be adjusted.
     * @param degreesPerChild the amount of degrees that a child's sector takes.
     * @param actorDistanceFromCenter the distance at which the child Actor is
     *                                positioned from the center of the widget.
     */
    public void adjustActorSize(Actor actor, float degreesPerChild, float actorDistanceFromCenter) {

    }

    /**
     * Used to estimate the radius of a circle to be constrained within the widget
     * according to the input parameters. Doubling the returned value would give
     * you the size of a contained Actor which would roughly fill most of its
     * sector, or possibly overflow slightly. It is suggested to adjust slightly
     * the returned value by multiplying it with a factor of your choice.<br>
     * The return value's is calculated this way:
     * <pre>
     * {@code
     * float tmp1 = actorDistanceFromCenter * MathUtils.sinDeg(degreesPerChild/2);
     * float tmp2 = style.radius - actorDistanceFromCenter;
     * float tmp3 = actorDistanceFromCenter - style.innerRadius;
     * return Math.min(tmp1, tmp2, tmp3); // pseudo-code for clarity
     * }
     * </pre>
     * It's basically the minimum between 3 different possible radius values
     * based on certain layout properties.
     *
     * @param degreesPerChild the amount of degrees that a child's sector takes.
     * @param actorDistanceFromCenter the distance at which the child Actor is
     *                                positioned from the center of the widget.
     * @return an estimated radius value for an Actor placed with the given
     *         constraints.
     */
    public float getEstimatedRadiusAt(float degreesPerChild, float actorDistanceFromCenter) {
        float tmp1 = actorDistanceFromCenter * MathUtils.sinDeg(degreesPerChild/2);
        float tmp2 = radius - actorDistanceFromCenter;
        float tmp3 = actorDistanceFromCenter - innerRadius;
        return Math.min(Math.min(tmp1, tmp2), tmp3);
    }

    @Override
    public void layout() {
        float degreesPerChild = totalDegreesDrawn / getAmountOfChildren();
        float half = 1f / 2;
        for (int i = 0; i < getAmountOfChildren(); i++) {
            Actor actor = getChildren().get(i);
            float dist = getActorDistanceFromCenter(actor);
            vector2.set(dist, 0);
            vector2.rotate(degreesPerChild*(i + half) + startDegreesOffset);
            adjustActorSize(actor, degreesPerChild, dist); // overridden by user
            actor.setPosition(vector2.x+radius, vector2.y+radius, Align.center);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawWithShapeDrawer(batch, parentAlpha, totalDegreesDrawn);
        drawMe(batch, parentAlpha);
    }

    @Deprecated
    protected void drawMe(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    /**
     * Used to propagate the parent's alpha value to the children.<br>
     * Changes the ShapeDrawer's color.
     *
     * @param sd the ShapeDrawer whose color will be changed.
     * @param input the Color to be copying RGB values from.
     */
    protected void propagateAlpha(ShapeDrawer sd, Color input) {
        sd.setColor(input.r, input.g, input.b, input.a * globalAlphaMultiplier);
    }

    /**
     * Takes care of drawing everything that {@link #layout()} didn't.
     * (Basically everything but the children Actors.)
     *
     * @param batch a Batch used to draw Drawables. The {@link #sd} is used to
     *              draw everything else.
     * @param parentAlpha
     * @param degreesToDraw how many degrees from the offset should be drawn.
     */
    protected void drawWithShapeDrawer(Batch batch, float parentAlpha, float degreesToDraw) {

        /* Pre-calculating */
        float bgRadian = MathUtils.degreesToRadians*degreesToDraw;
        float tmpOffset = MathUtils.degreesToRadians*startDegreesOffset;
        final int SIZE = getAmountOfChildren();
        float tmpRad = bgRadian / SIZE;

        /* Background image */
        if(style.background != null) {
            Color bc = batch.getColor();
            float restoreAlpha = bc.a;
            batch.setColor(bc.r, bc.g, bc.b, bc.a * globalAlphaMultiplier);
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
            batch.setColor(bc.r, bc.g, bc.b, restoreAlpha);
        }

        /* Rest of background */
        if(style.backgroundColor != null) {
            propagateAlpha(sd, style.backgroundColor);
            sd.sector(getX()+radius, getY()+radius,
                    radius-BG_BUFFER, tmpOffset, bgRadian);
        }

        /* Children */
        vector2.set(getX()+radius, getY()+radius); // center of widget
        for(int i=0; i<SIZE; i++) {
            float tmp = tmpOffset + i*tmpRad;
            drawChildWithoutSelection(vector2, i, tmp, tmpRad);

            /* Separator */
            drawChildSeparator(vector2, tmp);
        }

        /* The remaining last separator to be drawn */
        drawChildSeparator(vector2, tmpOffset + SIZE*tmpRad);
    }

    protected void drawChildSeparator(Vector2 vector2, float drawnRadianAngle) {
        if(getAmountOfChildren() > 1 && style.separatorColor != null) {
            propagateAlpha(sd, style.separatorColor);
            sd.line(pointAtAngle(vector22, vector2, innerRadius, drawnRadianAngle),
                    pointAtAngle(vector23, vector2, radius, drawnRadianAngle),
                    style.separatorWidth);
        }
    }

    protected void drawChildWithoutSelection(Vector2 vector2, int index, float startAngle, float radian) {
        if(style.childRegionColor != null) {
            if(style.alternateChildRegionColor != null) {
                propagateAlpha(sd,
                        index%2 == 0
                                ? style.childRegionColor
                                : style.alternateChildRegionColor);
                sd.arc(vector2.x, vector2.y, (radius+innerRadius)/2,
                        startAngle, radian, radius-innerRadius);
            } else {
                propagateAlpha(sd, style.childRegionColor);
                sd.arc(vector2.x, vector2.y, (radius+innerRadius)/2,
                        startAngle, radian, radius-innerRadius);
            }
        }

        /* Circumference */
        drawChildCircumference(vector2, startAngle, radian,
                radius - style.circumferenceWidth/2);
        if(innerRadius > 0)
            drawChildCircumference(vector2, startAngle, radian,
                    innerRadius + style.circumferenceWidth/2);
    }

    protected void drawChildCircumference(Vector2 vector2, float startAngle, float radian, float radius) {
        if(style.circumferenceColor != null && style.circumferenceWidth > 0) {
            propagateAlpha(sd, style.circumferenceColor);
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
        if (isValidIndex(childIndex)) {
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
        angle = ((angle - startDegreesOffset) % 360 + 360) % 360; // normalizing the angle
        int childIndex = MathUtils.floor(angle / totalDegreesDrawn * getAmountOfChildren());
        stageToLocalCoordinates(vector2.set(x,y));
        return isWithinRadii(vector2.x - radius, vector2.y - radius) ? childIndex : getAmountOfChildren(); // size is equivalent to "invalid"
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
        return MathUtils.radiansToDegrees * MathUtils.atan2(y - (getY() + radius), x - (getX() + radius));
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
        float innerRadSquared = pow2(innerRadius);
        float radSquared = pow2(radius);
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






    /*
    =================================== STYLE ==================================
     */


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
            this.circumferenceWidth = style.circumferenceWidth;
            this.separatorWidth = style.separatorWidth;
            this.separatorColor = new Color(style.separatorColor);
            this.childRegionColor = new Color(style.childRegionColor);
            this.alternateChildRegionColor = new Color(style.alternateChildRegionColor);
            this.backgroundColor = new Color(style.backgroundColor);
        }
    }







    /*
    =============================== GETTERS/SETTERS ============================
     */


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
     * @return the multiplier's value which is applied to all the alpha values
     *         of the things contained by the widget (regions, lines, drawables, etc.)
     */
    public float getGlobalAlphaMultiplier() {
        return globalAlphaMultiplier;
    }

    /**
     * This will globally change the alpha value of the widget.<br>
     * It defaults to 1 (completely opaque).
     *
     * @param globalAlphaMultiplier this value is multiplied to all of the alpha
     *                              value of the things contained by the widget
     *                              (regions, lines, drawables, etc.).
     */
    public void setGlobalAlphaMultiplier(float globalAlphaMultiplier) {
        this.globalAlphaMultiplier = globalAlphaMultiplier;
        setColor(getColor().r, getColor().g, getColor().b, getColor().a * globalAlphaMultiplier);
    }

    /**
     * @see #radius
     * @return The radius that defines how big the Widget will be.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * <i>Required.</i><br>
     * The radius that defines how big the Widget will be.
     *
     * @param radius The value must be bigger than {@value #BG_BUFFER}.
     *               If the value is smaller than the current {@link #innerRadius}
     *               then the {@link #innerRadius} is set to a smaller value.
     */
    public void setRadius(float radius) {
        if(radius < BG_BUFFER)
            throw new IllegalArgumentException("radius cannot be smaller than " + BG_BUFFER + ".");
        if(radius < innerRadius)
            setInnerRadius(radius - 1);
        if(radius != lastRadius) {
            this.radius = radius;
            lastRadius = radius;
            setSize(getPrefWidth(), getPrefHeight());
            invalidateHierarchy();
        }
    }

    /**
     * @see #innerRadius
     * @return How far from the center do the regions start being drawn.
     */
    public float getInnerRadius() {
        return innerRadius;
    }

    /**
     * <i>Optional.</i><br>
     * If provided, the {@link RadialGroupStyle#childRegionColor} will only fill
     * the region defined between the {@link #radius} and this value. A hole
     * will be left into the middle of the Widget, like a doughnut, and if a
     * {@link RadialGroupStyle#background} or a
     * {@link RadialGroupStyle#backgroundColor} was provided, it will be visible
     * in the middle.<br>
     * Actors inserted into the Widget are placed in the middle between the
     * innerRadius and the {@link #radius}.
     *
     * @param innerRadius How far from the center do the regions start being
     *                    drawn.<br>
     *                    The value must be between 0 (inclusive)
     *                    and {@link #radius} (exclusive).
     */
    public void setInnerRadius(float innerRadius) {
        if(innerRadius < 0)
            throw new IllegalArgumentException("innerRadius cannot be negative.");
        if(innerRadius >= radius)
            throw new IllegalArgumentException("innerRadius must be smaller than the radius.");
        this.innerRadius = innerRadius;
    }

    /**
     * @see #startDegreesOffset
     * @return How far from the origin, counter-clockwise, the widget starts
     *         drawing itself.
     */
    public float getStartDegreesOffset() {
        return startDegreesOffset;
    }

    /**
     * <i>Optional.</i><br>
     * Considers that angles start at 0 along the x-axis and increment up
     * to 360 in a counter-clockwise fashion.<br>
     * Defines how far from that origin the {@link #totalDegreesDrawn} will
     * be drawn.<br>
     * For example, if {@code startDegreesOffset = 90} and
     * {@code totalDegreesDrawn = 180}, you would obtain the left half of a
     * circle. All the children would be spread within that half-circle evenly.
     *
     * @param startDegreesOffset Must be between 0 (inclusive) and 360 (exclusive).
     */
    public void setStartDegreesOffset(float startDegreesOffset) {
        if(startDegreesOffset < 0)
            throw new IllegalArgumentException("startDegreesOffset cannot be negative.");
        if(startDegreesOffset >= 360)
            throw new IllegalArgumentException("startDegreesOffset must be lower than 360.");
        this.startDegreesOffset = startDegreesOffset;
    }

    /**
     * @see #totalDegreesDrawn
     * @return How many degrees should the widget span.
     */
    public float getTotalDegreesDrawn() {
        return totalDegreesDrawn;
    }

    /**
     * <i>Required.</i><br>
     * If not defined, will be initialized to 360 by default.<br>
     * Determines the total amount of degrees into which the contained
     * Actors will be spread.<br>
     * For example, if {@code startDegreesOffset = 0} and
     * {@code totalDegreesDrawn = 180}, you would obtain the top half of a
     * circle.
     *
     * @param totalDegreesDrawn Must be between 0 and 360 (inclusively).
     */
    public void setTotalDegreesDrawn(float totalDegreesDrawn) {
        if(totalDegreesDrawn < 0)
            throw new IllegalArgumentException("totalDegreesDrawn cannot be negative.");
        if(totalDegreesDrawn > 360)
            throw new IllegalArgumentException("totalDegreesDrawn must be lower or equal to 360.");
        this.totalDegreesDrawn = totalDegreesDrawn;
    }
}
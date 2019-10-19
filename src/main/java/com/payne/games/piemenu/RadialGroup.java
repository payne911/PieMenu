package com.payne.games.piemenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
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
     * The radius that defines how big the Widget will be (in terms of scene2d,
     * this is actually half the "minimal size" this widget will ever take).<br>
     * It must be bigger than {@value #BUFFER}.
     */
    protected float minRadius;

    /**
     * <i>Optional.</i><br>
     * If provided, the {@link RadialGroupStyle#sliceColor} will only fill
     * the region defined between the {@link #minRadius} and its percentage
     * value coming from this.<br>
     * For example, having a {@link #minRadius} of 80 and a
     * {@link #innerRadiusPercent} of 0.5 will mean that the inner-radius will
     * stand at 40 pixels from the center.<br>
     * A hole will be left into the middle of the Widget, like a doughnut, and
     * if a {@link RadialGroupStyle#background} or a
     * {@link RadialGroupStyle#backgroundColor} was provided, it will be visible
     * in the middle.<br>
     * Actors inserted into the Widget are placed in the middle between the
     * inner-radius and the {@link #minRadius}.
     */
    protected float innerRadiusPercent;

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
    protected static final float BUFFER = 1;
    protected static final Color TRANSPARENT = new Color(0,0,0,0);
    private static Vector2 vector2 = new Vector2();
    private static Vector2 vector22 = new Vector2();
    private static Vector2 vector23 = new Vector2();






    protected void constructorsCommon() {
        setTouchable(Touchable.childrenOnly);
    }

    /**
     * Used internally for the shared properties among constructors of RadialWidgets.
     */
    protected RadialGroup(final Batch batch, final TextureRegion whitePixel, float minRadius) {
        this.sd = new ShapeDrawer(batch, whitePixel) {
            /* OPTIONAL: Ensuring a certain smoothness. */
            @Override
            protected int estimateSidesRequired(float radiusX, float radiusY) {
                return 200;
            }
        };
        setMinRadius(minRadius);
        constructorsCommon();
    }

    /**
     * Used internally for the shared properties among constructors of RadialWidgets.
     */
    protected RadialGroup(final Batch batch, final TextureRegion whitePixel,
                          float minRadius, float innerRadiusPercent) {
        this(batch, whitePixel, minRadius);
        setInnerRadiusPercent(innerRadiusPercent);
    }

    /**
     * Used internally for the shared properties among constructors of RadialWidgets.
     */
    protected RadialGroup(final Batch batch, final TextureRegion whitePixel,
                          float minRadius, float innerRadiusPercent, float startDegreesOffset) {
        this(batch, whitePixel, minRadius, innerRadiusPercent);
        setStartDegreesOffset(startDegreesOffset);
    }

    /**
     * Used internally for the shared properties among constructors of RadialWidgets.
     */
    protected RadialGroup(final Batch batch, final TextureRegion whitePixel,
                          float minRadius, float innerRadiusPercent,
                          float startDegreesOffset, float totalDegreesDrawn) {
        this(batch, whitePixel, minRadius, innerRadiusPercent, startDegreesOffset);
        setTotalDegreesDrawn(totalDegreesDrawn);
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param style defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       RadialGroupStyle style, float minRadius) {
        this(batch, whitePixel, minRadius);
        setStyle(style);
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param style defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       RadialGroupStyle style, float minRadius,
                       float innerRadiusPercent) {
        this(batch, whitePixel, minRadius, innerRadiusPercent);
        setStyle(style);
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param style defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       RadialGroupStyle style, float minRadius,
                       float innerRadiusPercent, float startDegreesOffset) {
        this(batch, whitePixel, minRadius, innerRadiusPercent, startDegreesOffset);
        setStyle(style);
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param style defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       RadialGroupStyle style, float minRadius,
                       float innerRadiusPercent, float startDegreesOffset, float totalDegreesDrawn) {
        this(batch, whitePixel, minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
        setStyle(style);
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       Skin skin, float minRadius) {
        this(batch, whitePixel, minRadius);
        setStyle(skin.get(RadialGroupStyle.class));
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       Skin skin, float minRadius,
                       float innerRadiusPercent) {
        this(batch, whitePixel, minRadius, innerRadiusPercent);
        setStyle(skin.get(RadialGroupStyle.class));
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       Skin skin, float minRadius,
                       float innerRadiusPercent, float startDegreesOffset) {
        this(batch, whitePixel, minRadius, innerRadiusPercent, startDegreesOffset);
        setStyle(skin.get(RadialGroupStyle.class));
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       Skin skin, float minRadius,
                       float innerRadiusPercent, float startDegreesOffset, float totalDegreesDrawn) {
        this(batch, whitePixel, minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
        setStyle(skin.get(RadialGroupStyle.class));
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       Skin skin, String style, float minRadius) {
        this(batch, whitePixel, minRadius);
        setStyle(skin.get(style, RadialGroupStyle.class));
    }


    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       Skin skin, String style, float minRadius,
                       float innerRadiusPercent) {
        this(batch, whitePixel, minRadius, innerRadiusPercent);
        setStyle(skin.get(style, RadialGroupStyle.class));
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       Skin skin, String style, float minRadius,
                       float innerRadiusPercent, float startDegreesOffset) {
        this(batch, whitePixel, minRadius, innerRadiusPercent, startDegreesOffset);
        setStyle(skin.get(style, RadialGroupStyle.class));
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param batch used to draw everything but the contained actors.
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public RadialGroup(final Batch batch, final TextureRegion whitePixel,
                       Skin skin, String style, float minRadius,
                       float innerRadiusPercent, float startDegreesOffset, float totalDegreesDrawn) {
        this(batch, whitePixel, minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
        setStyle(skin.get(style, RadialGroupStyle.class));
    }












    /**
     * The current diameter of the widget.<br>
     * This might not be twice the {@link #minRadius}.
     *
     * @return {@code Math.min(getWidth(), getHeight())}
     */
    protected float getMaxDiameter() {
        return Math.min(getWidth(), getHeight());
    }

    /**
     * The current radius of the widget.<br>
     * This might not be {@link #minRadius}.
     *
     * @return {@code Math.min(getWidth(), getHeight()) / 2}
     */
    protected float getMaxRadius() {
        return getMaxDiameter()/2;
    }

    @Override
    public float getPrefWidth() {
        return getMaxDiameter();
    }

    @Override
    public float getPrefHeight() {
        return getMaxDiameter();
    }

    @Override
    public float getMinWidth() {
        return minRadius * 2;
    }

    @Override
    public float getMinHeight() {
        return minRadius * 2;
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
     * By default, the value is {@code (getMaxRadius() + getInnerRadiusLength())/2}.<br>
     * Override this method when creating your Widget if you want to have control
     * on where the Actors get placed.<br>
     * <b>Do not</b> position the Actor directly in this method: that is handled
     * internally. Just return the desired distance from the center.<br><br>
     * Here is an example:
     * <pre>
     * {@code
     * RadialGroup myWidget = new RadialGroup(shapeDrawer, myStyle, 77) {
     *     public float getActorDistanceFromCenter(Actor actor) {
     *         // We want the Actors to be placed closer to the edge than the default value
     *         return getAmountOfChildren() > 1
     *                 ? getRadius() - getChild(0).getWidth()
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
        return (getMaxRadius() + getInnerRadiusLength())/2;
    }

    /**
     * Used to apply changes to an Actor according to certain rules. By
     * default, there are no changes applied.<br>
     * Override this method when creating your Widget if you want to have control
     * on how to resize, rotate, etc., the Actors that get placed within your
     * Widget.<br>
     * Trying to change the position of the Actor will not work.<br><br>
     * Here is an example:
     * <pre>
     * {@code
     * RadialGroup myWidget = new RadialGroup(shapeDrawer, myStyle, 77) {
     *     public void modifyActor(Actor actor, float degreesPerChild, float actorDistanceFromCenter) {
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
    public void modifyActor(Actor actor, float degreesPerChild, float actorDistanceFromCenter) {

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
     * float tmp2 = getMaxRadius() - actorDistanceFromCenter;
     * float tmp3 = actorDistanceFromCenter - getInnerRadiusLength();
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
        float tmp2 = getMaxRadius() - actorDistanceFromCenter;
        float tmp3 = actorDistanceFromCenter - getInnerRadiusLength();
        return Math.min(Math.min(tmp1, tmp2), tmp3);
    }

    @Override
    public void layout() {
        updateOrigin(); // for rotations to happen around the actual center

        float degreesPerChild = totalDegreesDrawn / getAmountOfChildren();
        float half = 1f / 2;

        for (int i = 0; i < getAmountOfChildren(); i++) {
            Actor actor = getChildren().get(i);
            float dist = getActorDistanceFromCenter(actor);
            vector2.set(dist, 0);
            vector2.rotate(degreesPerChild*(i + half) + startDegreesOffset);
            modifyActor(actor, degreesPerChild, dist); // overridden by user
            actor.setPosition(vector2.x + getWidth()/2, vector2.y + getHeight()/2, Align.center);
        }
    }

    @Override
    protected void positionChanged() {
        updateOrigin();
    }

    /**
     * Used to properly set the origin's value to the center so that rotations
     * happen around the good point.
     */
    protected void updateOrigin() {
        setOrigin(getWidth()/2, getHeight()/2); // to support rotations around the center
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
     * Changes the {@link ShapeDrawer}'s color.
     *
     * @param sd the {@link ShapeDrawer} whose color will be changed.
     * @param input the {@link Color} to be copying RGB values from.
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
     * @param parentAlpha the inherited parent alpha.
     * @param degreesToDraw how many degrees from the offset should be drawn.
     */
    protected void drawWithShapeDrawer(Batch batch, float parentAlpha, float degreesToDraw) {

        /* Pre-calculating */
        float bgRadian  = MathUtils.degreesToRadians*degreesToDraw;
        float tmpOffset = MathUtils.degreesToRadians*(startDegreesOffset + getRotation());
        final int SIZE  = getAmountOfChildren();
        float tmpRad    = bgRadian / SIZE;

        /* Background image */
        if(style.background != null) {
            Color bc = batch.getColor();
            float restoreAlpha = bc.a;
            batch.setColor(bc.r, bc.g, bc.b, bc.a * globalAlphaMultiplier);
            style.background.draw(batch,
                    getX(Align.center) - getMaxRadius(), getY(Align.center) - getMaxRadius(),
                    getMaxRadius(), getMaxRadius(),
                    getMaxDiameter(), getMaxDiameter(),
                    getScaleX(), getScaleY(),
                    getRotation());
            batch.setColor(bc.r, bc.g, bc.b, restoreAlpha);
        }

        /* Rest of background */
        if(style.backgroundColor != null) {
            propagateAlpha(sd, style.backgroundColor);
            sd.sector(getX(Align.center), getY(Align.center),
                    getMaxRadius()-BUFFER, tmpOffset, bgRadian); // buffer to prevent bg from sticking out from below children
        }

        /* Children */
        vector2.set(getX(Align.center), getY(Align.center));
        for(int i=0; i<SIZE; i++) {
            float tmp = tmpOffset + i*tmpRad;
            drawChild(vector2, i, tmp, tmpRad);

            /* Separator */
            drawChildSeparator(vector2, tmp);
        }

        /* The remaining last separator to be drawn */
        drawChildSeparator(vector2, tmpOffset + SIZE*tmpRad);
    }

    protected void drawChildSeparator(Vector2 vector2, float drawnRadianAngle) {
        if(hasChildren() && style.separatorColor != null && style.separatorWidth > 0) {
            propagateAlpha(sd, style.separatorColor);
            sd.line(pointAtAngle(vector22, vector2, getInnerRadiusLength(), drawnRadianAngle),
                    pointAtAngle(vector23, vector2, getMaxRadius(), drawnRadianAngle),
                    style.separatorWidth);
        }
    }

    /**
     * Determines the color of the slice in which resides the Actor designated
     * by the {@code index} parameter. By default, the colors come from the way
     * you have set up your Style (anything that extends {@link RadialGroupStyle}).<br>
     * Override this method when creating your Widget if you want to have control
     * over those colors.<br>
     * <b>Do not</b> set the color of the {@link ShapeDrawer} in there: that is
     * handled internally. Just return the desired Color.<br><br>
     * Here is an example:
     * <pre>
     * {@code
     * RadialGroup myWidget = new RadialGroup(shapeDrawer, myStyle, 77) {
     *     public Color getColor(int index) {
     *         Color fader = super.getColor(index);
     *         // This will fade the slices' alpha value
     *         return new Color(fader.r, fader.g, fader.b, fader.a/index);
     *     }
     * };
     * }
     * </pre>
     *
     * @param index index of the child whose slice is to be drawn with the returned Color.
     * @return the Color to be used to draw the slice of the child with the given index.
     */
    public Color getColor(int index) {
        if(style.alternateSliceColor != null
                && index%2 == 1)
            return style.alternateSliceColor;

        if(style.sliceColor != null)
            return style.sliceColor;

        return TRANSPARENT;
    }

    protected void drawChild(Vector2 vector2, int index, float startAngle, float radian) {
        propagateAlpha(sd, getColor(index));
        sd.arc(vector2.x, vector2.y, (getMaxRadius()+ getInnerRadiusLength())/2,
                startAngle, radian, getMaxRadius()- getInnerRadiusLength());

        /* Circumferences */
        drawChildCircumference(vector2, startAngle, radian, getMaxRadius() - style.circumferenceWidth/2);
        if(innerRadiusPercent > 0)
            drawChildCircumference(vector2, startAngle, radian, getInnerRadiusLength() + style.circumferenceWidth/2);
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
        int childIndex = findChildIndexAtStage(vector2.x,vector2.y);
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
    public int findChildIndexAtStage(float x, float y) {
        int childIndex = findIndexFromAngle(angleAtStage(x,y));
        stageToLocalCoordinates(vector2.set(x,y));
        return isWithinRadii(vector2.x - getMaxRadius(), vector2.y - getMaxRadius())
                ? childIndex
                : getAmountOfChildren(); // "getAmountOfChildren" is equivalent to "invalid"
    }

    /**
     * Given an angle, find the index of the child (if any).
     *
     * @see #isValidIndex(int)
     * @see #angleAtStage(float, float)
     * @param angle a normalized angle of the coordinate relative to the center
     *              of the widget.
     * @return The index of the child at that coordinate. Not guaranteed to be valid.
     */
    public int findIndexFromAngle(float angle) {
        return MathUtils.floor(angle / totalDegreesDrawn * getAmountOfChildren());
    }

    /**
     * Finding the angle, in degrees, compared to the origin (i.e. center) of
     * the widget. The rotation and {@link #startDegreesOffset} of the widget
     * are automatically taken in count. The output is normalized.
     *
     * @param x x-coordinate in the Stage.
     * @param y y-coordinate in the Stage.
     * @return a normalized angle of the position of the cursor
     *         relative to the origin (i.e. middle) of the widget
     */
    public float angleAtStage(float x, float y) {
        return normalizeAngle(
                MathUtils.radiansToDegrees
                        * MathUtils.atan2(y - (getY(Align.center)), x - (getX(Align.center)))
                - getRotation() - startDegreesOffset
        );
    }

    /**
     * Used to ensure an angle is in between 0 and 360.
     *
     * @param angle to angle to normalize.
     * @return a normalized angle.
     */
    public float normalizeAngle(float angle) {
        return ((angle % 360 + 360) % 360);
    }

    /**
     * Checks whether or not the input coordinate is in between (inclusively)
     * the inner-radius and the current radius of the widget (which can
     * be bigger than {@link #minRadius} if you use {@link #setFillParent(boolean)},
     * for example).
     *
     * @param x x-coordinate relative to the center of the widget's
     * @param y y-coordinate relative to the center of the widget's
     * @return 'true' only if the coordinates fall within the widget's radii.
     */
    public boolean isWithinRadii(float x, float y) {
        float distance = pow2(x) + pow2(y);
        float innerRadSquared = pow2(getInnerRadiusLength());
        float radSquared = pow2(getMaxRadius());
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
     * Positions the Widget's center right in the middle of the current screen size.
     */
    public void centerOnScreen() {
        setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, Align.center);
    }

    /**
     * Centers the Widget on the center point of the provided Actor.<br>
     * Will not follow this Actor: it just sets the position of the center of
     * the Widget to the center position of that Actor at that specific time.
     *
     * @param actor the Actor to center on. If {@code null}, nothing happens.
     */
    public void centerOnActor(Actor actor) {
        if(actor == null)
            return;
        setPosition(actor.getX(Align.center), actor.getY(Align.center), Align.center);
    }

    /**
     * Given a child index, find whether or not it would be a valid candidate to
     * highlight or select.
     *
     * @param index an integer that would usually be the output of
     *              {@link #findChildIndexAtStage(float, float)}.
     * @return {@code true} only if the index is linked to a valid child sector.
     */
    public boolean isValidIndex(int index) {
        return !(index <= -1 || index >= getAmountOfChildren());
    }


    public void drawRudimentaryDebug() {
        debug();
        for (Actor a : getChildren()) {
            a.debug();
        }
    }




    /*
    =================================== STYLE ==================================
     */


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

        if(style.sliceColor == null && style.alternateSliceColor != null)
            throw new IllegalArgumentException("sliceColor must also be specified if you are defining alternateSliceColor. " +
                    "You can however only specify the sliceColor, if you want.");
    }

    /**
     * Encompasses the characteristics that define the style of the Widget
     * to be drawn.
     */
    public static class RadialGroupStyle {

        /**
         * <i>Recommended. Optional.</i><br>
         * A background that will be drawn behind everything else within the Widget.<br>
         * Be mindful of the fact that this is unaffected by any of the other
         * variables: it will be resized to fit in the whole region that
         * represents the position, width and height of the widget.
         */
        public TransformDrawable background;

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
         * It is recommended mostly for the case where you are not defining an
         * {@link #alternateSliceColor}.<br>
         * If you do not define a {@link #separatorWidth} along with this value,
         * no lines will be visible.
         */
        public Color separatorColor;

        /**
         * <i>Recommended. Optional.</i><br>
         * The color used to fill the "pie sectors" (i.e. slice) of each item.<br>
         * Consider using a fairly low alpha value if you are providing a
         * {@link #background} {@link Drawable}.
         */
        public Color sliceColor;

        /**
         * <i>Optional.</i><br>
         * If this color is set, the "pie sectors" will alternate between the
         * {@link #sliceColor} and this one so that their defining region
         * is more easily distinguished.
         */
        public Color alternateSliceColor;

        /**
         * <i>Optional.</i><br>
         * The color used for the line that defines the circumference of the
         * Widget. If the Widget is not a complete a circle, this will only be
         * applied along the partial circumference.<br>
         * If you have set a non-zero {@link #innerRadiusPercent} value, this will
         * also apply to the "inner radius" of your Widget.<br>
         * If you do not define a {@link #circumferenceWidth} along with this
         * value, no circumference will be visible.
         */
        public Color circumferenceColor;

        /**
         * <i>Recommended. Optional.</i><br>
         * Determines how wide the lines that separate each slice will be.<br>
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
         * Encompasses the characteristics that define the style of the Widget
         * to be drawn.
         */
        public RadialGroupStyle() {
        }

        /**
         * Encompasses the characteristics that define the style of the Widget
         * to be drawn.
         *
         * @param style a Style to copy the parameters from.
         */
        public RadialGroupStyle(RadialGroupStyle style) {
            this.background = style.background;
            this.circumferenceWidth = style.circumferenceWidth;
            this.circumferenceColor = new Color(style.circumferenceColor);
            this.separatorWidth = style.separatorWidth;
            this.separatorColor = new Color(style.separatorColor);
            this.sliceColor = new Color(style.sliceColor);
            this.alternateSliceColor = new Color(style.alternateSliceColor);
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
     * You probably shouldn't be messing with this, but it's provided for
     * convenience reasons.<br>
     * For example, it's possible that your widget will be <i>so</i> big that
     * the {@link ShapeDrawer} will crash with an Exception while trying to
     * draw it. You would then want to use this setter to provide a brand new
     * instance which didn't override the {@code estimateSidesRequired(float, float)}
     * method to try to increase the smoothness of the curves.
     *
     * @param sd the new instance of {@link ShapeDrawer} that you want to use.
     */
    public void setShapeDrawer(ShapeDrawer sd) {
        this.sd = sd;
    }

    /**
     * @return the multiplier's value which is applied to all the alpha values
     *         of the things contained by the widget (slices, lines, drawables, etc.)
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
     *                              (slices, lines, drawables, etc.).
     */
    public void setGlobalAlphaMultiplier(float globalAlphaMultiplier) {
        this.globalAlphaMultiplier = globalAlphaMultiplier;
        setColor(getColor().r, getColor().g, getColor().b, getColor().a * globalAlphaMultiplier);
    }

    /**
     * @see #minRadius
     * @return The radius that defines how big the Widget will be.
     */
    public float getMinRadius() {
        return minRadius;
    }

    /**
     * <i>Required.</i><br>
     * The radius that defines how big the Widget will be.<br>
     * This is used as the minimal radius value if anything such as a
     * {@link com.badlogic.gdx.scenes.scene2d.ui.Table} ends up modifying the
     * size of the widget. {@link #setFillParent(boolean)} will also end up
     * using that radius value as a minimum.
     *
     * @param minRadius The value must be bigger than {@value #BUFFER}.
     *                  If the value is smaller than the current
     *                  {@link #innerRadiusPercent} then the
     *                  {@link #innerRadiusPercent} is set to a smaller value.
     */
    public void setMinRadius(float minRadius) {
        if(minRadius < BUFFER)
            throw new IllegalArgumentException("radius cannot be smaller than " + BUFFER + ".");
        if(minRadius != lastRadius) {
            this.minRadius = minRadius;
            lastRadius = minRadius;

            setSize(getMinWidth(), getMinHeight()); // for orphan widgets (no parent)

            invalidateHierarchy();
        }
    }

    /**
     * The percentage of the radius that is taken out of the slice, starting
     * from the center of the widget.
     *
     * @see #innerRadiusPercent
     * @return How far from the center do the slices start being drawn.
     */
    public float getInnerRadiusPercent() {
        return innerRadiusPercent;
    }

    /**
     * The amount of pixels that are truncated from the radius.
     *
     * @return {@code minRadius * innerRadiusPercent}.
     */
    public float getInnerRadiusLength() {
        return getMaxRadius() * innerRadiusPercent;
    }

    /**
     * <i>Optional.</i><br>
     * If provided, the {@link RadialGroupStyle#sliceColor} will only fill
     * the slice defined between the {@link #minRadius} and its percentage
     * value coming from this. A hole will be left into the middle of the Widget,
     * like a doughnut, and if a {@link RadialGroupStyle#background} or a
     * {@link RadialGroupStyle#backgroundColor} was provided, it will be visible
     * in the middle.<br>
     * Actors inserted into the Widget are placed in the middle between the
     * innerRadius and the {@link #minRadius}. That is only the default behavior,
     * if you want to change that, see {@link #getActorDistanceFromCenter(Actor)}.
     *
     * @param innerRadiusPercent How far from the center do the slices start
     *                           being drawn, in terms of percentage of the
     *                           {@link #minRadius}.<br>
     *                           The value must be between 0 (inclusive)
     *                           and 1 (exclusive).
     */
    public void setInnerRadiusPercent(float innerRadiusPercent) {
        if(innerRadiusPercent < 0)
            throw new IllegalArgumentException("innerRadius cannot be negative.");
        if(innerRadiusPercent >= 1)
            throw new IllegalArgumentException("innerRadius must be smaller than 1.");
        if(this.innerRadiusPercent != innerRadiusPercent) {
            this.innerRadiusPercent = innerRadiusPercent;
            invalidate();
        }
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
        if(this.startDegreesOffset != startDegreesOffset) {
            this.startDegreesOffset = startDegreesOffset;
            invalidate();
        }
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
        if(this.totalDegreesDrawn != totalDegreesDrawn) {
            this.totalDegreesDrawn = totalDegreesDrawn;
            invalidate();
        }
    }
}
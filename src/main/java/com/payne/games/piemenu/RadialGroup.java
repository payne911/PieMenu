package com.payne.games.piemenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.payne.games.piemenu.PieWidget.PieWidgetStyle;
import space.earlygrey.shapedrawer.ShapeDrawer;


/**
 * A RadialGroup aims at providing the user with a simple way to lay out
 * the contained Actors in a circular fashion. It does not use a
 * {@link ShapeDrawer} in any way. Thus, no Style whatsoever is required.
 *
 * @author Jérémi Grenier-Berthiaume (aka "payne")
 */
public class RadialGroup extends WidgetGroup {

    /**
     * <i>Required.</i><br/>
     * The radius that defines how big the Widget will be (in terms of scene2d,
     * this is actually half the "minimal size" this widget will ever take).<br/>
     * It must be bigger than {@value #BUFFER}.
     */
    protected float preferredRadius;

    /**
     * <i>Optional.</i><br/>
     * If provided, the {@link PieWidgetStyle#sliceColor} will only fill
     * the region defined between the {@link #preferredRadius} and its percentage
     * value coming from this.<br/>
     * For example, having a {@link #preferredRadius} of 80 and a
     * {@link #innerRadiusPercent} of 0.5 will mean that the inner-radius will
     * stand at 40 pixels from the center.<br/>
     * A hole will be left into the middle of the Widget, like a doughnut, and
     * if a {@link PieWidgetStyle#background} or a
     * {@link PieWidgetStyle#backgroundColor} was provided, it will be visible
     * in the middle.<br/>
     * Actors inserted into the Widget are by default placed in the middle
     * between the inner-radius and the radius.
     *
     * @see #modifyActor(Actor, float, float)
     */
    protected float innerRadiusPercent;

    /**
     * <i>Optional.</i><br/>
     * Considers that angles start at 0 along the x-axis and increment up
     * to 360 in a counter-clockwise fashion.<br/>
     * Defines how far from that origin the {@link #totalDegreesDrawn} will
     * be drawn.<br/>
     * For example, if {@code startDegreesOffset = 90} and
     * {@code totalDegreesDrawn = 180}, you would obtain the left half of a
     * circle. All the children would be spread within that half-circle evenly.
     */
    protected float startDegreesOffset;

    /**
     * <i>Required.</i><br/>
     * If not defined, will be initialized to 360 by default.<br/>
     * Determines the total amount of degrees into which the contained
     * Actors will be spread.<br/>
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
    private static Vector2 vector2 = new Vector2();



    /** Used internally for the shared properties among constructors of RadialWidgets. */
    protected void constructorsCommon() {
        setTouchable(Touchable.childrenOnly);
    }


    /**
     * See {@link RadialGroup} for a description.
     *
     * @param preferredRadius the {@link #preferredRadius} that defines the 
     *                        size of the widget.
     */
    public RadialGroup(float preferredRadius) {
        setPreferredRadius(preferredRadius);
        constructorsCommon();
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param preferredRadius the {@link #preferredRadius} that defines the 
     *                        size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public RadialGroup(float preferredRadius, float innerRadiusPercent) {
        this(preferredRadius);
        setInnerRadiusPercent(innerRadiusPercent);
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param preferredRadius the {@link #preferredRadius} that defines the 
     *                        size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public RadialGroup(float preferredRadius, float innerRadiusPercent,
                       float startDegreesOffset) {
        this(preferredRadius, innerRadiusPercent);
        setStartDegreesOffset(startDegreesOffset);
    }

    /**
     * See {@link RadialGroup} for a description.
     *
     * @param preferredRadius the {@link #preferredRadius} that defines the 
     *                        size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public RadialGroup(float preferredRadius, float innerRadiusPercent,
                       float startDegreesOffset, float totalDegreesDrawn) {
        this(preferredRadius, innerRadiusPercent, startDegreesOffset);
        setTotalDegreesDrawn(totalDegreesDrawn);
    }










    /**
     * The current diameter of the widget.<br/>
     * This might not be twice the {@link #preferredRadius}.
     *
     * @return {@code Math.min(getWidth(), getHeight())}
     */
    protected float getCurrentDiameter() {
        return Math.min(getWidth(), getHeight());
    }

    /**
     * The current radius of the widget.<br/>
     * This might not be {@link #preferredRadius}.
     *
     * @return {@code Math.min(getWidth(), getHeight()) / 2}
     */
    protected float getCurrentRadius() {
        return getCurrentDiameter()/2;
    }

    @Override
    public float getPrefWidth() {
        return getCurrentDiameter();
    }

    @Override
    public float getPrefHeight() {
        return getCurrentDiameter();
    }

    @Override
    public float getMinWidth() {
        return preferredRadius * 2;
    }

    @Override
    public float getMinHeight() {
        return preferredRadius * 2;
    }

    @Override
    public void addActor(Actor actor) {
        // todo: this might not need to be Overriden
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
     * By default, the value is {@code (getMaxRadius() + getInnerRadiusLength())/2}.<br/>
     * Override this method when creating your Widget if you want to have control
     * on where the Actors get placed.<br/>
     * <b>Do not</b> position the Actor directly in this method: that is handled
     * internally. Just return the desired distance from the center.<br/><br/>
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
        return (getCurrentRadius() + getInnerRadiusLength())/2;
    }

    /**
     * Used to apply changes to an Actor according to certain rules. By
     * default, there are no changes applied.<br/>
     * Override this method when creating your Widget if you want to have control
     * on how to resize, rotate, etc., the Actors that get placed within your
     * Widget.<br/>
     * Trying to change the position of the Actor will not work.<br/><br/>
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
        // todo: possibly add a default resize-value for `Image` instances here?
    }

    /**
     * Used to estimate the radius of a circle to be constrained within the widget
     * according to the input parameters. Doubling the returned value would give
     * you the size of a contained Actor which would roughly fill most of its
     * sector, or possibly overflow slightly. It is suggested to adjust slightly
     * the returned value by multiplying it with a factor of your choice.<br/>
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
        float tmp2 = getCurrentRadius() - actorDistanceFromCenter;
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

        /* Checking all children first to catch the ones extending beyond the widget. */
        for (Actor child : getChildren()) {
            if(child.getTouchable() == Touchable.disabled)
                continue;
            child.parentToLocalCoordinates(vector2.set(x,y));
            Actor hit = child.hit(vector2.x, vector2.y, touchable);
            if(hit != null) {
//                System.out.println("hit");
                return hit;
            }
        }

        /* Then we want to consider the widget's boundaries itself. */
        localToStageCoordinates(vector2.set(x,y));
        int childIndex = findChildIndexAtStage(vector2.x,vector2.y);
        if(isValidIndex(childIndex)) {
//            System.out.println("this valid");
            return this;
        }

//        // todo: shouldn't return `null` if hitting background
//        if(isBackgroundHit(x,y)) {
//            System.out.println("this background");
//            return this;
//        }

//        System.out.println("null");
        return null;
    }

//    public boolean isBackgroundHit(float x, float y) {
//        stageToLocalCoordinates(vector2.set(x,y));
//        return isWithinInnerRadius(vector2.x - getWidth()/2, vector2.y - getHeight()/2);
//    }

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
        return isWithinRadii(vector2.x - getWidth()/2, vector2.y - getHeight()/2)
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
        localToStageCoordinates(vector2.set(getWidth()/2, getHeight()/2));
        return normalizeAngle(
                MathUtils.radiansToDegrees
                        * MathUtils.atan2(y - vector2.y, x - vector2.x)
                        - getTotalRotation() - startDegreesOffset
        );
    }

    /**
     * To obtain the rotation value, including the rotation induced
     * from the parent Groups.<br/>
     * That is because calling {@link #getRotation()} in scene2d
     * only returns the rotation value applied directly to the Actor.
     * If a {@link Group} including this Actor is rotated, then even
     * though the Actor appears rotated, the internal value will be 0.
     *
     * @return The total rotation value.
     */
    protected float getTotalRotation() {
        float rotation = super.getRotation();
        Group parent = getParent();
        if(parent == null)
            return rotation;
        return rotation + parent.getRotation();
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

    /**
     * Checks whether or not the input coordinate is in between (inclusively)
     * the inner-radius and the current radius of the widget (which can
     * be bigger than {@link #preferredRadius} if you use {@link #setFillParent(boolean)},
     * for example).
     *
     * @param x x-coordinate relative to the center of the widget.
     * @param y y-coordinate relative to the center of the widget.
     * @return 'true' only if the coordinates fall within the widget's radii.
     */
    public boolean isWithinRadii(float x, float y) {
        float distance = pow2(x) + pow2(y);
        float innerRadSquared = pow2(getInnerRadiusLength());
        float radSquared = pow2(getCurrentRadius());
        return distance >= innerRadSquared && distance <= radSquared;
    }

    /**
     * Checks whether or not the input coordinate is within (exclusively)
     * the inner-radius.
     *
     * @param x x-coordinate relative to the center of the widget.
     * @param y y-coordinate relative to the center of the widget.
     * @return 'true' only if the coordinates fall within the widget's
     *         inner radius.
     */
    public boolean isWithinInnerRadius(float x, float y) {
        float distance = pow2(x) + pow2(y);
        float innerRadSquared = pow2(getInnerRadiusLength());
        return distance < innerRadSquared;
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
     * Centers the Widget on the center point of the provided Actor.<br/>
     * Will not follow this Actor: it just sets the position of the center of
     * the Widget to the center position of that Actor at that specific time.
     *
     * @param actor the Actor to center on. If {@code null}, nothing happens.
     */
    public void centerOnActor(Actor actor) {
        if(actor == null)
            return;
        actor.localToStageCoordinates(vector2.set(actor.getWidth()/2, actor.getHeight()/2));
        setPosition(vector2.x, vector2.y, Align.center);
    }


    public void drawRudimentaryDebug() {
        debug();
        for (Actor a : getChildren()) {
            a.debug();
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
     * @return the multiplier's value which is applied to all the alpha values
     *         of the things contained by the widget (slices, lines, drawables, etc.)
     */
    public float getGlobalAlphaMultiplier() {
        return globalAlphaMultiplier;
    }

    /**
     * This will globally change the alpha value of the widget.<br/>
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
     * @see #preferredRadius
     * @return The radius that defines how big the Widget will be.
     */
    public float getPreferredRadius() {
        return preferredRadius;
    }

    /**
     * <i>Required.</i><br/>
     * The radius that defines how big the Widget will be.<br/>
     * This is generally used as the minimal radius value if anything such as a
     * {@link com.badlogic.gdx.scenes.scene2d.ui.Table} ends up modifying the
     * size of the widget. {@link #setFillParent(boolean)} will also end up
     * using that radius value as a minimum.<br/>
     * However, be aware that this value is not respected by the use of
     * {@link #setWidth(float)} or {@link #setHeight(float)}. In other words, it
     * is possible to set the size of your widget to a smaller value.
     *
     * @param preferredRadius The value must be bigger than {@value #BUFFER}.
     *                  If the value is smaller than the current
     *                  {@link #innerRadiusPercent} then the
     *                  {@link #innerRadiusPercent} is set to a smaller value.
     */
    public void setPreferredRadius(float preferredRadius) {
        if(preferredRadius < BUFFER)
            throw new IllegalArgumentException("radius cannot be smaller than " + BUFFER + ".");
        if(preferredRadius != lastRadius) {
            this.preferredRadius = preferredRadius;
            lastRadius = preferredRadius;

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
        return getCurrentRadius() * innerRadiusPercent;
    }

    /**
     * <i>Optional.</i><br/>
     * If provided, the {@link PieWidgetStyle#sliceColor} will only fill
     * the slice defined between the {@link #preferredRadius} and its percentage
     * value coming from this. A hole will be left into the middle of the Widget,
     * like a doughnut, and if a {@link PieWidgetStyle#background} or a
     * {@link PieWidgetStyle#backgroundColor} was provided, it will be visible
     * in the middle.<br/>
     * Actors inserted into the Widget are placed in the middle between the
     * innerRadius and the {@link #preferredRadius}. That is only the default behavior,
     * if you want to change that, see {@link #getActorDistanceFromCenter(Actor)}.
     *
     * @param innerRadiusPercent How far from the center do the slices start
     *                           being drawn, in terms of percentage of the
     *                           {@link #preferredRadius}.<br/>
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
     * <i>Optional.</i><br/>
     * Considers that angles start at 0 along the x-axis and increment up
     * to 360 in a counter-clockwise fashion.<br/>
     * Defines how far from that origin the {@link #totalDegreesDrawn} will
     * be drawn.<br/>
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
     * <i>Required.</i><br/>
     * If not defined, will be initialized to 360 by default.<br/>
     * Determines the total amount of degrees into which the contained
     * Actors will be spread.<br/>
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
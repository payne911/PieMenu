package com.payne.games.piemenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.HashMap;
import java.util.Map;


/**
 * An animated {@link RadialGroup}.<br>
 * A very simple folding/unfolding animation can be displayed whenever desired.<br>
 * Internally uses a {@link #currentAngle} attribute that is used for transitions
 * between states. Because of that, using {@link #setVisible(boolean)} might not
 * always reveal the Widget: you would have to ensure to call a setter before:
 * <pre>
 * {@code
 * myMenu.setCurrentAngle(myMenu.getStyle().totalDegreesDrawn);
 * myMenu.setVisible(true);
 * }
 * </pre>
 * The value of {@link #currentAngle} dictates the currently revealed amount of
 * degrees, out of the total amount of degrees to be drawn. Its value is of {@code 0}
 * when the class is first initialized, and after a closing animation ended:
 * that is why you might end up not seeing the widget despite setting its
 * visibility to {@code true} if you haven't called the recommended line of code
 * provided above.
 *
 * @deprecated Once {@link com.badlogic.gdx.scenes.scene2d.Action Actions} are
 * integrated into the library, this class will become obsolete. However,
 * the implementation is very easy to use, so it might remain in the code base
 * despite Actions being integrated.
 * @author Jérémi Grenier-Berthiaume (aka "payne")
 */
@Deprecated
public class AnimatedRadialGroup extends RadialGroup implements IAnimatedRadialGroup {

    /**
     * Duration of the animation.
     */
    private float duration;


    /* For internal use. */
    private boolean isOpening = false;
    private boolean isClosing = false;
    private float currentAngle = 0; // reused for transitions between entry and exit
    private Map<Actor, Color> originalColors = new HashMap<>();
    private static Vector2 tmp = new Vector2();





    /**
     * See {@link AnimatedRadialGroup} for a description.
     *
     * @param preferredRadius the {@link #preferredRadius} that defines the
     *                        size of the widget.
     */
    public AnimatedRadialGroup(float preferredRadius) {
        super(preferredRadius);
    }

    /**
     * See {@link AnimatedRadialGroup} for a description.
     *
     * @param preferredRadius the {@link #preferredRadius} that defines the
     *                        size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public AnimatedRadialGroup(float preferredRadius,
                             float innerRadiusPercent) {
        super(preferredRadius, innerRadiusPercent);
    }

    /**
     * See {@link AnimatedRadialGroup} for a description.
     *
     * @param preferredRadius the {@link #preferredRadius} that defines the
     *                        size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public AnimatedRadialGroup(float preferredRadius,
                             float innerRadiusPercent, float startDegreesOffset) {
        super(preferredRadius, innerRadiusPercent, startDegreesOffset);
    }

    /**
     * See {@link AnimatedRadialGroup} for a description.
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
    public AnimatedRadialGroup(float preferredRadius,
                             float innerRadiusPercent, float startDegreesOffset, float totalDegreesDrawn) {
        super(preferredRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
    }






    @Override
    public void layout() {
        myLayout();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        myAct(delta);
    }

    @Override
    public RadialGroup getSelf() {
        return this;
    }





    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void setDuration(float duration) {
        if(duration < 0)
            throw new IllegalArgumentException("duration cannot be negative.");
        this.duration = duration;
    }

    @Override
    public boolean isOpening() {
        return isOpening;
    }

    @Override
    public void setOpening(boolean isOpening) {
        this.isOpening = isOpening;
    }

    @Override
    public boolean isClosing() {
        return isClosing;
    }

    @Override
    public void setClosing(boolean isClosing) {
        this.isClosing = isClosing;
    }

    @Override
    public float getCurrentAngle() {
        return currentAngle;
    }

    @Override
    public void setCurrentAngle(float currentAngle) {
        this.currentAngle = currentAngle;
    }

    @Override
    public Map<Actor, Color> getOriginalColors() {
        return originalColors;
    }

    @Override
    public Vector2 getVector2() {
        return tmp;
    }
}

package com.payne.games.piemenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;


/**
 * An animated {@link PieMenu}.<br>
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
 * angles, out of the total amount of degrees to be drawn. Its value is of 0
 * when the class is first initialized, and after a closing animation ended:
 * that is why you might end up not seeing the widget despite setting its
 * visibility to {@code true} if you haven't called the recommended line of code
 * provided above.
 */
@Deprecated
public class AnimatedPieMenu extends PieMenu {

    /**
     * Duration of the animation.
     */
    private float duration;


    /* For internal use. */
    private boolean isOpening = false;
    private boolean isClosing = false;
    private float currentAngle = 0; // reused for transitions between entry and exit
    private static Vector2 vector2 = new Vector2();
    private HashMap<Actor, Color> originalColors = new HashMap<>();





    /**
     * See {@link AnimatedPieMenu} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param style defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     */
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           PieMenuStyle style, float minRadius) {
        super(whitePixel, style, minRadius);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param style defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           PieMenuStyle style, float minRadius,
                               float innerRadiusPercent) {
        super(whitePixel, style, minRadius, innerRadiusPercent);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param style defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           PieMenuStyle style, float minRadius,
                           float innerRadiusPercent, float startDegreesOffset) {
        super(whitePixel, style, minRadius, innerRadiusPercent, startDegreesOffset);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
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
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           PieMenuStyle style, float minRadius,
                           float innerRadiusPercent, float startDegreesOffset, float totalDegreesDrawn) {
        super(whitePixel, style, minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     */
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           Skin skin, float minRadius) {
        super(whitePixel, skin, minRadius);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           Skin skin, float minRadius, float innerRadiusPercent) {
        super(whitePixel, skin, minRadius, innerRadiusPercent);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           Skin skin, float minRadius, float innerRadiusPercent,
                           float startDegreesOffset) {
        super(whitePixel, skin, minRadius, innerRadiusPercent, startDegreesOffset);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
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
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           Skin skin, float minRadius, float innerRadiusPercent,
                           float startDegreesOffset, float totalDegreesDrawn) {
        super(whitePixel, skin, minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     */
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           Skin skin, String style, float minRadius) {
        super(whitePixel, skin, style, minRadius);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           Skin skin, String style, float minRadius,
                               float innerRadiusPercent) {
        super(whitePixel, skin, style, minRadius, innerRadiusPercent);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
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
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           Skin skin, String style, float minRadius,
                           float innerRadiusPercent, float startDegreesOffset) {
        super(whitePixel, skin, style, minRadius, innerRadiusPercent, startDegreesOffset);
    }

    /**
     * See {@link AnimatedPieMenu} for a description.
     *
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
    public AnimatedPieMenu(final TextureRegion whitePixel,
                           Skin skin, String style, float minRadius,
                           float innerRadiusPercent, float startDegreesOffset, float totalDegreesDrawn) {
        super(whitePixel, skin, style, minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
    }







    @Override
    public void layout() {
        updateOrigin(); // for rotations to happen around the actual center

        boolean notAnimated = !isCurrentlyAnimated() && !originalColors.isEmpty();
        float openingPercentage = currentAngle / totalDegreesDrawn;
        float degreesPerChild = currentAngle / getAmountOfChildren();
        float half = 1f / 2;

        for (int i = 0; i < getAmountOfChildren(); i++) {
            Actor actor = getChildren().get(i);
            float dist = getActorDistanceFromCenter(actor);
            vector2.set(dist, 0);
            vector2.rotate(degreesPerChild*(i + half) + startDegreesOffset);
            modifyActor(actor, degreesPerChild, dist); // overridden by user
            actor.setPosition(vector2.x+ getCurrentRadius(), vector2.y+ getCurrentRadius(), Align.center);

            /* Updating alpha (fade-in animation). */
            if(isCurrentlyAnimated()) {
                if (!originalColors.containsKey(actor))
                    originalColors.put(actor, new Color(actor.getColor()));
                Color color = originalColors.get(actor);
                actor.setColor(color.r, color.g, color.b, color.a * openingPercentage);

            /* Restoring the colors' state. */
            } else if(notAnimated) {
                actor.setColor(originalColors.get(actor));
            }
        }
        if (notAnimated) {
            originalColors.clear();
        }
    }

    /**
     * Gets the widget to transition into an opening animation from its
     * currently drawn angle (i.e. {@link #currentAngle}).
     *
     * @param durationSeconds How long the animation will take to finish.
     */
    public void transitionToOpening(float durationSeconds) {
        duration = durationSeconds/1000f;
        isOpening = true;
        isClosing = false;
        setVisible(true);
    }

    /**
     * Gets the widget to start an opening animation from the beginning.
     *
     * @param durationSeconds How long the animation will take to finish.
     */
    public void animateOpening(float durationSeconds) {
        currentAngle = 0;
        transitionToOpening(durationSeconds);
    }

    /**
     * Gets the widget to transition into a closing animation from its
     * currently drawn angle (i.e. {@link #currentAngle}).
     *
     * @param durationSeconds How long the animation will take to finish.
     */
    public void transitionToClosing(float durationSeconds) {
        duration = durationSeconds/1000f;
        isClosing = true;
        isOpening = false;
    }

    /**
     * Gets the widget to start a closing animation from the beginning (i.e.
     * from its fully opened state).
     *
     * @param durationSeconds How long the animation will take to finish.
     */
    public void animateClosing(float durationSeconds) {
        currentAngle = totalDegreesDrawn;
        transitionToClosing(durationSeconds);
    }

    /**
     * Transitions from the current state to the other.<br>
     * If the widget is opening, it will now be closing, for example.<br>
     * Visibility plays a role in determining the current state (for example,
     * if the widget is not visible, it is assumed that it's as if it was closed).
     *
     * @param durationSeconds How long the animation will take to finish.
     */
    public void toggleVisibility(float durationSeconds) {
        if(isOpening || (isVisible() && !isClosing)) {
            transitionToClosing(durationSeconds);
        } else if(isClosing || !isVisible()) {
            transitionToOpening(durationSeconds);
        }
    }

    /**
     * @return {@code true} if the widget is being closed or opened.<br>
     *         {@code false} otherwise.
     */
    public boolean isCurrentlyAnimated() {
        return (isOpening || isClosing);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(isCurrentlyAnimated()) {
            float speed = delta / duration;

            /* Opening. */
            if(isOpening) {
                currentAngle += speed;
                if(currentAngle >= totalDegreesDrawn) { // finishing the animation
                    currentAngle = totalDegreesDrawn;
                    isOpening = false;
                }

                /* Closing. */
            } else {
                currentAngle -= speed;
                if(currentAngle <= 0) { // finishing the animation
                    currentAngle = 0;
                    isClosing = false;
                    setVisible(false);
                }
            }

            /* Updates children. Calls `layout()`. */
            invalidate();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawWithShapeDrawer(batch, parentAlpha, currentAngle);
        drawMe(batch, parentAlpha);
    }








    /**
     * @return the duration of the animation.
     */
    public float getDuration() {
        return duration;
    }

    /**
     * Setting the duration to 0 will make any currently-running animation end
     * at the next {@code render()} call.
     *
     * @param duration a value corresponding to {@code durationInSecond/1000f}.
     */
    public void setDuration(float duration) {
        if(duration < 0)
            throw new IllegalArgumentException("duration cannot be negative.");
        this.duration = duration;
    }

    /**
     * @return {@code true} only when the Widget is currently running an opening
     *         animation.<br>
     *         To be more precise, for example: when an opening animation ends,
     *         this returns {@code false}
     */
    public boolean isOpening() {
        return isOpening;
    }

    /**
     * @return {@code true} only when the Widget is currently running a closing
     *         animation.<br>
     *         To be more precise, for example: when a closing animation ends,
     *         this returns {@code false}
     */
    public boolean isClosing() {
        return isClosing;
    }

    /**
     * After a closing animation, its value is equal to 0.<br>
     * After an opening animation, its value is equal to
     * {@code style.totalDegreesDrawn}.
     *
     * @return an angle in between 0 and {@code style.totalDegreesDrawn}. It
     *         represents the internal state of how far in an animation the widget
     *         is at. If its value is 15, for example, it means that only 15
     *         degrees, out of the total amount of degrees the widget should
     *         take, are being rendered on the screen.
     */
    public float getCurrentAngle() {
        return currentAngle;
    }

    /**
     * Use this if you want to manipulate the internal state of how much of the
     * widget should be drawn.<br>
     * After a closing animation, its value is equal to 0.<br>
     * After an opening animation, its value is equal to
     * {@code style.totalDegreesDrawn}.<br>
     * This means that the accepted values are from 0 to style.totalDegreesDrawn,
     * inclusively.<br>
     * <br>
     * It is recommended to use
     * <pre>
     * {@code
     * myMenu.setCurrentAngle(myMenu.getStyle().totalDegreesDrawn);
     * myMenu.setVisible(true);
     * }
     * </pre>
     * when you want to display the widget to your users, for example.
     *
     * @param currentAngle amount of angles, out of the total, which should
     *                     currently be displayed.
     */
    public void setCurrentAngle(float currentAngle) {
        if(currentAngle >= totalDegreesDrawn || currentAngle <= 0)
            throw new IllegalArgumentException("currentAngle must be between 0 and totalDegreesDrawn.");
        this.currentAngle = currentAngle;
    }
}
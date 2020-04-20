package com.payne.games.piemenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import java.util.Map;


/**
 * Exists to remove the amount of code-duplication and thus increase the
 * maintainability of the different Animated classes.
 */
interface IAnimatedRadialGroup {

    /**
     * Used by the interface as a way of accessing the implementation that uses it.
     * It's a hack to help achieve the goal of {@link IAnimatedRadialGroup this
     * interface}.
     *
     * @return nothing else but {@code this}, in the implementation.
     */
    RadialGroup getSelf();

    /**
     * @return the duration of the animation.
     */
    float getDuration();

    /**
     * Setting the duration to 0 will make any currently-running animation end
     * at the next {@code render()} call.
     *
     * @param duration a value corresponding to {@code durationInSecond/1000f}.
     */
    void setDuration(float duration);

    /**
     * @return {@code true} only when the Widget is currently running an opening
     *         animation.<br>
     *         To be more precise, for example: when an opening animation ends,
     *         this returns {@code false}.
     */
    boolean isOpening();

    /**
     * See {@link #isOpening()}'s JavaDoc.
     */
    void setOpening(boolean isOpening);

    /**
     * @return {@code true} only when the Widget is currently running a closing
     *         animation.<br>
     *         To be more precise, for example: when a closing animation ends,
     *         this returns {@code false}.
     */
    boolean isClosing();

    /**
     * See {@link #isClosing()}'s JavaDoc.
     */
    void setClosing(boolean isClosing);

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
    float getCurrentAngle();

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
     * @param currentAngle Amount of degrees, out of the total, which should
     *                     currently be displayed.<br>
     *                     Should remain between {@code 0} and {@code 360}.
     */
    void setCurrentAngle(float currentAngle);

    /**
     * Used internally for the fading animation.
     */
    Map<Actor, Color> getOriginalColors();

    /**
     * Used internally for optimization purposes.
     */
    Vector2 getVector2();
















    /*
    ================================= MAIN LOGIC =================================
     */


    /**
     * Updates the positions and colors (fading alpha) of the contained Actors.
     */
    default void myLayout() {
        getSelf().updateOrigin(); // for rotations to happen around the actual center

        boolean notAnimated = !isCurrentlyAnimated() && !getOriginalColors().isEmpty();
        float openingPercentage = getCurrentAngle() / getSelf().totalDegreesDrawn;
        float degreesPerChild = getCurrentAngle() / getSelf().getAmountOfChildren();
        float half = 1f / 2;

        for (int i = 0; i < getSelf().getAmountOfChildren(); i++) {
            Actor actor = getSelf().getChildren().get(i);
            float dist = getSelf().getActorDistanceFromCenter(actor);
            getVector2().set(dist, 0);
            getVector2().rotate(degreesPerChild*(i + half) + getSelf().startDegreesOffset);
            getSelf().modifyActor(actor, degreesPerChild, dist); // overridden by user
            actor.setPosition(
                    getVector2().x + getSelf().getWidth()/2,
                    getVector2().y + getSelf().getHeight()/2,
                    Align.center);

            /* Updating alpha (fade-in animation). */
            if(isCurrentlyAnimated()) {
                if (!getOriginalColors().containsKey(actor))
                    getOriginalColors().put(actor, new Color(actor.getColor()));
                Color color = getOriginalColors().get(actor);
                actor.setColor(color.r, color.g, color.b, color.a * openingPercentage);

            /* Restoring the colors' state. */
            } else if(notAnimated) {
                actor.setColor(getOriginalColors().get(actor));
            }
        }
        if (notAnimated) {
            getOriginalColors().clear();
        }
    }

    /**
     * Takes care of animating the widget.
     *
     * @param delta amount of time that has passed since last update.
     */
    default void myAct(float delta) {
        if(isCurrentlyAnimated()) {
            float speed = delta / getDuration();

            /* Opening. */
            if(isOpening()) {
                setCurrentAngle(getCurrentAngle() + speed);
                if(getCurrentAngle() >= getSelf().totalDegreesDrawn) { // finishing the animation
                    setCurrentAngle(getSelf().totalDegreesDrawn);
                    setOpening(false);
                }

            /* Closing. */
            } else {
                setCurrentAngle(getCurrentAngle() - speed);
                if(getCurrentAngle() <= 0) { // finishing the animation
                    setCurrentAngle(0);
                    setClosing(false);
                    getSelf().setVisible(false);
                }
            }

            /* Updates children. Calls `layout()`. */
            getSelf().invalidate();
        }
    }












    /*
    =============================== UTILITY METHODS ============================
     */


    /**
     * Gets the widget to transition into an opening animation from its
     * currently drawn angle (i.e. {@code currentAngle}).
     *
     * @param durationSeconds How long the animation will take to finish.
     */
    default void transitionToOpening(float durationSeconds) {
        setDuration(durationSeconds/1000f);
        setOpening(true);
        setClosing(false);
        getSelf().setVisible(true);
    }

    /**
     * Gets the widget to start an opening animation from the beginning.
     *
     * @param durationSeconds How long the animation will take to finish.
     */
    default void animateOpening(float durationSeconds) {
        setCurrentAngle(0);
        transitionToOpening(durationSeconds);
    }

    /**
     * Gets the widget to transition into a closing animation from its
     * currently drawn angle (i.e. {@code currentAngle}).
     *
     * @param durationSeconds How long the animation will take to finish.
     */
    default void transitionToClosing(float durationSeconds) {
        setDuration(durationSeconds/1000f);
        setClosing(true);
        setOpening(false);
    }

    /**
     * Gets the widget to start a closing animation from the beginning (i.e.
     * from its fully opened state).
     *
     * @param durationSeconds How long the animation will take to finish.
     */
    default void animateClosing(float durationSeconds) {
        setCurrentAngle(getSelf().totalDegreesDrawn);
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
    default void toggleVisibility(float durationSeconds) {
        if(isOpening() || (getSelf().isVisible() && !isClosing())) {
            transitionToClosing(durationSeconds);
        } else if(isClosing() || !getSelf().isVisible()) {
            transitionToOpening(durationSeconds);
        }
    }

    /**
     * @return {@code true} if the widget is being closed or opened.<br>
     *         {@code false} otherwise.
     */
    default boolean isCurrentlyAnimated() {
        return (isOpening() || isClosing());
    }
}

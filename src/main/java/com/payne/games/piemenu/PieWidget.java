package com.payne.games.piemenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.badlogic.gdx.utils.Align;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * A PieWidget aims at providing the user with a simple way to lay out
 * the contained Actors in a circular fashion. It uses a
 * {@link ShapeDrawer} to draw certain elements, when necessary.
 *
 * @author Jérémi Grenier-Berthiaume (aka "payne")
 */
public class PieWidget extends RadialGroup {


    /**
     * Defines the way the widget looks.
     */
    private PieWidgetStyle style;

    /**
     * Used to draw on the screen many elements of the style.
     */
    protected ShapeDrawer sd;

    /**
     * The white pixel used to initialized a {@link ShapeDrawer}.
     */
    protected TextureRegion whitePixel;


    /* For internal use (optimization). */
    protected static final Color TRANSPARENT = new Color(0,0,0,0);
    private static Vector2 vector2 = new Vector2();
    private static Vector2 vector22 = new Vector2();
    private static Vector2 vector23 = new Vector2();






    /** Used internally for the shared properties among constructors of RadialWidgets. */
    protected void constructorsCommon(TextureRegion whitePixel) {
        this.whitePixel = whitePixel;
    }

    /** Used internally for the shared properties among constructors of RadialWidgets. */
    protected PieWidget(final TextureRegion whitePixel, float minRadius) {
        super(minRadius);
        constructorsCommon(whitePixel);
    }

    /** Used internally for the shared properties among constructors of RadialWidgets. */
    protected PieWidget(final TextureRegion whitePixel,
                        float minRadius, float innerRadiusPercent) {
        super(minRadius, innerRadiusPercent);
        constructorsCommon(whitePixel);
    }

    /** Used internally for the shared properties among constructors of RadialWidgets. */
    protected PieWidget(final TextureRegion whitePixel,
                        float minRadius, float innerRadiusPercent, float startDegreesOffset) {
        super(minRadius, innerRadiusPercent, startDegreesOffset);
        constructorsCommon(whitePixel);
    }

    /** Used internally for the shared properties among constructors of RadialWidgets. */
    protected PieWidget(final TextureRegion whitePixel,
                        float minRadius, float innerRadiusPercent,
                        float startDegreesOffset, float totalDegreesDrawn) {
        super(minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
        constructorsCommon(whitePixel);
    }

    /**
     * See {@link PieWidget} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param style defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     */
    public PieWidget(final TextureRegion whitePixel,
                     PieWidgetStyle style, float minRadius) {
        this(whitePixel, minRadius);
        setStyle(style);
    }

    /**
     * See {@link PieWidget} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param style defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public PieWidget(final TextureRegion whitePixel,
                     PieWidgetStyle style, float minRadius,
                     float innerRadiusPercent) {
        this(whitePixel, minRadius, innerRadiusPercent);
        setStyle(style);
    }

    /**
     * See {@link PieWidget} for a description.
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
    public PieWidget(final TextureRegion whitePixel,
                     PieWidgetStyle style, float minRadius,
                     float innerRadiusPercent, float startDegreesOffset) {
        this(whitePixel, minRadius, innerRadiusPercent, startDegreesOffset);
        setStyle(style);
    }

    /**
     * See {@link PieWidget} for a description.
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
    public PieWidget(final TextureRegion whitePixel,
                     PieWidgetStyle style, float minRadius,
                     float innerRadiusPercent, float startDegreesOffset, float totalDegreesDrawn) {
        this(whitePixel, minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
        setStyle(style);
    }

    /**
     * See {@link PieWidget} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     */
    public PieWidget(final TextureRegion whitePixel,
                     Skin skin, float minRadius) {
        this(whitePixel, minRadius);
        setStyle(skin.get(PieWidgetStyle.class));
    }

    /**
     * See {@link PieWidget} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public PieWidget(final TextureRegion whitePixel,
                     Skin skin, float minRadius,
                     float innerRadiusPercent) {
        this(whitePixel, minRadius, innerRadiusPercent);
        setStyle(skin.get(PieWidgetStyle.class));
    }

    /**
     * See {@link PieWidget} for a description.
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
    public PieWidget(final TextureRegion whitePixel,
                     Skin skin, float minRadius,
                     float innerRadiusPercent, float startDegreesOffset) {
        this(whitePixel, minRadius, innerRadiusPercent, startDegreesOffset);
        setStyle(skin.get(PieWidgetStyle.class));
    }

    /**
     * See {@link PieWidget} for a description.
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
    public PieWidget(final TextureRegion whitePixel,
                     Skin skin, float minRadius,
                     float innerRadiusPercent, float startDegreesOffset, float totalDegreesDrawn) {
        this(whitePixel, minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
        setStyle(skin.get(PieWidgetStyle.class));
    }

    /**
     * See {@link PieWidget} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     */
    public PieWidget(final TextureRegion whitePixel,
                     Skin skin, String style, float minRadius) {
        this(whitePixel, minRadius);
        setStyle(skin.get(style, PieWidgetStyle.class));
    }


    /**
     * See {@link PieWidget} for a description.
     *
     * @param whitePixel a 1x1 white pixel.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param minRadius the {@link #minRadius} that defines the size of the widget.
     * @param innerRadiusPercent the {@link #innerRadiusPercent} that defines
     *                           the percentage of the radius that is cut off,
     *                           starting from the center of the widget.
     */
    public PieWidget(final TextureRegion whitePixel,
                     Skin skin, String style, float minRadius,
                     float innerRadiusPercent) {
        this(whitePixel, minRadius, innerRadiusPercent);
        setStyle(skin.get(style, PieWidgetStyle.class));
    }

    /**
     * See {@link PieWidget} for a description.
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
    public PieWidget(final TextureRegion whitePixel,
                     Skin skin, String style, float minRadius,
                     float innerRadiusPercent, float startDegreesOffset) {
        this(whitePixel, minRadius, innerRadiusPercent, startDegreesOffset);
        setStyle(skin.get(style, PieWidgetStyle.class));
    }

    /**
     * See {@link PieWidget} for a description.
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
    public PieWidget(final TextureRegion whitePixel,
                     Skin skin, String style, float minRadius,
                     float innerRadiusPercent, float startDegreesOffset, float totalDegreesDrawn) {
        this(whitePixel, minRadius, innerRadiusPercent, startDegreesOffset, totalDegreesDrawn);
        setStyle(skin.get(style, PieWidgetStyle.class));
    }








    /**
     * Ensures the {@link ShapeDrawer}'s {@link Batch} matches the one that is currently
     * trying to draw the widget.
     *
     * @param batch the batch that is being used to draw the widget.
     * @return {@code false} only if the batch used is {@code null}. {@code true} otherwise.
     */
    public boolean updateShapeDrawer(Batch batch) {
        if (batch == null) // todo: can this happen?
            return false;

        if (sd == null || sd.getBatch() == null) {
            sd = new ShapeDrawer(batch, whitePixel) {
                /* OPTIONAL: Ensuring a certain smoothness. */
                @Override
                protected int estimateSidesRequired(float radiusX, float radiusY) {
                    return 200;
                }
            };
            return true;
        }

        if (sd.getBatch() == batch)
            return true;

        sd = new ShapeDrawer(batch, whitePixel) {
            /* OPTIONAL: Ensuring a certain smoothness. */
            @Override
            protected int estimateSidesRequired(float radiusX, float radiusY) {
                return 200;
            }
        };
        return true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawWithShapeDrawer(batch, parentAlpha, totalDegreesDrawn);
        drawMe(batch, parentAlpha);
    }

    /** Calls the {@link WidgetGroup#draw(Batch, float)} method. */
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

        /* Ensure the ShapeDrawer uses the proper Batch to draw. */
        if(!updateShapeDrawer(batch))
            return;

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
            if(style.background instanceof TransformDrawable) {
                ((TransformDrawable)(style.background)).draw(batch,
                        getX(Align.center) - getCurrentRadius(), getY(Align.center) - getCurrentRadius(),
                        getCurrentRadius(), getCurrentRadius(),
                        getCurrentDiameter(), getCurrentDiameter(),
                        getScaleX(), getScaleY(),
                        getRotation());
            } else {
                style.background.draw(batch,
                        getX(Align.center) - getCurrentRadius(),
                        getY(Align.center) - getCurrentRadius(),
                        getCurrentDiameter(), getCurrentDiameter());
            }
            batch.setColor(bc.r, bc.g, bc.b, restoreAlpha);
        }

        /* Rest of background */
        if(style.backgroundColor != null) {
            propagateAlpha(sd, style.backgroundColor);
            sd.sector(getX(Align.center), getY(Align.center),
                    getCurrentRadius()-BUFFER, tmpOffset, bgRadian); // buffer to prevent bg from sticking out from below children
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
                    pointAtAngle(vector23, vector2, getCurrentRadius(), drawnRadianAngle),
                    style.separatorWidth);
        }
    }

    /**
     * Determines the color of the slice in which resides the Actor designated
     * by the {@code index} parameter. By default, the colors come from the way
     * you have set up your Style (anything that extends {@link PieWidgetStyle}).<br>
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
        sd.arc(vector2.x, vector2.y, (getCurrentRadius()+ getInnerRadiusLength())/2,
                startAngle, radian, getCurrentRadius()- getInnerRadiusLength());

        /* Circumferences */
        drawChildCircumference(vector2, startAngle, radian, getCurrentRadius() - style.circumferenceWidth/2);
        if(innerRadiusPercent > 0)
            drawChildCircumference(vector2, startAngle, radian, getInnerRadiusLength() + style.circumferenceWidth/2);
    }

    protected void drawChildCircumference(Vector2 vector2, float startAngle, float radian, float radius) {
        if(style.circumferenceColor != null && style.circumferenceWidth > 0) {
            propagateAlpha(sd, style.circumferenceColor);
            sd.arc(vector2.x, vector2.y, radius, startAngle, radian, style.circumferenceWidth);
        }
    }
        /*
    =================================== STYLE ==================================
     */


    /**
     * Returns the label's style. Modifying the returned style may not have an
     * effect until {@link #setStyle(PieWidgetStyle)} is called.<br>
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
    public PieWidgetStyle getStyle() {
        return style;
    }


    /**
     * Runs checks before assigning the style to the Widget. Only a valid style
     * will pass the test.
     *
     * @param style the style that will be checked before being assigned.
     */
    public void setStyle(PieWidgetStyle style) {
        checkStyle(style);
        this.style = style;
        invalidate();
    }

    /**
     * Ensures the input values for the given style are valid.
     *
     * @param style a style class you want to check properties of.
     */
    protected void checkStyle(PieWidgetStyle style) {
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
    public static class PieWidgetStyle {

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
        public PieWidgetStyle() {
        }

        /**
         * Encompasses the characteristics that define the style of the Widget
         * to be drawn.
         *
         * @param style a Style to copy the parameters from.
         */
        public PieWidgetStyle(PieWidgetStyle style) {
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
}

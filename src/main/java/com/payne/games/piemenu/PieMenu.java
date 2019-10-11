package com.payne.games.piemenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pools;
import space.earlygrey.shapedrawer.ShapeDrawer;


/**
 * A PieMenu reuses the {@link RadialGroup}'s functionalities to provide a way
 * to interact with the contained Actors through the "hit-box" of the slices.
 *
 * @author Jérémi Grenier-Berthiaume (aka "payne")
 */
public class PieMenu extends RadialGroup {

    /**
     * This listener controls the interactions with the {@link PieMenu}.<br>
     * It defaults to being a {@link PieMenuClickListener} but can be changed
     * to your own implementation by calling {@link #setPieMenuListener(InputListener)}.
     */
    private InputListener pieMenuListener = new PieMenuClickListener();

    /**
     * The index that is used as a fallback value whenever a processed
     * user-input does not map to a valid child index value.<br>
     * This value can be negative, if you want nothing to be the default.
     */
    private int defaultIndex = -1;

    /**
     * Index of the currently selected item.
     */
    private int selectedIndex = defaultIndex;

    /**
     * Index of the currently highlighted item.
     */
    private int highlightedIndex = defaultIndex;

    /**
     * Index of the currently highlighted item.
     */
    private int hoveredIndex = -1;

    /**
     * Determines whether or not selection should only happen if the mouse is
     * within the radius of the widget.
     */
    private boolean infiniteSelectionRange = false;

    /**
     * Defines the way the Widget looks.
     */
    private PieMenuStyle style;

//    /**
//     * WIP. Not yet implemented!
//     */ // todo: integrate into drawing so that highlighted slice is bigger than others
//    protected float highlightedRadius;
//
//    /**
//     * WIP. Not yet implemented!
//     */ // todo: integrate into drawing so that hovered slice is bigger than others
//    protected float hoveredRadius;

    /**
     * Determines which button must be used to interact with the Widget.
     */
    private int selectionButton = Input.Buttons.LEFT;


    /* For internal use. */
    private static Vector2 vector2 = new Vector2();




    private void constructorsCommon() {
        addListener(pieMenuListener);
        setTouchable(Touchable.enabled);
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param style defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     */
    public PieMenu(final ShapeDrawer sd, PieMenuStyle style, float radius) {
        super(sd, radius);
        setStyle(style);
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param style defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the slices start.
     */
    public PieMenu(final ShapeDrawer sd, PieMenuStyle style, float radius, float innerRadius) {
        super(sd, radius, innerRadius);
        setStyle(style);
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param style defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the slices start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public PieMenu(final ShapeDrawer sd, PieMenuStyle style, float radius, float innerRadius,
                   float startDegreesOffset) {
        super(sd, radius, innerRadius, startDegreesOffset);
        setStyle(style);
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param style defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the slices start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public PieMenu(final ShapeDrawer sd, PieMenuStyle style, float radius, float innerRadius,
                   float startDegreesOffset, float totalDegreesDrawn) {
        super(sd, radius, innerRadius, startDegreesOffset, totalDegreesDrawn);
        setStyle(style);
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     */
    public PieMenu(final ShapeDrawer sd, Skin skin, float radius) {
        super(sd, radius);
        setStyle(skin.get(PieMenuStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the slices start.
     */
    public PieMenu(final ShapeDrawer sd, Skin skin, float radius, float innerRadius) {
        super(sd, radius, innerRadius);
        setStyle(skin.get(PieMenuStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the slices start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public PieMenu(final ShapeDrawer sd, Skin skin, float radius, float innerRadius,
                   float startDegreesOffset) {
        super(sd, radius, innerRadius, startDegreesOffset);
        setStyle(skin.get(PieMenuStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the slices start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public PieMenu(final ShapeDrawer sd, Skin skin, float radius, float innerRadius,
                   float startDegreesOffset, float totalDegreesDrawn) {
        super(sd, radius, innerRadius, startDegreesOffset, totalDegreesDrawn);
        setStyle(skin.get(PieMenuStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param radius the {@link #radius} that defines the size of the widget.
     */
    public PieMenu(final ShapeDrawer sd, Skin skin, String style, float radius) {
        super(sd, radius);
        setStyle(skin.get(style, PieMenuStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the slices start.
     */
    public PieMenu(final ShapeDrawer sd, Skin skin, String style, float radius,
                   float innerRadius) {
        super(sd, radius, innerRadius);
        setStyle(skin.get(style, PieMenuStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the slices start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     */
    public PieMenu(final ShapeDrawer sd, Skin skin, String style, float radius,
                   float innerRadius, float startDegreesOffset) {
        super(sd, radius, innerRadius, startDegreesOffset);
        setStyle(skin.get(style, PieMenuStyle.class));
        constructorsCommon();
    }

    /**
     * See {@link PieMenu} for a description.
     *
     * @param sd used to draw everything but the contained actors.
     * @param skin defines the way the widget looks like.
     * @param style the name of the style to be extracted from the skin.
     * @param radius the {@link #radius} that defines the size of the widget.
     * @param innerRadius the {@link #innerRadius} that defines how far from the
     *                    center should the slices start.
     * @param startDegreesOffset the {@link #startDegreesOffset} that defines
     *                           how far from the origin the drawing begins.
     * @param totalDegreesDrawn the {@link #totalDegreesDrawn} that defines how
     *                          many degrees the widget will span, starting from
     *                          its {@link #startDegreesOffset}.
     */
    public PieMenu(final ShapeDrawer sd, Skin skin, String style, float radius,
                   float innerRadius, float startDegreesOffset, float totalDegreesDrawn) {
        super(sd, radius, innerRadius, startDegreesOffset, totalDegreesDrawn);
        setStyle(skin.get(style, PieMenuStyle.class));
        constructorsCommon();
    }














    /**
     * Resets selected and highlighted child.<br>
     * Does <i>not</i> trigger the
     * {@link ChangeListener#changed(ChangeListener.ChangeEvent, Actor)},
     * nor the {@link PieMenuCallbacks#onHighlightChange(int)}
     * (if you had set it up).
     */
    public void resetSelection() {
        highlightedIndex = defaultIndex;
        selectedIndex = defaultIndex;
    }

    /**
     * Deselects any hovered index.<br>
     * Does <i>not</i> trigger the
     * {@link PieMenuCallbacks#onHoverChange(int)}
     * (if you had set it up).
     */
    public void resetHover() {
        hoveredIndex = -1;
    }

    @Override
    public int findChildIndexAtStage(float x, float y) {
        int childIndex = findIndexFromAngle(angleAtStage(x,y));
        if(infiniteSelectionRange)
            return childIndex;
        stageToLocalCoordinates(vector2.set(x,y));
        return isWithinRadii(vector2.x - radius, vector2.y - radius)
                ? childIndex
                : getAmountOfChildren(); // "getAmountOfChildren" is equivalent to "invalid"
    }

    /**
     * Used to transform an index into a known range: it'll either remain itself
     * if it was designating a valid child index, else it becomes the
     * {@link #defaultIndex} value.
     *
     * @param index any index that you want to map to the values described above.
     * @return either a valid index, ot the {@link #defaultIndex}.
     */
    public int mapIndex(int index) {
        return isValidIndex(index) ? index : defaultIndex;
    }

    /**
     * @param x x-coordinate relative to the origin (bottom left) of the widget
     * @param y y-coordinate relative to the origin (bottom left) of the widget
     * @param touchable if {@code true}, hit detection will respect the
     *                  {@link #setTouchable(Touchable) touchability}.
     * @return deepest child's hit at (x,y). Else, the widget itself if it's
     *         the background. Else the widget itself if with infinite range.
     *         Else null.
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

        if(infiniteSelectionRange)
            return this;

        return null;
    }

    @Override
    public Color getColor(int index) {
        if(style.hoveredAndSelectedSliceColor != null
                && index == selectedIndex
                && index == hoveredIndex)
            return style.hoveredAndSelectedSliceColor;

        if(style.selectedSliceColor != null
                && index == selectedIndex)
            return style.selectedSliceColor;

        if(style.highlightedSliceColor != null
                && index == highlightedIndex)
            return style.highlightedSliceColor;

        if(style.hoveredSliceColor != null
                && index == hoveredIndex)
            return style.hoveredSliceColor;

        if(style.alternateSliceColor != null
                && index%2 == 1)
            return style.alternateSliceColor;

        if(style.sliceColor != null)
            return style.sliceColor;

        return TRANSPARENT;
    }








    /*
    =================================== STYLE ==================================
     */


    /**
     * Runs checks before assigning the style to the Widget. Only a valid style
     * will pass the test.
     *
     * @param style the style that will be checked before being assigned.
     */
    public void setStyle(PieMenuStyle style) {
        super.setStyle(style);
        checkStyle(style);
        this.style = style;
        invalidate();
    }

    /**
     * Ensures the input values for the given style are valid.<br>
     * Only looks at the properties related to the current class, and doesn't
     * look back at parent's properties.<br>
     * Generally shouldn't be called, aside from within the
     * {@link #setStyle(PieMenuStyle)} method which itself takes care of calling
     * the parent's method.
     *
     * @param style a style class you want to check properties of.
     */
    protected void checkStyle(PieMenuStyle style) {
        if(style.hoveredAndSelectedSliceColor != null
                && (style.hoveredSliceColor == null || style.selectedSliceColor == null))
            throw new IllegalArgumentException("hoveredAndSelectedSliceColor cannot be set " +
                    "if hoveredSliceColor or selectedSliceColor is null.");

//        if(style.highlightedRadius < 0)
//            throw new IllegalArgumentException("selectedRadius cannot be negative.");
//        if(style.highlightedRadius == 0)
//            style.highlightedRadius = radius;
        // todo: once integrated, if highlightRadius > radius, don't forget to setSize on the Widget
    }

    /**
     * Encompasses the characteristics that define the style of the Widget
     * to be drawn.
     */
    public static class PieMenuStyle extends RadialGroupStyle {

        /**
         * <i>Recommended. Optional.</i><br>
         * Defines the color of the slice which is currently selected.
         *
         * @see #selectionButton
         * @see #selectedIndex
         */
        public Color selectedSliceColor;

        /**
         * <i>Recommended. Optional.</i><br>
         * Defines the color of the slice which is currently highlighted.<br>
         * Highlights come from dragging the mouse over the {@link PieMenu} while
         * pressing down a mouse-button. The mobile-equivalent is of having your
         * finger pressing down on the PieMenu and dragging it around without
         * releasing.
         *
         * @see #selectionButton
         * @see #highlightedIndex
         */
        public Color highlightedSliceColor;

        /**
         * <i>Recommended. Optional.</i><br>
         * Defines the color of the slice which is currently hovered by the mouse.<br>
         * Only works for the desktops.
         *
         * @see #selectionButton
         * @see #highlightedIndex
         */
        public Color hoveredSliceColor;

        /**
         * <i>Recommended. Optional.</i><br>
         * Defines the color of the slice which is currently hovered by the mouse
         * when this slice was also a highlighted slice.<br>
         * Both {@link #hoveredSliceColor} and {@link #selectedSliceColor}
         * must be defined for this attribute to be allowed to be set.
         */
        public Color hoveredAndSelectedSliceColor;

        /**
         * Encompasses the characteristics that define the style of the Widget
         * to be drawn.
         */
        public PieMenuStyle() {
        }

        /**
         * Encompasses the characteristics that define the style of the Widget
         * to be drawn.
         *
         * @param style a Style to copy the parameters from.
         */
        public PieMenuStyle(PieMenu.PieMenuStyle style) {
            super(style);
            this.selectedSliceColor = new Color(style.selectedSliceColor);
            this.highlightedSliceColor = new Color(style.highlightedSliceColor);
            this.hoveredSliceColor = new Color(style.hoveredSliceColor);
            this.hoveredAndSelectedSliceColor = new Color(style.hoveredAndSelectedSliceColor);
        }
    }









    /*
    ========================== LISTENERS / CALLBACKS ===========================
     */


    /**
     * Selects the child at the given index. Triggers the
     * {{@link ChangeListener#changed(ChangeListener.ChangeEvent, Actor)}}.<br>
     * Indices are based on the order which was used to add child Actors to the
     * Widget. First one added is at index 0, of course, and so on.<br>
     * It would be good practice to use {@link #isValidIndex(int)} to ensure that
     * the index provided to this method is valid.
     *
     * @param newIndex index of the child (and thus slice) which was selected.
     */
    public void selectIndex(int newIndex) {
        newIndex = mapIndex(newIndex);
        int oldHighlightedIndex = highlightedIndex;
        int oldSelectedIndex = selectedIndex;

        selectedIndex = newIndex;
        highlightedIndex = newIndex;
        if (newIndex != oldHighlightedIndex)
            fire(new PieMenuHighlightChangeEvent(newIndex));

        ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
        if (fire(changeEvent)) {
            highlightedIndex = oldHighlightedIndex;
            selectedIndex = oldSelectedIndex;
        }
        Pools.free(changeEvent);
    }

    /**
     * Checks the input coordinate for a candidate  slice. Will take the
     * appropriate action to select it or not based on the configuration of the
     * Widget.
     *
     * @param x x-coordinate in the Stage.
     * @param y y-coordinate in the Stage.
     */
    public void selectSliceAtStage(float x, float y) {
        selectIndex(findChildIndexAtStage(x, y));
    }

    /**
     * Called to check if the candidate slice is different from the previous
     * one. If it is, {@link PieMenuCallbacks#onHighlightChange(int)}
     * is called.<br>
     * Indices are based on the order which was used to add child Actors to the
     * Widget. First one added is at index 0, of course, and so on.<br>
     * It would be good practice to use {@link #isValidIndex(int)} to ensure that
     * the index provided to this method is valid.<br>
     * If the input index is the same as the index of the currently highlighted
     * item, nothing will happen.
     *
     * @param newIndex index of the child (and thus slice) which was highlighted.
     */
    public void highlightIndex(int newIndex) {
        newIndex = mapIndex(newIndex);
        if(newIndex != highlightedIndex) {
            highlightedIndex = newIndex;
            resetHover();
            fire(new PieMenuHighlightChangeEvent(newIndex));
        }
    }

    /**
     * Called to find the slice that is to be interacted with at the
     * given coordinate. If there is one, checks if the highlighted item should
     * be highlighted.<br>
     * If it is a different index,
     * {@link PieMenuCallbacks#onHighlightChange(int)} is called.<br>
     *
     * @param x x-coordinate in the stage.
     * @param y y-coordinate in the stage.
     */
    public void highlightSliceAtStage(float x, float y) {
        highlightIndex(findChildIndexAtStage(x, y));
    }

    /**
     * Called to check if the candidate slice is different from the previous
     * one. If it is, the {@link PieMenuCallbacks#onHoverChange(int)}
     * is called.<br>
     * Indices are based on the order which was used to add child Actors to the
     * Widget. First one added is at index 0, of course, and so on.<br>
     * It would be good practice to use {@link #isValidIndex(int)} to ensure that
     * the index provided to this method is valid.<br>
     * If the input index is the same as the index of the currently hovered
     * item, nothing will happen.
     *
     * @param newIndex index of the child (and thus slice) which was hovered.
     */
    public void hoverIndex(int newIndex) {
        newIndex = mapIndex(newIndex);
        if(newIndex != hoveredIndex) {
            hoveredIndex = newIndex;
            fire(new PieMenuHoverChangeEvent(newIndex));
        }
    }

    /**
     * Called to find the slice that is to be interacted with at the
     * given coordinate. If there is one, checks if the hovered item is different
     * from the previous one.
     * If it is a different index,
     * {@link PieMenuCallbacks#onHoverChange(int)} is called.<br>
     *
     * @param x x-coordinate in the stage.
     * @param y y-coordinate in the stage.
     */
    public void hoverSliceAtStage(float x, float y) {
        hoverIndex(findChildIndexAtStage(x, y));
    }



    /**
     * The default {@link ClickListener} that comes with the {@link PieMenu}. You
     * are not obligated to use it, but this one has been designed to work as is,
     * for the most part.
     */
    public static class PieMenuClickListener extends ClickListener {

        /**
         * The suggested {@link ClickListener} that comes with the {@link PieMenu}.
         * You are not obligated to use it, but this one has been designed to work
         * as is, for the most part.
         */
        public PieMenuClickListener() {
            setTapSquareSize(Integer.MAX_VALUE);
        }

        /**
         * @param x x-coordinate in pixels, relative to the bottom left of the attached actor
         * @param y y-coordinate in pixels, relative to the bottom left of the attached actor
         */
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if(!(event.getListenerActor() instanceof PieMenu))
                return false;
            PieMenu pie = (PieMenu)event.getListenerActor();

            boolean accepted = (button == pie.getSelectionButton());
            if(accepted)
                pie.highlightSliceAtStage(event.getStageX(), event.getStageY());
            return accepted;
        }

        /**
         * @param x x-coordinate in pixels, relative to the bottom left of the attached actor
         * @param y y-coordinate in pixels, relative to the bottom left of the attached actor
         */
        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if(!(event.getListenerActor() instanceof PieMenu))
                return;
            PieMenu pie = (PieMenu)event.getListenerActor();

            pie.highlightSliceAtStage(event.getStageX(), event.getStageY());
            super.touchDragged(event, x, y, pointer);
        }

        /**
         * @param x x-coordinate in pixels, relative to the bottom left of the attached actor
         * @param y y-coordinate in pixels, relative to the bottom left of the attached actor
         */
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if(!(event.getListenerActor() instanceof PieMenu))
                return;
            PieMenu pie = (PieMenu)event.getListenerActor();

            if(button != pie.getSelectionButton())
                return;
            pie.selectSliceAtStage(event.getStageX(), event.getStageY()); // todo: just use highlighted instead of finding index again?
            super.touchUp(event, x, y, pointer, button);
        }

        /**
         * @param x x-coordinate in pixels, relative to the bottom left of the attached actor
         * @param y y-coordinate in pixels, relative to the bottom left of the attached actor
         */
        @Override
        public boolean mouseMoved(InputEvent event, float x, float y) {
            if(!(event.getListenerActor() instanceof PieMenu))
                return false;
            PieMenu pie = (PieMenu)event.getListenerActor();

            pie.hoverSliceAtStage(event.getStageX(), event.getStageY());
            return true;
        }

        /**
         * @param x x-coordinate in pixels, relative to the bottom left of the attached actor
         * @param y y-coordinate in pixels, relative to the bottom left of the attached actor
         */
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if(!(event.getListenerActor() instanceof PieMenu))
                return;
            PieMenu pie = (PieMenu)event.getListenerActor();

            // todo: for each child : if targetActor.isDescendantOf(child) ... (true even when targetActor is child)
            /* Reset the hover only when the mouse exits the PieMenu. */
            if(toActor != pie && (toActor == null || !(pie == toActor.getParent())))
                pie.hoverIndex(-1);
            super.exit(event, x, y, pointer, toActor);
        }
    }


    private static class PieMenuHighlightChangeEvent extends Event {
        private int newIndex;

        public PieMenuHighlightChangeEvent(int newIndex) {
            this.newIndex = newIndex;
        }
    }

    private static class PieMenuHoverChangeEvent extends Event {
        private int newIndex;

        public PieMenuHoverChangeEvent(int newIndex) {
            this.newIndex = newIndex;
        }
    }

    /**
     * If the {@link ChangeListener} wasn't enough, you can add a
     * {@link PieMenuCallbacks} to be able to execute code every
     * time the currently-highlighted or currently-hovered value changes.<br>
     * Keep in mind that if you use {@link #resetSelection()} or
     * {@link #setHighlightedIndex(int)}, this callback is not triggered.<br>
     * It might also be useful to know that, on a desktop,
     * {@link #onHoverChange(int)} and {@link #onHighlightChange(int)} will never
     * be called at the same time: it's one or the other.
     */
    public abstract static class PieMenuCallbacks implements EventListener {

        @Override
        public final boolean handle(Event event) {
            if(event instanceof PieMenuHighlightChangeEvent)
                onHighlightChange(((PieMenuHighlightChangeEvent)event).newIndex);
            else if(event instanceof PieMenuHoverChangeEvent)
                onHoverChange(((PieMenuHoverChangeEvent)event).newIndex);
            return false;
        }

        /**
         * Called every time the "currently highlighted" value changes.<br>
         * This only applies when a button is down and the mouse (or finger)
         * is moving. It thus works on desktops and mobiles.
         *
         * @see PieMenuStyle#selectedSliceColor
         * @param highlightedIndex the newly highlighted index.
         */
        public void onHighlightChange(int highlightedIndex){
            // implementation left to the user, but not abstract to not force the user to implement it
        }

        /**
         * Called every time the "currently hovered" value changes.<br>
         * This only applies when no button is down while the mouse is moving,
         * and will only work for desktops.
         *
         * @see PieMenuStyle#hoveredSliceColor
         * @param hoveredIndex the newly hovered index.
         */
        public void onHoverChange(int hoveredIndex) {
            // implementation left to the user, but not abstract to not force the user to implement it
        }
    }









    /*
    =============================== GETTERS/SETTERS ============================
     */



    /**
     * Returns the label's style. Modifying the returned style may not have an
     * effect until {@link #setStyle(PieMenuStyle)} is called.<br>
     * It's probable that your code will look like this (to give you an idea):
     * <pre>
     * {@code
     * pieMenu.getStyle().whatYouWantToChange = someNewValue;
     * pieMenu.setStyle(pieMenu.getStyle());
     * }
     * </pre>
     *
     * @return the Style that defines this Widget. This style contains information
     * about what is the value of the radius or the width of the separators, for
     * example.
     */
    public PieMenuStyle getStyle() {
        return style;
    }

    /**
     * @see #selectionButton
     * @return the mouse-button that is expected to be required to be pressed or
     * released to interact with the widget.
     */
    public int getSelectionButton() {
        return selectionButton;
    }

    /**
     * Determines which button must be used to interact with the Widget.<br>
     * If you are not using the {@link PieMenuClickListener}, then this
     * option will not work "as-is" and you will have to implement it again (if
     * you want to have it).
     *
     * @param selectionButton Use {@link Input.Buttons} to find the proper integer.
     */
    public void setSelectionButton(int selectionButton) {
        this.selectionButton = selectionButton;
    }

    /**
     * The {@link #infiniteSelectionRange} flag determines whether or not selection
     * should only happen if the mouse is within the radius of the widget.
     */
    public boolean isInfiniteSelectionRange() {
        return infiniteSelectionRange;
    }

    /**
     * Determines whether or not selection should only happen if the mouse is
     * within the radius of the widget.
     * @see #infiniteSelectionRange
     */
    public void setInfiniteSelectionRange(boolean infiniteSelectionRange) {
        this.infiniteSelectionRange = infiniteSelectionRange;
    }

    /**
     * Returns the currently selected  item's index. A highlighted item can be
     * considered selected, depending on the customized behavior of the Widget.<br>
     * The {@link #isValidIndex(int)} method is provided to easily check if the
     * returned value can be mapped to a child or not.
     *
     * @return The value of the {@link #defaultIndex} after a "reset", if
     *         no item were ever selected on this Widget, or if the selection
     *         happened outside of the boundaries.<br>
     *         Else, returns the index of the currently selected item.<br>
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Changes the selected index to the desired value, but will not trigger any
     * callback such as the {@link ChangeListener}. If you want the callback to
     * be trigger, use {@link #selectIndex(int)} instead.<br>
     * To ensure the index you are giving to this method is valid, use
     * {@link #isValidIndex(int)}. An invalid index will result in a simple
     * deselection. If you want the {@link #defaultIndex} to be the
     * new value, look into {@link #mapIndex(int)}.
     *
     * @param selectedIndex the index of the desired item to appear as selected.
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * If negative, it means nothing gets selected by default.
     *
     * @see #defaultIndex
     * @return the index that is used as a fallback value whenever a processed
     *         user-input does not map to a valid child index value.
     */
    public int getDefaultIndex() {
        return defaultIndex;
    }

    /**
     * The index that is used as a fallback value whenever a processed
     * user-input does not map to a valid child index value.<br>
     * This value can be negative, if you want nothing to be the default.<br>
     * If the current value of {@link #selectedIndex} isn't valid, then both
     * {@link #selectedIndex} and {@link #highlightedIndex} are set to that new
     * value, but only the selection's {@link ChangeListener} will be triggered.
     *
     * @see #isValidIndex(int)
     * @see #mapIndex(int)
     * @param defaultIndex the desired default value.
     */
    public void setDefaultIndex(int defaultIndex) {
        if(this.defaultIndex == defaultIndex)
            return;
        if(!isValidIndex(selectedIndex)) {
            selectIndex(defaultIndex);
            setHighlightedIndex(defaultIndex);
        }
        this.defaultIndex = defaultIndex;
    }

    /**
     * @return the currently highlighted item's index.
     */
    public int getHighlightedIndex() {
        return highlightedIndex;
    }

    /**
     * Changes the highlighted index to the desired value, but will not trigger
     * any callback such as the {@link PieMenuCallbacks#onHighlightChange(int)}.
     * If you want the callback to be trigger, use {@link #highlightIndex(int)}
     * instead.<br>
     * To ensure the index you are giving to this method is valid, use
     * {@link #isValidIndex(int)}. An invalid index will result in a simple
     * deselection. If you want the {@link #defaultIndex} to be the
     * new value, look into {@link #mapIndex(int)}.
     *
     * @param highlightedIndex the index of the child to appear as highlighted.
     */
    public void setHighlightedIndex(int highlightedIndex) {
        this.highlightedIndex = highlightedIndex;
    }

    /**
     * @return the index of the child that is hovered by the mouse.
     */
    public int getHoveredIndex() {
        return hoveredIndex;
    }

    /**
     * Changes the hovered index to the desired value, but will not trigger
     * any callback such as the {@link PieMenuCallbacks#onHoverChange(int)}.
     * If you want the callback to be trigger, use {@link #highlightIndex(int)}
     * instead.<br>
     * To ensure the index you are giving to this method is valid, use
     * {@link #isValidIndex(int)}. An invalid index will result in a simple
     * deselection: if that is what you wanted, you might want to look into
     * {@link #resetHover()}. If you want the {@link #defaultIndex} to be the
     * new value, look into {@link #mapIndex(int)}.
     *
     * @param hoveredIndex the index of the child to appear as hovered.
     */
    public void setHoveredIndex(int hoveredIndex) {
        this.hoveredIndex = hoveredIndex;
    }

    /**
     * @see #pieMenuListener
     * @return the listener that controls the interactions with the {@link PieMenu}.
     */
    public InputListener getPieMenuListener() {
        return pieMenuListener;
    }

    /**
     * By calling this, the previous listener is removed from the widget, and
     * this new one is added automatically.
     *
     * @see #pieMenuListener
     * @param pieMenuListener the listener that controls the interactions
     *                        with the {@link PieMenu}.
     */
    public void setPieMenuListener(InputListener pieMenuListener) {
        if(this.pieMenuListener == pieMenuListener)
            return;
        if(this.pieMenuListener != null)
            removeListener(this.pieMenuListener);
        if(pieMenuListener != null) {
            this.pieMenuListener = pieMenuListener;
            addListener(pieMenuListener);
        }
    }
}

package com.payne.games.piemenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pools;
import space.earlygrey.shapedrawer.ShapeDrawer;


/**
 * A PieMenu reuses the RadialGroup's functionalities to provide a way to
 * interact with the contained Actors through the "hit-box" of the regions.
 *
 * @author Jérémi Grenier-Berthiaume (aka "payne")
 */
public class PieMenu extends RadialGroup {
    private SuggestedClickListener suggestedClickListener;

    /**
     * Index of the currently selected item.
     */
    private int selectedIndex = -1;

    /**
     * Index of the currently highlighted item.
     */
    private int highlightedIndex = -1;

    /**
     * Set to {@code true} if you want the
     * {@link ChangeListener#changed(ChangeListener.ChangeEvent, Actor)}
     * to be called every time the currently highlighted value changes. This
     * will also cause the automatic selection of the item that was highlighted.<br>
     * {@link HighlightChangeListener#onHighlightChange()} is also provided as
     * a "convenience interface" if you would prefer to keep selection separated
     * from highlights in the callbacks.
     */
    private boolean highlightIsSelection = false;

    /**
     * Set to {@code true} if you want the Widget to stop controlling its visibility.
     * You will be in charge of calling {@link #setVisible(boolean)} whenever
     * you want to influence its visibility.
     */
    private boolean manualControlOfVisibility = false;

    /**
     * Should selection only happen if mouse is within the radius of the widget?
     */
    private boolean infiniteSelectionRange = true;

    /**
     * Defines the way the Widget looks.
     */
    private PieMenuStyle style;

    /**
     * If the "ChangeListener" wasn't enough, you can add a "HighlightChangeListener"
     * to be able to execute code every time the "currently highlighted" value
     * changes.
     */
    private HighlightChangeListener highlightChangeListener;

    /**
     * Determines which button must be used to interact with the Widget.
     */
    private int selectionButton = Input.Buttons.LEFT;

    /* For internal use. */
    private static Vector2 vector2 = new Vector2();
    private static final Color TRANSPARENT = new Color(0,0,0,0);




    public PieMenu(final ShapeDrawer sd, PieMenuStyle style) {
        super(sd);
        setStyle(style);
        setTouchable(Touchable.enabled);

        suggestedClickListener = new SuggestedClickListener(); // todo: if keeping this, copy in other constructors
    }

    public PieMenu(final ShapeDrawer sd, Skin skin) {
        super(sd);
        setStyle(skin.get(PieMenuStyle.class));
        setTouchable(Touchable.enabled);
    }

    public PieMenu(final ShapeDrawer sd, Skin skin, String style) {
        super(sd);
        setStyle(skin.get(style, PieMenuStyle.class));
        setTouchable(Touchable.enabled);
    }


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
        if(style.selectedRadius < 0)
            throw new IllegalArgumentException("selectedRadius cannot be negative.");
        if(style.selectedRadius == 0)
            style.selectedRadius = style.radius;
    }


    /**
     * Resets selected and highlighted child.<br>
     * Does <i>not</i> trigger the
     * {@link ChangeListener#changed(ChangeListener.ChangeEvent, Actor)},
     * nor the {@link HighlightChangeListener#onHighlightChange()}
     * (if you had set it up).
     */
    public void resetSelection() {
        highlightedIndex = -1;
        selectedIndex = -1;
    }

    /**
     * Used to trigger programmatically a {@code touchDown} event in such a way
     * that will allow the user to directly be interacting with the
     * {@code touchDragged} event.<br>
     * This bypasses the {@link #selectionButton} filter that is inside the
     * {@link SuggestedClickListener}
     */
    @Deprecated
    public void triggerDefaultListenerTouchDown() {
        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.touchDown);
        event.setButton(getSelectionButton());
        fire(event);
    }

    /**
     * Selects the child at the given index. Triggers the
     * {{@link ChangeListener#changed(ChangeListener.ChangeEvent, Actor)}}.<br>
     * Indices are based on the order which was used to add child Actors to the
     * Widget. First one added is at index 0, of course, and so on.
     *
     * @param newIndex index of the child (and thus region) which was selected.
     */
    public void selectIndex(int newIndex) {
        if(newIndex != selectedIndex) {
            int oldHighlightedIndex = highlightedIndex;
            int oldSelectedIndex = selectedIndex;

            selectedIndex = newIndex;
            highlightedIndex = newIndex;
            if (highlightChangeListener != null && newIndex != oldHighlightedIndex)
                highlightChangeListener.onHighlightChange(); // todo: should we really call this?!

            ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
            if (fire(changeEvent)) {
                highlightedIndex = oldHighlightedIndex;
                selectedIndex = oldSelectedIndex;
            }
            Pools.free(changeEvent);
        }
    }

    /**
     * Checks the input coordinate for a candidate child region. Will take the
     * appropriate action to select it or not based on the configuration of the
     * Widget.
     *
     * @param x x-coordinate in the Stage.
     * @param y y-coordinate in the Stage.
     */
    public void selectChildRegionAtStage(float x, float y) {
        selectIndex(findChildSectorAtStage(x, y));
    }

    /**
     * Called to check if the candidate region should be highlighted (and possibly
     * selected). If it is the case, will apply the appropriate action.<br>
     * Indices are based on the order which was used to add child Actors to the
     * Widget. First one added is at index 0, of course, and so on.
     *
     * @param newIndex index of the child (and thus region) which was highlighted.
     * @param highlightIsSelection whether or not a highlight is to be
     *                             considered as a selection. Basically
     *                             corresponds to {@link #highlightIsSelection}.
     */
    public void highlightIndex(int newIndex, boolean highlightIsSelection) {
        if(highlightIsSelection) {
            selectIndex(newIndex);
            return;
        }

        if(newIndex != highlightedIndex) {
            highlightedIndex = newIndex;
            if(highlightChangeListener != null)
                highlightChangeListener.onHighlightChange();
        }
    }

    /**
     * Called to find the child region that is to be interacted with at the
     * given coordinate. If there is one, checks if the highlighted item should be
     * highlighted (and possibly selected). If it is the case, will apply
     * the appropriate action.
     *
     * @param x x-coordinate in the stage.
     * @param y y-coordinate in the stage.
     */
    public void highlightChildRegionAtStage(float x, float y) {
        highlightIndex(findChildSectorAtStage(x, y), highlightIsSelection);
    }

    @Override
    public int findChildSectorAtStage(float x, float y) {
        float angle = angleAtStage(x,y);
        angle = ((angle - style.startDegreesOffset) % 360 + 360) % 360; // normalizing the angle
        int childIndex = MathUtils.floor(angle / style.totalDegreesDrawn * getAmountOfChildren());
        if(infiniteSelectionRange)
            return childIndex;
        stageToLocalCoordinates(vector2.set(x,y));
        return isWithinRadii(vector2.x - style.radius, vector2.y - style.radius) ? childIndex : getAmountOfChildren(); // size is equivalent to "invalid"
    }

    @Override
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
            if(style.selectedChildRegionColor != null) {
                drawChildWithSelection(vector2, i, tmp, tmpRad);
            } else {
                drawChildWithoutSelection(vector2, i, tmp, tmpRad);
            }

            /* Separator */
            drawChildSeparator(vector2, tmp);
        }

        /* The remaining last separator to be drawn */
        drawChildSeparator(vector2, tmpOffset + size*tmpRad);
    }

    protected void drawChildWithSelection(Vector2 vector2, int index, float startAngle, float radian) {
        if(style.childRegionColor != null) {
            if(style.alternateChildRegionColor != null) {
                sd.setColor(index == highlightedIndex ? style.selectedChildRegionColor
                        : index%2 == 0 ? style.childRegionColor
                        : style.alternateChildRegionColor);
                sd.arc(vector2.x, vector2.y, (style.radius+style.innerRadius)/2, startAngle, radian, style.radius-style.innerRadius);
            } else {
                sd.setColor(index == highlightedIndex ? style.selectedChildRegionColor : style.childRegionColor);
                sd.arc(vector2.x, vector2.y, (style.radius+style.innerRadius)/2, startAngle, radian, style.radius-style.innerRadius);
            }
        } else {
            sd.setColor(index == highlightedIndex ? style.selectedChildRegionColor
                    : TRANSPARENT); // for when the user only specified a "selectedColor"
            sd.arc(vector2.x, vector2.y, (style.radius+style.innerRadius)/2, startAngle, radian, style.radius-style.innerRadius);
        }

        drawChildCircumference(vector2, startAngle, radian, style.radius - style.circumferenceWidth/2); // todo: integrate selectedRadius here
    }


    /**
     * The suggested ClickListener that comes with the PieMenu. You are not
     * obligated to use it, but this one has been designed to work as in, for
     * the most part.
     */
    public class SuggestedClickListener extends ClickListener {

        /**
         * The suggested ClickListener that comes with the PieMenu. You are not
         * obligated to use it, but this one has been designed to work as in, for
         * the most part.
         */
        public SuggestedClickListener() {
            setTapSquareSize(Integer.MAX_VALUE);
        }

        /**
         * @param x x-coordinate in pixels, relative to the bottom left of the attached actor
         * @param y y-coordinate in pixels, relative to the bottom left of the attached actor
         */
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if(button != selectionButton)
                return false;
            if(!manualControlOfVisibility)
                setVisible(true);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if(button != selectionButton)
                return;
            selectChildRegionAtStage(event.getStageX(), event.getStageY()); // todo: just use highlighted instead of finding index again?
            if(!manualControlOfVisibility)
                setVisible(false);
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            highlightChildRegionAtStage(event.getStageX(), event.getStageY());
            super.touchDragged(event, x, y, pointer);
        }
    }




    /**
     * Encompasses all the characteristics that define the way the PieMenu will be drawn.
     */
    public static class PieMenuStyle extends RadialGroupStyle {

        /**
         * <i>Recommended. Optional.</i><br>
         * Defines the color of the region which is currently selected.
         */
        public Color selectedChildRegionColor;
        public Color hoveredColor; // todo: integrate hoveredColor?
        public float selectedRadius; // todo: integrate into drawing so that selected region is bigger than others

        /**
         * Encompasses all the characteristics that define the way the
         * PieMenu will be drawn.
         */
        public PieMenuStyle() {
        }

        public PieMenuStyle(PieMenu.PieMenuStyle style) {
            super(style);
            this.selectedChildRegionColor = style.selectedChildRegionColor;
            this.selectedRadius = style.selectedRadius;
        }
    }


    /**
     * A "convenience interface" that allows users to execute code whenever
     * the "currently highlighted" value changes.
     */
    public interface HighlightChangeListener {

        /**
         * Called every time the "currently highlighted" value changes.<br>
         * It is possible to achieve somewhat the same goal by calling
         * {@code setHighlightIsSelection(true)} on the PieMenu: that will
         * trigger the {@link ChangeListener} every time the currently
         * highlighted value changes.
         */
        void onHighlightChange();
    }





    /*
    =============================== GETTERS/SETTERS ============================
     */ // todo: JavaDoc


    public int getSelectionButton() {
        return selectionButton;
    }
    public void setSelectionButton(int selectionButton) {
        this.selectionButton = selectionButton;
    }
    public void setSuggestedClickListener(SuggestedClickListener suggestedClickListener) {
        this.suggestedClickListener = suggestedClickListener;
    }
    public SuggestedClickListener getSuggestedClickListener() {
        return suggestedClickListener;
    }
    public boolean isInfiniteSelectionRange() {
        return infiniteSelectionRange;
    }
    public void setInfiniteSelectionRange(boolean infiniteSelectionRange) {
        this.infiniteSelectionRange = infiniteSelectionRange;
    }
    public HighlightChangeListener getHighlightChangeListener() {
        return highlightChangeListener;
    }
    public void setHighlightChangeListener(HighlightChangeListener highlightChangeListener) {
        this.highlightChangeListener = highlightChangeListener;
    }

    /**
     * Returns the currently selected  item's index. A highlighted item can be
     * considered selected, depending on the customized behavior of the Widget.<br>
     * The {@link #isValidIndex(int)} method is provided to easily check if the
     * returned value can be mapped to a child or not.
     *
     * @return -1 after a "reset" or if no item were ever selected on this Widget.<br>
     *         Else, return the index of the currently selected item.<br>
     *         Returns the amount of children when the selection happened outside
     *         of the boundaries (this boundary depends on the customized behaviors
     *         such as the radius, the inner radius, or whether or not the
     *         selection has an infinite range).
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
    public int getHighlightedIndex() {
        return highlightedIndex;
    }
    public void setHighlightedIndex(int highlightedIndex) {
        this.highlightedIndex = highlightedIndex;
    }
    public boolean isHighlightIsSelection() {
        return highlightIsSelection;
    }
    public void setHighlightIsSelection(boolean highlightIsSelection) {
        this.highlightIsSelection = highlightIsSelection;
    }
    public PieMenuStyle getStyle() {
        return style;
    }
    public boolean isManualControlOfVisibility() {
        return manualControlOfVisibility;
    }
    public void setManualControlOfVisibility(boolean manualControlOfVisibility) {
        this.manualControlOfVisibility = manualControlOfVisibility;
    }
}

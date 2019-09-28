package com.payne.games;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Pools;
import space.earlygrey.shapedrawer.ShapeDrawer;


/**
 * A PieMenu reuses the RadialGroup's functionalities to provide a way to
 * interact with the contained Actors through the "hit-box" of the regions.
 *
 * @author Jérémi Grenier-Berthiaume (aka "payne")
 */
public class PieMenu extends RadialGroup {
    @Deprecated public MyDragListener dragListener; // todo: uncertain if appropriate

    /**
     * Index of the currently selected item.
     */
    private int selectedIndex = -1;

    /**
     * Index of the currently highlighted item.
     */
    private int highlightedIndex = -1;

    /**
     * If hovering an item calls ChangeListener and selects the item.
     */
    private boolean hoverIsSelection = false;

    /**
     * When redrawing the widget, should it still select the last selected item?
     */
    private boolean resetSelectionOnAppear = true;

    /**
     * The widget should remain visible. The "visible-flow" becomes the responsibility of the user.
     */
    private boolean remainDisplayed = false;

    /**
     * Should selection only happen if mouse is within the radius of the widget?
     */
    private boolean infiniteSelectionRange = true;

    /**
     * Defines the way the Widget looks.
     */
    private PieMenuStyle style;

    /**
     * If the "ChangeListener" wasn't enough, you can add a "HoverChangeListener"
     * to be able to execute code every time the "currently highlighted" value
     * changes.
     */
    private HoverChangeListener hoverChangeListener;

    /* For internal use. */
    private static Vector2 vector2 = new Vector2();
    private Color transparent = new Color(0,0,0,0);




    public PieMenu(final ShapeDrawer sd, PieMenuStyle style) {
        super(sd, style);
        setStyle(style);
        setVisible(false);

        dragListener = new MyDragListener(style.radius);
        dragListener.setTapSquareSize(0);
    }

    public PieMenu(final ShapeDrawer sd, Skin skin) {
        super(sd, skin);
        setStyle(skin.get(PieMenuStyle.class));
        setVisible(false);
    }

    public PieMenu(final ShapeDrawer sd, Skin skin, String style) {
        super(sd, skin, style);
        setStyle(skin.get(style, PieMenuStyle.class));
        setVisible(false);
    }



    public void setStyle(PieMenuStyle style) {
        checkStyle(style);
        this.style = style;
        invalidate();
    }

    protected void checkStyle(PieMenuStyle style) {
        // todo: is there really something to check for PieMenu?

        super.checkStyle(style);
    }

    public void setRadius(float radius) { // todo: add restrictions (range [0,360])
        style.radius = radius;
        dragListener.maxRange = radius;
    }


    /**
     * Designates an Actor on which the PieMenu will be centered.
     * Also takes care of attaching the DragListener to that Actor.
     *
     * @param attachedTo an Actor
     */
    @Deprecated
    public void attachToActor(Actor attachedTo) {
        this.attachedTo = attachedTo;
        attachedTo.addListener(dragListener);
    }

    /**
     * Resets selected and highlighted child.
     */
    public void resetSelection() {
        highlightedIndex = -1;
        selectedIndex = -1;
    }

    /**
     * Selects the child at the given index. Triggers the ChangeListener.<br>
     * Indices are based on the order which was used to add child Actors to the
     * RadialWidget. First one added is at index 0, of course, and so on.
     *
     * @param newIndex index of the child (and thus region) which was selected.
     */
    public void selectIndex(int newIndex) {
        if(newIndex != selectedIndex) {
            int oldHighlightedIndex = highlightedIndex;
            int oldSelectedIndex = selectedIndex;

            selectedIndex = newIndex;
            highlightedIndex = newIndex;
            if (hoverChangeListener != null && newIndex != oldHighlightedIndex)
                hoverChangeListener.onHoverChange(); // todo: should we really call this?!

            ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
            if (fire(changeEvent)) {
                highlightedIndex = oldHighlightedIndex;
                selectedIndex = oldSelectedIndex;
            }
            Pools.free(changeEvent);
        }
    }

    /**
     * Called to check if the hovered item should be highlighted (and possibly selected).
     *
     * @param newIndex index of the child (and thus region) which was hovered.
     * @param hoverIsSelection whether or not a hover is to be considered as a selection.
     */
    public void hoverIndex(int newIndex, boolean hoverIsSelection) {
        if(hoverIsSelection) {
            selectIndex(newIndex);
            return;
        }

        if(newIndex != highlightedIndex) {
            highlightedIndex = newIndex;
            if(hoverChangeListener != null)
                hoverChangeListener.onHoverChange();
        }
    }

    @Override
    public int findRegionAtAbsolute(float x, float y) {
        float angle = angleAtAbsolute(x,y);
        angle = ((angle - style.startDegreesOffset) % 360 + 360) % 360; // normalizing the angle
        int childIndex = MathUtils.floor(angle / style.totalDegreesDrawn * getChildren().size);
        if(infiniteSelectionRange)
            return childIndex;
        stageToLocalCoordinates(vector2.set(x,y));
        return isWithinRadius(vector2.x - style.radius, vector2.y - style.radius) ? childIndex : getChildren().size; // size is equivalent to "invalid"
    }

    @Override
    protected void drawWithShapeDrawer(Batch batch, float parentAlpha) {

        /* Pre-calculating */
        float bgRadian = MathUtils.degreesToRadians*style.totalDegreesDrawn;
        float tmpOffset = MathUtils.degreesToRadians*style.startDegreesOffset;
        int size = getChildren().size;
        float tmpRad = bgRadian / size;

        /* Background image */
        if(style.background != null)
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());

        /* Rest of background */
        if(style.backgroundColor != null) {
            sd.setColor(style.backgroundColor);
            sd.sector(getX()+style.radius, getY()+style.radius, style.radius, tmpOffset, bgRadian);
        }

        /* Children */
        vector2.set(getX()+style.radius, getY()+style.radius);
        for(int i=0; i<size; i++) {
            float tmp = tmpOffset + i*tmpRad;
            if(style.selectedColor != null) {
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
                sd.setColor(index == highlightedIndex ? style.selectedColor
                        : index%2 == 0 ? style.childRegionColor
                        : style.alternateChildRegionColor);
                sd.arc(vector2.x, vector2.y, style.radius, startAngle, radian, style.radius-style.innerRadius);
            } else {
                sd.setColor(index == highlightedIndex ? style.selectedColor : style.childRegionColor);
                sd.arc(vector2.x, vector2.y, style.radius, startAngle, radian, style.radius-style.innerRadius);
            }
        } else {
            sd.setColor(index == highlightedIndex ? style.selectedColor
                    : transparent);
            sd.arc(vector2.x, vector2.y, style.radius, startAngle, radian, style.radius-style.innerRadius);
        }
    }




    public class MyDragListener extends DragListener {
        public float maxRange; // todo: this can be removed since it's an inner-class


        public MyDragListener(float maxRange) {
            this.maxRange = maxRange;
        }


        /**
         * @param x x-coordinate in pixels, relative to the bottom left of the attached actor
         * @param y y-coordinate in pixels, relative to the bottom left of the attached actor
         */
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if(button != Input.Buttons.LEFT) // only-left clicks should activate that   todo: let user decide which buttons
                return true;

            if(resetSelectionOnAppear)
                resetSelection();
            setVisible(true);
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if(button != Input.Buttons.LEFT) // only-left clicks should activate that   todo: let user decide which buttons
                return;

            selectIndex(findRegionAtAbsolute(event.getStageX(), event.getStageY())); // todo: just use highlighted instead of finding index again?
            if(!remainDisplayed)
                setVisible(false);
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if(!isVisible())
                return;

            hoverIndex(findRegionAtAbsolute(event.getStageX(), event.getStageY()), hoverIsSelection);
            super.touchDragged(event, x, y, pointer);
        }
    }





    public static class PieMenuStyle extends RadialGroupStyle {
        public Color selectedColor;
        public float selectedRadius; // todo: integrate into drawing so that selected region is smaller or bigger than others

        public PieMenuStyle() {
        }

        public PieMenuStyle(PieMenu.PieMenuStyle style) {
            super(style);
            this.selectedColor = style.selectedColor;
            this.selectedRadius = style.selectedRadius;
        }
    }



    public interface HoverChangeListener {
        void onHoverChange();
    }





    /*
    =============================== GETTERS/SETTERS ============================
     */ // todo: JavaDoc


    public boolean isInfiniteSelectionRange() {
        return infiniteSelectionRange;
    }
    public void setInfiniteSelectionRange(boolean infiniteSelectionRange) {
        this.infiniteSelectionRange = infiniteSelectionRange;
    }
    public HoverChangeListener getHoverChangeListener() {
        return hoverChangeListener;
    }
    public void setHoverChangeListener(HoverChangeListener hoverChangeListener) {
        this.hoverChangeListener = hoverChangeListener;
    }
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
    public boolean isHoverIsSelection() {
        return hoverIsSelection;
    }
    public void setHoverIsSelection(boolean hoverIsSelection) {
        this.hoverIsSelection = hoverIsSelection;
    }
    public boolean isResetSelectionOnAppear() {
        return resetSelectionOnAppear;
    }
    public void setResetSelectionOnAppear(boolean resetSelectionOnAppear) {
        this.resetSelectionOnAppear = resetSelectionOnAppear;
    }
    public PieMenuStyle getStyle() {
        return style;
    }
    public boolean isRemainDisplayed() {
        return remainDisplayed;
    }
    public void setRemainDisplayed(boolean remainDisplayed) {
        this.remainDisplayed = remainDisplayed;
    }
}

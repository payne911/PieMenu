package com.payne.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Pools;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class PieMenu extends RadialGroup {
    public MyDragListener dragListener;

    private int selectedIndex = -1; // index of the currently selected item
    private int highlightedIndex = -1; // index of the currently highlighted item

    private boolean infiniteDragRange = true; // should gestures select if drag finishes outside of radius
    private boolean hoverIsSelection = false; // if hovering an item calls ChangeListener and selects the item
    private boolean resetSelectionOnAppear = true; // when redrawing the widget, should it still select the last selected item?

    private PieMenuStyle style;
    private HoverChangeListener hoverChangeListener;

    /* For internal use. */
    private float mouseX, mouseY;
    private static Vector2 vector2 = new Vector2();
    private Color transparent = new Color(0,0,0,0);


    public PieMenu(final ShapeDrawer sd, PieMenuStyle style) {
        super(sd, style);
        setStyle(style);

        dragListener = new MyDragListener(style.radius);
        dragListener.setTapSquareSize(0);

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
    @Override
    public void attachToActor(Actor attachedTo) {
        this.attachedTo = attachedTo;
        attachedTo.addListener(dragListener);
    }

    private Vector2 getStageBottomLeftCoordinates(Actor actor) {
        return actor.localToStageCoordinates(new Vector2());
    }

    private void updateMousePosition(float x, float y) {
        vector2 = getStageBottomLeftCoordinates(attachedTo);
        mouseX = x + vector2.x;
        mouseY = y + vector2.y;
    }

    /**
     * @return a non-normalized angle of the position of the cursor relative to the origin of the widget
     */
    private float angleAtMouse() {
        return MathUtils.radiansToDegrees * MathUtils.atan2(mouseY - getY(), mouseX - getX());
    }

    private void resetSelection() {
        highlightedIndex = -1;
        selectedIndex = -1;
    }

    /**
     * Given the angle, find the index of the child.
     *
     * @param angle angle in degrees relative to the origin
     * @return the index of the child whose region encompasses the angle
     */
    private int findRegionAtMouse(float angle) {
        angle = ((angle - style.startDegreesOffset) % 360 + 360) % 360;
        return MathUtils.floor(angle / style.totalDegreesDrawn * getChildren().size);
    }

    /**
     * Selects the child at the given index. Triggers the ChangeListener.
     *
     * @param newIndex index of the child (and thus region) which was selected.
     */
    private void selectIndex(int newIndex) {
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
    private void hoverIndex(int newIndex, boolean hoverIsSelection) {
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
            sd.sector(getX(), getY(), style.radius, tmpOffset, bgRadian);
        }

        /* Children */
        vector2.set(getX(), getY());
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
            if(button != 0) // only-left clicks should activate that   todo: let user decide which buttons
                return true;

            if(resetSelectionOnAppear)
                resetSelection();
            setVisible(true);
            updateMousePosition(x, y);
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if(button != 0) // only-left clicks should activate that   todo: let user decide which buttons
                return;

            selectIndex(findRegionAtMouse(angleAtMouse())); // todo: just use highlighted instead of finding index again?
            setVisible(false);
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if(!isVisible())
                return;

            updateMousePosition(x, y);
            hoverIndex(findRegionAtMouse(angleAtMouse()), hoverIsSelection);
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
     */


    public boolean isInfiniteDragRange() {
        return infiniteDragRange;
    }
    public void setInfiniteDragRange(boolean infiniteDragRange) {
        this.infiniteDragRange = infiniteDragRange;
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
}

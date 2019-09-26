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
    public int selectedIndex = -1; // index of the currently selected item
    public int highlightedIndex = -1; // index of the currently highlighted item

    private boolean infiniteDragRange = true; // should gestures select if drag finishes outside of radius
    private boolean hoverIsSelection = false; // if hovering an item calls ChangeListener and selects the item
    private boolean resetSelectionOnAppear = true; // when redrawing the widget, should it still select the last selected item?
    private float mouseX, mouseY;
    private PieMenuStyle style;
    private HoverChangeListener hoverChangeListener;


    public PieMenu(final ShapeDrawer sd, PieMenuStyle style) {
        super(sd, style);
        setStyle(style);

        dragListener = new MyDragListener(style.radius);
        dragListener.setTapSquareSize(0);

        setVisible(false);
    }


    public PieMenuStyle getStyle() {
        return style;
    }

    public void setStyle(PieMenuStyle style) {
        checkStyle(style);
        this.style = style;
        invalidate();
    }

    protected void checkStyle(PieMenuStyle style) {


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
        Vector2 pos = getStageBottomLeftCoordinates(attachedTo);
        mouseX = x + pos.x;
        mouseY = y + pos.y;
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


    @Override // todo: optimize by including the "vector2" attributes
    protected void drawWithShapeDrawer(Batch batch, float parentAlpha) {

        /* Background */
        sd.setColor(style.backgroundColor);
        float bgRadian = MathUtils.degreesToRadians*style.totalDegreesDrawn;
        float tmpOffset = MathUtils.degreesToRadians*style.startDegreesOffset;
        sd.sector(getX(), getY(), style.radius, tmpOffset, bgRadian);

        /* Children */
        Vector2 center = new Vector2(getX(), getY());
        float tmpRad = bgRadian / getChildren().size;
        for(int i=0; i<getChildren().size; i++) {
            float tmp = tmpOffset + i*tmpRad;
            sd.setColor(i== highlightedIndex ? Color.MAGENTA : i%2 == 0 ? Color.CHARTREUSE : Color.FIREBRICK);
            sd.arc(center.x, center.y, style.radius, tmp, tmpRad, style.radius-style.innerRadius);
            sd.line(pointAtAngle(center, style.innerRadius, tmp),
                    pointAtAngle(center, style.radius, tmp),
                    Color.PINK, 3);
        }

        /* The remaining last line to be drawn */
        sd.line(pointAtAngle(center, style.innerRadius, tmpOffset + getChildren().size*tmpRad),
                pointAtAngle(center, style.radius, tmpOffset + getChildren().size*tmpRad),
                Color.PINK, 3);

        /* DEBUG */
        sd.setColor(Color.BLACK);
        sd.circle(mouseX, mouseY, 7, 3);
    }

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
            if(resetSelectionOnAppear)
                resetSelection();
            setVisible(true);
            updateMousePosition(x, y);
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            selectIndex(findRegionAtMouse(angleAtMouse())); // todo: just use highlighted instead of finding index again?
            setVisible(false);
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            updateMousePosition(x, y);
            hoverIndex(findRegionAtMouse(angleAtMouse()), hoverIsSelection);
            super.touchDragged(event, x, y, pointer);
        }
    }





    public static class PieMenuStyle extends RadialGroupStyle {
        public Color selectedColor;

        public PieMenuStyle() {
        }

        public PieMenuStyle(PieMenu.PieMenuStyle style) {
            super(style);
            this.selectedColor = style.selectedColor;
        }
    }



    public interface HoverChangeListener {
        void onHoverChange();
    }
}

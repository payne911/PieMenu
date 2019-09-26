package com.payne.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class PieMenu extends RadialWidget {
    public MyDragListener dragListener;
    public int selectedIndex = -1; // index of the selected item

    private boolean infiniteDragRange = true; // should gestures select if drag finishes outside of radius
    private float mouseX, mouseY;
    private PieMenuStyle style;


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
        if(style.innerRadius < 0)
            throw new IllegalArgumentException("innerRadius cannot be negative.");
        if(style.innerRadius >= style.radius)
            throw new IllegalArgumentException("innerRadius must be smaller than the radius.");

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
        selectedIndex = -1;
    }

    private void childIndexAtMouse(float angle) {
        angle = ((angle - style.startDegreesOffset) % 360 + 360) % 360;
        selectedIndex = (int)Math.floor(angle / style.totalDegreesDrawn * getChildren().size);
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
            sd.setColor(i==selectedIndex ? Color.MAGENTA : i%2 == 0 ? Color.CHARTREUSE : Color.FIREBRICK);
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
            updatePosition(); // todo: shouldn't be there
            setVisible(true);
            updateMousePosition(x, y);
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            setVisible(false);
            resetSelection();
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            updateMousePosition(x, y);
            childIndexAtMouse(angleAtMouse());
            super.touchDragged(event, x, y, pointer);
        }
    }





    public static class PieMenuStyle extends RadialWidgetStyle {
        public Color selectedColor;

        public PieMenuStyle() {
        }

        public PieMenuStyle(PieMenu.PieMenuStyle style) {
            super(style);
            this.selectedColor = style.selectedColor;
        }
    }
}

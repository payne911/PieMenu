package com.payne.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.PolygonShapeDrawer;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class PieMenu extends RadialWidget {
    private PolygonShapeDrawer sd;
    private ShapeDrawerDrawable sdd;
    private Actor attachedTo;
    private Array<Actor> items = new Array<>();
    public MyDragListener dragListener;
    public boolean infiniteDragRange = true; // should gestures select if drag finishes outside of radius
    private float fullRadius = 100; // how far the circle extends
    private float innerRadius = 0; // the inner radius wouldn't trigger an option if dragged within it
    public float angleOffset = 0; // how far from origin should the drawing begin
    public float angleDrawn = 360; // from horizontal, toward up : how much of the whole circle to draw
    public int selectedIndex = -1; // index of the selected item

    private float mouseX, mouseY;


    public PieMenu(final PolygonShapeDrawer sd) {
        this.sd = sd;

        dragListener = new MyDragListener(fullRadius);
        dragListener.setTapSquareSize(0);

        sdd = new ShapeDrawerDrawable(sd) {
            @Override
            public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {

                /* Background */
                sd.setColor(Color.WHITE);
                float bgRadian = MathUtils.degreesToRadians*angleDrawn;
                float tmpOffset = MathUtils.degreesToRadians*angleOffset;
                sd.sector(x, y, fullRadius, tmpOffset, bgRadian);

                /* Children */
                Vector2 center = new Vector2(x, y);
                float tmpRad = bgRadian / items.size;
                for(int i=0; i<items.size; i++) {
                    float tmp = tmpOffset + i*tmpRad;
                    sd.setColor(i==selectedIndex ? Color.MAGENTA : i%2 == 0 ? Color.CHARTREUSE : Color.FIREBRICK);
                    sd.arc(center.x, center.y, fullRadius, tmp, tmpRad, fullRadius-innerRadius);
                    sd.line(pointAtAngle(center, innerRadius, tmp),
                            pointAtAngle(center, fullRadius, tmp),
                            Color.PINK, 3);
                }

                /* The remaining last line to be drawn */
                sd.line(pointAtAngle(center, innerRadius, tmpOffset + items.size*tmpRad),
                        pointAtAngle(center, fullRadius, tmpOffset + items.size*tmpRad),
                        Color.PINK, 3);

                /* DEBUG */
                sd.setColor(Color.BLACK);
                sd.circle(mouseX, mouseY, 7, 3);
            }
        };


        final RadialWidget.RadialWidgetStyle style = new RadialWidget.RadialWidgetStyle();
//        style.background = skin.getDrawable("round-gray");
        style.background = sdd;
        style.radius = fullRadius;
        setStyle(style);

        setVisible(false);
    }


    /**
     * To get the coordinates of a point along a line traced from the center,
     * along the designated angle, at the designated distance from the center.
     *
     * @param center center point of the Circle
     * @param radius how far from the center the desired point is
     * @param radian the angle along which the line is calculated
     * @return the point associated with those parameters
     */
    private Vector2 pointAtAngle(Vector2 center, float radius, float radian) {
        return new Vector2(
                center.x + radius * MathUtils.cos(radian),
                center.y + radius * MathUtils.sin(radian));
    }

    public void addItem(Actor actor) { // todo: fix so that Actors are properly aligned
        items.add(actor);
//        super.addActor(actor);
//        invalidate();
    }

    public float getFullRadius() {
        return fullRadius;
    }

    public void setFullRadius(float fullRadius) { // todo: add restrictions (range [0,360])
        this.fullRadius = fullRadius;
        dragListener.maxRange = fullRadius;
    }

    public float getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(float innerRadius) { // todo: add restrictions (range [0,360])
        this.innerRadius = innerRadius;
        dragListener.setTapSquareSize(innerRadius);
    }


    /**
     * Designates an Actor on which the PieMenu will be centered.
     *
     * @param attachedTo an Actor
     */
    public void attachToActor(Actor attachedTo) {
        this.attachedTo = attachedTo;
        attachedTo.addListener(dragListener);
    }

    /**
     * Positions the widget right at the middle of its attached Actor.
     */
    private void updatePosition() { // todo: option for offsets
        Vector2 position = getStageMiddleCoordinates(attachedTo);
        setPosition(position.x, position.y);
    }

    private Vector2 getStageMiddleCoordinates(Actor actor) {
        return actor.localToStageCoordinates(new Vector2(
                actor.getWidth()/2,
                actor.getHeight()/2));
    }

    private Vector2 getStageBottomLeftCoordinates(Actor actor) {
        return actor.localToStageCoordinates(new Vector2());
    }

    private void updateMousePosition(float x, float y) {
        Vector2 pos = getStageBottomLeftCoordinates(attachedTo);
        mouseX = x + pos.x;
        mouseY = y + pos.y;
    }

    private double angleAtMouse() {
        double angle = MathUtils.radiansToDegrees * MathUtils.atan2(mouseY - getY(), mouseX - getX());
        return (angle + 360) % 360;
    }

    private void childIndexAtMouse(double angle) { // todo: adjust for offset angles!
        float totalDegrees = angleDrawn - angleOffset;
        float sliceAngle = totalDegrees / items.size;
        System.out.println("total: " + totalDegrees + " | size: " + items.size + " | degrees per slice: " + sliceAngle);

        int tmpIndex = 0;
        while(angle > sliceAngle) {
            angle -= sliceAngle;
            tmpIndex++;
        }

        if(tmpIndex > items.size-1)
            selectedIndex = -1;
        else
            selectedIndex = tmpIndex;

        System.out.println(selectedIndex);
    }





    public class MyDragListener extends DragListener {
        public float maxRange;


        public MyDragListener(float maxRange) {
            this.maxRange = maxRange;
        }


        /**
         * @param x x-coordinate in pixels, relative to the bottom left of the attached actor
         * @param y y-coordinate in pixels, relative to the bottom left of the attached actor
         */
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            updatePosition();
            setVisible(true);
            updateMousePosition(x, y);
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            setVisible(false);
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            updateMousePosition(x, y);
            double angle = angleAtMouse();
            childIndexAtMouse(angle);
            super.touchDragged(event, x, y, pointer);
        }
    }
}

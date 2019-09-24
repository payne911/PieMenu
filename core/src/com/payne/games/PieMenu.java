package com.payne.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.PolygonShapeDrawer;


public class PieMenu {
    private PolygonShapeDrawer sd;
    private Array<PieItem> items = new Array<>();
    public boolean draw = false;
    public float radius = 70;
    public float angleOffset = 0;
    public float angleDrawn = 360; // from horizontal, toward up : how much of the whole circle to draw
    public float x, y;


    public PieMenu(PolygonShapeDrawer sd) {
        this.sd = sd;
    }


    public void addItem(PieItem item) {
        items.add(item);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {

        if(!draw)
            return;

        /* Background */
        sd.setColor(Color.WHITE);
        float bgRadian = MathUtils.degreesToRadians*angleDrawn;
        float tmpOffset = MathUtils.degreesToRadians*angleOffset;
        sd.sector(x, y, radius, tmpOffset, bgRadian);

        /* Children */
        float tmpRad = bgRadian / items.size;
        for(int i=0; i<items.size; i++) {
            sd.setColor(i%2 == 0 ? Color.CHARTREUSE : Color.FIREBRICK);
            sd.arc(x, y, radius, tmpOffset + i*tmpRad, tmpRad, 4);
        }
    }

}

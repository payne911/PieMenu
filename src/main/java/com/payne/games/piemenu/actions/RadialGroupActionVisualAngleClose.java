package com.payne.games.piemenu.actions;

import com.badlogic.gdx.math.Interpolation;
import com.payne.games.piemenu.RadialGroup;

public class RadialGroupActionVisualAngleClose extends RadialGroupActionVisualAngle {

    public RadialGroupActionVisualAngleClose() {
    }

    public RadialGroupActionVisualAngleClose(RadialGroup radialGroup, float duration) {
        super(radialGroup, 0f, duration);
    }

    public RadialGroupActionVisualAngleClose(RadialGroup radialGroup, float duration, Interpolation interpolation) {
        super(radialGroup, 0f, duration, interpolation);
    }

    @Override
    protected void begin() {
        super.begin();
        endAngle = 0f;
        radialGroup.incrementVisualActionCloseCount();
    }

    @Override
    protected void end() {
        super.end();
        radialGroup.setVisible(false);
        radialGroup.decrementVisualActionCloseCount();
    }
}
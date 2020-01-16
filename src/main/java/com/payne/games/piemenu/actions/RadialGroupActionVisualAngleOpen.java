package com.payne.games.piemenu.actions;

import com.badlogic.gdx.math.Interpolation;
import com.payne.games.piemenu.RadialGroup;

public class RadialGroupActionVisualAngleOpen extends RadialGroupActionVisualAngle {

    public RadialGroupActionVisualAngleOpen() {
    }

    public RadialGroupActionVisualAngleOpen(RadialGroup radialGroup, float duration) {
        super(radialGroup, 0f, duration);
    }

    public RadialGroupActionVisualAngleOpen(RadialGroup radialGroup, float duration,
            Interpolation interpolation) {
        super(radialGroup, 0f, duration, interpolation);
    }

    @Override
    protected void begin() {
        super.begin();
        radialGroup.setVisible(true);
        endAngle = radialGroup.getTotalDegreesDrawn();
        radialGroup.incrementVisualActionOpenCount();
    }

    @Override
    protected void end() {
        super.end();
        radialGroup.decrementVisualActionOpenCount();
    }
}
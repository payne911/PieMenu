package com.payne.games.piemenu.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.payne.games.piemenu.RadialGroup;

public class RadialGroupActionVisualAngle extends AbsoluteTemporalAction {

    protected RadialGroup radialGroup;
    protected float startAngle;
    protected float endAngle;

    public RadialGroup getRadialGroup() {
        return radialGroup;
    }

    public void setRadialGroup(RadialGroup radialGroup) {
        this.radialGroup = radialGroup;
    }

    public float getAngle() {
        return endAngle;
    }

    public void setAngle(float angle) {
        endAngle = angle;
    }

    public RadialGroupActionVisualAngle() {
    }

    public RadialGroupActionVisualAngle(RadialGroup radialGroup, float angle, float duration) {
        super(duration);
        this.radialGroup = radialGroup;
        this.endAngle = angle;
    }

    public RadialGroupActionVisualAngle(RadialGroup radialGroup, float angle, float duration,
            Interpolation interpolation) {
        super(duration, interpolation);
        this.radialGroup = radialGroup;
        this.endAngle = angle;
    }

    @Override
    protected float getRelativeRatio() {
        if (radialGroup.getTotalDegreesDrawn() == 0) {
            return 0;
        } else {
            return Math.abs(endAngle - startAngle) / radialGroup.getTotalDegreesDrawn();
        }
    }

    @Override
    protected void begin() {
        startAngle = radialGroup.getVisualAngle();
    }

    @Override
    protected void update(float percent) {
        radialGroup.setVisualAngle(MathUtils.clamp(startAngle +
                (endAngle - startAngle) * percent, 0f, radialGroup.getTotalDegreesDrawn()));
    }
}
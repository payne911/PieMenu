package com.payne.games.piemenu.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.payne.games.piemenu.RadialGroup;
import java.util.HashMap;

public class RadialGroupActionColorBasic extends ConditionalAction {
    protected RadialGroup radialGroup;
    private HashMap<Actor, Color> originalColors = new HashMap<>();

    public RadialGroup getRadialGroup() {
        return radialGroup;
    }

    public void setRadialGroup(RadialGroup radialGroup) {
        this.radialGroup = radialGroup;
    }

    public RadialGroupActionColorBasic() {
    }

    public RadialGroupActionColorBasic(RadialGroup radialGroup) {
        this.radialGroup = radialGroup;
    }

    @Override
    protected void begin() {
    }

    @Override
    protected void end() {
        /* Restoring the colors' state. */
        for (int i = 0; i < radialGroup.getAmountOfChildren(); i++) {
            Actor localActor = radialGroup.getChildren().get(i);
            Color localColor = originalColors.get(localActor);
            if (localColor != null) {
                localActor.setColor(localColor);
            }
        }
        originalColors.clear();
    }

    protected boolean update() {
        float localOpeningPercentage = radialGroup.getVisualAnglePercent();

        for (int i = 0; i < radialGroup.getAmountOfChildren(); i++) {
            Actor localActor = radialGroup.getChildren().get(i);
            /* Updating alpha (fade-in animation). */
            if (!originalColors.containsKey(localActor)) {
                originalColors.put(localActor, new Color(localActor.getColor()));
            }
            Color localColor = originalColors.get(localActor);
            localActor.setColor(localColor.r, localColor.g, localColor.b,
                    localColor.a * localOpeningPercentage);
        }

        if (radialGroup.getVisualActionOpenCount() + radialGroup.getVisualActionCloseCount() > 0) {
            return radialGroup.getTotalDegreesDrawn() == radialGroup.getVisualAngle();
        }
        return true;
    }

    @Override
    public void restart() {
        super.restart();
        end();
    }
}
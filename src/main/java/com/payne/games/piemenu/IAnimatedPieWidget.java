package com.payne.games.piemenu;

import com.badlogic.gdx.graphics.g2d.Batch;


/**
 * See {@link IAnimatedRadialGroup}.
 */
interface IAnimatedPieWidget extends IAnimatedRadialGroup {

    @Override
    PieWidget getSelf();

    /**
     * Enforces an order of execution for the drawing of the widget.
     */
    default void myDraw(Batch batch, float parentAlpha) {
        getSelf().drawWithShapeDrawer(batch, parentAlpha, getCurrentAngle());
        getSelf().drawMe(batch, parentAlpha);
    }
}

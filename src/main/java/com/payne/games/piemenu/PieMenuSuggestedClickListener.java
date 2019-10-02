package com.payne.games.piemenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


/**
 * The suggested {@link ClickListener} that comes with the {@link PieMenu}. You
 * are not obligated to use it, but this one has been designed to work as is,
 * for the most part.
 */
public class PieMenuSuggestedClickListener extends ClickListener {

    /**
     * The suggested ClickListener that comes with the {@link PieMenu}. You are not
     * obligated to use it, but this one has been designed to work as is, for
     * the most part.
     */
    public PieMenuSuggestedClickListener() {
        setTapSquareSize(Integer.MAX_VALUE);
    }

    /**
     * @param x x-coordinate in pixels, relative to the bottom left of the attached actor
     * @param y y-coordinate in pixels, relative to the bottom left of the attached actor
     */
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(!(event.getListenerActor() instanceof PieMenu))
            return false;
        PieMenu pie = (PieMenu)event.getListenerActor();

        return button == pie.getSelectionButton();
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if(!(event.getListenerActor() instanceof PieMenu))
            return;
        PieMenu pie = (PieMenu)event.getListenerActor();

        if(button != pie.getSelectionButton())
            return;
        pie.selectChildRegionAtStage(event.getStageX(), event.getStageY()); // todo: just use highlighted instead of finding index again?
        super.touchUp(event, x, y, pointer, button);
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if(!(event.getListenerActor() instanceof PieMenu))
            return;
        PieMenu pie = (PieMenu)event.getListenerActor();

        pie.highlightChildRegionAtStage(event.getStageX(), event.getStageY());
        super.touchDragged(event, x, y, pointer);
    }
}

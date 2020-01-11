package com.payne.games.piemenu.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Pool;

public abstract class ConditionalAction extends Action {
    private boolean began, complete;

    public ConditionalAction() {
    }

    public boolean act(float delta) {
        if (complete) return true;
        Pool pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool while executing.
        try {
            if (!began) {
                begin();
                began = true;
            }
            complete = update();

            if (complete) end();
            return complete;
        } finally {
            setPool(pool);
        }
    }

    /**
     * Called the first time {@link #act(float)} is called. This is a good place to query the
     * {@link #actor actor's} starting state.
     */
    protected void begin() {
    }

    /**
     * Called the last time {@link #act(float)} is called.
     */
    protected void end() {
    }

    /**
     * @return {@code true} if the action is done. This method will not be called after the action
     * is done.
     */
    protected abstract boolean update();

    @Override
    public void restart() {
        began = false;
        complete = false;
    }

    /**
     * @return {@code true} after {@link #act(float)} has been called where time >= duration.
     */
    public boolean isComplete() {
        return complete;
    }
}
package com.uchump.prime.Core.System.IO;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;
import com.uchump.prime.Core.System.Event.iEventHandler;

public interface GdxAdapter extends InputProcessor, iEventHandler, Disposable {

	@Override
	public default boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public default boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public default boolean keyTyped(char character) {
		return false;
	}

	@Override
	public default boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public default boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public default boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public default boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public default boolean scrolled(float amountX, float amountY) {
		return false;
	}
	
	@Override
	public default void dispose () {
		
	}

}

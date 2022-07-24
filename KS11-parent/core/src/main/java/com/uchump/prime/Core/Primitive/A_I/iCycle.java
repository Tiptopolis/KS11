package com.uchump.prime.Core.Primitive.A_I;

import java.io.IOException;

import com.uchump.prime.Core.System.Event.iHandler;

public interface iCycle {

	public void next() throws IOException;

	public default void resume() {

	}

	public default void pause() {

	};

	public default void terminate() {

	};
}

package utils;

import physics.Body;
import statemachine.SimpleStateMachine;

public class Helper {
	public static void changeStateRelToDir(SimpleStateMachine sm, boolean goingLeft, int to) {
		to *= 2;
		if(!goingLeft) {
			to += 1;
		}
		
		sm.changeState(to);
	}
}

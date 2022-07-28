package statemachine;

import com.danceEngine.action.Action;
import com.danceEngine.ecs.EComponent;
import com.danceEngine.ecs.Entity;

public class SimpleStateMachine extends EComponent {
	public State[] states;
	public State current = null;
	
	public SimpleStateMachine(State...states) {
		this.states = states;
		changeState(0);
	}
	
	public SimpleStateMachine(Entity e, Action... actions) {
		this.states = new State[actions.length];
		for(int i = 0; i < states.length; ++i) {
			states[i] = new ActionState(e, actions[i]);
		}
		
		changeState(0);
	}
	
	public void changeState(int id) {
		if(states[id] == current) return;
		if(current != null) current.exit();
		current = states[id];
		current.enter();
	}
}

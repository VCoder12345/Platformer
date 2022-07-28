package statemachine;

import com.danceEngine.action.Action;
import com.danceEngine.ecs.Entity;

public class ActionState extends State {
	public Action action;
	public Entity entity;
	
	
	
	public ActionState(Entity entity, Action action) {
		super();
		this.entity = entity;
		this.action = action;
	}

	@Override
	public void enter() {
		entity.addAction(action);
	}
	
	@Override
	public void exit() {
		action.reset();
		entity.removeAllActions();
	}
}

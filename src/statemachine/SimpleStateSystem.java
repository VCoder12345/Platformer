package statemachine;

import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;

public class SimpleStateSystem extends ESystem {
	@Override
	public void update(float dt) {
		for(Entity e : getEntitiesWithTypes(SimpleStateMachine.class)) {
			var ssm = e.getComponentByType(SimpleStateMachine.class);
			ssm.current.update(dt);
		}
	}
}

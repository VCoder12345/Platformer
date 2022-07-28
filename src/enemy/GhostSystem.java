package enemy;

import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;
import com.danceEngine.event.EventSystem;

import physics.Body;
import statemachine.SimpleStateMachine;
import traps.KilledEvent;
import utils.Helper;

public class GhostSystem extends ESystem {
	
	@Override
	public void start() {
		EventSystem.addEntityTypeListener(KilledEvent.class, this::onKilled, Ghost.class);
	}

	@Override
	public void update(float dt) {
		for (Entity e : getEntitiesWithTypes(Ghost.class, Body.class, Transform.class)) {
			Ghost g = e.getComponentByType(Ghost.class);
			Body b = e.getComponentByType(Body.class);
			Transform t = e.getComponentByType(Transform.class);
			SimpleStateMachine sm = e.getComponentByType(SimpleStateMachine.class);
			
			if(g.left) {
				g.left = !(t.position.x < g.limitLeft.x);
			}else {
				g.left = t.position.x > g.limitRight.x;
			}
			
			if(g.left) {
				b.velocity.x = -g.speed;
			}else {
				b.velocity.x = g.speed;
			}
			Helper.changeStateRelToDir(sm, g.left, 0);
		}
	}
	
	public void onKilled(KilledEvent ev) {
		Entity e = ev.entity;
		Ghost g = e.getComponentByType(Ghost.class);
		g.enabled = false;
		SimpleStateMachine sm = e.getComponentByType(SimpleStateMachine.class);
		Helper.changeStateRelToDir(sm, g.left, 1);
	}

}

package enemy;

import com.danceEngine.action.ChangeImage;
import com.danceEngine.action.RunAction;
import com.danceEngine.action.SequenceAction;
import com.danceEngine.action.WaitAction;
import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.event.EventSystem;
import com.danceEngine.resources.ResourceManager;

import game.LooseEvent;
import physics.Body;
import physics.Trigger;
import physics.TriggerEvent;
import statemachine.SimpleStateMachine;
import traps.KilledEvent;
import utils.Helper;

public class SlimeSystem extends ESystem {
	@Override
	public void start() {
		EventSystem.addEntityTypeListener(KilledEvent.class, this::onKilled, Slime.class);
	}

	@Override
	public void update(float dt) {
		for (Entity e : getEntitiesWithTypes(Slime.class, Body.class)) {
			Slime s = e.getComponentByType(Slime.class);
			Body b = e.getComponentByType(Body.class);
			SimpleStateMachine sm = e.getComponentByType(SimpleStateMachine.class);
			
			if(b.onGround) {
				b.velocity.x = 0;
				if(System.currentTimeMillis() - s.timer > s.intervall) {
					s.timer = System.currentTimeMillis();
				
					b.velocity.y = -s.jumpVelocity;
				}
			}else {
				s.timer = System.currentTimeMillis();
				b.velocity.x = s.forwardVelocity;
				//s.left = b.velocity.x < 0;
				Helper.changeStateRelToDir(sm, s.left, 0);
			}

			
		}
	}
	

	

	
	public void onKilled(KilledEvent ev) {
		Entity e = ev.entity;
		Slime sl = e.getComponentByType(Slime.class);
		sl.enabled = false;
		SimpleStateMachine sm = e.getComponentByType(SimpleStateMachine.class);
		Helper.changeStateRelToDir(sm, sl.left, 1);
	}

}

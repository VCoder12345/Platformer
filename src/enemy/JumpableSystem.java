package enemy;

import com.danceEngine.ecs.ESystem;
import com.danceEngine.event.EventSystem;

import physics.Body;
import physics.Trigger;
import physics.TriggerEvent;
import traps.KilledEvent;

public class JumpableSystem extends ESystem {
	
	@Override
	public void start() {
		EventSystem.addEntityTypeListener(TriggerEvent.class, this::onTrigger, Jumpable.class);
	}
	public void onTrigger(TriggerEvent ev) {
		Trigger tr = ev.entity.getComponentByType(Trigger.class);
		tr.enabled = false;
		System.out.println(ev.dir);
		if(ev.dir  == 1) {
			Body oBody = ev.oEntity.getComponentByType(Body.class);
			oBody.velocity.y = -16;
			EventSystem.submit(new KilledEvent(ev.entity));
		}else {
			EventSystem.submit(new KilledEvent(ev.oEntity));
		}
	}
}

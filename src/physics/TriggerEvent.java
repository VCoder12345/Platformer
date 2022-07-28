package physics;

import com.danceEngine.ecs.Entity;
import com.danceEngine.event.EntityEvent;

public class TriggerEvent extends EntityEvent {
	public Entity oEntity;
	public int dir;
	public TriggerEvent(Entity trigger, Entity oEntity, int dir) {
		super(trigger);
		this.oEntity = oEntity;
		this.dir = dir;
	}
	
}

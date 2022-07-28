package physics;

import com.danceEngine.ecs.Entity;
import com.danceEngine.event.EntityEvent;

public class CollisionEvent extends EntityEvent {
	public Entity oEntity;
	public int dir;

	public CollisionEvent(Entity entity, Entity oEntity, int dir) {
		super(entity);
		this.oEntity = oEntity;
		this.dir = dir;
		
	}

}

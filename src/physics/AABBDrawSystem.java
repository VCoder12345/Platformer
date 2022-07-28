package physics;

import java.awt.Color;

import com.danceEngine.debug.Debug;
import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;

public class AABBDrawSystem extends ESystem {
	
	@Override
	public void update(float dt) {
		for(Entity e : getEntitiesWithTypes(ShapeComponent.class, Transform.class)) {
			ShapeComponent sc = e.getComponentByType(ShapeComponent.class);
			Transform t = e.getComponentByType(Transform.class);
			
			Debug.drawRect(sc.aabb.getMin(t), sc.aabb.size, 0, Color.green, true, 0.01f);
		}
	}
}

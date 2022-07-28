package physics;

import com.danceEngine.ecs.EComponent;
import com.danceEngine.physics.AABB;

public class ShapeComponent extends EComponent {
	public AABB aabb;

	public ShapeComponent(AABB aabb) {
		super();
		this.aabb = aabb;
	}
	
	
}

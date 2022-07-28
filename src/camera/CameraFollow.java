package camera;

import com.danceEngine.ecs.EComponent;
import com.danceEngine.ecs.Entity;

public class CameraFollow extends EComponent {
	public Entity target;
	public float smoothSpeed;

	public CameraFollow(Entity target, float smoothSpeed) {
		super();
		this.target = target;
		this.smoothSpeed = smoothSpeed;
	}
	
	
}

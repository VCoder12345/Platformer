package camera;

import com.danceEngine.ecs.EComponent;
import com.danceEngine.ecs.Entity;

public class CameraFollow extends EComponent {
	public Entity target;
	public float smoothSpeed;
	public float maxFrontLook;
	public float frontLook;

	public CameraFollow(Entity target, float smoothSpeed, float maxFrontLook) {
		super();
		this.target = target;
		this.smoothSpeed = smoothSpeed;
		this.maxFrontLook = maxFrontLook;
	}
	
	
}

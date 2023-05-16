package physics;

import com.danceEngine.ecs.Entity;
import com.danceEngine.scene.Scene;

import tiles.Map;

public class Physics {
	public static final float gravity = 0.8f;
	public static Map map;
	public static Entity gameMaster;
	public static Scene checkpointState = null;
	
	public static float velocityForJumpHeight(float jumpHeight) {
		return (float) Math.sqrt(2 * jumpHeight * gravity);
	}
}

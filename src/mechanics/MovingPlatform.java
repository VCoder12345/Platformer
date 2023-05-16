package mechanics;

import java.util.ArrayList;

import com.danceEngine.ecs.EComponent;
import com.danceEngine.ecs.Entity;
import com.danceEngine.utils.Vector2;

public class MovingPlatform extends EComponent {
	public Vector2 start, end;
	public boolean movingHorizontal;
	public float speed;
	public int dir = 1;
	public ArrayList<Entity> load = new ArrayList<>();
	
	public MovingPlatform(float speed, Vector2 start, Vector2 end, boolean movingHorizontal) {
		super();
		this.speed = speed;
		this.start = start;
		this.end = end;
		this.movingHorizontal = movingHorizontal;
	}
	
	
}

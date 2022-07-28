package enemy;

import com.danceEngine.ecs.EComponent;
import com.danceEngine.utils.Vector2;

public class Ghost extends EComponent {
	public float speed;
	public Vector2 limitLeft, limitRight;
	public boolean left;
	public boolean pause = false;

	public Ghost(float speed, Vector2 limit1, Vector2 limit2) {
		super();
		this.speed = speed;
		if(limit1.x < limit2.x) {
			left = false;
			limitLeft = limit1;
			limitRight = limit2;
		}else {
			left = true;
			limitLeft = limit2;
			limitRight = limit1;
		}
	}
	
	
}

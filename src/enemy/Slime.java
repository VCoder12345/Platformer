package enemy;

import com.danceEngine.action.Action;
import com.danceEngine.ecs.EComponent;

import physics.Physics;

public class Slime extends EComponent {
	public int intervall;
	public long timer;
	public float jumpVelocity, forwardVelocity;
	public boolean left;
	
	public Slime(float jumpHeight, float forwardVelocity, int intervall, boolean left) {
		this.intervall = intervall;
		this.jumpVelocity = Physics.velocityForJumpHeight(jumpHeight);
		this.forwardVelocity = forwardVelocity;
		this.left = left;
		
		if(left) {
			this.forwardVelocity = -forwardVelocity;
		}
	}
}

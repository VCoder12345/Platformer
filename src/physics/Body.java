package physics;

import com.danceEngine.ecs.EComponent;
import com.danceEngine.utils.Vector2;

import game.GameTag;
import tiles.Tile;

public class Body extends EComponent {
	public float mass;
	public Vector2 velocity = Vector2.zero();
	public Vector2 acceleration = Vector2.zero();
	public float bounceFactor = 0.1f;
	public boolean onGround = false;
	public Tile groundTile;
	public int collisionTag = GameTag.NOTHING;
	
	
	public Body(float mass) {
		super();
		this.mass = mass;
	}
	
	public Body() {
		this(1.0f);
	}


	public void addForce(Vector2 force) {
		acceleration.addE(force.div(mass));
	}
}

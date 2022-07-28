package player;

import com.danceEngine.ecs.EComponent;

import physics.Physics;
import statemachine.SimpleStateMachine;

public class PlayerController extends EComponent {
	public float jumpImpuls;
	public float moveAcceleration;
	public float maxSpeed;
	public SimpleStateMachine statemachine;
	public boolean jumpBtnPressed = false;
	
	
	public PlayerController(float jumpHeight, float moveAcceleration, float maxSpeed, SimpleStateMachine statemachine) {
		setJumpHeight(jumpHeight);
		this.moveAcceleration = moveAcceleration;
		this.maxSpeed = maxSpeed;
		this.statemachine = statemachine;
	}
	
	public void setJumpHeight(float jumpHeight) {
		this.jumpImpuls = Physics.velocityForJumpHeight(jumpHeight);
	}
}

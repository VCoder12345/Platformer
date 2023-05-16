package player;

import com.danceEngine.action.MoveByAction;
import com.danceEngine.action.RunAction;
import com.danceEngine.action.SequenceAction;
import com.danceEngine.action.WaitAction;
import com.danceEngine.ai.behaviourTree.Sequence;
import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;
import com.danceEngine.event.EventSystem;
import com.danceEngine.game.Game;
import com.danceEngine.input.Input;
import com.danceEngine.resources.ResourceManager;
import com.danceEngine.transition.FadeToBlackTransition;
import com.danceEngine.utils.Vector2;

import game.GameTag;
import game.LooseEvent;
import physics.Body;
import physics.Physics;
import tiles.Map;
import tiles.Tile;
import traps.KilledEvent;

public class PlayerControlSystem extends ESystem {
	@Override
	public void start() {
		EventSystem.addEntityTagListener(KilledEvent.class, this::onKilled, GameTag.PLAYER);
	}
	
	
	public void onKilled(KilledEvent ev) {
		Entity e = ev.entity;
		Body b = e.getComponentByType(Body.class);
		PlayerController pc = e.getComponentByType(PlayerController.class);
		Transform t = e.getComponentByType(Transform.class);
		pc.enabled = false;
		b.enabled = false;
		pc.statemachine.changeState(5);
		e.addAction(new SequenceAction(new WaitAction(500), new RunAction(e, x -> EventSystem.submit(new LooseEvent()))));
	}

	@Override
	public void update(float dt) {
		for (Entity e : getEntitiesWithTypes(PlayerController.class, Body.class, Transform.class)) {
			PlayerController pc = e.getComponentByType(PlayerController.class);
			
			Body b = e.getComponentByType(Body.class);
			Transform t = e.getComponentByType(Transform.class);
			
			
			float movex = Input.getControlValue("movex");

			if(Math.abs(movex) > 0.2f) {
				b.acceleration.x += pc.moveAcceleration * movex;
				
			}
			
			
			if(Math.abs(b.velocity.x) > pc.maxSpeed) {
				b.velocity.x = Math.signum(b.velocity.x) * pc.maxSpeed;
			}
			
			if(b.onGround) {
				if(Math.abs(b.velocity.x) < 0.3f) {
					pc.statemachine.changeState(2);
				}else {
					if(b.velocity.x < 0) {
						pc.statemachine.changeState(0);
					}else{
						pc.statemachine.changeState(1);
					}
				}
			}else {
				if(b.velocity.x < -0.2f) {
					pc.statemachine.changeState(3);
				}else {
					pc.statemachine.changeState(4);
				}
				
			}
			
			
			
			//friction
			Vector2 friction = Vector2.zero();
			friction.x = -b.velocity.normalized().x;
			friction.x *= 0.5f;
			b.addForce(friction);
			
			
			if(Input.isControlDown("jump")) {
				if(b.onGround && !pc.jumpBtnPressed) {
					b.velocity.y = -pc.jumpImpuls;
					pc.jumpBtnPressed = true;
				}
			}else {
				pc.jumpBtnPressed = false;
			}
			
			
		}
	}

}

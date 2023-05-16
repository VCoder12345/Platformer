package mechanics;

import com.danceEngine.action.MoveByAction;
import com.danceEngine.action.MoveToAction;
import com.danceEngine.action.RunAction;
import com.danceEngine.action.ScaleTo;
import com.danceEngine.action.SequenceAction;
import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;
import com.danceEngine.event.EventSystem;
import com.danceEngine.game.Game;
import com.danceEngine.rendering.Renderer;
import com.danceEngine.resources.ResourceManager;
import com.danceEngine.transition.FadeToBlackTransition;
import com.danceEngine.utils.Vector2;

import physics.Body;
import physics.Trigger;
import physics.TriggerEvent;
import player.PlayerController;

public class DoorSystem extends ESystem {
	

	@Override
	public void start() {
		EventSystem.addEntityTypeListener(TriggerEvent.class, this::onTrigger, Door.class);
	}
	
	public void onTrigger(TriggerEvent ev) {
		
		Entity oe = ev.oEntity;
		PlayerController opc = oe.getComponentByType(PlayerController.class);
		Body ob = oe.getComponentByType(Body.class);
		
		if(!ob.onGround) return;
		Entity e = ev.entity;
		Trigger tr = e.getComponentByType(Trigger.class);
		Transform t = e.getComponentByType(Transform.class);
		tr.enabled = false;
		

		Transform ot = oe.getComponentByType(Transform.class);
		ob.velocity.x = 0;
		opc.enabled = false;
		ob.enabled = false;
		
		oe.addAction(new SequenceAction(new MoveToAction(oe, new Vector2(t.position.x + t.size.x / 2 - ot.size.x / 2, ot.position.y), 100),
				new RunAction(oe, x -> {opc.statemachine.changeState(6);})));
		
		
		
	}

}

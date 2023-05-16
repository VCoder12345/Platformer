package mechanics;

import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;
import com.danceEngine.event.EventSystem;

import physics.Body;
import physics.CollisionEvent;

public class MovingPlatformSystem extends ESystem {
	@Override
	public void start() {
		EventSystem.addEntityTypeListener(CollisionEvent.class, this::onCollision, MovingPlatform.class);
	}
	
	public void onCollision(CollisionEvent ev) {
		MovingPlatform mp = ev.entity.getComponentByType(MovingPlatform.class);
		if(!mp.load.contains(ev.oEntity)) {
			mp.load.add(ev.oEntity);
		}
		
	}
	
	@Override
	public void preUpdate(float dt) {
		for(Entity e: getAllEntitiesWithTypes(MovingPlatform.class)) {
			MovingPlatform mp = e.getComponentByType(MovingPlatform.class);
			Transform t = e.getComponentByType(Transform.class);
			
			float delta = mp.dir * mp.speed;
			if(mp.movingHorizontal) {
				t.position.x += delta * dt;
				
				
				
				if(mp.dir < 0) {
					if(t.position.x <= mp.start.x) {
						mp.dir = 1;
					}
				}else {
					if(t.position.x + t.size.x >= mp.end.x) {
						mp.dir = -1;
					}
				}
			}else {
				t.position.y += delta * dt;
				
				if(mp.dir < 0) {
					if(t.position.y <= mp.start.y) {
						mp.dir = 1;
					}
				}else {
					if(t.position.y + t.size.y >= mp.end.y) {
						mp.dir = -1;
					}
				}
			}
			
			//System.out.println(mp.load.size());
			mp.load.removeIf((oe) -> {
				Transform ot = oe.getComponentByType(Transform.class);
				return !oe.getComponentByType(Body.class).onGround || !(ot.position.x + ot.size.x > t.position.x && ot.position.x < t.position.x + t.size.x);
				});

			for(Entity oe : mp.load) {
				Transform ot = oe.getComponentByType(Transform.class);
				if(mp.movingHorizontal) {
					ot.position.x += delta * dt;
				}else {
					ot.position.y += delta * dt;
				}
				
			}
			
			
		}
	}
}

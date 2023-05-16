package camera;

import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;
import com.danceEngine.game.Game;
import com.danceEngine.utils.Utils;
import com.danceEngine.utils.Vector2;

import physics.Body;
import physics.Physics;
import tiles.Map;

public class CameraFollowSystem extends ESystem {
	@Override
	public void start() {
		for (Entity e : getEntitiesWithTypes(CameraFollow.class, Transform.class)) {
			CameraFollow cf = e.getComponentByType(CameraFollow.class);
			Transform t = e.getComponentByType(Transform.class);
			Transform targetT = cf.target.getComponentByType(Transform.class);
			
			
			Vector2 targetPos = targetT.position.add(targetT.size.div(2)).sub(new Vector2(Game.width, Game.height).div(2));
			t.position = targetPos;
		}
	}

	@Override
	public void update(float dt) {
		for (Entity e : getEntitiesWithTypes(CameraFollow.class, Transform.class)) {
			CameraFollow cf = e.getComponentByType(CameraFollow.class);
			Transform t = e.getComponentByType(Transform.class);
			Transform targetT = cf.target.getComponentByType(Transform.class);
			Body targetBody = cf.target.getComponentByType(Body.class);
			
			Vector2 lookahead = Vector2.zero();
			
			if(Math.abs(targetBody.velocity.x) > 0.2f) {
				lookahead.x = targetBody.velocity.x * cf.maxFrontLook;
			}
			Vector2 targetPos = targetT.position.add(lookahead).add(targetT.size.div(2)).sub(new Vector2(Game.width, Game.height).div(2));
			Vector2 oldPos = t.position.copy();
			t.position = Utils.lerp(t.position, targetPos, cf.smoothSpeed * dt);
			t.position = t.position.add(oldPos).div(2);
			//Map map = Physics.map;
			//t.position = Utils.clampf(t.position, Vector2.zero(), new Vector2(map.w, map.h).mul(map.size));
			
			if(targetBody.velocity.sqrLength() < 0.001f) {
				if(oldPos.sub(t.position).sqrLength() < 0.02f) {
					t.position = oldPos;
				}
			}

		}
	}

}

package camera;

import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;
import com.danceEngine.game.Game;
import com.danceEngine.utils.Utils;
import com.danceEngine.utils.Vector2;

public class CameraFollowSystem extends ESystem {
	

	@Override
	public void update(float dt) {
		for (Entity e : getEntitiesWithTypes(CameraFollow.class, Transform.class)) {
			CameraFollow cf = e.getComponentByType(CameraFollow.class);
			Transform t = e.getComponentByType(Transform.class);
			Transform targetT = cf.target.getComponentByType(Transform.class);
			
			Vector2 targetPos = targetT.position.add(targetT.size.div(2)).sub(new Vector2(Game.width, Game.height).div(2));
			
			//t.position = Utils.lerp(t.position, targetPos, cf.smoothSpeed * dt);
			t.position = targetPos;
			
		}
	}

}

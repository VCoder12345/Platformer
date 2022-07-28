package tiles;

import com.danceEngine.ecs.Entity;
import com.danceEngine.event.EntityEvent;

public class TileTriggerEvent extends EntityEvent {
	public Tile tile;
	public int dir;
	
	//dir = 0 --> up, dir = 1 -> down, dir = 2 -> left, dir = 3 -> right
	public TileTriggerEvent(Entity entity, Tile tile, int dir) {
		super(entity);
		this.tile = tile;
		this.dir = dir;
	}
	
}

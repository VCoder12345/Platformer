package game;

import com.danceEngine.ecs.EComponent;

public class GameMaster extends EComponent {
	public Level level;

	public GameMaster(Level level) {
		super();
		this.level = level;
	}
	
	
}

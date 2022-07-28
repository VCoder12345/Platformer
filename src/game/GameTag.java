package game;

import com.danceEngine.ecs.EntityTag;

public class GameTag extends EntityTag {
	public static final int NOTHING = 0;
	public static final int PLAYER = getUniqueId();
	public static final int SLIME = getUniqueId();
	public static final int GHOST = getUniqueId();
}

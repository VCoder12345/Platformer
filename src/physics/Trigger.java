package physics;

import com.danceEngine.ecs.EComponent;


public class Trigger extends EComponent {
	public int triggerMask;
	public boolean triggered = false;

	public Trigger(int triggerMask) {
		super();
		this.triggerMask = triggerMask;
	}
	
	
}

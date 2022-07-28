package player;

import com.danceEngine.action.Action;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;

public class StretchSquash implements Action{
	private float stretch;
	private Transform t;
	private float angle = 0;
	private float startSize;
	private boolean complete = false;
	
	

	public StretchSquash(float stretch, Entity e) {
		super();
		this.stretch = stretch;
		this.t = e.getComponentByType(Transform.class);
		this.startSize = t.size.y;
	}

	@Override
	public void execute(float dt) {
		float sizeChange = (float)Math.sin(angle) * stretch;
		//t.position.y -= sizeChange / 2;
		t.size.y = startSize + sizeChange;
		angle += dt * Math.PI / 10.0;
		
		if(angle > 2 * Math.PI) {
			complete = true;
		}
	}

	@Override
	public boolean isComplete() {
		return complete;
	}
	
	@Override
	public void end() {
		t.size.y = startSize;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

}

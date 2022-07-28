package game;

import java.awt.event.KeyEvent;
import java.io.IOException;

import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.event.EventSystem;
import com.danceEngine.game.Game;
import com.danceEngine.input.Input;
import com.danceEngine.input.KeyReleased;
import com.danceEngine.transition.FadeToBlackTransition;

import physics.Physics;

public class GameMasterSystem extends ESystem {
	
	@Override
	public void start() {
		EventSystem.addListener(KeyReleased.class, this::onKeyReleased);
		EventSystem.addListener(WinEvent.class, this::onWin);
		EventSystem.addListener(LooseEvent.class, this::onLoose);
	}
	
	@Override
	public void update(float dt) {
		if(Input.isControlDown("quit")) {
			Game.exit();
		}
	}
	
	public void onWin(WinEvent ev) {
		Game.reloadScene(new FadeToBlackTransition(120, 800));
	}
	
	public void onLoose(LooseEvent ev) {
		if(Physics.checkpointState != null) {
			
		}
		Game.loadScene(Game.currentSceneIndex, new FadeToBlackTransition(0, 800));
	}
	

	public void onKeyReleased(KeyReleased ev) {
		if(ev.key != KeyEvent.VK_F5) return;
		for (Entity e : getEntitiesWithTypes(GameMaster.class)) {
			GameMaster gm = e.getComponentByType(GameMaster.class);
			
			try {
				gm.level.completeRestart();
				Game.reloadScene();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}

package traps;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.danceEngine.action.MoveByAction;
import com.danceEngine.action.RunAction;
import com.danceEngine.action.SequenceAction;
import com.danceEngine.action.WaitAction;
import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;
import com.danceEngine.event.EventSystem;
import com.danceEngine.game.Game;
import com.danceEngine.resources.ResourceManager;
import com.danceEngine.time.Time;
import com.danceEngine.transition.FadeToBlackTransition;
import com.danceEngine.utils.Utils;
import com.danceEngine.utils.Vector2;

import game.GameTag;
import game.LooseEvent;
import physics.Body;
import physics.Physics;
import player.PlayerController;
import tiles.Tile;
import tiles.TileCollisionEvent;
import tiles.TileTriggerEvent;
import tiles.TileType;

public class TrapSystem extends ESystem {
	@Override
	public void start() {
		EventSystem.addEntityTypeListener(TileCollisionEvent.class, this::onTileCollision, TrapDependent.class);
		EventSystem.addEntityTypeListener(TileTriggerEvent.class, this::onTileTrigger, TrapDependent.class);
	}
	
	public void onTileTrigger(TileTriggerEvent ev) {
		onTileAction(ev.entity, ev.tile, ev.dir);
	}
	
	public void onTileCollision(TileCollisionEvent ev) {
		onTileAction(ev.entity, ev.tile, ev.dir);
	}
	
	public void onTileAction(Entity e, Tile tile, int dir) {
		TrapDependent td = e.getComponentByType(TrapDependent.class);
		Body b = e.getComponentByType(Body.class);
		Transform t = e.getComponentByType(Transform.class);
		
		if(!tile.enabled) return;

		switch(tile.type) {
		case TRAMPOLINE:
			if(dir != 1) break;
			b.velocity.y = -20.0f;
			Physics.gameMaster.addAction(new SequenceAction(new RunAction(e, s -> {
				tile.image = ResourceManager.getImage("objects1");
				tile.enabled = false;
				
			}),
			new WaitAction(500), new RunAction(e, s -> {
				tile.image = ResourceManager.getImage("objects0");
				tile.enabled = true;
			})));
			break;
		case SPIKE:
			Color red = new Color(139, 0, 0);
			BufferedImage newTileImg = Utils.deepCopy(tile.image);
			int px = 0; 
			while(px < 15) {
				int x = new Random().nextInt(tile.image.getWidth());
				int y = new Random().nextInt(tile.image.getHeight());
				
				Color color = new Color(tile.image.getRGB(x, y));
				if(color.getRed() != 0 || color.getBlue() != 0 || color.getGreen() != 0) {
					newTileImg.setRGB(x, y, red.getRGB());
					++px;
				}
			}
			td.enabled = false;
			tile.image = newTileImg;
			//ev.tile.image = ResourceManager.getImage("objects3");
			EventSystem.submit(new KilledEvent(e));
			break;
		case FLAG:
			tile.image = ResourceManager.getImage("objects6");
			tile.type = TileType.NONE;
			Physics.checkpointState = Game.getCurrentScene();
			break;
		case BREAK_WALL:
			if(dir != 1) break;
			Lifetimer lt = (Lifetimer) tile.attribute;
			
			lt.lifetime -= Time.deltaTime;
			System.out.println(lt.lifetime);
			
			if(lt.lifetime <= 0) {
				tile.clear();
			}
			break;
		}
	}

	

}

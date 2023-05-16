package game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.danceEngine.action.Animation;
import com.danceEngine.action.ChangeImage;
import com.danceEngine.action.GroupAction;
import com.danceEngine.action.RunAction;
import com.danceEngine.action.ScaleTo;
import com.danceEngine.action.SequenceAction;
import com.danceEngine.action.WaitAction;
import com.danceEngine.action.fsm.StateMachine;
import com.danceEngine.camera.CameraComponent;
import com.danceEngine.ecs.DataLocator;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;
import com.danceEngine.event.EventSystem;
import com.danceEngine.game.Game;
import com.danceEngine.physics.AABB;
import com.danceEngine.rendering.RenderData;
import com.danceEngine.rendering.RenderSystem;
import com.danceEngine.rendering.Renderer;
import com.danceEngine.resources.ResourceManager;
import com.danceEngine.scene.Scene;
import com.danceEngine.tiled.GameObject;
import com.danceEngine.tiled.Tile;
import com.danceEngine.tiled.TileMap;
import com.danceEngine.tiled.TiledLoader;
import com.danceEngine.transition.FadeToBlackTransition;
import com.danceEngine.utils.Utils;
import com.danceEngine.utils.Vector2;

import camera.CameraFollow;
import camera.CameraFollowSystem;
import enemy.Ghost;
import enemy.GhostSystem;
import enemy.Jumpable;
import enemy.JumpableSystem;
import enemy.Slime;
import enemy.SlimeSystem;
import mechanics.Door;
import mechanics.DoorSystem;
import mechanics.MovingPlatform;
import mechanics.MovingPlatformSystem;
import mechanics.Obstacle;
import physics.AABBDrawSystem;
import physics.Body;
import physics.Physics;
import physics.PhysicsSystem;
import physics.ShapeComponent;
import physics.Trigger;
import statemachine.ActionState;
import statemachine.SimpleStateMachine;
import player.PlayerControlSystem;
import player.PlayerController;
import statemachine.SimpleStateSystem;
import tiles.Map;
import tiles.TilemapModel;
import traps.TrapDependent;
import traps.TrapSystem;

public class Level extends Scene {
	private TileMap map;
	private String mapPath;
	private final float tileZoom = 3;
	private final int relTileSize = 21;
	private final float absTileSize = tileZoom * relTileSize;
	
	public Level(String mapPath) throws IOException {
		this.mapPath = mapPath;
		completeRestart();
	}
	
	
	public void completeRestart() throws IOException {
		map = TiledLoader.loadMap(mapPath);
	}

	@Override
	public void prepare() {
		Entity mapBackground = new Entity("map-background", 0, 0, 0, 0, 10);
		mapBackground.addRenderer(new TilemapModel((int)absTileSize));
		addEntity(mapBackground);
		
		Entity player = createPlayer();
		addEntity(player);
		
		for(GameObject gob : map.getGameObjects()) {
			handleGameObj(gob);
		}
		
		Entity gameMaster = new Entity("gameMaster");
		gameMaster.addComponent(new GameMaster(this));
		addEntity(gameMaster);
		
		Physics.gameMaster = gameMaster;
		
		Entity camera = new Entity("camera", 0, 900, Game.width, Game.height, 0);
		camera.addComponent(new CameraComponent());
		camera.addComponent(new CameraFollow(player, 0.06f, 50));
		addEntity(camera);
		
		DataLocator.provide(new RenderData(camera));
		
		Physics.map = new Map(map, (int)absTileSize, tileZoom);
		
		addSystem(new RenderSystem());
		addSystem(new GameMasterSystem());
		addSystem(new PhysicsSystem());
		addSystem(new PlayerControlSystem());
		addSystem(new CameraFollowSystem());
		addSystem(new SimpleStateSystem());
		addSystem(new TrapSystem());
		addSystem(new SlimeSystem());
		//addSystem(new AABBDrawSystem());
		addSystem(new DoorSystem());
		addSystem(new JumpableSystem());
		addSystem(new GhostSystem());
		addSystem(new MovingPlatformSystem());
	}
	
	private Entity createPlayer() {
		GameObject playerObj = map.getFirstGameObjByType("player");
		Vector2 playerSize = new Vector2(absTileSize - 2, absTileSize - 2);
		Vector2 pos = playerObj.getPos().mul(tileZoom);
		pos.y -= playerSize.y;
		Entity player = new Entity("player", pos, playerSize, 6);
		var body = new Body();
		body.collisionTag |= GameTag.SLIME;
		body.collisionTag |= GameTag.GHOST;
		player.addComponent(body);
		player.addComponent(new ShapeComponent(new AABB(5, 0, 11, 21).scale(tileZoom)));
		player.addComponent(Renderer.spriteRenderer(ResourceManager.getImage("sprite0")));
		player.addTag(GameTag.PLAYER);
		
		var walkImgs = ResourceManager.getImages("sprite1", "sprite7", "sprite10", "sprite8");
		var walkLeftAnim = new Animation(player, 100, true, Utils.flipHorizontal(walkImgs));
		var walkRightAnim = new Animation(player, 100, true, walkImgs);
		var idleAnim = new ChangeImage(player, ResourceManager.getImage("sprite0"));
		var looseAnim = new ChangeImage(player, ResourceManager.getImage("sprite4"));
		var fallRightAnim = new ChangeImage(player, ResourceManager.getImage("sprite9"));
		var fallLeftAnim = new ChangeImage(player, Utils.flipHorizontal(ResourceManager.getImage("sprite9")));
		var enterDoorAnim = new GroupAction(
				new Animation(player, 200, true, ResourceManager.getImages("sprite5", "sprite6")),
				new SequenceAction(new ScaleTo(player, playerSize.div(1.2f), 50), new RunAction(player, x -> {x.destroy(); EventSystem.submit(new WinEvent());})));
		
		SimpleStateMachine statemachine = new SimpleStateMachine(player, 
				walkLeftAnim,
				walkRightAnim,
				idleAnim,
				fallLeftAnim,
				fallRightAnim,
				looseAnim,
				enterDoorAnim);
		player.addComponent(statemachine);
		
		player.addComponent(new PlayerController(absTileSize * 2 + 14, 0.7f, 7.0f, statemachine));
		player.addComponent(new TrapDependent());
		
		return player;
	}
	
	private void handleGameObj(GameObject gob) {
		if(gob.type == null)
			return;
		Entity en = null;
		Vector2 pos = gob.getPos().mul(tileZoom);
		switch(gob.type) {
		case "slime":
			Vector2 size = new Vector2(absTileSize, absTileSize);
			boolean left = gob.getBoolProperty("left");
			pos.y -= size.y;
			en = new Entity("slime", pos.intVec(), size.intVec(), 5);
			en.addTag(GameTag.SLIME);
			en.addComponent(Renderer.spriteRenderer(ResourceManager.getImage("enemies1")));
			en.addComponent(new Body());
			en.addComponent(new ShapeComponent(new AABB(4, 10, 13, 10).scale(tileZoom)));
			en.addComponent(new Slime(absTileSize, 2.2f, 500, left));
			en.addComponent(new TrapDependent());
			en.addComponent(new Trigger(GameTag.PLAYER));
			en.addComponent(new Jumpable());
			
			var leftAnim = new ChangeImage(en, ResourceManager.getImage("enemies1"));
			var rightAnim = new ChangeImage(en, Utils.flipHorizontal(ResourceManager.getImage("enemies1")));
			var jumpLeftAnim = new Animation(en, 200, ResourceManager.getImages("enemies2", "enemies1"));
			var dieLeft = new SequenceAction(
					new ChangeImage(en, ResourceManager.getImage("enemies3")),
					new WaitAction(200),
					new RunAction(en, x -> x.destroy()));
			var dieRight = new SequenceAction(
					new ChangeImage(en, Utils.flipHorizontal(ResourceManager.getImage("enemies3"))),
					new WaitAction(200),
					new RunAction(en, x -> x.destroy()));
			en.addComponent(new SimpleStateMachine(en, leftAnim, rightAnim, dieLeft, dieRight));
			break;
		case "door":
			size = new Vector2(absTileSize, absTileSize * 2);
			pos.y -= size.y;
			en = new Entity("door", pos.intVec(), size.intVec(), 13);
			en.addComponent(Renderer.spriteRenderer(ResourceManager.getImage("door0")));
			en.addComponent(new Trigger(GameTag.PLAYER));
			en.addComponent(new ShapeComponent(new AABB(3, 15, 15, 27).scale(tileZoom)));
			en.addComponent(new Door());
			break;
		case "ghost":
			Vector2 limit = map.getGameObjectByID(gob.getIntProperty("limit")).getPos().mul(tileZoom);
			size = new Vector2(absTileSize, absTileSize);
			pos.y -= size.y;
			en = new Entity("ghost", pos.intVec(), size.intVec(), 5);
			en.addComponent(Renderer.spriteRenderer(ResourceManager.getImage("enemies4")));
			en.addComponent(new Body());
			en.addComponent(new ShapeComponent(new AABB(2, 10, 14, 10).scale(tileZoom)));
			en.addComponent(new Trigger(GameTag.PLAYER));
			en.addComponent(new Jumpable());
			en.addComponent(new Ghost(2, pos, limit));
			en.addComponent(new TrapDependent());
			en.addTag(GameTag.GHOST);
			
			leftAnim = new ChangeImage(en, ResourceManager.getImage("enemies4"));
			rightAnim = new ChangeImage(en, Utils.flipHorizontal(ResourceManager.getImage("enemies4")));
			dieLeft = new SequenceAction(
					new ChangeImage(en, ResourceManager.getImage("enemies6")),
					new WaitAction(200),
					new RunAction(en, x -> x.destroy()));
			dieRight = new SequenceAction(
					new ChangeImage(en, Utils.flipHorizontal(ResourceManager.getImage("enemies6"))),
					new WaitAction(200),
					new RunAction(en, x -> x.destroy()));
			en.addComponent(new SimpleStateMachine(en, leftAnim, rightAnim, dieLeft, dieRight));
			break;
		case "platform":
			size = gob.getSize().mul(tileZoom);
			en = new Entity("platform", pos.intVec(), size.intVec(), 5);
			en.addComponent(Renderer.rectRenderer(Color.cyan));
			en.addComponent(new ShapeComponent(new AABB(0, 0, size.xToInt(), size.yToInt())));
			en.addComponent(new Obstacle());
			float speed = gob.getFloatProperty("speed");
			Vector2 start = pos.intVec().copy();
			Vector2 end = map.getGameObjectByID(gob.getIntProperty("end")).getPos().mul(tileZoom).intVec();
			
			Vector2 dist = start.sub(end);
			en.addComponent(new MovingPlatform(speed, start, end, Math.abs(dist.x) > Math.abs(dist.y)));
			break;
		}
		if(en != null) {
			addEntity(en);
		}
	}

}

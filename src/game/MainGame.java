package game;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.danceEngine.game.Game;
import com.danceEngine.input.Input;
import com.danceEngine.input.control.GroupControl;
import com.danceEngine.input.control.JoystickAxisControl;
import com.danceEngine.input.control.JoystickButtonControl;
import com.danceEngine.input.control.KeyControl;
import com.danceEngine.input.joystick.Joystick;
import com.danceEngine.resources.ResourceManager;

public class MainGame {

	public static void main(String[] args) throws IOException {
		
		
		Game.fpsDisplay = false;
		Game.width = 2000;
		Game game = new Game();
		game.addScene(new Menu());
		game.addScene(new Level("res/maps/test.tmx"));
		game.loadScene(1);
		
		defineControls();
		loadResources();
		
		game.startGame();
	}
	
	private static void defineControls() {
		Input.addControl("jump", new GroupControl(new KeyControl(KeyEvent.VK_UP), new JoystickButtonControl(0, Joystick.A)));
		Input.addControl("movex", new GroupControl(new KeyControl(KeyEvent.VK_LEFT, -1), new KeyControl(KeyEvent.VK_RIGHT, 1), new JoystickAxisControl(0, "x")));
		Input.addControl("quit", new GroupControl(new KeyControl(KeyEvent.VK_ESCAPE), new JoystickButtonControl(0, Joystick.START)));
	}
	
	private static void loadResources() {
		ResourceManager.loadSpriteSheet("res/sprites/spritesheet21.png", 21, 21, "sprite");
		ResourceManager.loadSpriteSheet("res/sprites/tilemap_creation.png", 21, 21, "objects");
		ResourceManager.loadSpriteSheet("res/sprites/enemies.png", 21, 21, "enemies");
		ResourceManager.loadSpriteSheet("res/sprites/door.png", 21, 42, "door");
	}

}

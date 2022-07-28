package game;

import java.awt.Color;
import java.awt.Font;

import com.danceEngine.ecs.Entity;
import com.danceEngine.game.Game;
import com.danceEngine.rendering.RenderSystem;
import com.danceEngine.scene.Scene;
import com.danceEngine.ui.Button;
import com.danceEngine.ui.ButtonModel;
import com.danceEngine.ui.ButtonSystem;
import com.danceEngine.utils.Vector2;

public class Menu extends Scene {

	@Override
	public void prepare() {
		Vector2 btnSize = new Vector2(200, 180);
		Entity playBtn = new Entity("playBtn", new Vector2(Game.width / 2- btnSize.x / 2, 100), btnSize, 5);
		playBtn.addRenderer(new ButtonModel("play", new Font("Serif", Font.BOLD, 60), Color.blue, Color.black, 20, 20));
		playBtn.addComponent(new Button(x -> Game.loadScene(1), Color.blue, new Color(50, 50, 240)));
		addEntity(playBtn);
		
		addSystem(new RenderSystem());
		addSystem(new ButtonSystem());
	}

}

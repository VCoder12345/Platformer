package tiles;

import java.awt.Graphics2D;

import com.danceEngine.ecs.Transform;
import com.danceEngine.game.Game;
import com.danceEngine.rendering.Model;
import com.danceEngine.tiled.Layer;
import com.danceEngine.tiled.TileMap;

import physics.Physics;

public class TilemapModel extends Model {
	private int tileSize;
	
	

	public TilemapModel(int tileSize) {
		super();
		this.optimisation = false;
		this.tileSize = tileSize;
	}



	@Override
	protected void render(Graphics2D g2d, int px, int py, int sx, int sy, Transform camT) {
		Map map = Physics.map;
		for(int x = 0; x < map.w; ++x) {
			for(int y = 0; y < map.h; ++y) {
				Tile tile = map.get(x, y);
				int tx = px + x * tileSize;
				int ty = py + y * tileSize;
				
				if(tx + tileSize > 0 && ty + tileSize > 0 && tx < Game.width && ty < Game.height) {
					g2d.drawImage(tile.image, tx, ty, tileSize, tileSize, null);
				}
				
			}
		}
	}

}

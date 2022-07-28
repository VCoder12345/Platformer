package tiles;

import java.awt.image.BufferedImage;

import com.danceEngine.physics.AABB;
import com.danceEngine.utils.Vector2;

public class Tile {
	public boolean solid = false;
	public float frictionCoefficient = 0.1f;
	public TileType type = TileType.NONE;
	public BufferedImage image;
	public AABB aabb;
	public boolean enabled = true;
	
	public Tile(BufferedImage image, AABB aabb) {
		this.image = image;
		this.aabb = aabb;
	}
	
	
	public static Tile border() {
		Tile tile = new Tile(null, null);
		tile.solid = true;
		return tile;
	}
}

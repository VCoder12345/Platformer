package tiles;


import com.danceEngine.physics.AABB;
import com.danceEngine.tiled.Layer;
import com.danceEngine.tiled.TileMap;
import com.danceEngine.utils.Vector2;

public class Map {
	public int w, h;
	public Tile[][] tiles;
	public int size;
	
	public Map(TileMap tileMap, int size, float zoom) {
		Layer layer = tileMap.layers.get(0);
		this.size = size;
		this.w = layer.width;
		this.h = layer.height;
		this.tiles = new Tile[w][h];
		var ts = tileMap.tileset;
		for(int x = 0; x < w; ++x) {
			for(int y = 0; y < h; ++y) {
				var tl = ts.tiles[layer.tileIndices[x][y]];
				
				AABB aabb = new AABB(new Vector2(Math.max(0, tl.aabb.pos.x * zoom), Math.max(0, tl.aabb.pos.y * zoom)).intVec(),
						new Vector2(Math.min(size, tl.aabb.size.x * zoom), Math.min(size, tl.aabb.size.y * zoom)).intVec());
				Tile tile = new Tile(tl.image, aabb);

				if(tl.hasProperty("solid")) {
					tile.solid = tl.getProperty("solid").equals("true");
				}
				if(tl.type != null) {
					switch(tl.type) {
					case "spike":
						tile.type = TileType.SPIKE;
						break;
					case "trampoline":
						tile.type = TileType.TRAMPOLINE;
						break;
					case "flag":
						tile.type = TileType.FLAG;
						break;
					}
				}
				
				tiles[x][y] = tile;
				
			}
		}
	}
	
	public int getIndex(float p) {
		return (int) (p / size);
	}
	
	public Tile get(int x, int y) {
		if(x < 0 || x >= w || y < 0 || y >= h) {
			return Tile.border();
		}
		return tiles[x][y];
	}
	
	public Vector2 getTilePos(int x, int y) {
		return new Vector2(x * size, y * size);
	}
	
	public Vector2 getBoxPos(int x, int y) {
		return getTilePos(x, y).add(get(x, y).aabb.pos);
	}
}

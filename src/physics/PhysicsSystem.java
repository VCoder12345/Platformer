package physics;

import com.danceEngine.ecs.ESystem;
import com.danceEngine.ecs.Entity;
import com.danceEngine.ecs.Transform;
import com.danceEngine.event.EventSystem;
import com.danceEngine.utils.Utils;
import com.danceEngine.utils.Vector2;

import mechanics.Obstacle;
import tiles.Map;
import tiles.Tile;
import tiles.TileCollisionEvent;
import tiles.TileTriggerEvent;
import tiles.TileType;

public class PhysicsSystem extends ESystem {
	

	@Override
	public void physics(float dt) {
		var entities = getEntitiesWithTypes(Body.class, ShapeComponent.class, Transform.class);
		for (Entity e : entities) {
			Body b = e.getComponentByType(Body.class);
			Transform t = e.getComponentByType(Transform.class);
			
			if(b.onGround && Math.abs(b.velocity.y) > 2.0f) {
				b.onGround = false;
			}
			
			b.addForce(new Vector2(0, Physics.gravity * b.mass));
			
			b.velocity.addE(b.acceleration.mul(dt));

			
			//step y
			t.position.y += b.velocity.y * dt;

		}
		
		for (Entity e : entities) {
			Body b = e.getComponentByType(Body.class);
			ShapeComponent sc = e.getComponentByType(ShapeComponent.class);
			Transform t = e.getComponentByType(Transform.class);
			resolveCollisionsY(e, b, sc, t);
			
			//step x
			t.position.x += b.velocity.x * dt;

			
		}	
		
		for (Entity e : entities) {
			Body b = e.getComponentByType(Body.class);
			ShapeComponent sc = e.getComponentByType(ShapeComponent.class);
			Transform t = e.getComponentByType(Transform.class);
			resolveCollisionsX(e, b, sc, t);
			

			b.acceleration.mulE(0);
		}
		
		
		resetTriggers();
	}
	
	private void resetTriggers() {
		for(Entity trigger : getEntitiesWithTypes(Trigger.class, Transform.class)) {
			Trigger tr = trigger.getComponentByType(Trigger.class);
			
			tr.triggered = false;
		}
	}
	
	private void resolveCollisionsY(Entity e, Body b, ShapeComponent sc, Transform t) {
		int dir = b.velocity.y < 0 ? 0 : 1;
		Map map = Physics.map;
		Vector2 min = sc.aabb.getMin(t);
		Vector2 max = sc.aabb.getMax(t);
		
		int startIx = map.getIndex(min.x);
		int endIx = map.getIndex(max.x);
		int startIy = map.getIndex(min.y);
		int endIy = map.getIndex(max.y);
		
		Vector2 intersection = null;
		
		collisionLoop:
		for(int ix = startIx; ix <= endIx; ++ix) {
			if(b.velocity.y < 0) {
				for(int iy = endIy; iy >= startIy; --iy) {
					intersection = intersectsSolid(e, map, ix, iy, min, sc.aabb.size, 0);
					if(intersection != null) {
						break collisionLoop;
					}
				}
			}else {
				for(int iy = startIy; iy <= endIy; ++iy) {
					intersection = intersectsSolid(e, map, ix, iy, min, sc.aabb.size, 1);
					if(intersection != null) {
						break collisionLoop;
					}
				}
			}
		}
		
		float bestDist = Float.POSITIVE_INFINITY;
		if(intersection != null) {
			bestDist = Math.abs(t.position.y - intersection.y);
		}
		
		Vector2 bestObstPos = checkObstacles(e, sc, t, bestDist, dir);
		if(bestObstPos != null) {
			intersection = bestObstPos;
		}
		
		
		if(intersection != null) {
			if(b.velocity.y < 0) {
				int tiley = intersection.yToInt();
				b.velocity.y = -b.bounceFactor * b.velocity.y;
				t.position.y = tiley + 1 - sc.aabb.pos.y;
			}else {
				int tiley = intersection.yToInt();
				b.velocity.y = 0;
				t.position.y = tiley - sc.aabb.pos.y - sc.aabb.size.y - 1;
				b.onGround = true;
			}
		}
		
		checkTriggers(e, sc, t, dir);
		
	}
	
	private Vector2 intersectsSolid(Entity entity, Map map, int ix, int iy, Vector2 pos, Vector2 size, int dir) {
		Tile tile = map.get(ix, iy);
		Vector2 trp = map.getBoxPos(ix, iy);
		boolean negativeVel = dir % 2 == 0;
		if((tile.aabb.pos.x == 0 && tile.aabb.pos.y == 0 && tile.aabb.size.x == map.size && tile.aabb.size.y == map.size) || Utils.rectRectIntersection(trp, tile.aabb.size, pos, size)) {
			if(tile.solid) {
				EventSystem.submit(new TileCollisionEvent(entity, tile, dir));
				if(negativeVel) {
					return trp.add(tile.aabb.size);
				}else {
					return trp;
				}
			}else {
				if(tile.type != TileType.NONE) {
					EventSystem.submit(new TileTriggerEvent(entity, tile, dir));
				}
			}
		}
		
		return null;
	}
	
	private Vector2 intersectsBody(Entity entity, Body b,  ShapeComponent sc, Transform t, int dir) {
		for(Entity oe : getEntitiesWithTag(b.collisionTag)) {
			Transform ot = oe.getComponentByType(Transform.class);
			if(Utils.rectRectIntersection(ot.position, ot.size, t.position, t.size)) {
				EventSystem.submit(new CollisionEvent(entity, oe, dir));
				
				if(dir % 2 == 0) {
					return ot.position.add(ot.size);
				}else {
					return ot.position;
				}
			}
		}
		
		return null;
	}
	
	private void checkTriggers(Entity oEntity,  ShapeComponent osc, Transform ot, int dir) {
		for(Entity trigger : getEntitiesWithTypes(Trigger.class, ShapeComponent.class, Transform.class)) {
			Trigger tr = trigger.getComponentByType(Trigger.class);
			
			if(tr.triggered) continue;
			
			if(oEntity.hasTag(tr.triggerMask)) {
				Transform t = trigger.getComponentByType(Transform.class);
				ShapeComponent sc = trigger.getComponentByType(ShapeComponent.class);
				
				if(Utils.rectRectIntersection(sc.aabb.getMin(t), sc.aabb.size, osc.aabb.getMin(ot), osc.aabb.size)) {
					EventSystem.submit(new TriggerEvent(trigger, oEntity, dir));
					tr.triggered = true;
				}
			}
			
			
			
		}
	}
	
	private Vector2 checkObstacles(Entity oEntity,  ShapeComponent osc, Transform ot, float bestDistSqr, int dir) {
		Vector2 bestObstPos = null;
		Vector2 opos = osc.aabb.getMin(ot);
		for(Entity obst : getEntitiesWithTypes(Obstacle.class, ShapeComponent.class, Transform.class)) {
			Transform t = obst.getComponentByType(Transform.class);
			ShapeComponent sc = obst.getComponentByType(ShapeComponent.class);
			Vector2 pos = sc.aabb.getMin(t);
			float distSqr;
			if(dir < 2) {
				distSqr = Math.abs(pos.y - opos.y);
			}else {
				distSqr = Math.abs(pos.x - opos.x);
			}
			
			if(distSqr < bestDistSqr) {
				if(Utils.rectRectIntersection(pos, sc.aabb.size, opos, osc.aabb.size)) {
					EventSystem.submit(new CollisionEvent(obst, oEntity, dir));
					bestObstPos = pos;
					bestDistSqr = distSqr;
					if(dir % 2 == 0) {
						bestObstPos.addE(sc.aabb.size);
					}
					
				}
			}
			
			
		}

		return bestObstPos;
	}
	
	private void resolveCollisionsX(Entity e, Body b,  ShapeComponent sc, Transform t) {
		int dir = b.velocity.x < 0 ? 2 : 3;
		Map map = Physics.map;
		Vector2 min = sc.aabb.getMin(t);
		Vector2 max = sc.aabb.getMax(t);
		int startIx = map.getIndex(min.x);
		int endIx = map.getIndex(max.x);
		int startIy = map.getIndex(min.y);
		int endIy = map.getIndex(max.y);
		
		boolean collision = false;
		int intersx = 0;
		collisionLoop:
		for(int iy = startIy; iy <= endIy; ++iy) {
			if(b.velocity.x < 0) {
				for(int ix = endIx; ix >= startIx; --ix) {
					Vector2 intersection = intersectsSolid(e, map, ix, iy, min, sc.aabb.size, 2);
					if(intersection != null) {
						collision = true;
						intersx = intersection.xToInt();
						
						break collisionLoop;
					}
				}
			}else {
				for(int ix = startIx; ix <= endIx; ++ix) {
					Vector2 intersection = intersectsSolid(e, map, ix, iy, min, sc.aabb.size, 3);
					if(intersection != null) {
						collision = true;
						intersx = intersection.xToInt();
						
						break collisionLoop;
					}
				}
			}
		}
		
		float bestDist = Float.POSITIVE_INFINITY;
		if(collision) {
			bestDist = Math.abs(t.position.x - intersx);
		}
		Vector2 bestObstPos = checkObstacles(e, sc, t, bestDist, dir);
		if(bestObstPos != null) {
			intersx = bestObstPos.xToInt();
			collision = true;
		}
		
		if(collision) {
			if(b.velocity.x < 0) {
				b.velocity.x = 0;
				t.position.x = intersx + 1 - sc.aabb.pos.x;
			}else {
				b.velocity.x = 0;
				t.position.x = intersx - sc.aabb.size.x - sc.aabb.pos.x - 1;
			}
		}
		
		checkTriggers(e, sc, t, dir);
			
	}

}

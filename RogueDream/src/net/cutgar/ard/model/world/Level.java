package net.cutgar.ard.model.world;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.cutgar.ard.model.agents.BasicEnemy;
import net.cutgar.ard.model.object.Item;
import net.cutgar.ard.state.PlayState;

import org.flixel.FlxPoint;

public class Level {
	
	public Tile[][] map;
	public List<Item> items = new LinkedList<Item>();
	public Item grail;
	public int grailx; public int graily;
	
	public int enemyTypes = 3;
	public List<BasicEnemy> enemies = new LinkedList<BasicEnemy>();
	public Map<Integer, List<BasicEnemy>> enemyTypeMap = new HashMap<Integer, List<BasicEnemy>>();
	
	public enum BLOCKTYPE {INVALID, NONE, WALL, ENEMY, PLAYER;
		public BasicEnemy target;
	};
	
	public static int default_floor = 0xffffffff;
	public static int default_wall = 0xff000000;
	
	public BLOCKTYPE blocked(int x, int y){
		for(BasicEnemy e : enemies){
			if(e.mx == x && e.my == y){
				BLOCKTYPE.ENEMY.target = e;
				return BLOCKTYPE.ENEMY;
			}
		}
		if(PlayState.player.mx == x && PlayState.player.my == y)
			return BLOCKTYPE.PLAYER;
		if(x > map.length || y > map[0].length || x < 0 || y < 0)
			return BLOCKTYPE.WALL;
		if(map[x][y].blocksMovement)
			return BLOCKTYPE.WALL;
		return BLOCKTYPE.NONE;
	}

	public List<FlxPoint> sightRay(int x0, int y0, int x1, int y1){
		
		List<FlxPoint> res = new LinkedList<FlxPoint>();
		int deltax = x1-x0;
		if(deltax == 0){
			for(int i=Math.min(y0, y1); i<=Math.max(y0,y1); i++){
				if(map[x0][i].blocksSight){
					return new LinkedList<FlxPoint>();
				}
				res.add(new FlxPoint(x0, i));
			}
			return res;
		}
		int deltay = y1-y0;
		if(deltay == 0){
			for(int i=Math.min(x0, x1); i<=Math.max(x0,x1); i++){
				if(map[i][y0].blocksSight){
					return new LinkedList<FlxPoint>();
				}
				res.add(new FlxPoint(i, y0));
			}
			return res;
		}
		
		float error = 0;
		float delta_error = Math.abs((float)deltay/(float)deltax);
		int y = y0;
		int diffy = y0 < y1 ? 1 : -1;
		int diff = x0 < x1 ? 1 : -1;
		for(int x=x0; x!=x1; x+=diff){
			if(map[x][y].blocksSight){
				return new LinkedList<FlxPoint>();
			}
			res.add(new FlxPoint(x, y));
			error += delta_error;
			while(error > 0.5f){
				if(map[x][y].blocksSight){
					return new LinkedList<FlxPoint>();
				}
				y += diffy;
				error -= 1.0f;
				res.add(new FlxPoint(x, y));
			}
		}
		return res;

	}
	
	private List<FlxPoint> flip(List<FlxPoint> list) {
		List<FlxPoint> res = new LinkedList<FlxPoint>();
		for(FlxPoint p : list)
			res.add(0, p);
		return res;
	}

	public boolean canSee(float x0, float y0, float x1, float y1){
		
		if(x1 < x0 && y1 < y0)
			return canSee(x1, y1, x0, y0);
		
		int dx = (int)x1- (int)x0;
		int dy = (int)y1- (int)y0;
		int sx, sy;
		if(x0<x1) sx = 1; else sx = -1;
		if(y0<y1) sy = 1; else sy = -1;
		int error = dx - dy;
		
		while(true){
			if(map[(int)x0][(int)y0].blocksSight)
				return false;
			if(x0 == x1 && y0 == y1)
				return true;
			int e2 = 2*error;
			if(e2 > -dy){
				error = error - dy;
				x0 = x0 + sx;
			}
			if(e2 < dx){
				error = error + dx;
				y0 = y0 + sy;
			}
		}
	}
	
	public boolean canSee2(int x0, int y0, int x1, int y1){
		int deltax = x1-x0;
		if(deltax == 0){
			for(int i=Math.min(y0, y1); i<=Math.max(y0,y1); i++){
				if(map[x0][i].blocksSight){
					return false;
				}
			}
			return true;
		}
		int deltay = y1-y0;
		if(deltay == 0){
			for(int i=Math.min(x0, x1); i<=Math.max(x0,x1); i++){
				if(map[i][y0].blocksSight){
					return false;
				}
			}
			return true;
		}
		
		float error = 0;
		float delta_error = Math.abs((float)deltay/(float)deltax);
		int y = y0;
		int diffy = y0 < y1 ? 1 : -1;
		int diff = x0 < x1 ? 1 : -1;
		for(int x=x0; x!=x1; x+=diff){
			if(map[x][y].blocksSight){
				return false;
			}
			error += delta_error;
			while(error > 0.5f){
				if(map[x][y].blocksSight){
					return false;
				}
				y += diffy;
				error -= 1.0f;
			}
			
		}
		return true;
	}
	
}

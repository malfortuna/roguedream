package net.cutgar.ard.generator;

import java.util.LinkedList;
import java.util.List;

import net.cutgar.ard.RogueDream;
import net.cutgar.ard.model.agents.BasicEnemy;
import net.cutgar.ard.model.world.Level;
import net.cutgar.ard.model.world.Tile;

import org.flixel.FlxPoint;
import org.flixel.FlxRect;

public class LevelGenerator {
	
	public static int max_rooms = 10;
	public static int min_room_width = 5;
	public static int min_room_height = 5;
	public static int max_room_width = 15;
	public static int max_room_height = 15;
	public static int max_monsters = 3;
	
	public static Level generateLevel(int maxwidth, int maxheight){
		Level l = new Level();
		Tile[][] map = new Tile[maxwidth][maxheight];
		for(int i=0; i<maxwidth; i++){
			for(int j=0; j<maxheight; j++){
				map[i][j] = new Tile(i, j, true, true, Level.default_wall);
			}
		}
		
		List<FlxRect> rooms = new LinkedList<FlxRect>();
		
		while(rooms.size() < max_rooms){
			int w = RogueDream.RAND.nextInt(max_room_width-min_room_width) + min_room_height;
			int h = RogueDream.RAND.nextInt(max_room_height-min_room_height) + min_room_height;
			int x = RogueDream.RAND.nextInt(maxwidth - w - 1);
			int y = RogueDream.RAND.nextInt(maxheight - h - 1);
			
			FlxRect room = new FlxRect(x, y, w, h);
			
			boolean failed = false;
			for(FlxRect otherRoom : rooms){
				if(otherRoom.overlaps(room)){
					failed = true;
					break;
				}
			}
			
			if(!failed){
				addRoom(map, x, y, x+w, y+h);
				int monsters = RogueDream.RAND.nextInt(max_monsters);
				for(int i=0; i<monsters; i++){
					BasicEnemy en = new BasicEnemy(x+RogueDream.RAND.nextInt(w), y+RogueDream.RAND.nextInt(h));
					l.enemies.add(en);
					int type = 1+RogueDream.RAND.nextInt(l.enemyTypes-1);
					//The hardest type has to roll twice to appear.
					if(type == l.enemyTypes)
						type = 1+RogueDream.RAND.nextInt(l.enemyTypes-1);
					en.setType(type);
					
				}
				if(l.grailx == -1 && RogueDream.RAND.nextInt(50) == 1){
					l.grailx = x+RogueDream.RAND.nextInt(w);
					l.graily = y+RogueDream.RAND.nextInt(h);
				}
				
				if(rooms.size() != 0){
					FlxRect last = rooms.get(rooms.size()-1);
					int lastmidx = (int) (last.x+(last.width/2));
					int lastmidy = (int) (last.y+(last.height/2));
					int roommidx = (int) (room.x+(room.width/2));
					int roommidy = (int) (room.y+(room.height/2));
					if(RogueDream.RAND.nextBoolean()){
						if(lastmidx < roommidx){
							addRoom(map, lastmidx, lastmidy, roommidx, lastmidy+1);
							if(lastmidy < roommidy){
								addRoom(map, roommidx, lastmidy, roommidx+1, roommidy);
							}
							else{
								addRoom(map, roommidx, roommidy, roommidx+1, lastmidy);
							}
						}
						else{
							addRoom(map, roommidx, roommidy, lastmidx, roommidy+1);
							if(lastmidy < roommidy){
								addRoom(map, lastmidx, lastmidy, lastmidx+1, roommidy);
							}
							else{
								addRoom(map, lastmidx, roommidy, lastmidx+1, lastmidy);
							}
						}
						
					}
					else{
						if(lastmidx < roommidx){
							addRoom(map, lastmidx, lastmidy, roommidx, lastmidy+1);
							if(lastmidy < roommidy){
								addRoom(map, roommidx, lastmidy, roommidx+1, roommidy);
							}
							else{
								addRoom(map, roommidx, roommidy, roommidx+1, lastmidy);
							}
						}
						else{
							addRoom(map, roommidx, roommidy, lastmidx, roommidy+1);
							if(lastmidy < roommidy){
								addRoom(map, lastmidx, lastmidy, lastmidx+1, roommidy);
							}
							else{
								addRoom(map, lastmidx, roommidy, lastmidx+1, lastmidy);
							}
						}
					}
				}
				rooms.add(room);
			}
			
			
		}
		l.map = map;
		return l;
	}
	
	public static void addRoom(Tile[][] map, int x, int y, int x2, int y2){
		for(int i=x; i<x2; i++){
			for(int j=y; j<y2; j++){
				if(map.length > i && map[0].length > j && i >= 0 && j >= 0)
					map[i][j] = new Tile(i, j, false, false, Level.default_floor);
			}
		}
	}

}

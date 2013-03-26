package net.cutgar.ard.model.agents;

import java.util.List;

import net.cutgar.ard.RogueDream;
import net.cutgar.ard.model.world.Level.BLOCKTYPE;
import net.cutgar.ard.state.PlayState;

import org.flixel.FlxPoint;
import org.flixel.FlxSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BasicEnemy extends FlxSprite {
	
	public int ENEMY_TYPE = 0;

	public int mx;
	public int my;
	public String name = "mysterious shadow";
	
	public int hp = 10;
	public int damage_min = 1;
	public int damage_max = 5;
	
	public BasicEnemy(int x, int y){
		super(x*RogueDream.SIDE, y*RogueDream.SIDE);
		mx = x;
		my = y;
		makeGraphic(RogueDream.SIDE, RogueDream.SIDE, RogueDream.RAND.nextInt() | 0xff000000);
	}
	
	public void doTurn(){
		float s = RogueDream.SIDE;
		if(PlayState.level.canSee2((int)(x/s), (int)(y/s), (int)(PlayState.player.x/s), (int)(PlayState.player.y/s))){
			if(RogueDream.RAND.nextInt(10) == 1)
				PlayState.log("The "+name+" pulsates and glows.");
			
			List<FlxPoint> pts = PlayState.level.sightRay((int)(x/s), (int)(y/s), (int)(PlayState.player.x/s), (int)(PlayState.player.y/s));
			if(pts.size() < 1){
				return;
			}
			FlxPoint next = pts.get(1);
			
			
			if(PlayState.level.blocked((int) next.x, (int) next.y) == BLOCKTYPE.NONE){
				mx = (int) next.x;
				my = (int) next.y;
				x = mx*RogueDream.SIDE;
				y = my*RogueDream.SIDE;
			}
			else if(PlayState.level.blocked((int) next.x, (int) next.y) == BLOCKTYPE.PLAYER){
				//Attack
				int damage = RogueDream.RAND.nextInt(damage_max-damage_min)+damage_min;
				PlayState.log("The "+name+" burns you for "+damage+" damage.");
				PlayState.player.hp -= damage;
				if(PlayState.player.hp <= 0){
					PlayState.player.kill();
					PlayState.log("YOU WAKE UP.");
				}
				return;
			}
			
			if(ENEMY_TYPE == 1){
				if(PlayState.level.blocked((int) next.x, (int) next.y) == BLOCKTYPE.PLAYER)
					makeMove(next);
				else
					makeMove(pts.get(2));
			}
		}
	}
	
	public void makeMove(FlxPoint next){
		if(PlayState.level.blocked((int) next.x, (int) next.y) == BLOCKTYPE.NONE){
			System.out.println("The way is clear");
			mx = (int) next.x;
			my = (int) next.y;
			x = mx*RogueDream.SIDE;
			y = my*RogueDream.SIDE;
		}
		else if(PlayState.level.blocked((int) next.x, (int) next.y) == BLOCKTYPE.PLAYER){
			System.out.println("Attack the player!");
			//Attack
			int damage = RogueDream.RAND.nextInt(damage_max-damage_min)+damage_min;
			PlayState.log("The "+name+" burns you for "+damage+" damage.");
			PlayState.player.hp -= damage;
			if(PlayState.player.hp <= 0){
				PlayState.player.kill();
				PlayState.log("YOU WAKE UP.");
			}
			return;
		}
	}
	
	public void notify(String name){
		this.setPixels(new TextureRegion(new Texture(Gdx.files.internal(name))));
	}

	public void damage(int damage) {
		hp -= damage;
		if(hp <= 0){
			PlayState.log("The "+name+" turns into mist and disappears!");
			PlayState.level.enemies.remove(this);
			kill();
		}
	}

	public void setType(int type) {
		switch(type){
		case 0: makeGraphic(RogueDream.SIDE, RogueDream.SIDE, 0xff662266); break;
		case 1: makeGraphic(RogueDream.SIDE, RogueDream.SIDE, 0xff22aa88); hp -= 4; damage_min++; name = "dreamlike void"; break;
		case 2: makeGraphic(RogueDream.SIDE, RogueDream.SIDE, 0xffff0000); hp += 10; damage_min += 2; damage_max += 2; name = "terrifying nightmare"; break;
		}
	}

}

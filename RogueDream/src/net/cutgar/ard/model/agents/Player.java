package net.cutgar.ard.model.agents;

import net.cutgar.ard.RogueDream;
import net.cutgar.ard.state.PlayState;

import org.flixel.FlxSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends FlxSprite {
	
	public int mx;
	public int my;
	
	public int hp = 25;
	public int maxHP = 25;
	
	public int damage_min = 4;
	public int damage_max = 6;
	
	public String playerclass = "Player";
	public String weapon = "fists";
	
	public Player(int x, int y, String dream){
		super(x*RogueDream.SIDE, y*RogueDream.SIDE);
		mx = x;
		my = y;
		this.playerclass = dream.substring(0, 1).toUpperCase() + dream.substring(1);
		makeGraphic(RogueDream.SIDE, RogueDream.SIDE, RogueDream.RAND.nextInt() | 0xff000000);
	}

	public void attack(BasicEnemy target) {
		int damage = RogueDream.RAND.nextInt(damage_max-damage_min) + damage_min;
		PlayState.log("You swing your "+weapon+" at the "+target.name+".");
		PlayState.log("You deal "+damage+" damage!");
		target.damage(damage);
	}
	
	public void notify(String name){
		this.setPixels(new TextureRegion(new Texture(Gdx.files.internal(name))));
	}
	
}

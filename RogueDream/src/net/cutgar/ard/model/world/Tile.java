package net.cutgar.ard.model.world;

import net.cutgar.ard.RogueDream;

import org.flixel.FlxSprite;

public class Tile extends FlxSprite{

	public boolean blocksSight = true;
	public boolean blocksMovement = true;
	public int color;
	
	public Tile(int x, int y, boolean sight, boolean movement, int color){
		super(x * RogueDream.SIDE, y*RogueDream.SIDE);
		blocksSight = sight;
		blocksMovement = movement;
		makeGraphic(RogueDream.SIDE, RogueDream.SIDE, color);
	}
	
}

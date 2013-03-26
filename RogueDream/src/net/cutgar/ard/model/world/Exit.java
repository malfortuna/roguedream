package net.cutgar.ard.model.world;

import net.cutgar.ard.RogueDream;

import org.flixel.FlxSprite;

public class Exit extends FlxSprite {
	
	public Exit(int x, int y){
		super(x*RogueDream.SIDE + 8, y*RogueDream.SIDE + 8);
		
		makeGraphic(48, 48, 0xffff0000);
	}

}

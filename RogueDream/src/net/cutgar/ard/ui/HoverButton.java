package net.cutgar.ard.ui;

import org.flixel.FlxG;
import org.flixel.FlxSprite;

public class HoverButton extends FlxSprite {
	
	boolean hovering = false;
	
	public HoverButton(int x, int y, int color){
		super(x+2, y+2);
		makeGraphic(60, 60, 0xff880088);
		
	}
	
	@Override
	public void update(){
		if(FlxG.mouse.screenX > x && FlxG.mouse.screenX < x+64
				&& FlxG.mouse.screenY > y && FlxG.mouse.screenY < y+64){
			if(!hovering){
				makeGraphic(60, 60, 0xffAA22AA);
				hovering = true;
			}
			System.out.println("Hovering!");
		}
		else{
			if(hovering){
				makeGraphic(60, 60, 0xff880088);
				hovering = false;
			}
		}
	}

}

package net.cutgar.ard;

import java.util.Random;

import net.cutgar.ard.state.MenuState;

import org.flixel.FlxGame;
import org.flixel.FlxPoint;

public class RogueDream extends FlxGame
{
	
	public static final int SIDE = 64;
	public static final Random RAND = new Random(123102321L);
	public static final FlxPoint NP = new FlxPoint(0, 0);
	
	public RogueDream()
	{
		super(800, 600, MenuState.class, 1, 50, 50, false);
//		super(800, 600, PlayState.class, 1, 50, 50, false);
	}
}

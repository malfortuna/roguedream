package net.cutgar.ard.ui;

import org.flixel.FlxG;

import net.cutgar.ard.state.PlayState;

import com.badlogic.gdx.Input.TextInputListener;

public class GetTextInput implements TextInputListener {

	@Override
	public void input(String text) {
		PlayState.setDream(text);
		FlxG.switchState(new PlayState());
	}

	@Override
	public void canceled() {
		
	}

	
	
}

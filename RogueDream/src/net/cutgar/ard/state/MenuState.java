package net.cutgar.ard.state;

import net.cutgar.ard.ui.GetTextInput;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;

import com.badlogic.gdx.Gdx;

public class MenuState extends FlxState {

	private GetTextInput listener;
	private boolean input = false;
	
	@Override
	public void create() {
		
		listener = new GetTextInput();
		
		FlxText title = new FlxText(0, FlxG.height/3, FlxG.width, "A Rogue Dream");
		title.setFont("AfterShok.ttf");
		title.setSize(64);
		title.setAlignment("center");
		add(title);
		
		FlxText instr = new FlxText(0, FlxG.height - FlxG.height/5, FlxG.width, "SPACE TO BEGIN");
		instr.setFont("AfterShok.ttf");
		instr.setSize(16);
		instr.setAlignment("center");
		add(instr);
		
		Gdx.audio.newMusic(Gdx.files.internal("music.mp3")).play();
	}
	
	@Override
	public void update(){
		if(FlxG.keys.SPACE && !input){
			Gdx.input.getTextInput(listener, "Last night, I dreamt I was a...", "");
			input = true;
		}
	}

}

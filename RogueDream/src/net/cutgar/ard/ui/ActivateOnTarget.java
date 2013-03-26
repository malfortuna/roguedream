package net.cutgar.ard.ui;

import net.cutgar.ard.model.ability.Ability;
import net.cutgar.ard.state.PlayState;
import net.cutgar.ard.state.PlayState.MODE;

import org.flixel.event.IFlxButton;

public class ActivateOnTarget implements IFlxButton {
	
	Ability ability;
	public boolean activated = false;
	
	public ActivateOnTarget(Ability a){
		this.ability = a;
	}

	@Override
	public void callback() {
		if(!activated){
			PlayState.mode = MODE.TARGET;
			PlayState.mode.active = ability;
			activated = true;
		}
		else{
			PlayState.mode = MODE.NORMAL;
			PlayState.mode.active = null;
			activated = false;
		}
	}

}

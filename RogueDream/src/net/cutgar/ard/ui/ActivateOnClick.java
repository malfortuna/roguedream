package net.cutgar.ard.ui;

import net.cutgar.ard.model.ability.Ability;

import org.flixel.event.IFlxButton;

public class ActivateOnClick implements IFlxButton {

	private Ability ability;

	public ActivateOnClick(Ability a){
		this.ability = a;
	}
	
	@Override
	public void callback() {
		ability.activate();
	}

}

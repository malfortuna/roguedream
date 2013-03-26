package net.cutgar.ard.ui;

import net.cutgar.ard.model.ability.Ability;
import net.cutgar.ard.model.object.Item;
import net.cutgar.ard.state.PlayState;

import org.flixel.event.IFlxButton;

public class UpdateTooltipHover implements IFlxButton {

	private Ability ability;
	private Item item;

	public UpdateTooltipHover(Ability a){
		this.ability = a;
	}
	
	public UpdateTooltipHover(Item i){
		this.item = i;
	}
	
	@Override
	public void callback() {
		if(ability != null)
			PlayState.tooltip.setText(ability.getTooltip());
		else
			PlayState.tooltip.setText(item.name);
	}

}

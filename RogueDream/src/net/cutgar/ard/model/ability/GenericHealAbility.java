package net.cutgar.ard.model.ability;

import net.cutgar.ard.state.PlayState;

public class GenericHealAbility extends Ability {
	
	private int amount;
	
	public GenericHealAbility(String tooltip, int amount){
		super(tooltip);
		this.amount = amount;
	}
	
	@Override
	public void reallyActivate(){
		PlayState.player.hp = Math.min(PlayState.player.hp+amount, PlayState.player.maxHP);
		PlayState.log("You heal yourself for "+amount+" HP.");
		PlayState.state.endTurn();
	}

}

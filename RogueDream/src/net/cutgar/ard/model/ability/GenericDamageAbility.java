package net.cutgar.ard.model.ability;

import net.cutgar.ard.model.agents.BasicEnemy;

public class GenericDamageAbility extends Ability{
	
	private int range;
	private int amount;
	
	public GenericDamageAbility(String tooltip, int amount, int range){
		super(tooltip);
		this.amount = amount;
		this.range = range;
	}
	
	@Override
	public void reallyActivate(){
		if(target == null)
			return;
		BasicEnemy enemy = ((BasicEnemy) target);
		enemy.damage(amount);
	}

}

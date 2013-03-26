package net.cutgar.ard.model.ability;

public class Ability {
	
	public Object target;
	public int cooldown = 0;
	public int cooldown_time = 15;
	public String tooltip;
	
	public Ability(String tooltip){
		this.tooltip = tooltip;
	}
	
	public void setTarget(Object target){
		this.target = target;
	}
	
	public void activate(){
		if(this.cooldown > 0)
			return;
		this.cooldown += cooldown_time;
		reallyActivate();
	}
	
	public void reallyActivate(){
		
	}

	public String getTooltip() {
		if(cooldown == 0)
			return tooltip;
		else
			return tooltip+"\nCooldown ("+cooldown+" turns)";
	}

}

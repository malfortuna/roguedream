package net.cutgar.ard.coldread;

import java.util.LinkedList;
import java.util.List;

import org.atteo.evo.inflector.English;

public class TestAbilityCreation {

	public static final String[] colours = new String[]{"black", "white", "blue", "grey", "green", "red", "yellow", "orange"};
	
	public static String replace = "%NOUN%";
	public static String query_1 = "why do %NOUN%";
	public static String query_2 = "do %NOUN% always";
	public static String query_3 = "do all %NOUN%";
	public static String[] queries = new String[]{query_1, query_2, query_3};

	List<String> abilities = new LinkedList<String>();
	List<String> items = new LinkedList<String>();
	List<String> enemies = new LinkedList<String>();
	String goal;
	int abcount = 0;
	int itemcount = 0;
	boolean hasgoal;
	
	public static void main(String[] args){
		TestAbilityCreation tac = new TestAbilityCreation();
		String playerclass = "wolf";
		tac.generateAbilities(English.plural(playerclass));
		System.out.println(playerclass.toUpperCase()+":");
		for(String ab : tac.abilities){
			System.out.println(ab);
		}
		for(String it : tac.items){
			System.out.println(it);
		}
		
		if(tac.goal != null)
			System.out.println("\nObjective: To retrieve the Legendary "+tac.goal.substring(0,1).toUpperCase()+tac.goal.substring(1));
		System.out.println("\nEnemies:");
		for(String en : tac.enemies){
			System.out.println(en);
		}
	}

	private List<String> generateAbilities(String noun) {
		
		for(String q : queries){
			for(String s : GoogleAutocomplete.getAutoComplete(q.replace(replace, noun))){
				s = s += " ";
				if(s.contains(" you ") || s.contains(" your ") || s.contains(" yours ")
				   || s.contains(" me ") || s.contains(" my ") || s.contains(" I ")
				   || s.contains(" their ") || s.contains(" them ")){
					continue;
				}
				s = s.trim();
				
				if(checkItem(s))
					continue;
				else if(checkGoal(s))
					continue;
				else if(checkEnemy(s))
					continue;
				else
					checkAbilities(s);
			}
		}
		
		return abilities;
	}
	
	public boolean checkAbilities(String s){
		if(abilities.size() < 4){
			if(s.startsWith("eat")){
				abilities.add(s+": Restores HP");
				return true;
			}
			else if(s.startsWith("look")){
				abilities.add(s+": (Passive)");
				return true;
			}
			else{
				abilities.add(s+": Attacks the enemy");
				return true;
			}
		}
		return false;
	}
	
	public boolean checkItem(String s){
		if(s.startsWith("wear")){
			String wearsWhat = s.replace("wear", "").trim();
			boolean add = true;
			for(String c : colours){
				if(wearsWhat.equalsIgnoreCase(c))
					return true;
			}
			if(add){
				items.add("Item: "+s.replace("wear", ""));
				return true;
			}
		}
		else if(s.startsWith("have")){
			items.add("Item: "+s.replace("have", ""));
			return true;
		}
		else if(s.startsWith("use")){
			items.add("Item: "+s.replace("use", ""));
			return true;
		}
		return false;
	}
	
	public boolean checkGoal(String s){
		if(!hasgoal){
			if(s.startsWith("need")){
				goal = (s.replace("need", "").trim());
				hasgoal = true;
				return true;
			}
			else if(s.startsWith("like")){
				goal = (s.replace("like", "").trim());
				hasgoal = true;
				return true;
			}
			else if(s.startsWith("love")){
				goal = (s.replace("love", "").trim());
				hasgoal = true;
				return true;
			}
		}
		return false;
	}
	
	public boolean checkEnemy(String s){
		if(s.startsWith("hate")){
			enemies.add("Enemy: "+s.replace("hate", ""));
			return true;
		}
		else if(s.startsWith("dislike")){
			enemies.add("Enemy: "+s.replace("hate", ""));
			return true;
		}
		return false;
	}
	
}

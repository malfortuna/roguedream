package net.cutgar.ard.coldread;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.atteo.evo.inflector.English;

public class AbilityGenerator {

	public static final String[] colours = new String[] { "black", "white",
			"blue", "grey", "green", "red", "yellow", "orange" };

	public static String replace = "%NOUN%";
	public static String query_1 = "why do %NOUN%";
	public static String query_2 = "do %NOUN% always";
	public static String query_3 = "do all %NOUN%";
	public static String query_4 = "why do %NOUN% hate";
	public static String[] queries = new String[] { query_1, query_2, query_3 };

	public List<String> abilities = new LinkedList<String>();
	public List<String> enemies = new LinkedList<String>();
	public List<String> items = new LinkedList<String>();
	public List<String> goal = new LinkedList<String>();
	public Map<String, String> abilityTypes = new HashMap<String, String>();
	
	public static void main(String[] args){
		generateEnemies("dog");
	}
	
	public static List<String> generateEnemies(String seed){
		System.out.println("SEARCHING FOR "+seed);
		seed = English.plural(seed);
		AbilityGenerator ag = new AbilityGenerator();
		List<String> res = new LinkedList<String>();

			for (String s : GoogleAutocomplete.getAutoComplete(query_4.replace(
					replace, seed))) {
				s = s += " ";
				if (s.contains(" you ") || s.contains(" your ")
						|| s.contains(" yours ") || s.contains(" me ")
						|| s.contains(" my ") || s.contains(" I ")
						|| s.contains(" their ") || s.contains(" them ")) {
					continue;
				}
				s = "hate "+s.trim();
				
				System.out.println(s);

				if (ag.checkEnemy(s))
					continue;
			}
			
			res.addAll(ag.enemies);
			return res;
	}

	public static Map<String, Object> generateGameContent(String seed) {
		seed = English.plural(seed);
		AbilityGenerator ag = new AbilityGenerator();

		for (String q : queries) {
			for (String s : GoogleAutocomplete.getAutoComplete(q.replace(
					replace, seed))) {
				s = s += " ";
				if (s.contains(" you ") || s.contains(" your ")
						|| s.contains(" yours ") || s.contains(" me ")
						|| s.contains(" my ") || s.contains(" I ")
						|| s.contains(" their ") || s.contains(" them ")) {
					continue;
				}
				s = s.trim();
				
				System.out.println(s);

				if (ag.checkItem(s))
					continue;
				else if (ag.checkGoal(s))
					continue;
				else if (ag.checkEnemy(s))
					continue;
				else
					ag.checkAbilities(s);
			}
		}

		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("goals", ag.goal);
		res.put("abilities", ag.abilities);
		res.put("items", ag.items);
		res.put("enemies", ag.enemies);
		res.put("abilityTypes", ag.abilityTypes);
		return res;
	}

	public boolean checkAbilities(String s) {
		s = s.trim();
		if (abilities.size() < 4) {
			for(String ability : abilities){
				if(s.startsWith(ability) || ability.startsWith(s))
					return false;
			}
			if (s.startsWith("eat")) {
				abilities.add(s);
				abilityTypes.put(s,  "Restores HP");
				return true;
			} else if (s.startsWith("look")) {
				abilities.add(s);
				abilityTypes.put(s, "(Passive)");
				return true;
			} else {
				abilities.add(s);
				abilityTypes.put(s, "Attacks the enemy");
				return true;
			}
		}
		return false;
	}

	public boolean checkItem(String s) {
		if (s.startsWith("wear")) {
			String wearsWhat = s.replace("wear", "").trim();
			boolean add = true;
			for (String c : colours) {
				if (wearsWhat.equalsIgnoreCase(c))
					return true;
			}
			if (add) {
				items.add(0, "Item: " + s.replace("wear", ""));
				return true;
			}
		} else if (s.startsWith("have")) {
			items.add("Item: " + s.replace("have", ""));
			return true;
		} else if (s.startsWith("use")) {
			items.add("Item: " + s.replace("use", ""));
			return true;
		}
		return false;
	}

	public boolean checkGoal(String s) {
		if (goal.size() == 0) {
			if (s.startsWith("need")) {
				goal.add(s.replace("need", "").trim());
				return true;
			} else if (s.startsWith("like")) {
				goal.add(s.replace("like", "").trim());
				return true;
			} else if (s.startsWith("love")) {
				goal.add(s.replace("love", "").trim());
				return true;
			}
		}
		return false;
	}

	public boolean checkEnemy(String s) {
		if (s.startsWith("hate")) {
			enemies.add( s.replace("hate", ""));
			return true;
		} else if (s.startsWith("dislike")) {
			enemies.add( s.replace("dislike", ""));
			return true;
		}
		return false;
	}

}

package net.cutgar.ard.coldread;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class GoogleAutocomplete {
	
	public static String AC_URL = "http://suggestqueries.google.com/complete/search?client=chrome&q=";

	public static void main(String[] args){
		getAutoComplete("why do dogs");
		getAutoComplete("do dogs always");
		getAutoComplete("do all dogs");
	}
	
	public static List<String> getAutoComplete(String query){
		List<String> acs = new LinkedList<String>();
		
		try{
			URL url = new URL(AC_URL + query.replace(' ', '+'));
			URLConnection connection = url.openConnection();
			
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = reader.readLine()) != null){
				builder.append(line);
			}
			
			JsonParser parser = new JsonParser();
			JsonArray obj = parser.parse(builder.toString()).getAsJsonArray();
			JsonArray autocomps = (JsonArray) obj.get(1);
			
			for(JsonElement e : autocomps){
				String ac = e.getAsString();
				if(!ac.startsWith(query))
					continue;
				ac = ac.substring(query.length());
				acs.add(ac);
//				System.out.println(ac);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return acs;
	}

}

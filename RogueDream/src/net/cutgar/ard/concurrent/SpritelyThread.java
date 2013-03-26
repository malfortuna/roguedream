package net.cutgar.ard.concurrent;

import net.cutgar.ard.RogueDream;

import org.gba.spritely.Spritely;

public class SpritelyThread implements Runnable{
	
	String dream = "knight";
	String theme = "heroic";
	private String target;
	
	public boolean purgeGoogle = false;
	public int size = -1;
	
	public SpritelyThread(String dream, String theme, String target){
		this.dream = dream;
		this.theme = theme;
		this.target = target;
	}
	
	@Override
	public void run() {
		System.out.println("Searching for "+dream);
		System.out.println("Saving to "+target);
		Spritely s = new Spritely();
		if(purgeGoogle){
			s.googleSearchPrefixes = new String[]{""};
			s.googleSearchSuffixes = new String[]{};
		}
		s.setQuery(dream);
//		s.setRecolor("");
		s.setSize(RogueDream.SIDE);
		if(size > 0)
			s.setSize(size);
		s.setImagesPerSource(1);
		s.setSearchGoogleImages(true);
		s.setSearchOpenClipart(true);
		s.setSearchWikimediaCommons(false);
		s.setOutputPath("/develop/workspace/RogueDream-desktop");
		s.setOutputFilename(target);
		s.write(s.search());
	}


}

package net.cutgar.ard.ui;

import java.util.HashMap;
import java.util.Map;

import net.cutgar.ard.RogueDream;
import net.cutgar.ard.model.object.Item;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxText;
import org.flixel.FlxTileblock;
import org.flixel.event.IFlxButton;

public class InventoryScreen extends FlxGroup {
	
	private String weaponSlotString = "WEAPON";
	private String armourSlotString = "ARMOUR";
	private String relicSlotString = "RELIC";
	
	Item weaponSlot;
	Item armourSlot;
	Item relicSlot;
	
	Map<String, Item> bonusSlots = new HashMap<String, Item>();
	public boolean showing;
	
	public InventoryScreen(){
		FlxTileblock background = new FlxTileblock(0, 0, FlxG.width, FlxG.height);
		background.makeGraphic(FlxG.width, FlxG.height, 0xff880000);
		background.scrollFactor = RogueDream.NP;
		add(background);
		FlxText invText = new FlxText(0, 32, FlxG.width);
		invText.setSize(32); invText.setText("INVENTORY");
		invText.setAlignment("center");
		invText.scrollFactor = RogueDream.NP;
		add(invText);
		
//		HoverButton weapon = new HoverButton(FlxG.width/4 - 32, FlxG.height/3, 0xff00ff00);
//		add(weapon); weapon.scrollFactor = RogueDream.NP;
//		HoverButton armour = new HoverButton(2*FlxG.width/4 - 32, FlxG.height/3, 0xff00ff00);
//		add(armour); armour.scrollFactor = RogueDream.NP;
//		HoverButton relic = new HoverButton(3*FlxG.width/4 - 32, FlxG.height/3, 0xff00ff00);
//		add(relic); relic.scrollFactor = RogueDream.NP;
		
		FlxButton fb = new FlxButton(FlxG.width/4-32,  FlxG.height/3, "test");
		fb.makeGraphic(96, 96, 0xffffffff);
		add(fb); fb.scrollFactor = RogueDream.NP;
		fb.onUp = new IFlxButton() {
			@Override
			public void callback() {
				System.out.println("Pressed!");
			}
		};
		
		hide();
	}
	
	/*
	 * Because it's a FlxGroup, visibility is shared with its contents. Easy screen switching!
	 */
	public void show(){
		this.visible = true;
	}
	
	public void hide(){
		this.visible = false;
	}

}

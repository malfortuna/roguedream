package net.cutgar.ard.state;

import java.lang.Thread.State;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.cutgar.ard.RogueDream;
import net.cutgar.ard.coldread.AbilityGenerator;
import net.cutgar.ard.concurrent.SpritelyThread;
import net.cutgar.ard.generator.LevelGenerator;
import net.cutgar.ard.model.ability.Ability;
import net.cutgar.ard.model.ability.GenericHealAbility;
import net.cutgar.ard.model.agents.BasicEnemy;
import net.cutgar.ard.model.agents.Player;
import net.cutgar.ard.model.object.Item;
import net.cutgar.ard.model.world.Exit;
import net.cutgar.ard.model.world.Level;
import net.cutgar.ard.model.world.Level.BLOCKTYPE;
import net.cutgar.ard.ui.ActivateOnClick;
import net.cutgar.ard.ui.ClearTooltip;
import net.cutgar.ard.ui.UpdateTooltipHover;

import org.flixel.FlxBasic;
import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxRect;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTileblock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayState extends FlxState
{
	boolean verbose = true; //Debug mode
	
	//Model
	public static Level level;
	public static Player player;
	public static Exit exit;
	public static PlayState state;
	public static String dream;
	public static Map<String, Object> dreamConcepts;
	
	//View
	FlxText statCounter = new FlxText(5, 37, FlxG.width/3, "HP - 25");
	FlxText turnCounter = new FlxText(5, 5, FlxG.width/3, "The Player's Dream, Turn 0");
	boolean turnEnded = false;
	int turnCount = 0;
	
	public static FlxText tooltip;
	
	public FlxText logText = new FlxText(FlxG.width/4, 5, FlxG.width/4+FlxG.width/2-5, "You drift into sleep.");
	public static List<String> log = new LinkedList<String>();
	public int logLen = 0;

	private Thread worker;
	private List<String> imagesToFind = new LinkedList<String>();
	private Map<String, String> threadTargets = new HashMap<String, String>();
	private List<Boolean> purge = new LinkedList<Boolean>();
	private int loadStage = 0;

	private FlxSprite overlay;

	public static MODE mode = MODE.NORMAL;
	public static enum MODE {NORMAL, TARGET; public Ability active; public FlxButton ability;};
	private FlxGroup abilities;
	private List<Ability> intern_abilities = new LinkedList<Ability>();
	private String[] passives = new String[]{"Lucid", "Restless", "Troubled", "Dreaming"};
	private String[] startitems = new String[]{"Pillow", "Blanket", "Nightgown", "Candle"};

	private FlxGroup items;
	
	private boolean showingInventory;
	
	@Override
	public void create()
	{
		PlayState.state = this;

		worker = new Thread(new SpritelyThread(dream, "holy", "player"));
		worker.start();
		
		int x = -1; int y = -1;
		level = LevelGenerator.generateLevel(75, 75);
		for(int i=0; i<level.map.length; i++){
			for(int j=0; j<level.map[0].length; j++){
				add(level.map[i][j]);
				if(!level.map[i][j].blocksMovement && x == -1){
					x = i; y = j;
				}
			}
		}
		
		while(exit == null){
			int rx = RogueDream.RAND.nextInt(level.map.length);
			int ry = RogueDream.RAND.nextInt(level.map[0].length);
			if(!level.map[rx][ry].blocksMovement){
				exit = new Exit(rx, ry);
				System.out.println(rx+"x"+ry);
				add(exit);
			}
		}
		
		player = new Player(x, y, dream);
		add(player);
		FlxG.camera.follow(player);
		
		//The 64 and 32 here are the sizes of the sidebar and header respectively
		FlxG.camera.bounds = new FlxRect(0, 0, level.map.length * RogueDream.SIDE, level.map[0].length * RogueDream.SIDE);
		
		for(BasicEnemy e : level.enemies){
			add(e);
		}
		
		//Header bar
		FlxTileblock header = new FlxTileblock(0, 0, FlxG.width, 64);
		header.makeGraphic(FlxG.width, 64, 0xff444444);
		add(header);
		//Sidebar
		FlxTileblock sidebar = new FlxTileblock(0, FlxG.height-64, FlxG.width, 64);
		sidebar.makeGraphic(FlxG.width, 64, 0xff777777);
		add(sidebar);
		//Don't want them moving with the camera
		header.scrollFactor = RogueDream.NP;
		sidebar.scrollFactor = RogueDream.NP;
		
		turnCounter.setSize(8);
		turnCounter.scrollFactor = RogueDream.NP;
		turnCounter.setText("The "+player.playerclass+"'s Dream - Turn "+turnCount);
		add(turnCounter);
		
		statCounter.setSize(16);
		statCounter.scrollFactor = RogueDream.NP;
		statCounter.setText("HP - "+player.hp);
		add(statCounter);
		
		logText.setSize(8); logText.setAlignment("right");
		logText.scrollFactor = RogueDream.NP;
		add(logText);
		log("You drift into sleep.");
		
		//Generate some base passives that do nothing
		abilities = new FlxGroup();
		for(int i=0; i<4; i++){
			FlxButton ability = new FlxButton(8+i*(64+8), FlxG.height - 64, "");
			ability.makeGraphic(64, 64, RogueDream.RAND.nextInt() | 0xff000000);
			ability.onOver = new UpdateTooltipHover(new Ability(passives[i]+" - Passive\nYou are dreaming."));
			ability.onOut = new ClearTooltip();
			abilities.add(ability);
			ability.scrollFactor = RogueDream.NP;
		}
		add(abilities);
		
		items = new FlxGroup();
		for(int i=0; i<4; i++){
			FlxButton item = new FlxButton(8+i*(64+8), FlxG.height + 64, "");
			item.makeGraphic(64, 64, RogueDream.RAND.nextInt() | 0xff000000);
			item.onOver = new UpdateTooltipHover(new Ability(startitems[i]+" (Soulbound)"));
			item.onOut = new ClearTooltip();
			items.add(item);
			item.scrollFactor = RogueDream.NP;
		}
		add(items);
		
		tooltip = new FlxText(FlxG.width/2, FlxG.height - 56, FlxG.width/2, "");
		tooltip.setSize(16);
		tooltip.setAlignment("left");
		tooltip.scrollFactor = RogueDream.NP;
		add(tooltip);
		
//		inv = new InventoryScreen();
//		add(inv);
		
		overlay = new FlxSprite(-64, -64);
		overlay.setPixels(new TextureRegion(new Texture(Gdx.files.internal("tileoverlay.png"))));
		add(overlay);
		overlay.scrollFactor = RogueDream.NP;
	}
	
	@Override
	public void update(){
		super.update();
		
		boolean setTooltip = false;
		
		if(mode == MODE.TARGET){
			if(FlxG.mouse.screenY >= 64 && FlxG.mouse.screenY < FlxG.height-64){
				if(FlxG.mouse.justPressed()){
					int tlx = (int)(((float)FlxG.mouse.screenX + FlxG.camera.x)/RogueDream.SIDE);
					int tly = (int)(((float)FlxG.mouse.screenY + FlxG.camera.y)/RogueDream.SIDE);
					System.out.println(tlx+","+tly);
					for(BasicEnemy e : level.enemies){
						if(e.x == tlx && e.y == tly){
							System.out.println("Found a target");
							mode.active.target = e;
							mode.active.activate();
							mode = MODE.NORMAL;
							mode.active = null;
							mode.ability.active = false;
						}
					}
				}
				overlay.x = FlxG.mouse.screenX - (FlxG.mouse.screenX%RogueDream.SIDE) - 16;
				overlay.y = FlxG.mouse.screenY - (FlxG.mouse.screenY%RogueDream.SIDE) + 12;
			}
			else{
				overlay.x = -64;
				overlay.y = -64;
			}
		}
		else{
			if(FlxG.mouse.screenY >= RogueDream.SIDE && FlxG.mouse.screenY < FlxG.height-RogueDream.SIDE){
				int tlx = (int)(((float)FlxG.mouse.screenX + FlxG.camera.scroll.x)/RogueDream.SIDE);
				int tly = (int)(((float)FlxG.mouse.screenY + FlxG.camera.scroll.y)/RogueDream.SIDE);
				
				if(player.x/RogueDream.SIDE == tlx && player.y/RogueDream.SIDE == tly){
					tooltip.setText(dream+" the dreamer");
				}
				
				if(exit.x/RogueDream.SIDE == tlx && exit.x/RogueDream.SIDE == tly){
					tooltip.setText("a mysterious gate");
				}
				
				for(BasicEnemy e : level.enemies){
					if(e.x/RogueDream.SIDE == tlx && e.y/RogueDream.SIDE == tly){
						tooltip.setText(e.name);
						setTooltip = true;
						break;
					}
				}
			}
		}
		
		if(!setTooltip){
			if(FlxG.mouse.screenY >= 64 && FlxG.mouse.screenY < FlxG.height-64){
				tooltip.setText("");
			}
		}
		
		if(worker.getState() == State.TERMINATED){
			if(loadStage == 0){
				//If we're done with the player, we can start queuing up requests.
				player.notify("player.png");
				loadStage++;
				dreamConcepts = AbilityGenerator.generateGameContent(dream);
				List<String> enemies = (List<String>) dreamConcepts.get("enemies");
				int numDone = Math.min(enemies.size(), level.enemyTypes);
				if(enemies.size() != 0){
					for(int i=0; i<Math.min(enemies.size(), level.enemyTypes); i++){
						imagesToFind.add(0, enemies.get(0));
						purge.add(false);
						threadTargets.put(enemies.get(0), "enemy"+i);
					}
				}
				else{
					List<String> enemies_2 = AbilityGenerator.generateEnemies(dream);
					dreamConcepts.put("enemies", enemies_2);
					System.out.println(enemies_2.size()+" extra enemies found");
					for(int i=0; i<Math.min(numDone+enemies_2.size(), level.enemyTypes); i++){
						String enemy = enemies_2.get(0);
						imagesToFind.add(0, enemy);
						purge.add(0, false);
						threadTargets.put(enemy, "enemy"+(i+numDone));
					}
				}
				//Set up abilities
				if(((List<String>)dreamConcepts.get("abilities")).size() != 0){
					List<String> n_abilities = (List<String>) dreamConcepts.get("abilities");
					Map<String, String> abilityTypes = (Map<String, String>) dreamConcepts.get("abilityTypes");
					for(int i=0; i<Math.min(n_abilities.size(), 4); i++){
						FlxButton old = (FlxButton) abilities.members.get(i);
						FlxButton newb = new FlxButton(old.x, old.y, "");
						newb.makeGraphic(64, 64, RogueDream.RAND.nextInt() | 0xff000000);
						newb.onOut = new ClearTooltip();
						String abilityName = n_abilities.get(i).substring(0,1).toUpperCase()+n_abilities.get(i).substring(1);
						if(abilityTypes.get(n_abilities.get(i)).contains("Attacks the enemy")){
//							newb.onUp = new ActivateOnTarget(new GenericDamageAbility(10, 3));
							Ability ab = new GenericHealAbility(abilityName+"\n"+abilityTypes.get(n_abilities.get(i)), 10);
							newb.onOver = new UpdateTooltipHover(ab);
							newb.onUp = new ActivateOnClick(ab);
							intern_abilities.add(ab);
						}
						else if(abilityTypes.get(n_abilities.get(i)).contains("Restores HP")){
							Ability ab = new GenericHealAbility(abilityName+"\n"+abilityTypes.get(n_abilities.get(i)), 10);
							newb.onOver = new UpdateTooltipHover(ab);
							newb.onUp = new ActivateOnClick(ab);
							intern_abilities.add(ab);
						}
						newb.scrollFactor = RogueDream.NP;
						abilities.remove(old);
						abilities.add(newb);
						imagesToFind.add(n_abilities.get(i));
						threadTargets.put(n_abilities.get(i), "ability"+i);
						purge.add(false);
					}
				}
				if(((List<String>)dreamConcepts.get("items")).size() != 0){
					List<String> n_items = (List<String>) dreamConcepts.get("items");
					for(int i=0; i<Math.min(n_items.size(), 4); i++){
						FlxButton old = (FlxButton) items.members.get(i);
						FlxButton newb = new FlxButton(old.x, old.y, "");
						newb.makeGraphic(64, 64, RogueDream.RAND.nextInt() | 0xff000000);
						newb.onOut = new ClearTooltip();
						newb.onOver = new UpdateTooltipHover(new Item(n_items.get(i)));
						newb.scrollFactor = RogueDream.NP;
						items.remove(old);
						items.add(newb);
						imagesToFind.add(n_items.get(i));
						threadTargets.put(n_items.get(i), "item"+i);
						purge.add(false);
					}
				}
				if(((List<String>)dreamConcepts.get("goals")).size() != 0){
					List<String> n_goals = (List<String>) dreamConcepts.get("goals");
						Item item_grail = new Item(n_goals.get(0));
						item_grail.makeGraphic(64, 64, RogueDream.RAND.nextInt() | 0xff000000);
						imagesToFind.add(n_goals.get(0));
						threadTargets.put(n_goals.get(0), "goal");
						level.grail = item_grail;
						add(level.grail);
						purge.add(false);
				}
				else{
					if(((List<String>)dreamConcepts.get("items")).size() != 0){
						List<String> n_items = (List<String>) dreamConcepts.get("items");
						for(int i=0; i<Math.min(n_items.size(), 4); i++){
							FlxButton old = (FlxButton) items.members.get(i);
							FlxButton newb = new FlxButton(old.x, old.y, "");
							newb.makeGraphic(64, 64, RogueDream.RAND.nextInt() | 0xff000000);
							newb.onOut = new ClearTooltip();
							newb.onOver = new UpdateTooltipHover(new Item(n_items.get(i)));
							newb.scrollFactor = RogueDream.NP;
							items.remove(old);
							items.add(newb);
							imagesToFind.add(n_items.get(i));
							threadTargets.put(n_items.get(i), "item"+i);
							purge.add(false);
						}
					}
				}
				
				imagesToFind.add("door");
				threadTargets.put("door", "exit");
				purge.add(false);
			}
			else if(loadStage == 1 && imagesToFind.size() > 0){
				String target = threadTargets.get(imagesToFind.remove(0));
				if(target.startsWith("enemy")){
					int enemyType = Integer.valueOf(target.substring("enemy".length()));
					for(BasicEnemy e : level.enemies){
						if(e.ENEMY_TYPE == enemyType){
							e.name = ((List<String>)dreamConcepts.get("enemies")).get(0);
							e.setPixels(new TextureRegion(new Texture(Gdx.files.internal(target+".png"))));
						}
					}
				}
				else if(target.startsWith("ability")){
					int abilitynum = Integer.valueOf(target.substring("ability".length()));
					((FlxButton) abilities.members.get(abilitynum)).setPixels(new TextureRegion(new Texture(Gdx.files.internal(target+".png"))));
				}
				else if(target.startsWith("item")){
					int abilitynum = Integer.valueOf(target.substring("item".length()));
					((FlxButton) items.members.get(abilitynum)).setPixels(new TextureRegion(new Texture(Gdx.files.internal(target+".png"))));
				}
				else if(target.equalsIgnoreCase("exit")){
					exit.setPixels(new TextureRegion(new Texture(Gdx.files.internal("exit.png"))));
				}
			}
			
			if(imagesToFind.size() > 0){
				SpritelyThread st = new SpritelyThread(imagesToFind.get(0).trim(), "holy", threadTargets.get(imagesToFind.get(0)));
				st.purgeGoogle = purge.remove(0);
				worker = new Thread(st);
				worker.start();
			}
			
		}
		
		if(turnEnded)
			turnEnded = false;
		
		if(!player.alive){
			return;
		}
		
		if(player.x == exit.x && player.y == exit.y){
			if(FlxG.keys.justPressed("N")){
				//Descend
				
			}
		}
		
		BLOCKTYPE res = BLOCKTYPE.INVALID;
		if(FlxG.keys.justPressed("LEFT")){
			res = level.blocked(player.mx-1,player.my);
			if(res == BLOCKTYPE.NONE){
				player.mx--;
				player.x -= RogueDream.SIDE;
				endTurn();
			}
		}
		else if(FlxG.keys.justPressed("RIGHT")){
			res =  level.blocked(player.mx+1,player.my);
			if(res == BLOCKTYPE.NONE){
				player.mx++;
				player.x += RogueDream.SIDE;
				endTurn();
			}
		}
		else if(FlxG.keys.justPressed("UP")){
			res = level.blocked(player.mx,player.my-1);
			if(res == BLOCKTYPE.NONE){
				player.my--;
				player.y -= RogueDream.SIDE;
				endTurn();
			}
		}
		else if(FlxG.keys.justPressed("DOWN")){
			res = level.blocked(player.mx,player.my+1);
			if(res == BLOCKTYPE.NONE){
				player.my++;
				player.y += RogueDream.SIDE;
				endTurn();
			}
		}
		else if(FlxG.keys.justPressed("SPACE")){
			endTurn();
		}
		
		if(res == BLOCKTYPE.ENEMY){
			//This is really not very good code - I've not seen extra returns being stored 
			//within enums, and I think there are good reasons for that!
			player.attack(res.target);
			endTurn();
		}
		
		if(FlxG.keys.justPressed("I")){
			toggleInventory();
		}
		
		if(logLen != log.size()){
			String lt = "";
			for(int i=0; i<Math.min(4, log.size()); i++){
				lt = log.get(i)+"\n" + lt;
			}
			logText.setText(lt);
		}
	}

	private void toggleInventory() {
		if(!showingInventory){
			for(FlxBasic b : abilities.members){
				((FlxButton)b).y += 128;
			}
			for(FlxBasic b : items.members){
				((FlxButton)b).y -= 128;
			}
		}
		else{
			for(FlxBasic b : abilities.members){
				((FlxButton)b).y -= 128;
			}
			for(FlxBasic b : items.members){
				((FlxButton)b).y += 128;
			}
		}
		showingInventory = !showingInventory;
		
	}

	public void endTurn() {
		turnCount++;
		turnCounter.setText("The "+player.playerclass+"'s Dream - Turn "+turnCount);
		
		turnEnded = true;
		
		if(verbose)
			System.out.println("Turn ended!");
		for(BasicEnemy e : level.enemies){
			e.doTurn();
		}
		
		for(Ability a : intern_abilities){
			if(a.cooldown > 0){
				a.cooldown--;
			}
		}
		
		statCounter.setText("HP - "+player.hp);
	}
	
	public static void log(String msg){
		log.add(0, msg+" [Turn "+state.turnCount+"]");
	}

	public static void setDream(String text) {
		dream = text;
	}
	
}
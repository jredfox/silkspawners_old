package com.EvilNotch.silkspawners;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.event.world.WorldEvent;

public class WorldLoadEvent {
	
	public static List<String> DungeonisRight = new ArrayList<String>();
	public static List<String> clear = new ArrayList<String>();
	public static Map<String, String> clearhash = new HashMap<String, String>();
	public static World world2;
	
	private boolean checkspawner = false;
	

	 @SubscribeEvent
	 public void onWorldLoad(WorldEvent.Load event) throws IOException
	 {
		 World world = DimensionManager.getWorld(0);
		 world2 = world;
		 if (config.ModdedSpawners == false && !event.world.isRemote)
			 return;
		 
		 if (event.world == world)
		 { 	
			 	//Doesn't Clear DungeonWrongList as that list is the core list that all other lists fallow It will Reset When The Game Relaunches
			 		SilkSpawners.regenFiles(); //Regenerates any Empty Files Crashes if a program interfears
			 		
				 //Forces Dungeon Hooks To Load My Mod?
				 new DungeonHooks();
				 
				//Removes vanilla so configurations are accurate when configging
				if (config.ModdedSpawners == true)
				{
					DungeonHooks.removeDungeonMob("Zombie");
				    DungeonHooks.removeDungeonMob("Spider");
					DungeonHooks.removeDungeonMob("Skeleton");
					DungeonHooks.addDungeonMob("Skeleton", 1);
				}
				

				Iterator pre = MainJava.DungeonWrongList.iterator();
				//takes the dungeon entity list from main java and transforms it to usable code for the array list for further use
				   while (pre.hasNext())
				   {
					   String it = pre.next().toString();
					   Entity entity = EntityList.createEntityByName(it, event.world);
					   String raw = EntityLookUp.getEntityMod(entity);
					   String[] parts = raw.split("\u00A9");
					   String modid = parts[1];
					   String add = modid + "\u00A9" + it;
					   DungeonisRight.add(add);
					   MainJava.DungeonHashMap.put(it, modid);
					   
					   //Clears Current Dungeon Hash Map For Multiple World Instances
					   DungeonHooks.removeDungeonMob(it);
				   }

				   //System.out.println("Dugeon ArrayList:" + DungeonisRight);
				   //System.out.println("Dugeon Hash Map:" + MainJava.DungeonHashMap);
				   
				   //Creates any files if they don't exsist along with adding the files to the file array list for writing entites to...
				   Iterator dmk = DungeonisRight.iterator();
				   
				   while(dmk.hasNext())
				   {
				   String jk = dmk.next().toString();
				   String[] jkml = jk.split("\u00A9");
				   String modid = jkml[0];
				   String EntityType = jkml[1];
				  
				   String output = SilkSpawners.toFileCharacters(modid) + ".txt";
				   File filemodid = new File(config.DungeonTweeks, output); //They're both strings
				   if (filemodid.exists() == false)
				   {
				     filemodid.createNewFile();
					 FileWriter outit = new FileWriter(filemodid.getAbsoluteFile(), true);
					 outit.write("#Dungeon Tweeks Config For: " + modid + "\r\n#You can Config the Chances or Just Disable Modded Spawners and Vanilla Spawners In The Config\r\n\r\n");
					 outit.close();
					 MainJava.FileList.add(modid);
					 MainJava.FileHashBrown.put(modid, "");
				   }

					  
				   if (filemodid.exists() == true && !MainJava.FileHashBrown.containsKey(modid))
				   {
					   MainJava.FileList.add(modid);
					   MainJava.FileHashBrown.put(modid, "");
				   }
   
				 }
				   
				   //removes any entities to prevent dupes
	               Iterator file = MainJava.FileList.iterator();
	               while (file.hasNext())
	               {
	               String output = file.next().toString();
	               output = SilkSpawners.toFileCharacters(output) + ".txt";
	               File filemodid = new File(config.DungeonTweeks, output);
	               Scanner fileIn = new Scanner(filemodid);
	               
	                while (fileIn.hasNextLine())
	                {
	                    String line = fileIn.nextLine().toString();
	                    
	                    String WhiteSpaced = line.replaceAll("\\s+", "");
	                    
	                    if (WhiteSpaced.contains("="))
	                    {
	                    	String[] splitmodid = WhiteSpaced.split("=");
	                    	String entity = splitmodid[0];
	                    	
	                    if(MainJava.DungeonHashMap.containsKey(entity))
	                    	MainJava.DungeonHashMap.remove(entity);
	                    
	                    //Preventative if there is unnecessary spacing in there....
	                    else{
	                    	Iterator itnow = MainJava.DungeonWrongList.iterator();
	                    		while(itnow.hasNext())
	                    		{
	                    			String s = itnow.next().toString();
	                    			String ssr = s.replaceAll("\\s+", "");
	                    				if(ssr.equals(entity))
	                    				{
	                    					MainJava.DungeonHashMap.remove(s);
	                    					System.out.println("Removed STDSpacedOUt:\t" + s);
	                    				}
	                    		}
	                    	}
	                    }
	                 }
	               
	                fileIn.close();
	               }
	               
	               Map<String, String> treeMap = new TreeMap<String, String>(MainJava.DungeonHashMap);

	                Iterator dmh = treeMap.entrySet().iterator();
	                
	                //Prints orginized hashmap (tree map orginizes hashmap)
	               // System.out.println("AAAAAAAAAAFTEEEEEEEEEEEER Dungeon Hash Map<String,String>: " + treeMap);
	                
	                while (dmh.hasNext())
	                {
	                    Map.Entry pair2 = (Map.Entry)dmh.next();
	                    String entityn = pair2.getKey().toString();
	                    String modidn = pair2.getValue().toString();
	                    String minecraft = "minecraft";
	                    
	                    
	                    String modidfile = SilkSpawners.toFileCharacters(modidn) + ".txt";
	                    File filemodidn = new File(config.DungeonTweeks, modidfile);
	                    FileWriter out = new FileWriter(filemodidn.getAbsoluteFile(), true);
	                    
	                    if (!modidn.equals("minecraft") )
	                    {
	                       out.write("\r\n" + entityn + " = " + config.ModdedSpawnerChance);
	                       out.close();
	                    }
	                    
	                    if (modidn.equals("minecraft"))
	                    {
	                        if (entityn.equals("Bat"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "10");
	                           }
	                           else if (entityn.equals("Blaze"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "25");
	                           }
	                           else if (entityn.equals("CaveSpider"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "100");
	                           }
	                           else if (entityn.equals("Chicken"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "7");
	                           }
	                           else if (entityn.equals("Cow"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "15");
	                           }
	                           else if (entityn.equals("Creeper"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "110");
	                           }
	                           else if (entityn.equals("EnderDragon"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "1");
	                           }
	                           else if (entityn.equals("Enderman"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "100");
	                           }
	                           else if (entityn.equals("EntityHorse"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "12");
	                           }
	                           else if (entityn.equals("Ghast"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "15");
	                           }
	                           else if (entityn.equals("Giant"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "5");
	                           }
	                           else if (entityn.equals("LavaSlime"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "15");
	                           }
	                           else if (entityn.equals("MushroomCow"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "10");
	                           }
	                           else if (entityn.equals("Ozelot"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "20");
	                           }
	                           else if (entityn.equals("Pig"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "10");
	                           }
	                           else if (entityn.equals("PigZombie"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "20");
	                           }
	                           else if (entityn.equals("Sheep"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "12");
	                           }
	                           else if (entityn.equals("Silverfish"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "20");
	                           }
	                           else if (entityn.equals("Skeleton"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "100");
	                           }
	                           else if (entityn.equals("Slime"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "70");
	                           }
	                           else if (entityn.equals("SnowMan"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "12");
	                           }
	                           else if (entityn.equals("Spider"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "80");
	                           }
	                           else if (entityn.equals("Squid"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "10");
	                           }
	                           else if (entityn.equals("Villager"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "5");
	                           }
	                           else if (entityn.equals("VillagerGolem"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "10");
	                           }
	                           else if (entityn.equals("Witch"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "70");
	                           }
	                           else if (entityn.equals("WitherBoss"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "10");
	                           }
	                           else if (entityn.equals("Wolf"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "20");
	                           }
	                           else if (entityn.equals("Zombie"))
	                           {
	                           out.write("\r\n" + entityn + " = " + "130");
	                           }
	                           else{
	                               out.write("\r\n" + entityn + " = " + config.ModdedSpawnerChance);
	                           }
	                           out.close();
	                        }
	                    
	                     }
				   
	                
	                	//BlackListing And WhiteListing
	           //___________________________________________________\\
	              
	                	//Creates WhiteListingByModId
	                   FileConstructer.ModWhiteList(event);
	                   FileConstructer.readModWhiteList(event);
	                   
	                   //removes WhiteList's unnecessary objects
	                   MainJava.ModIdWhiteList.remove("<ModIdWhiteList>");
	                   MainJava.ModIdWhiteList.remove("</ModIdWhiteList>");
	                   MainJava.ModIdWhiteList.remove("");
	                   MainJava.ModIdWhiteList.remove("");
	                   
	                   //Sets the file list = to the whitelistbymodid if enabled
	                   if (config.WhiteListByModId == true)
	                   {
	                	   MainJava.FileList = MainJava.ModIdWhiteList;
	                   }
	                   
	                   //Black List Mod By Modid
	                   FileConstructer.ModBlackList(event);
	                   FileConstructer.readModBlackList(event);
	                   
	                   //removes modidblacklist unessasary objects
	                   MainJava.ModIdBlackList.remove("<ModIdBlackList>");
	                   MainJava.ModIdBlackList.remove("</ModIdBlackList>");
	                   
	                   
	                   
	                   //Removal Of Mod Files From Dungeon Hooks
	                   Iterator itmod = MainJava.ModIdBlackList.iterator();
	                   if (config.BlackListByModID == true)
	                   {
	                   while (itmod.hasNext())
	                   {
	                       String itmodid = itmod.next().toString();
	                       MainJava.FileList.remove(itmodid);
	                       if (itmodid.equals("minecraft"))
	                       {
	                           checkspawner = true;
	                       }
	                   }
	                   
	                   }
	                   
	                 //fixes vanilla spawners if vanilla is now blacklisted by modid
	                   if (checkspawner == true && config.ModdedSpawners == true)
	                   {
	                       DungeonHooks.addDungeonMob("Skeleton", 100);
	                       DungeonHooks.addDungeonMob("Zombie", 200);
	                       DungeonHooks.addDungeonMob("Spider", 100);

	                       //sets it to false to prevent higher chances of the mobs from spawning
	                       checkspawner = false;
	                   }

	                   
	                //Adds Them To Dungeon Hooks\\
	           //____________________________________\\
	                   
		            //BlackList && WhiteList Generates Files
		             FileConstructer.Blacklist(event);
		             FileConstructer.Whitelist(event);
		                
		                 
		            //READS THEM   
		             FileConstructer.readBlackList(event);
		             FileConstructer.readWhiteList(event);
		             
	                 //Removes unnecessary objects 
	                 MainJava.DungeonBlackList.remove("");
	                 MainJava.DungeonBlackList.remove("<BlackList>");
	                 MainJava.DungeonBlackList.remove("</BlackList>");
	                   
	                Iterator fileit = MainJava.FileList.iterator();
	               // System.out.println("File List: " + MainJava.FileList);
	                while (fileit.hasNext())
	                   {
	                    

	                   String outputit = fileit.next().toString();
	                   outputit = SilkSpawners.toFileCharacters(outputit) + ".txt";
	                   File filemodid2 = new File(config.DungeonTweeks, outputit);
	                   Scanner fileIn1 = new Scanner(filemodid2);
	                while (fileIn1.hasNextLine())
	                {
	                    String line = fileIn1.nextLine().toString();
	                    
	                    String WhiteSpaced = line.replaceAll("\\s+", "");
	                    
	                if (WhiteSpaced.contains("="))
	                {
	                    String[] splitmodid = WhiteSpaced.split("=");
	                    String entity = splitmodid[0];
	                    String stringchance = splitmodid[1];
	                    int chance = Integer.parseInt(stringchance);
	                    
	                    if (entity.equals("Skeleton"))
	                    {
	                    	if (chance > 1 && config.whiteList == false)
	                    	{
	                    		chance -=1;
	                    		DungeonHooks.addDungeonMob(entity, chance);
	                    	}
	                    	else{
	                    		if (config.whiteList == false)
	                    		DungeonHooks.addDungeonMob(entity, chance);
	                    	}
	                    	
	                    }
	                    
	                    if (config.whiteList == false && !entity.equals("Skeleton"))
	                    {
	                       DungeonHooks.addDungeonMob(entity, chance);
	                    }
	                }
	                
	                 }
	                fileIn1.close();
	                
	            }

	                 if (config.whiteList == true)
	                 {
	                     Iterator itwhite = MainJava.DungeonWhiteList.iterator();
	                     while (itwhite.hasNext())
	                     {
	                            String linewhite = itwhite.next().toString();
	                            String[] splitline = linewhite.split("=");
	                            String white_entity = splitline[0];
	                            String stringchancewhite = splitline[1];
	                            int whitechance = Integer.parseInt(stringchancewhite);
	                            if (white_entity.equals("Skeleton"))
	    	                    {
	    	                    	if (whitechance > 1)
	    	                    	{
	    	                    		whitechance -=1;
	    	                    		DungeonHooks.addDungeonMob(white_entity, whitechance);
	    	                    	}
	    	                    	else{
	    	                    		DungeonHooks.addDungeonMob(white_entity, whitechance);
	    	                    	}	
	    	                    }
	                            if (!white_entity.equals("Skeleton"))
	                            DungeonHooks.addDungeonMob(white_entity, whitechance);
	                     }
	                 }
	                 
	                 Iterator itreadBlack = MainJava.DungeonBlackList.iterator();
	                 while (itreadBlack.hasNext())
	                 {
	                     String BlackList = itreadBlack.next().toString();
	                     if (config.blackList == true)
	                     {
	                    	 DungeonHooks.removeDungeonMob(BlackList);
	                     }
	                     
	                 }
	                 System.out.println("File List:\t" + MainJava.FileList);
				 	 //Clear
				 	DungeonisRight.clear();
				 	MainJava.FileList.clear();
				 	MainJava.ModIdWhiteList.clear();
				 	MainJava.ModIdBlackList.clear();
				 	MainJava.DungeonWhiteList.clear();
				 	MainJava.DungeonBlackList.clear();
				 	MainJava.FileHashBrown.clear();
				 	MainJava.DungeonHashMap.clear();
				 	clear.clear();
					checkspawner = false;
		 }	
	 }

}
package com.EvilNotch.silkspawners;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.WorldEvent;

public class FileConstructer {
	
	
	public static String Path;
	public static File file;
	public static File file2;
	public static FileWriter out1;
	public static File fileBlackList;
	public static File fileWhiteList;
	public static File ModBlackList;
	public static File ModWhiteList;
	public static File hardfile;
	public static File Resistence;
	public static File Light;
	public static File Harvest;
	public static File tool;
	public static File DisableStats;
	private static File DisableDrops;
	public static File efile;
	public static File QuantityDropped;
	public static File EfileOverride;
	private static File ITool;
	public static File AutoSpawnerBlacklist;

	
	public static void loadconfig (FMLPreInitializationEvent event) throws IOException
	{
	File configdirectory = config.configdirectory;

	
	
	  
	    file = new File(config.SilkSpawners, "DefineSpawners.txt"); //theyre both strings
	   if (file.exists() == false)
	   {
	   file.createNewFile();
	   FileWriter out = new FileWriter(file.getAbsoluteFile(), true);
	   out.write("#Define Spawners here. It's the same you would use in the /give command.\r\n#The parameters are modid:block\r\n\r\n\r\n");
	   out.write("//Config Can Be used to Drop Any Block With Silk Touch Regardless if it is a TileEntiyMobSpawner or not (anyblock that means)\r\n\r\n");
	   out.write("<Spawner>\r\n");
	   out.write("minecraft:mob_spawner\r\n");
	   out.write("minecraft:end_portal_frame\r\n");
	   out.write("silkspawners:CaveSpawner\r\n");
	   out.write("silkspawners:CustomMobSpawner\r\n");
	   out.write("silkspawners:End_Supreme_Spawner\r\n");
	   out.write("silkspawners:EndSpawner\r\n");
	   out.write("silkspawners:ForestSpawner\r\n");
	   out.write("silkspawners:IceSpawner\r\n");
	   out.write("silkspawners:NetherSpawner\r\n");
	   out.write("silkspawners:RustedSpawner\r\n");
	   out.write("silkspawners:SnowSpawner\r\n");
	   out.write("silkspawners:chocoSpawner\r\n");
	   out.write("DraconicEvolution:customSpawner\r\n");
	   out.write("HardcoreEnderExpansion:custom_spawner\r\n\r\n");
	   out.write("//DivineRPG Support\r\n");
	   out.write("divinerpg:biphronSpawner\r\n");
	   out.write("divinerpg:deathcryxSpawner\r\n");
	   out.write("divinerpg:deathHoundSpawner\r\n");
	   out.write("divinerpg:dreamWreckerSpawner\r\n");
	   out.write("divinerpg:dungeonPrisonerSpawner\r\n");
	   out.write("divinerpg:enthralledDramcryxSpawner\r\n");
	   out.write("divinerpg:frostArcherSpawner\r\n");
	   out.write("divinerpg:gorgosionSpawner\r\n");
	   out.write("divinerpg:livingStatueSpawner\r\n");
	   out.write("divinerpg:razorbackSpawner\r\n");
	   out.write("divinerpg:roamerSpawner\r\n");
	   out.write("divinerpg:rollumSpawner\r\n");
	   out.write("divinerpg:rotatickSpawner\r\n");
	   out.write("divinerpg:twinsSpawner\r\n");
	   out.write("divinerpg:vermenousSpawner\r\n\r\n");
	   out.write("//Advent Of Ascension Support\r\n");
	   out.write("nevermine:spawnerAmphibior\r\n");
	   out.write("nevermine:spawnerAmphibiyte\r\n");
	   out.write("nevermine:spawnerArkzyne\r\n");
	   out.write("nevermine:spawnerArocknid\r\n");
	   out.write("nevermine:spawnerAutomaton\r\n");
	   out.write("nevermine:spawnerBaumba\r\n");
	   out.write("nevermine:spawnerBloodsucker\r\n");
	   out.write("nevermine:spawnerCaneBug\r\n");
	   out.write("nevermine:spawnerCaveBug\r\n");
	   out.write("nevermine:spawnerCenturion\r\n");
	   out.write("nevermine:spawnerCrusilisk\r\n");
	   out.write("nevermine:spawnerDawnlight\r\n");
	   out.write("nevermine:spawnerDaysee\r\n");
	   out.write("nevermine:spawnerEnforcer\r\n");
	   out.write("nevermine:spawnerExohead\r\n");
	   out.write("nevermine:spawnerFacelessFloater\r\n");
	   out.write("nevermine:spawnerFade\r\n");
	   out.write("nevermine:spawnerFenix\r\n");
	   out.write("nevermine:spawnerFlesh\r\n");
	   out.write("nevermine:spawnerFlowerface\r\n");
	   out.write("nevermine:spawnerFungock\r\n");
	   out.write("nevermine:spawnerGhastus\r\n");
	   out.write("nevermine:spawnerGingerbird\r\n");
	   out.write("nevermine:spawnerGingerbreadMan\r\n");
	   out.write("nevermine:spawnerGoldem\r\n");
	   out.write("nevermine:spawnerGoldus\r\n");
	   out.write("nevermine:spawnerHaunted1\r\n");
	   out.write("nevermine:spawnerHaunted2\r\n");
	   out.write("nevermine:spawnerHaunted3\r\n");
	   out.write("nevermine:spawnerHaunted4\r\n");
	   out.write("nevermine:spawnerHaven\r\n");
	   out.write("nevermine:spawnerHaven2\r\n");
	   out.write("nevermine:spawnerHaven3\r\n");
	   out.write("nevermine:spawnerInmateX\r\n");
	   out.write("nevermine:spawnerInmateY\r\n");
	   out.write("nevermine:spawnerJawe\r\n");
	   out.write("nevermine:spawnerKaiyu\r\n");
	   out.write("nevermine:spawnerMechyon\r\n");
	   out.write("nevermine:spawnerMegaGuardian\r\n");
	   out.write("nevermine:spawnerMerkyre\r\n");
	   out.write("nevermine:spawnerMermage\r\n");
	   out.write("nevermine:spawnerMushroomSpider\r\n");
	   out.write("nevermine:spawnerNethengeic\r\n");
	   out.write("nevermine:spawnerNightwing\r\n");
	   out.write("nevermine:spawnerOpteryx\r\n");
	   out.write("nevermine:spawnerParavite\r\n");
	   out.write("nevermine:spawnerPodPlant\r\n");
	   out.write("nevermine:spawnerPoseido\r\n");
	   out.write("nevermine:spawnerPrecasian\r\n");
	   out.write("nevermine:spawnerPrecasian2\r\n");
	   out.write("nevermine:spawnerPrecasian3\r\n");
	   out.write("nevermine:spawnerRawbone\r\n");
	   out.write("nevermine:spawnerReaver\r\n");
	   out.write("nevermine:spawnerRefluct\r\n");
	   out.write("nevermine:spawnerRunic\r\n");
	   out.write("nevermine:spawnerRunicGuardian\r\n");
	   out.write("nevermine:spawnerSceptron\r\n");
	   out.write("nevermine:spawnerShavo\r\n");
	   out.write("nevermine:spawnerSkeledon\r\n");
	   out.write("nevermine:spawnerSkelekyte\r\n");
	   out.write("nevermine:spawnerSpectralWizard\r\n");
	   out.write("nevermine:spawnerTharafly\r\n");
	   out.write("nevermine:spawnerTorano\r\n");
	   out.write("nevermine:spawnerUrioh\r\n");
	   out.write("nevermine:spawnerUrv\r\n");
	   out.write("nevermine:spawnerVineWizard\r\n");
	   out.write("nevermine:spawnerVisage\r\n");
	   out.write("nevermine:spawnerZarg\r\n");
	   out.write("nevermine:spawnerZhinx\r\n");
	   out.write("nevermine:spawnerZorp\r\n");
	   out.write("</Spawner>\r\n");
	   out.close();
	   }
	}
	

	
	   public static void Blacklist(WorldEvent.Load event) throws IOException
		{
		   fileBlackList = new File(config.SilkSpawners, "BlackList.txt"); //theyre both strings
		   
		   if (fileBlackList.exists() == false)
		   {
			   
			 fileBlackList.createNewFile();
			 FileWriter Black = new FileWriter(fileBlackList.getAbsoluteFile(), true);
			 Black.write("#Dungeon BlackList The Parameters are EntityId\r\n#To Get EntityId's Refer To Dungeon Tweaks Config File For Vanilla Refer To Main Config\r\n\r\n<BlackList>\r\n\r\n</BlackList>");
			 Black.close();
			//outBlack.write("#Config Dungeon BlackList\r\nThe Parameters are EntityId \r\nEntityHorse\r\n#To Comment use the #");

		   }
		}
	   
	   public static void ModBlackList(WorldEvent.Load event) throws IOException
		{
		    ModBlackList = new File(config.SilkSpawners, "ModBlackList.txt"); //theyre both strings
		   
		   if (ModBlackList.exists() == false)
		   {
			   
			   ModBlackList.createNewFile();
			 FileWriter ModBlack = new FileWriter(ModBlackList.getAbsoluteFile(), true);
			 ModBlack.write("#Mod BlackList The Parameters are Mod Id To Ban An Entire Mod From DungeonHooks\r\n\r\n<ModIdBlackList>\r\n\r\n</ModIdBlackList>");
			 ModBlack.close();
			
		   }
		}
	   
	   public static void ModWhiteList(WorldEvent.Load event) throws IOException
		{
		   ModWhiteList = new File(config.SilkSpawners, "ModWhiteList.txt"); //theyre both strings
		   
		   if (ModWhiteList.exists() == false)
		   {
			   
			   ModWhiteList.createNewFile();
			 FileWriter ModWhite = new FileWriter(ModWhiteList.getAbsoluteFile(), true);
			 ModWhite.write("#Mod ModWhiteList The Parameters are Mod Id To Use and only Use Specified Mods An Entire Mod From DungeonHooks\r\n#Note That It Will Still use things from the WhiteList\r\n\r\n<ModIdWhiteList>\r\n\r\n</ModIdWhiteList>");
			 ModWhite.close();
			
		   }
		}
		   public static void Whitelist(WorldEvent.Load event) throws IOException
			{
			   fileWhiteList = new File(config.SilkSpawners, "WhiteList.txt"); //theyre both strings
			   
			   if (fileWhiteList.exists() == false)
			   {
				   fileWhiteList.createNewFile();
				   FileWriter WhiteOut = new FileWriter(fileWhiteList.getAbsoluteFile(), true);
				   WhiteOut.write("#Use whitelist for only specifed entities to get into dungeon hooks. You need to enable whitelist in the config\r\n#The Parameters are EntityId = chance\r\n\r\n<WhiteList>\r\n\r\n</WhiteList>");
				   WhiteOut.close();
			      
			   }
	   
		     }
		   
		   //Load Block Properties
		   public static void loadhardness (FMLPreInitializationEvent event) throws IOException
			{
			 hardfile = new File(config.BlockProperties, "Hardness.txt"); //theyre both strings
			   if (hardfile.exists() == false)
			   {
			   hardfile.createNewFile();
			   FileWriter out = new FileWriter(hardfile.getAbsoluteFile(), true);
			   out.write("#Block Properties For Hardness\r\n#The Parameters Are modid:block = whole_number\r\n\r\n<Hardness>\r\n");
			   out.write("minecraft:mob_spawner = 5\r\n");
			   out.write("minecraft:end_portal_frame = 5\r\n");
			   out.write("silkspawners:CaveSpawner = 5\r\n");
			   out.write("silkspawners:CustomMobSpawner = 5\r\n");
			   out.write("silkspawners:End_Supreme_Spawner = 5\r\n");
			   out.write("silkspawners:EndSpawner = 5\r\n");
			   out.write("silkspawners:ForestSpawner = 5\r\n");
			   out.write("silkspawners:IceSpawner = 5\r\n");
			   out.write("silkspawners:NetherSpawner = 5\r\n");
			   out.write("silkspawners:RustedSpawner = 5\r\n");
			   out.write("silkspawners:SnowSpawner = 5\r\n");
			   out.write("silkspawners:chocoSpawner = 5\r\n");
			   out.write("DraconicEvolution:customSpawner = 5\r\n");
			   out.write("HardcoreEnderExpansion:custom_spawner = 5\r\n\r\n");
			   out.write("//DivineRPG Support\r\n");
			   out.write("divinerpg:biphronSpawner = 5\r\n");
			   out.write("divinerpg:deathcryxSpawner = 5\r\n");
			   out.write("divinerpg:deathHoundSpawner = 5\r\n");
			   out.write("divinerpg:dreamWreckerSpawner = 5\r\n");
			   out.write("divinerpg:dungeonPrisonerSpawner = 5\r\n");
			   out.write("divinerpg:enthralledDramcryxSpawner = 5\r\n");
			   out.write("divinerpg:frostArcherSpawner = 5\r\n");
			   out.write("divinerpg:gorgosionSpawner = 5\r\n");
			   out.write("divinerpg:livingStatueSpawner = 5\r\n");
			   out.write("divinerpg:razorbackSpawner = 5\r\n");
			   out.write("divinerpg:roamerSpawner = 5\r\n");
			   out.write("divinerpg:rollumSpawner = 5\r\n");
			   out.write("divinerpg:rotatickSpawner = 5\r\n");
			   out.write("divinerpg:twinsSpawner = 5\r\n");
			   out.write("divinerpg:vermenousSpawner = 5\r\n\r\n");
			   out.write("//Advent Of Ascension Support\r\n");
			   out.write("nevermine:spawnerAmphibior = 5\r\n");
			   out.write("nevermine:spawnerAmphibiyte = 5\r\n");
			   out.write("nevermine:spawnerArkzyne = 5\r\n");
			   out.write("nevermine:spawnerArocknid = 5\r\n");
			   out.write("nevermine:spawnerAutomaton = 5\r\n");
			   out.write("nevermine:spawnerBaumba = 5\r\n");
			   out.write("nevermine:spawnerBloodsucker = 5\r\n");
			   out.write("nevermine:spawnerCaneBug = 5\r\n");
			   out.write("nevermine:spawnerCaveBug = 5\r\n");
			   out.write("nevermine:spawnerCenturion = 5\r\n");
			   out.write("nevermine:spawnerCrusilisk = 5\r\n");
			   out.write("nevermine:spawnerDawnlight = 5\r\n");
			   out.write("nevermine:spawnerDaysee = 5\r\n");
			   out.write("nevermine:spawnerEnforcer = 5\r\n");
			   out.write("nevermine:spawnerExohead = 5\r\n");
			   out.write("nevermine:spawnerFacelessFloater = 5\r\n");
			   out.write("nevermine:spawnerFade = 5\r\n");
			   out.write("nevermine:spawnerFenix = 5\r\n");
			   out.write("nevermine:spawnerFlesh = 5\r\n");
			   out.write("nevermine:spawnerFlowerface = 5\r\n");
			   out.write("nevermine:spawnerFungock = 5\r\n");
			   out.write("nevermine:spawnerGhastus = 5\r\n");
			   out.write("nevermine:spawnerGingerbird = 5\r\n");
			   out.write("nevermine:spawnerGingerbreadMan = 5\r\n");
			   out.write("nevermine:spawnerGoldem = 5\r\n");
			   out.write("nevermine:spawnerGoldus = 5\r\n");
			   out.write("nevermine:spawnerHaunted1 = 5\r\n");
			   out.write("nevermine:spawnerHaunted2 = 5\r\n");
			   out.write("nevermine:spawnerHaunted3 = 5\r\n");
			   out.write("nevermine:spawnerHaunted4 = 5\r\n");
			   out.write("nevermine:spawnerHaven = 5\r\n");
			   out.write("nevermine:spawnerHaven2 = 5\r\n");
			   out.write("nevermine:spawnerHaven3 = 5\r\n");
			   out.write("nevermine:spawnerInmateX = 5\r\n");
			   out.write("nevermine:spawnerInmateY = 5\r\n");
			   out.write("nevermine:spawnerJawe = 5\r\n");
			   out.write("nevermine:spawnerKaiyu = 5\r\n");
			   out.write("nevermine:spawnerMechyon = 5\r\n");
			   out.write("nevermine:spawnerMegaGuardian = 5\r\n");
			   out.write("nevermine:spawnerMerkyre = 5\r\n");
			   out.write("nevermine:spawnerMermage = 5\r\n");
			   out.write("nevermine:spawnerMushroomSpider = 5\r\n");
			   out.write("nevermine:spawnerNethengeic = 5\r\n");
			   out.write("nevermine:spawnerNightwing = 5\r\n");
			   out.write("nevermine:spawnerOpteryx = 5\r\n");
			   out.write("nevermine:spawnerParavite = 5\r\n");
			   out.write("nevermine:spawnerPodPlant = 5\r\n");
			   out.write("nevermine:spawnerPoseido = 5\r\n");
			   out.write("nevermine:spawnerPrecasian = 5\r\n");
			   out.write("nevermine:spawnerPrecasian2 = 5\r\n");
			   out.write("nevermine:spawnerPrecasian3 = 5\r\n");
			   out.write("nevermine:spawnerRawbone = 5\r\n");
			   out.write("nevermine:spawnerReaver = 5\r\n");
			   out.write("nevermine:spawnerRefluct = 5\r\n");
			   out.write("nevermine:spawnerRunic = 5\r\n");
			   out.write("nevermine:spawnerRunicGuardian = 5\r\n");
			   out.write("nevermine:spawnerSceptron = 5\r\n");
			   out.write("nevermine:spawnerShavo = 5\r\n");
			   out.write("nevermine:spawnerSkeledon = 5\r\n");
			   out.write("nevermine:spawnerSkelekyte = 5\r\n");
			   out.write("nevermine:spawnerSpectralWizard = 5\r\n");
			   out.write("nevermine:spawnerTharafly = 5\r\n");
			   out.write("nevermine:spawnerTorano = 5\r\n");
			   out.write("nevermine:spawnerUrioh = 5\r\n");
			   out.write("nevermine:spawnerUrv = 5\r\n");
			   out.write("nevermine:spawnerVineWizard = 5\r\n");
			   out.write("nevermine:spawnerVisage = 5\r\n");
			   out.write("nevermine:spawnerZarg = 5\r\n");
			   out.write("nevermine:spawnerZhinx = 5\r\n");
			   out.write("nevermine:spawnerZorp = 5\r\n");
			   out.write("</Hardness>\r\n");
			   out.close();
			   }
			}
		   
		   public static void loadResistence(FMLPreInitializationEvent event) throws IOException
			{
			 Resistence = new File(config.BlockProperties, "BlastResistance.txt"); //theyre both strings Resistance
			   if (Resistence.exists() == false)
			   {
				   Resistence.createNewFile();
				   FileWriter out = new FileWriter(Resistence.getAbsoluteFile(), true);
				   out.write("#Block Properties For Blast Resistance\r\n#The Parameters Are modid:block = Resistence\r\n#To Make A Block BlastProof Set it To 6000000\r\n\r\n<Resistance>\r\n");
				   out.write("minecraft:mob_spawner = 12\r\n");
				   out.write("minecraft:end_portal_frame = 12\r\n");
				   out.write("silkspawners:CaveSpawner = 12\r\n");
				   out.write("silkspawners:CustomMobSpawner = 12\r\n");
				   out.write("silkspawners:End_Supreme_Spawner = 12\r\n");
				   out.write("silkspawners:EndSpawner = 12\r\n");
				   out.write("silkspawners:ForestSpawner = 12\r\n");
				   out.write("silkspawners:IceSpawner = 12\r\n");
				   out.write("silkspawners:NetherSpawner = 12\r\n");
				   out.write("silkspawners:RustedSpawner = 12\r\n");
				   out.write("silkspawners:SnowSpawner = 12\r\n");
				   out.write("silkspawners:chocoSpawner = 12\r\n");
				   out.write("DraconicEvolution:customSpawner = 12\r\n");
				   out.write("HardcoreEnderExpansion:custom_spawner = 12\r\n\r\n");
				   out.write("//DivineRPG Support\r\n");
				   out.write("divinerpg:biphronSpawner = 12\r\n");
				   out.write("divinerpg:deathcryxSpawner = 12\r\n");
				   out.write("divinerpg:deathHoundSpawner = 12\r\n");
				   out.write("divinerpg:dreamWreckerSpawner = 12\r\n");
				   out.write("divinerpg:dungeonPrisonerSpawner = 12\r\n");
				   out.write("divinerpg:enthralledDramcryxSpawner = 12\r\n");
				   out.write("divinerpg:frostArcherSpawner = 12\r\n");
				   out.write("divinerpg:gorgosionSpawner = 12\r\n");
				   out.write("divinerpg:livingStatueSpawner = 12\r\n");
				   out.write("divinerpg:razorbackSpawner = 12\r\n");
				   out.write("divinerpg:roamerSpawner = 12\r\n");
				   out.write("divinerpg:rollumSpawner = 12\r\n");
				   out.write("divinerpg:rotatickSpawner = 12\r\n");
				   out.write("divinerpg:twinsSpawner = 12\r\n");
				   out.write("divinerpg:vermenousSpawner = 12\r\n\r\n");
				   out.write("//Advent Of Ascension Support\r\n");
				   out.write("nevermine:spawnerAmphibior = 12\r\n");
				   out.write("nevermine:spawnerAmphibiyte = 12\r\n");
				   out.write("nevermine:spawnerArkzyne = 12\r\n");
				   out.write("nevermine:spawnerArocknid = 12\r\n");
				   out.write("nevermine:spawnerAutomaton = 12\r\n");
				   out.write("nevermine:spawnerBaumba = 12\r\n");
				   out.write("nevermine:spawnerBloodsucker = 12\r\n");
				   out.write("nevermine:spawnerCaneBug = 12\r\n");
				   out.write("nevermine:spawnerCaveBug = 12\r\n");
				   out.write("nevermine:spawnerCenturion = 12\r\n");
				   out.write("nevermine:spawnerCrusilisk = 12\r\n");
				   out.write("nevermine:spawnerDawnlight = 12\r\n");
				   out.write("nevermine:spawnerDaysee = 12\r\n");
				   out.write("nevermine:spawnerEnforcer = 12\r\n");
				   out.write("nevermine:spawnerExohead = 12\r\n");
				   out.write("nevermine:spawnerFacelessFloater = 12\r\n");
				   out.write("nevermine:spawnerFade = 12\r\n");
				   out.write("nevermine:spawnerFenix = 12\r\n");
				   out.write("nevermine:spawnerFlesh = 12\r\n");
				   out.write("nevermine:spawnerFlowerface = 12\r\n");
				   out.write("nevermine:spawnerFungock = 12\r\n");
				   out.write("nevermine:spawnerGhastus = 12\r\n");
				   out.write("nevermine:spawnerGingerbird = 12\r\n");
				   out.write("nevermine:spawnerGingerbreadMan = 12\r\n");
				   out.write("nevermine:spawnerGoldem = 12\r\n");
				   out.write("nevermine:spawnerGoldus = 12\r\n");
				   out.write("nevermine:spawnerHaunted1 = 12\r\n");
				   out.write("nevermine:spawnerHaunted2 = 12\r\n");
				   out.write("nevermine:spawnerHaunted3 = 12\r\n");
				   out.write("nevermine:spawnerHaunted4 = 12\r\n");
				   out.write("nevermine:spawnerHaven = 12\r\n");
				   out.write("nevermine:spawnerHaven2 = 12\r\n");
				   out.write("nevermine:spawnerHaven3 = 12\r\n");
				   out.write("nevermine:spawnerInmateX = 12\r\n");
				   out.write("nevermine:spawnerInmateY = 12\r\n");
				   out.write("nevermine:spawnerJawe = 12\r\n");
				   out.write("nevermine:spawnerKaiyu = 12\r\n");
				   out.write("nevermine:spawnerMechyon = 12\r\n");
				   out.write("nevermine:spawnerMegaGuardian = 12\r\n");
				   out.write("nevermine:spawnerMerkyre = 12\r\n");
				   out.write("nevermine:spawnerMermage = 12\r\n");
				   out.write("nevermine:spawnerMushroomSpider = 12\r\n");
				   out.write("nevermine:spawnerNethengeic = 12\r\n");
				   out.write("nevermine:spawnerNightwing = 12\r\n");
				   out.write("nevermine:spawnerOpteryx = 12\r\n");
				   out.write("nevermine:spawnerParavite = 12\r\n");
				   out.write("nevermine:spawnerPodPlant = 12\r\n");
				   out.write("nevermine:spawnerPoseido = 12\r\n");
				   out.write("nevermine:spawnerPrecasian = 12\r\n");
				   out.write("nevermine:spawnerPrecasian2 = 12\r\n");
				   out.write("nevermine:spawnerPrecasian3 = 12\r\n");
				   out.write("nevermine:spawnerRawbone = 12\r\n");
				   out.write("nevermine:spawnerReaver = 12\r\n");
				   out.write("nevermine:spawnerRefluct = 12\r\n");
				   out.write("nevermine:spawnerRunic = 12\r\n");
				   out.write("nevermine:spawnerRunicGuardian = 12\r\n");
				   out.write("nevermine:spawnerSceptron = 12\r\n");
				   out.write("nevermine:spawnerShavo = 12\r\n");
				   out.write("nevermine:spawnerSkeledon = 12\r\n");
				   out.write("nevermine:spawnerSkelekyte = 12\r\n");
				   out.write("nevermine:spawnerSpectralWizard = 12\r\n");
				   out.write("nevermine:spawnerTharafly = 12\r\n");
				   out.write("nevermine:spawnerTorano = 12\r\n");
				   out.write("nevermine:spawnerUrioh = 12\r\n");
				   out.write("nevermine:spawnerUrv = 12\r\n");
				   out.write("nevermine:spawnerVineWizard = 12\r\n");
				   out.write("nevermine:spawnerVisage = 12\r\n");
				   out.write("nevermine:spawnerZarg = 12\r\n");
				   out.write("nevermine:spawnerZhinx = 12\r\n");
				   out.write("nevermine:spawnerZorp = 12\r\n");
				   out.write("</Resistance>");
				   out.close();
			   }
			}
		   
		   public static void loadLight(FMLPreInitializationEvent event) throws IOException
			{
			 Light = new File(config.BlockProperties, "LightLevel.txt"); //theyre both strings Resistance
			   if (Light.exists() == false)
			   {
				   Light.createNewFile();
				   FileWriter out = new FileWriter(Light.getAbsoluteFile(), true);
				   out.write("#Block Properties For Light Level\r\n#The Parameters Are modid:block = whole_number\r\n\r\n<LightLevel>\r\n");
				   out.write("silkspawners:LavaSpawner = 1\r\n");
				   out.write("</LightLevel>");
				   out.close();
			   }
			}
		   
		   public static void loadharvest(FMLPreInitializationEvent event) throws IOException
			{
			 Harvest = new File(config.BlockProperties, "HarvestLevel.txt"); //theyre both strings Resistance
			   if (Harvest.exists() == false)
			   {
				   Harvest.createNewFile();
				   FileWriter out = new FileWriter(Harvest.getAbsoluteFile(), true);
				   out.write("#Block Properties For Harvest Level\r\n#The Parameters Are modid:block = harvest level\r\n\r\n<HarvestLevel>\r\n");
				   out.write("minecraft:mob_spawner = 0\r\n");
				   out.write("minecraft:end_portal_frame = 0\r\n");
				   out.write("silkspawners:CaveSpawner = 0\r\n");
				   out.write("silkspawners:CustomMobSpawner = 0\r\n");
				   out.write("silkspawners:End_Supreme_Spawner = 0\r\n");
				   out.write("silkspawners:EndSpawner = 0\r\n");
				   out.write("silkspawners:ForestSpawner = 0\r\n");
				   out.write("silkspawners:IceSpawner = 0\r\n");
				   out.write("silkspawners:NetherSpawner = 0\r\n");
				   out.write("silkspawners:RustedSpawner = 0\r\n");
				   out.write("silkspawners:SnowSpawner = 0\r\n");
				   out.write("silkspawners:chocoSpawner = 0\r\n");
				   out.write("DraconicEvolution:customSpawner = 0\r\n");
				   out.write("HardcoreEnderExpansion:custom_spawner = 0\r\n\r\n");
				   out.write("//DivineRPG Support\r\n");
				   out.write("divinerpg:biphronSpawner = 0\r\n");
				   out.write("divinerpg:deathcryxSpawner = 0\r\n");
				   out.write("divinerpg:deathHoundSpawner = 0\r\n");
				   out.write("divinerpg:dreamWreckerSpawner = 0\r\n");
				   out.write("divinerpg:dungeonPrisonerSpawner = 0\r\n");
				   out.write("divinerpg:enthralledDramcryxSpawner = 0\r\n");
				   out.write("divinerpg:frostArcherSpawner = 0\r\n");
				   out.write("divinerpg:gorgosionSpawner = 0\r\n");
				   out.write("divinerpg:livingStatueSpawner = 0\r\n");
				   out.write("divinerpg:razorbackSpawner = 0\r\n");
				   out.write("divinerpg:roamerSpawner = 0\r\n");
				   out.write("divinerpg:rollumSpawner = 0\r\n");
				   out.write("divinerpg:rotatickSpawner = 0\r\n");
				   out.write("divinerpg:twinsSpawner = 0\r\n");
				   out.write("divinerpg:vermenousSpawner = 0\r\n\r\n");
				   out.write("//Advent Of Ascension Support\r\n");
				   out.write("nevermine:spawnerAmphibior = 0\r\n");
				   out.write("nevermine:spawnerAmphibiyte = 0\r\n");
				   out.write("nevermine:spawnerArkzyne = 0\r\n");
				   out.write("nevermine:spawnerArocknid = 0\r\n");
				   out.write("nevermine:spawnerAutomaton = 0\r\n");
				   out.write("nevermine:spawnerBaumba = 0\r\n");
				   out.write("nevermine:spawnerBloodsucker = 0\r\n");
				   out.write("nevermine:spawnerCaneBug = 0\r\n");
				   out.write("nevermine:spawnerCaveBug = 0\r\n");
				   out.write("nevermine:spawnerCenturion = 0\r\n");
				   out.write("nevermine:spawnerCrusilisk = 0\r\n");
				   out.write("nevermine:spawnerDawnlight = 0\r\n");
				   out.write("nevermine:spawnerDaysee = 0\r\n");
				   out.write("nevermine:spawnerEnforcer = 0\r\n");
				   out.write("nevermine:spawnerExohead = 0\r\n");
				   out.write("nevermine:spawnerFacelessFloater = 0\r\n");
				   out.write("nevermine:spawnerFade = 0\r\n");
				   out.write("nevermine:spawnerFenix = 0\r\n");
				   out.write("nevermine:spawnerFlesh = 0\r\n");
				   out.write("nevermine:spawnerFlowerface = 0\r\n");
				   out.write("nevermine:spawnerFungock = 0\r\n");
				   out.write("nevermine:spawnerGhastus = 0\r\n");
				   out.write("nevermine:spawnerGingerbird = 0\r\n");
				   out.write("nevermine:spawnerGingerbreadMan = 0\r\n");
				   out.write("nevermine:spawnerGoldem = 0\r\n");
				   out.write("nevermine:spawnerGoldus = 0\r\n");
				   out.write("nevermine:spawnerHaunted1 = 0\r\n");
				   out.write("nevermine:spawnerHaunted2 = 0\r\n");
				   out.write("nevermine:spawnerHaunted3 = 0\r\n");
				   out.write("nevermine:spawnerHaunted4 = 0\r\n");
				   out.write("nevermine:spawnerHaven = 0\r\n");
				   out.write("nevermine:spawnerHaven2 = 0\r\n");
				   out.write("nevermine:spawnerHaven3 = 0\r\n");
				   out.write("nevermine:spawnerInmateX = 0\r\n");
				   out.write("nevermine:spawnerInmateY = 0\r\n");
				   out.write("nevermine:spawnerJawe = 0\r\n");
				   out.write("nevermine:spawnerKaiyu = 0\r\n");
				   out.write("nevermine:spawnerMechyon = 0\r\n");
				   out.write("nevermine:spawnerMegaGuardian = 0\r\n");
				   out.write("nevermine:spawnerMerkyre = 0\r\n");
				   out.write("nevermine:spawnerMermage = 0\r\n");
				   out.write("nevermine:spawnerMushroomSpider = 0\r\n");
				   out.write("nevermine:spawnerNethengeic = 0\r\n");
				   out.write("nevermine:spawnerNightwing = 0\r\n");
				   out.write("nevermine:spawnerOpteryx = 0\r\n");
				   out.write("nevermine:spawnerParavite = 0\r\n");
				   out.write("nevermine:spawnerPodPlant = 0\r\n");
				   out.write("nevermine:spawnerPoseido = 0\r\n");
				   out.write("nevermine:spawnerPrecasian = 0\r\n");
				   out.write("nevermine:spawnerPrecasian2 = 0\r\n");
				   out.write("nevermine:spawnerPrecasian3 = 0\r\n");
				   out.write("nevermine:spawnerRawbone = 0\r\n");
				   out.write("nevermine:spawnerReaver = 0\r\n");
				   out.write("nevermine:spawnerRefluct = 0\r\n");
				   out.write("nevermine:spawnerRunic = 0\r\n");
				   out.write("nevermine:spawnerRunicGuardian = 0\r\n");
				   out.write("nevermine:spawnerSceptron = 0\r\n");
				   out.write("nevermine:spawnerShavo = 0\r\n");
				   out.write("nevermine:spawnerSkeledon = 0\r\n");
				   out.write("nevermine:spawnerSkelekyte = 0\r\n");
				   out.write("nevermine:spawnerSpectralWizard = 0\r\n");
				   out.write("nevermine:spawnerTharafly = 0\r\n");
				   out.write("nevermine:spawnerTorano = 0\r\n");
				   out.write("nevermine:spawnerUrioh = 0\r\n");
				   out.write("nevermine:spawnerUrv = 0\r\n");
				   out.write("nevermine:spawnerVineWizard = 0\r\n");
				   out.write("nevermine:spawnerVisage = 0\r\n");
				   out.write("nevermine:spawnerZarg = 0\r\n");
				   out.write("nevermine:spawnerZhinx = 0\r\n");
				   out.write("nevermine:spawnerZorp = 0\r\n");
				   out.write("</HarvestLevel>");
				   out.close();
			   }
			}
		   
		   public static void loadtools(FMLPreInitializationEvent event) throws IOException
			{
			 tool = new File(config.BlockProperties, "DefineTools.txt"); //theyre both strings Resistance
			   if (tool.exists() == false)
			   {
				   tool.createNewFile();
				   FileWriter out = new FileWriter(tool.getAbsoluteFile(), true);
				   out.write("#Define Tools Here\r\n#The Parameters Are tool\r\n\r\n<tool>\r\n");
				   out.write("pickaxe\r\n");
				   out.write("shovel\r\n");
				   out.write("spade\r\n");
				   out.write("axe\r\n");
				   out.write("hoe\r\n");
				   out.write("</tool>");
				   out.close();
			   }
			}
		   
		   public static void loadQuantityDropped(FMLPreInitializationEvent event) throws IOException
			{
			   QuantityDropped = new File(config.BlockProperties, "QuantityDropped.txt"); //theyre both strings Resistance
			   if (QuantityDropped.exists() == false)
			   {
				   QuantityDropped.createNewFile();
				   FileWriter out = new FileWriter(QuantityDropped.getAbsoluteFile(), true);
				   out.write("#Block Properties For Quantity Dropped\r\n#The Parameters Are modid:block = Quantity Dropped\r\n\r\n<QuantityDropped>\r\n\r\n");
				   out.write("</QuantityDropped>");
				   out.close();
			   }
			}
		   
		   public static void loadDisableDrops(FMLPreInitializationEvent event) throws IOException
			{
			   DisableDrops = new File(config.BlockProperties, "DisableDrops.txt"); //theyre both strings Resistance
			   if (DisableDrops.exists() == false)
			   {
				   DisableDrops.createNewFile();
				   FileWriter out = new FileWriter(DisableDrops.getAbsoluteFile(), true);
				   out.write("#Block Properties For Disable Drops\r\n#The Parameters Are modid:block = Quantity Dropped\r\n\r\n<DisableDrops>\r\n\r\n");
				   out.write("</DisableDrops>");
				   out.close();
			   }
			}
		   
		   public static void loadEnchant(FMLPreInitializationEvent event) throws IOException
			{
			 efile = new File(config.SilkSpawners, "EnchantmentsSpanwerConfig.txt"); //theyre both strings Resistance
			   if (efile.exists() == false)
			   {
				   efile.createNewFile();
				   FileWriter out = new FileWriter(efile.getAbsoluteFile(), true);
				   out.write("#Enchantment Config Choose Which Enchantment To Use and What Level\r\n#The Parameters Are EnchantmentId:Level\r\n#The Parameters for no Enchantment is null:null or 0:0 at the first enchantment\r\n\r\n<Enchantment>\r\n");
				   out.write("33:1\r\n");
				   out.write("</Enchantment>");
				   out.close();
			   }
			}
		   
		   public static void loadEnchantOverride(FMLPreInitializationEvent event) throws IOException
			{
			 EfileOverride = new File(config.SilkSpawners, "EnchantmentBlockOverride.txt"); //theyre both strings Resistance
			   if (EfileOverride.exists() == false)
			   {
				   EfileOverride.createNewFile();
				   FileWriter out = new FileWriter(EfileOverride.getAbsoluteFile(), true);
				   out.write("#Enchantment Config Choose Which Enchantment To Use and What Level\r\n#The Parameters For Single Enchantment Are modid:block = EnchantmentId:Level\r\n#The Parameters For Multi Enchantment Are modid:block = EnchamentId:Level,EnchantmentId:level etc.. (No Limit on Multi Enchants)\r\n#The Parameters for no Enchantment is null:null or 0:0 at the first enchantment\r\n\r\n<Enchantment>\r\n");
				   out.write("silkspawners:CustomMobSpawner = 33:1\r\n");
				   out.write("</Enchantment>");
				   out.close();
			   }
			}
		   
		   public static void loadIgnoringTools(FMLPreInitializationEvent event) throws IOException
			{
			 ITool = new File(config.SilkSpawners, "IgnoreHarvestLevelTools.txt"); //theyre both strings Resistance
			   if (ITool.exists() == false)
			   {
				   ITool.createNewFile();
				   FileWriter out = new FileWriter(ITool.getAbsoluteFile(), true);
				   out.write("#Tools That Ignore Harvest Level Config Like a Golden Pickaxe By Default\r\n#The Parameters Are modid:item\r\n\r\n<IgnoreHarvestLevel>\r\n");
				   out.write("minecraft:golden_pickaxe\r\n");
				   out.write("</IgnoreHarvestLevel>");
				   out.close();
			   }
			}
		   
		   public static void loadAutoSpawnerBlacklist(FMLPreInitializationEvent event) throws IOException
			{
			   AutoSpawnerBlacklist = new File(config.SilkSpawners, "AutoDetectionSpawnerBlackList.txt"); //theyre both strings Resistance
			   if (AutoSpawnerBlacklist.exists() == false)
			   {
				   AutoSpawnerBlacklist.createNewFile();
				   FileWriter out = new FileWriter(AutoSpawnerBlacklist.getAbsoluteFile(), true);
				   out.write("#Black List From Auto Detection Of Spawner\r\n#The Parameters Are modid:block\r\n\r\n<SpawnerBlacklist>\r\n\r\n");
				   out.write("</SpawnerBlacklist>");
				   out.close();
			   }
			}
	
// REEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAD LINES DEFINE SPAWNERS
   
	public static String read(FMLInitializationEvent event) throws IOException
	{
   
		Scanner fileIn = new Scanner(file);
		while (fileIn.hasNextLine())
		{
			String modid = fileIn.nextLine().toString();
			String WhiteSpaced = modid.replaceAll("\\s+", "");
			
			if (WhiteSpaced.contains(":"))
			{
			MainJava.SpawnerList.add(WhiteSpaced);
			}
			
		}
		fileIn.close();
		return Path;
		
    
    }
	
	public static String readIgnoringTools(FMLInitializationEvent event) throws IOException
	{
   
		Scanner fileIn = new Scanner(ITool);
		while (fileIn.hasNextLine())
		{
			String modid = fileIn.nextLine().toString();
			String WhiteSpaced = modid.replaceAll("\\s+", "");
			
			if (WhiteSpaced.contains(":") == true && WhiteSpaced.contains("#") == false)
			{
				MainJava.IToolList.add(WhiteSpaced);
			}
			
		}
		fileIn.close();
		return Path;
		
    
    }
	
	public static String readAutoSpawnerBlacklist(FMLInitializationEvent event) throws IOException
	{
   
		Scanner fileIn = new Scanner(AutoSpawnerBlacklist);
		while (fileIn.hasNextLine())
		{
			String modid = fileIn.nextLine().toString();
			String WhiteSpaced = modid.replaceAll("\\s+", "");
			
			if (WhiteSpaced.contains(":") && WhiteSpaced.contains("#") == false)
			{
				MainJava.SpawnerBlackList.add(WhiteSpaced);
			}
			
		}
		fileIn.close();
		return Path;
		
    
    }
//REEEEEEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADDDDDDDDDDDDDDD
	
	public static String readBlackList(WorldEvent.Load event) throws IOException
	{
        
		Scanner fileIn = new Scanner(fileBlackList);
		while (fileIn.hasNextLine())
		{
			String line = fileIn.nextLine().toString();
			
			String WhiteSpaced = line.replaceAll("\\s+", "");
			
			if (WhiteSpaced.contains("#") == false)
			{
			MainJava.DungeonBlackList.add(WhiteSpaced);
			}
			
			
		}
		fileIn.close();
		return Path;
	}
	
	public static String readModBlackList(WorldEvent.Load event) throws IOException
	{
        
		Scanner fileIn = new Scanner(ModBlackList);
		while (fileIn.hasNextLine())
		{
			String line = fileIn.nextLine().toString();
			
			String WhiteSpaced = line.replaceAll("\\s+", "");
			
			if (WhiteSpaced.contains("#") == false)
			{
			MainJava.ModIdBlackList.add(WhiteSpaced);
			}
			
			
		}
		fileIn.close();
		return Path;
	}
	
	public static String readModWhiteList(WorldEvent.Load event) throws IOException
	{
        
		Scanner fileIn = new Scanner(ModWhiteList);
		while (fileIn.hasNextLine())
		{
			String line = fileIn.nextLine().toString();
			
			String WhiteSpaced = line.replaceAll("\\s+", "");
			
			if (WhiteSpaced.contains("#") == false)
			{
			MainJava.ModIdWhiteList.add(WhiteSpaced);
			}
			
			
		}
		fileIn.close();
		return Path;
	}
		
	public static String readWhiteList(WorldEvent.Load event) throws IOException
	{
        
		Scanner fileIn = new Scanner(fileWhiteList);
		while (fileIn.hasNextLine())
		{
			String line = fileIn.nextLine().toString();
			
			String WhiteSpaced = line.replaceAll("\\s+", "");
			
			if (WhiteSpaced.contains("=") && !WhiteSpaced.contains("#"))
			{
			MainJava.DungeonWhiteList.add(WhiteSpaced);
			}
			
		}
		fileIn.close();
		return Path;
	}
	
	//Block Properties
	public static String readhardness(FMLInitializationEvent event) throws IOException
	{
        
		Scanner fileIn = new Scanner(hardfile);
		while (fileIn.hasNextLine())
		{
			String line = fileIn.nextLine().toString();
			
			String WhiteSpaced = line.replaceAll("\\s+", "");
			
			if (WhiteSpaced.contains("=") && WhiteSpaced.contains("#") == false)
			{
			MainJava.Hardness.add(WhiteSpaced);
			}
			
		}
		fileIn.close();
		return Path;
	}
	
	  public static String readResistance(FMLInitializationEvent event) throws IOException
		{
		  Scanner fileIn = new Scanner(Resistence);
			while (fileIn.hasNextLine())
			{
				String line = fileIn.nextLine().toString();
				
				String WhiteSpaced = line.replaceAll("\\s+", "");
				
				if (WhiteSpaced.contains("=") && WhiteSpaced.contains("#") == false)
				{
				MainJava.Resistance.add(WhiteSpaced);
				}
		    }
			fileIn.close();
			return Path;
		}
	  
	  public static String readLight(FMLInitializationEvent event) throws IOException
		{
		  Scanner fileIn = new Scanner(Light);
			while (fileIn.hasNextLine())
			{
				String line = fileIn.nextLine().toString();
				
				String WhiteSpaced = line.replaceAll("\\s+", "");
				
				if (WhiteSpaced.contains("=") && WhiteSpaced.contains("#") == false)
				{
				MainJava.LightLevel.add(WhiteSpaced);
				}
		    }
			fileIn.close();
			return Path;
		}
	
	  public static String readHarvest(FMLInitializationEvent event) throws IOException
		{
		  Scanner fileIn = new Scanner(Harvest);
			while (fileIn.hasNextLine())
			{
				String line = fileIn.nextLine().toString();
				
				String WhiteSpaced = line.replaceAll("\\s+", "");
				
				if (WhiteSpaced.contains("=") && WhiteSpaced.contains("#") == false)
				{
				MainJava.HarvestLevel.add(WhiteSpaced);
				}
		    }
			fileIn.close();
			return Path;
		}
	  
	  public static String readtool(FMLInitializationEvent event) throws IOException
		{
	        
			Scanner fileIn = new Scanner(tool);
			while (fileIn.hasNextLine())
			{
				String line = fileIn.nextLine().toString();
				
				String WhiteSpaced = line.replaceAll("\\s+", "");
				
				if (WhiteSpaced.contains("#") == false)
				{
				MainJava.ToolHash.add(WhiteSpaced);
				}
				
				
			}
			fileIn.close();
			return Path;
		}
	  
	  public static String readQuantityDropped(FMLInitializationEvent event) throws IOException
	  {
	   
			Scanner fileIn = new Scanner(QuantityDropped);
			while (fileIn.hasNextLine())
			{
				String modid = fileIn.nextLine().toString();
				String WhiteSpaced = modid.replaceAll("\\s+", "");
				
				if (WhiteSpaced.contains("=") == true && WhiteSpaced.contains("#") == false)
				{
					MainJava.QuantityDroppedList.add(WhiteSpaced);
				}
				
			}
			fileIn.close();
			return Path;

	    }
	  
	  public static String readDisableDrops(FMLInitializationEvent event) throws IOException
	  {
	   
			Scanner fileIn = new Scanner(DisableDrops);
			while (fileIn.hasNextLine())
			{
				String modid = fileIn.nextLine().toString();
				String WhiteSpaced = modid.replaceAll("\\s+", "");
				
				if (WhiteSpaced.contains(":") == true && WhiteSpaced.contains("#") == false)
				{
					MainJava.DisableDropsList.add(WhiteSpaced);
				}
				
			}
			fileIn.close();
			return Path;

	    }
	  
	  public static String readEnchantment(FMLInitializationEvent event) throws IOException
			{
		   
				Scanner fileIn = new Scanner(efile);
				while (fileIn.hasNextLine())
				{
					String modid = fileIn.nextLine().toString();
					String WhiteSpaced = modid.replaceAll("\\s+", "");
					
					if (WhiteSpaced.contains(":") == true && !WhiteSpaced.contains("#"))
					{
						MainJava.EList.add(WhiteSpaced);
					}
					
				}
				fileIn.close();
				return Path;
				
		    
		    }
	  
	  public static String readEnchantmentOverride(FMLInitializationEvent event) throws IOException
		{
	   
			Scanner fileIn = new Scanner(EfileOverride);
			while (fileIn.hasNextLine())
			{
				String modid = fileIn.nextLine().toString();
				String WhiteSpaced = modid.replaceAll("\\s+", "");
				
				if (WhiteSpaced.contains(":") == true && WhiteSpaced.contains("=") == true && WhiteSpaced.contains("#") == false)
				{

					MainJava.EListOverride.add(WhiteSpaced);
				}
				
			}
			fileIn.close();
			return Path;
			
	    
	    }
	
    
    
	
}

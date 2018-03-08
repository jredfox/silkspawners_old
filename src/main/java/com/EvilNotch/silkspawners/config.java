package com.EvilNotch.silkspawners;


import java.io.File;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class config {

	public static boolean DisableEggDropFromSpawner = false;
	public static boolean DisableEggDropChanceFromSpawner = false;
	public static int harvestlevel = 0;
	public static int ModdedSpawnerChance = 10;
	public static boolean GoldenPickaxeAlwaysEnabled = true;
	public static boolean SpawnersBlastProof = false;
	public static boolean CustomVanillaDungeSpawners = true;
	public static boolean ModdedSpawners = true;
	public static boolean NetherSpawnerUseBrightTexture = true;
	public static boolean EndSpawnerUseLessDetailedTexture = true;
	public static boolean caged_eggs = false;
	public static boolean cross_nei_compat = true;
	public static boolean whiteList = false;
	public static boolean WriteModdedSpawnersToNBT = true;
	public static int ForgeSpawnerTexture = 0;
	public static File SilkSpawners;
	public static File DungeonTweeks;
	public static File BlockProperties;
	public static boolean blackList = true;
	public static boolean WhiteListByModId = false;
    public static boolean BlackListByModID = true;
	public static boolean UseGlobalHarvestLevel = false;
	public static boolean GlobalBlastResistentSpawners = false;
	public static int GlobalBlastResitentLevel = 12;
	public static boolean DisableBlastResistentSpawners = false;
	public static boolean DropWithoutEnchants = false;
	public static boolean isEnchantsExcat = false;
	public static boolean setModdedSpawnersName = true;
	public static boolean GlobalIdForgeEggs = false;
	public static boolean NEIGlobalIdForgeSpawners = false;
	public static boolean DoesEnchantOverride = false;
	public static int EggCap = 101;
	public static int blankCaptureCap = 5; 
	public static boolean OverrideEnchants = true;
	public static boolean ForgeSpawners = true;
	public static boolean CraftableMobSpawners = true;
	public static int BarDropCount = 3;
	public static boolean AutoDetectSpawners = true;
	public static boolean VanillaLogicEnchants = false;
	public static boolean FireandWaterEnchants = true;
	public static boolean autowrite = true;
	public static boolean ChangeBucketTexture = true;
	public static boolean UseChocoSpawner = false;
	public static File configdirectory;
	public static boolean colorText = true;
	public static boolean Debug = false;
	public static boolean AllSpawnersSameFormat = false;
	public static String MonsterSpawner = "";
	//NBT
	public static boolean MaxNearbyEntities = true;
	public static boolean RequiredPlayerRange = true;
	public static boolean SpawnCount = true;
	public static boolean MaxSpawnDelay = true;
	public static boolean SpawnRange = true;
	public static boolean Delay = false;
	public static boolean MinSpawnDelay = true;
	public static boolean SpawnData = true;
	public static boolean SpawnPotentials = true;
	//NBTEND
	public static boolean autoName = true;
	public static boolean nameVmobSpawner;
	
	public static void loadconfig (FMLPreInitializationEvent event)
	{
		if(event != null)
		configdirectory = event.getModConfigurationDirectory();
		
		SilkSpawners = new File(configdirectory, "SilkSpawners");
		SilkSpawners.mkdir();
		
	    DungeonTweeks = new File(config.SilkSpawners, "DungeonTweeks");
		DungeonTweeks.mkdir();
		
		BlockProperties = new File(config.SilkSpawners, "BlockProperties");
		BlockProperties.mkdir();
		
		File CustomConfigFile = new File(SilkSpawners, "SilkSpawners-1.7.10.cfg");
	Configuration config = new Configuration(CustomConfigFile);
	
	config.load();
	
	//harvest level
	harvestlevel = config.get(Configuration.CATEGORY_GENERAL, "Harvest Level", 0).getInt(0);
	blankCaptureCap = config.get(Configuration.CATEGORY_GENERAL, "blank Capture Cap", 5).getInt(5);
	EggCap = config.get(Configuration.CATEGORY_GENERAL, "Spawner Egg Drop Cap", 101).getInt(101);
	ForgeSpawnerTexture = config.get(Configuration.CATEGORY_GENERAL, "Forge Spawner Texture", 0).getInt(0);
	GlobalBlastResitentLevel = config.get(Configuration.CATEGORY_GENERAL, "Global Blast Resitent Level", 12).getInt(12);
	Property property = config.get(Configuration.CATEGORY_GENERAL, "Forge Spawner Texture", ForgeSpawnerTexture);
	property.comment = "Forge Spawner Textures; 0 = Default, 1= Classic";
	
	
	//boolean
	GoldenPickaxeAlwaysEnabled = config.get(Configuration.CATEGORY_GENERAL, "White List of Tools Ignoring Harvest Level Like a Golden Pickaxe By Default", true).getBoolean(true);
	SpawnersBlastProof = config.get(Configuration.CATEGORY_GENERAL, "Spawners Blast Proof", false).getBoolean(false);
	NetherSpawnerUseBrightTexture = config.get(Configuration.CATEGORY_GENERAL, "Nether Spawner Use Bright Texture", true).getBoolean(true);
	EndSpawnerUseLessDetailedTexture = config.get(Configuration.CATEGORY_GENERAL, "End Spawner Use Less Detailed Texture", true).getBoolean(true);
	caged_eggs = config.get(Configuration.CATEGORY_GENERAL, "Caged Eggs", false).getBoolean(false);
	cross_nei_compat = config.get(Configuration.CATEGORY_GENERAL, "Cross NEI Compatibility", true).getBoolean(true);
	WriteModdedSpawnersToNBT = config.get(Configuration.CATEGORY_GENERAL, "Write Modded Spawners To NBT", true).getBoolean(true);
	GlobalBlastResistentSpawners = config.get(Configuration.CATEGORY_GENERAL, "Global Blast Resistent Spawners", false).getBoolean(false);
	DisableBlastResistentSpawners = config.get(Configuration.CATEGORY_GENERAL, "Disable Blast Resistent Spawners", false).getBoolean(false);
	DropWithoutEnchants = config.get(Configuration.CATEGORY_GENERAL, "Drop Without Enchants", false).getBoolean(false);
	isEnchantsExcat = config.get(Configuration.CATEGORY_GENERAL, "Is Enchantments Excat", false).getBoolean(false);
	setModdedSpawnersName = config.get(Configuration.CATEGORY_GENERAL, "Set Modded Spawners Names", true).getBoolean(true);
	GlobalIdForgeEggs = config.get(Configuration.CATEGORY_GENERAL, "Global Id Forge Eggs", false).getBoolean(false);
	NEIGlobalIdForgeSpawners = config.get(Configuration.CATEGORY_GENERAL, "NEI Global Id Forge Spawners", false).getBoolean(false);
	DoesEnchantOverride = config.get(Configuration.CATEGORY_GENERAL, "Does EnchantOverride File Override Drop Spawner Without Enchantments", false).getBoolean(false);
	UseGlobalHarvestLevel = config.get(Configuration.CATEGORY_GENERAL, "Use Global Harvest Level", false).getBoolean(false);
	OverrideEnchants = config.get(Configuration.CATEGORY_GENERAL, "Override Anvil Enchantments", true).getBoolean(true);
	ForgeSpawners = config.get(Configuration.CATEGORY_GENERAL, "Forge Spawners", true).getBoolean(true);
	CraftableMobSpawners = config.get(Configuration.CATEGORY_GENERAL, "Craftable Mob Spawners", true).getBoolean(true);
	AutoDetectSpawners = config.get(Configuration.CATEGORY_GENERAL, "Auto Detect Spawners", true).getBoolean(true);
	VanillaLogicEnchants = config.get(Configuration.CATEGORY_GENERAL, "Vanilla Logic Enchantments", false).getBoolean(false);
	FireandWaterEnchants = config.get(Configuration.CATEGORY_GENERAL, "Fire and Water Enchantments Enabled", true).getBoolean(true);
	ChangeBucketTexture = config.get(Configuration.CATEGORY_GENERAL, "Change Vanilla Bucket Textures", true).getBoolean(true);
	UseChocoSpawner = config.get(Configuration.CATEGORY_GENERAL, "Use Choco Spawner When NEI Is Loaded", false).getBoolean(false);
	autowrite = config.get(Configuration.CATEGORY_GENERAL, "Auto Write Auto Detected Spawners To NBT", true).getBoolean(true);
	colorText = config.get(Configuration.CATEGORY_GENERAL, "Colored Text On Spawners and Eggs", true).getBoolean(true);
	Debug = config.get(Configuration.CATEGORY_GENERAL, "Debug Mode", false).getBoolean(false);
	autoName = config.get(Configuration.CATEGORY_GENERAL, "Auto Detected Spawners Get AutoNamed", true).getBoolean(true);
	AllSpawnersSameFormat = config.get(Configuration.CATEGORY_GENERAL, "All Spawners Named the Same Format<MobName Monster Spawner>", false).getBoolean(false);
	MonsterSpawner = config.getString("Monster Spawner", Configuration.CATEGORY_GENERAL, "Monster Spawner", "Monster Spawner Name");
	DisableEggDropChanceFromSpawner = config.get(Configuration.CATEGORY_GENERAL, "Disable Spawn Eggs Chance Dropping From Spawners", false).getBoolean(false);
	DisableEggDropFromSpawner = config.get(Configuration.CATEGORY_GENERAL, "Disable Spawn Eggs Dropping From Spawners", false).getBoolean(false);
	
	
	
	//DungeonTweeks
	String CATEGORY = "dungeon_tweeks";
	config.addCustomCategoryComment(CATEGORY, "DungeonTweeks Main Configuration Of How You Want Dungeon Tweeks To Operate");
	blackList = config.get(CATEGORY, "Black List", true).getBoolean(true);
	BlackListByModID = config.get(CATEGORY, "Black List By Mod ID", true).getBoolean(true);
	whiteList = config.get(CATEGORY, "White List", false).getBoolean(false);
	WhiteListByModId = config.get(CATEGORY, "White List By Mod Id", false).getBoolean(false);
	ModdedSpawners = config.get(CATEGORY, "Modded Dungeon Spawners", true).getBoolean(true);
	ModdedSpawnerChance = config.get(CATEGORY, "Default Modded Spawner Chance", 10).getInt(10);
	
	//NBT SECTION
	String cat = "NBTTagCompound";
	config.addCustomCategoryComment(cat, "Config NBT Structures Only Change To True If Mod Is Incompatible With A Function Of Mob Spawner");
	MaxNearbyEntities = config.get(cat, "MaxNearbyEntities", true).getBoolean(true);
	RequiredPlayerRange = config.get(cat, "RequiredPlayerRange", true).getBoolean(true);
	SpawnCount = config.get(cat, "SpawnCount", true).getBoolean(true);
	MaxSpawnDelay = config.get(cat, "MaxSpawnDelay", true).getBoolean(true);
	SpawnRange = config.get(cat, "SpawnRange", true).getBoolean(true);
	Delay = config.get(cat, "Delay", false).getBoolean(false);
	MinSpawnDelay = config.get(cat, "MinSpawnDelay", true).getBoolean(true);
	SpawnData = config.get(cat, "SpawnData", true).getBoolean(true);
	SpawnPotentials = config.get(cat, "SpawnPotentials", true).getBoolean(true);
	
	
	config.save();
	
	}
}



package com.EvilNotch.silkspawners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import com.EvilNotch.silkspawners.entity.MinecartMobSpawner;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;


//before:IAmRemovingWaila
@Mod(modid="silkspawners", name="Silk Spawners", version="1.6.2",dependencies = "before:IAmRemovingWaila",acceptableRemoteVersions="*")

public class MainJava
{
	public static String varhooks;
	public static List<String> List = new ArrayList<String>();
	public static List<String> BlockList = new ArrayList<String>();
	public static List<String> CagedList = new ArrayList<String>();
	public static List<String> SpawnerList = new ArrayList<String>();
	public static List<String> SpawnerBlackList = new ArrayList<String>();
	public static List<String> FileList = new ArrayList<String>();
	public static List<String> DungeonWrongList = new ArrayList<String>();
	public static List<String> DungeonBlackList = new ArrayList<String>();
	public static List<String> DungeonWhiteList = new ArrayList<String>();
	public static List<String> ModIdBlackList = new ArrayList<String>();
	public static List<String> ModIdWhiteList = new ArrayList<String>();
	public static List<String> ToolHash = new ArrayList<String>();
	public static List<String> DisableDropsList = new ArrayList<String>();
	public static List<String> EList = new ArrayList<String>();
	public static List<String> EListOverride = new ArrayList<String>();
	public static Map<String, String> DungeonHashMap = new HashMap<String, String>();
	public static Map<String, String> FileHashBrown = new HashMap<String, String>();
	public static Map<String, String> PrintBlock = new HashMap<String, String>();
	public static Map<String, String> SpawnerChecker = new HashMap<String, String>();
	public static List<String> Hardness = new ArrayList<String>();
	public static List<String> Resistance = new ArrayList<String>();
	public static List<String> HarvestLevel = new ArrayList<String>();
	public static List<String> LightLevel = new ArrayList<String>();
	public static List<String> QuantityDroppedList = new ArrayList<String>();
	public static List<String> IToolList = new ArrayList<String>();
	public static List<Item> Egg_List = new ArrayList<Item>();
	public static List<String> end_ents = new ArrayList<String>();
	public static List<String> Entities = new ArrayList<String>();
	public static String modid = "silkspawners:";
	public static Item forge_egg_blank;
	public static Item caged_egg_blank;
	public static Item forge_egg;
	public static Item caged_egg;
	public static Item enchanted_egg;
	public static Item egg_blue_soul;
	public static Item water_spawner_bucket;
	public static Item lava_spawner_bucket;
	public static Item CustomMilkBucket;
	public static Item egg_water;
	public static Item book_capture;
	public static ItemMinecartSpawner minecartSpawner = new ItemMinecartSpawner();
	public static Block CustomMobSpawner;
	public static Block NetherSpawner;
	public static Block EndSpawner;
	public static Block RustedSpawner;
	public static Block WaterSpawner; 
	public static Block LavaSpawner;
	public static Block IceSpawner;
	public static Block CaveSpawner;
	public static Block SnowSpawner;
	public static Block End_Supreme_Spawner;
	public static Block ForestSpawner;
	public static Block iron_fence;
	public static Block chocoSpawner;
	public static Block test;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException
	{
		     config.loadconfig(event);
		     
		     //Generates IRecipes For Minecart Spawners
		      GameRegistry.addRecipe(new MinecartSpawnerIRecipe());
		      RecipeSorter.register("silkspawners:Minecart", MinecartSpawnerIRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless");
		     
		     //Generates Define Spawners From File
             FileConstructer.loadconfig(event);
             
             //Block Properties
             FileConstructer.loadhardness(event);
             FileConstructer.loadResistence(event);
             FileConstructer.loadLight(event);
             FileConstructer.loadharvest(event);
             FileConstructer.loadtools(event);
             FileConstructer.loadQuantityDropped(event);
             FileConstructer.loadDisableDrops(event);
             FileConstructer.loadEnchant(event);
             FileConstructer.loadEnchantOverride(event);
             FileConstructer.loadIgnoringTools(event);
             FileConstructer.loadAutoSpawnerBlacklist(event);
             
             //Registry my minecart
             SilkSpawners.replaceVanillaEntity(MinecartMobSpawner.class, 47);
             //Sets minecart Fixes
             GameRegistry.addRecipe(new ItemStack(Items.command_block_minecart), new Object[]{" A "," B ", 'A', Blocks.command_block,'B', Items.minecart});
             
             //Chest Loot {first parameter is string dungeon chest second is wieghted random chest}
             // the parameters for weighted random chest is (itemstack , min stack size, max stack size chance)
             //Only loads it in if dragon mounts doesn't do it itself...
             if (!Loader.isModLoaded("DragonMounts"))
             {
            	 ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.dragon_egg), 1,3,10));
            	 ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(new ItemStack(Blocks.dragon_egg), 1,3,7));
            	 ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(Blocks.dragon_egg), 1,3,6));
            	 ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.dragon_egg), 1,3,5));
             }
         
             if (config.blankCaptureCap < 0)
             {
            	 config.blankCaptureCap = 1;
             }
             
             //Changes Defaults Buckets Texture
             if (config.ChangeBucketTexture == true)
             {
            	 Items.water_bucket.setTextureName("silkspawners:water_bucket");
            	 Items.lava_bucket.setTextureName("silkspawners:lava_bucket");
             }
             
             //Add Some Blocks to Creative Tab That should have already been there
             Blocks.command_block.setCreativeTab(CreativeTabs.tabRedstone);
             Blocks.dragon_egg.setCreativeTab(CreativeTabs.tabBlock);
             Blocks.mob_spawner.setCreativeTab(CreativeTabs.tabRedstone);
             
             //blocks and items
             CustomMobSpawner = new CustomMobSpawner("CustomMobSpawner");
			 GameRegistry.registerBlock(CustomMobSpawner, ItemBlockHelper.class, "CustomMobSpawner");
			 
			 GameRegistry.registerItem(minecartSpawner, "MineCartSpawner");
			 
			 NetherSpawner = new DungeonSpawner("NetherMobSpawner");
			 GameRegistry.registerBlock(NetherSpawner, ItemBlockHelper.class, "NetherSpawner");
			 
			 EndSpawner = new DungeonSpawner("EndSpawner");
			 GameRegistry.registerBlock(EndSpawner, ItemBlockHelper.class, "EndSpawner");
			 
			 RustedSpawner = new DungeonSpawner("RustedSpawner").setBlockTextureName("silkspawners:spawner_rust");
			 GameRegistry.registerBlock(RustedSpawner, ItemBlockHelper.class, "RustedSpawner");
			 String debug = "mob_spawner";
			 if(config.Debug)
				 debug = "silkspawners:iron";
			 chocoSpawner = new DungeonSpawner("chocoSpawner").setBlockTextureName(debug);
			 GameRegistry.registerBlock(chocoSpawner, ItemBlockHelper.class, "chocoSpawner");
			 
			 WaterSpawner = new LiquidMobSpawner(Material.water, "WaterSpawner").setBlockTextureName("water_flow");
			 GameRegistry.registerBlock(WaterSpawner, ItemBlockHelper.class, "WaterSpawner");
			 
			 LavaSpawner = new LiquidMobSpawner(Material.lava, "LavaSpawner").setBlockTextureName("lava_flow");
			 GameRegistry.registerBlock(LavaSpawner, ItemBlockHelper.class, "LavaSpawner");
			 
			 String s = "silkspawners:iron_fence"; //Sets string and iicons of the iron fence based on the spawner textures
			 if (config.ForgeSpawnerTexture == 1)
			 {
				 s = "silkspawners:forged_fence2";
			 }
			 iron_fence = new ForgedFence(s, s, Material.iron, true).setBlockName("iron_fence").setBlockTextureName("silkspawners:iron_bars");
			 GameRegistry.registerBlock(iron_fence, "iron_fence");
			 
			
			 forge_egg = new forge_egg("Test");
			 GameRegistry.registerItem(forge_egg, "forge_egg");
			 
			 test = new DungeonSpawner("test").setBlockTextureName("silkspawners:lava_flow");
			 GameRegistry.registerBlock(test, "test");
			 
			 enchanted_egg = new Enchanted_Egg("enchanted_egg");
			 GameRegistry.registerItem(enchanted_egg, "enchanted_egg");
			 
			 book_capture = new Book_capture("book_capture");
			 GameRegistry.registerItem(book_capture, "book_capture");
			 
			 egg_blue_soul = new Blue_Soul_Egg("egg_blue_soul");
			 GameRegistry.registerItem(egg_blue_soul, "egg_blue_soul");
			 
			 egg_water = new blankegg("egg_water");
			 GameRegistry.registerItem(egg_water, "egg_water");
			 
			 water_spawner_bucket = new CustomBucket(WaterSpawner).setUnlocalizedName("water_spawner_bucket").setTextureName("silkspawners:BlueSoul");
			 GameRegistry.registerItem(water_spawner_bucket, "water_spawner_bucket");
			 
			 lava_spawner_bucket = new CustomBucket(LavaSpawner).setUnlocalizedName("lava_spawner_bucket").setTextureName("silkspawners:EnchantedSoul");
			 GameRegistry.registerItem(lava_spawner_bucket, "lava_spawner_bucket");
			 
			 CustomMilkBucket = new MilkBucket().setUnlocalizedName("CustomMilkBucket").setTextureName("bucket_milk").setCreativeTab(CreativeTabs.tabBrewing);
			 GameRegistry.registerItem(CustomMilkBucket, "CustomMilkBucket");
			 
			 caged_egg = new caged_egg("caged_egg");
			 GameRegistry.registerItem(caged_egg, "caged_egg");
			 
			 forge_egg_blank = new blankegg("forge_egg_blank");
			 GameRegistry.registerItem(forge_egg_blank, "forge_egg_blank");
			 
			 caged_egg_blank = new blankegg("caged_egg_blank");
			 GameRegistry.registerItem(caged_egg_blank, "caged_egg_blank");
			 
			 IceSpawner = new BlockIceSpawner("IceSpawner").setBlockTextureName("ice");
			 GameRegistry.registerBlock(IceSpawner, ItemBlockHelper.class, "IceSpawner");

			 CaveSpawner = new DungeonSpawner("CaveSpawner").setBlockTextureName("silkspawners:CaveSpawner");
			 GameRegistry.registerBlock(CaveSpawner, ItemBlockHelper.class, "CaveSpawner");
			 
			 SnowSpawner = new BlockIceSpawner("SnowSpawner").setBlockTextureName("silkspawners:SnowSpawner");
			 GameRegistry.registerBlock(SnowSpawner, ItemBlockHelper.class, "SnowSpawner");
			 
			 ForestSpawner = new DungeonSpawner("ForestSpawner").setBlockTextureName("silkspawners:theone");
			 GameRegistry.registerBlock(ForestSpawner, ItemBlockHelper.class, "ForestSpawner");
			 
			 End_Supreme_Spawner = new DungeonSpawner("End_Supreme_Spawner").setBlockTextureName("silkspawners:Eneder_Supreme_Spawner");
			 GameRegistry.registerBlock(End_Supreme_Spawner, ItemBlockHelper.class, "End_Supreme_Spawner");
			 
			 if (config.ForgeSpawnerTexture > 1)
			 {
				 config.ForgeSpawnerTexture = 1;
			 }
			 if (config.ForgeSpawnerTexture == 0)
			 {
				 CustomMobSpawner.setBlockTextureName("silkspawners:shiny1");
			 }
			 if (config.ForgeSpawnerTexture == 1)
			 {
				 CustomMobSpawner.setBlockTextureName("silkspawners:iron");
			 }

			 if (config.NetherSpawnerUseBrightTexture == true)
			 {
				 NetherSpawner.setBlockTextureName("silkspawners:vn2");
			 }
			 else{
				 NetherSpawner.setBlockTextureName("silkspawners:vn1");
			 }
			 if (config.EndSpawnerUseLessDetailedTexture == true)
			 {
				 EndSpawner.setBlockTextureName("silkspawners:end1");
			 }
			 else{
				 EndSpawner.setBlockTextureName("silkspawners:end2");
			 }
			 
			
			 
			 //Recipes
			 ItemStack spawner = new ItemStack(CustomMobSpawner);
			 ItemStack Vspawner = new ItemStack(Blocks.mob_spawner);
			 NBTTagCompound spawnernbt = new NBTTagCompound();
			 NBTTagCompound spawnernbt2 = new NBTTagCompound();
			 NBTTagCompound a = new NBTTagCompound();
			 NBTTagCompound b = new NBTTagCompound();
			 spawnernbt2.setString("EntityId", "Blank");
			 spawnernbt.setString("EntityId", "Blank");
			 a.setString("Name", "Blank" + " " + Blocks.mob_spawner.getLocalizedName());
	    	 spawnernbt2.setTag("display", a);
			 
			 spawner.setTagCompound(spawnernbt);
			 Vspawner.setTagCompound(spawnernbt2);
			 GameRegistry.addRecipe(new ItemStack(iron_fence), new Object[]{"xxx","xxx",'x', Blocks.iron_block, 'y', Items.lava_bucket});
			 if (config.CraftableMobSpawners == true)
			 {
				 GameRegistry.addRecipe(spawner, new Object[]{"xxx","xyx","xxx",'x', iron_fence, 'y', Items.lava_bucket});
			 	 GameRegistry.addRecipe(Vspawner, new Object[]{"xxx","xyx","xxx",'x', Blocks.iron_bars, 'y', Items.lava_bucket}); //May Change In Future To More Complex Recipe
			 }
			 GameRegistry.addRecipe(new ItemStack(forge_egg_blank), new Object[]{" x ","xyx"," x ",'x', Blocks.iron_block, 'y', Blocks.dragon_egg});
			 //GameRegistry.addRecipe(new ItemStack(caged_egg_blank), new Object[]{" x ","xyx"," x ",'x', Blocks.iron_bars, 'y', Items.egg});
		  }

	 @Mod.EventHandler
	  public void Init(FMLInitializationEvent event) throws IOException
	  {
		 
		 //Allocates Anvil Egg List
         Egg_List.add(GameRegistry.findItem("silkspawners", "forge_egg") );
         Egg_List.add(GameRegistry.findItem("silkspawners", MainJava.caged_egg.getUnlocalizedName().substring(5) ) );
         Egg_List.add(GameRegistry.findItem("silkspawners", MainJava.forge_egg_blank.getUnlocalizedName().substring(5) ) );
         Egg_List.add(GameRegistry.findItem("silkspawners", MainJava.caged_egg_blank.getUnlocalizedName().substring(5) ) );
         Egg_List.add(GameRegistry.findItem("silkspawners", MainJava.enchanted_egg.getUnlocalizedName().substring(5) ) );
         Egg_List.add(GameRegistry.findItem("silkspawners", MainJava.egg_blue_soul.getUnlocalizedName().substring(5) ) );
         Egg_List.add(GameRegistry.findItem("silkspawners", MainJava.egg_water.getUnlocalizedName().substring(5) ) );
		
		//Reads Lines and adds each line if it contains a : to the array list for dropping
		FileConstructer.read(event);
		
		//Block Properties
		FileConstructer.readhardness(event);
		FileConstructer.readResistance(event);
		FileConstructer.readLight(event);
		FileConstructer.readHarvest(event);
		FileConstructer.readtool(event);
		FileConstructer.readQuantityDropped(event);
		FileConstructer.readDisableDrops(event);
		FileConstructer.readEnchantment(event);
		FileConstructer.readEnchantmentOverride(event);
		FileConstructer.readIgnoringTools(event);
		FileConstructer.readAutoSpawnerBlacklist(event);
		
		MainJava.EList.remove("#TheParametersAreEnchantmentId:Level");
		
		
		//Adds Eggs Event regardless of mods loaded
		MinecraftForge.EVENT_BUS.register(new Forge_Egg_Event() );

		
		
        //Block Properties Setting The Properties
		Iterator ithard = Hardness.iterator();
		while (ithard.hasNext())
		{
			
			String raw = ithard.next().toString();
			String[] parts = raw.split("=");
			String value = parts[1];
			String strid = parts[0];
			String replaced = strid.replaceFirst(":", "\u00A9");
			String[] partblock = replaced.split("\u00A9");
			String modid = partblock[0];
			String name = partblock[1];
			int hardness = Integer.parseInt(value);
			Block block = GameRegistry.findBlock(modid, name);
			if (block != null)
			{
				block.setHardness(hardness);

			}
			
		}
		
		Iterator itResistence = Resistance.iterator();
		while (itResistence.hasNext())
		{
			
			String raw = itResistence.next().toString();
			String[] parts = raw.split("=");
			String value = parts[1];
			String strid = parts[0];
			String replaced = strid.replaceFirst(":", "\u00A9");
			String[] partblock = replaced.split("\u00A9");
			String modid = partblock[0];
			String name = partblock[1];
			int resistence = Integer.parseInt(value);
			
			
			
			Block block = GameRegistry.findBlock(modid, name);
			if (block != null)
			{

			    if (!SpawnerList.contains(strid))
			    {
				   block.setResistance(resistence);
			    }
			    
			  if (SpawnerList.contains(strid))
			  {
			    	
			    if (config.GlobalBlastResistentSpawners == true)
				{
					resistence = config.GlobalBlastResitentLevel;
				}
			    if (config.SpawnersBlastProof == true)
			    {
			    	resistence = 6000000;
			    }
			    if (config.DisableBlastResistentSpawners == false)
			    {
			    
				   block.setResistance(resistence);
			    }
			    if (config.DisableBlastResistentSpawners == true)
			    {
			    	//although won't set the spawners blast resistent if it by default has it with the mod it will then set it to 0. This fixes potential bugs.
			    	block.setResistance(0.0F);
			    }
			    
			  }

			}
			
		}
		
		Iterator itlight = LightLevel.iterator();
		while (itlight.hasNext())
		{
			
			String raw = itlight.next().toString();
			String[] parts = raw.split("=");
			String value = parts[1];
			String strid = parts[0];
			String replaced = strid.replaceFirst(":", "\u00A9");
			String[] partblock = replaced.split("\u00A9");
			String modid = partblock[0];
			String name = partblock[1];
			int lightlevel = Integer.parseInt(value);
			Block block = GameRegistry.findBlock(modid, name);
			if (block != null)
			{
				block.setLightLevel(lightlevel);

			}
			
		}
		
		 
		
		 
	  }
	
	 @Mod.EventHandler
	  public void postInit(FMLPostInitializationEvent event) throws IOException
	  {
 
		  SpawnerList.remove("#Theparametersaremodid:block");
		  
		  //modded registry for spawner blocks. Let's user define silk spawner blocks.
		  MinecraftForge.EVENT_BUS.register(new EventArrayBlockDrop() );
		  MinecraftForge.EVENT_BUS.register(new WorldLoadEvent());
		  MinecraftForge.EVENT_BUS.register(new AnvilEventHandler());
			

			Map var10 = EntityList.stringToClassMapping;
			Iterator it = var10.entrySet().iterator();
			while (it.hasNext()) 
			{
				
				
				Map.Entry pair = (Map.Entry)it.next();
				//System.out.println(pair.getKey() + " = " + pair.getValue());  // would print key and config path but is commented out
				String varhooks = pair.getKey().toString();
				Class EntityLiving = (Class) EntityList.stringToClassMapping.get(varhooks);

				boolean isAbstract = Modifier.isAbstract(EntityLiving.getModifiers());
				boolean isInterface = Modifier.isInterface(EntityLiving.getModifiers());
				boolean hasDefault = true;
				try {
					Constructor k = EntityLiving.getConstructor(new Class[] {World.class});
				} catch (Throwable e) {
					hasDefault = false;
					
					for(int i=0;i<4;i++)
					System.out.println("404 Entity Not Found:" + varhooks);
				}
				
			   if (EntityLivingBase.class.isAssignableFrom(EntityLiving) && !isAbstract && !isInterface && hasDefault || EntityLiving.class.isAssignableFrom(EntityLiving) && !isAbstract && !isInterface && hasDefault)
			   { 

				   if (varhooks != "Monster" && varhooks != "Mob" && varhooks != "ArmorStand")
				   {
					 if (config.ModdedSpawners == true)
					 {
						 DungeonWrongList.add(varhooks);
						 BlockList.add(varhooks);
					 }
					 
				     //forge egg list
				     List.add(varhooks);
						Entities.add(varhooks);
				   }
			   }
			   
			   
			   if (!EntityLivingBase.class.isAssignableFrom(EntityLiving) && !EntityLiving.class.isAssignableFrom(EntityLiving) && !isAbstract && !isInterface && hasDefault)
			   {
				   if (varhooks != "Monster" && varhooks != "Mob" && varhooks != "ArmorStand")
				   {
					   CagedList.add(varhooks);
						Entities.add(varhooks);
				   }
			   }
				
		   }
			
            	Iterator itwhile = HarvestLevel.iterator();
            	Iterator ittool = ToolHash.iterator();
            
               //Forces forge hooks to read harvest levels for versions 1.7.10-1.8.9
               new ForgeHooks();
               
               //Registers This Harvest level after it recognizes that there is a new forge hooks
       		   MinecraftForge.EVENT_BUS.register(new HarvestLevelEvent() );
               
               while (itwhile.hasNext())
               {
                   String raw1 = itwhile.next().toString();
                   String[] partaa = raw1.split("=");
                   String blocka = partaa[0];
                   String strvalue = partaa[1];
                   int value = Integer.parseInt(strvalue);
                   String replaced = blocka.replaceFirst(":", "\u00A9");
                   String[] partsH = replaced.split("\u00A9");
                   String modidH = partsH[0];
                   String blockH = partsH[1];
                   String itool = "pickaxe";
                   boolean istool = false;
                   if (!SpawnerList.contains(blocka))
                   {
                       MainJava.SpawnerChecker.put(blocka, "no_value");
                   }
                   
                   Block blockh = GameRegistry.findBlock(modidH, blockH);
                   if (blockh != null)
                   {
                           for (int i=0;i<16;i++)
                           {
                           
                                 String toolit = blockh.getHarvestTool(i);
                                 if (toolit == null)
                                 {
                                     toolit = "pickaxe";
                                 }
                               if (!toolit.equals("pickaxe"))
                               {
                                    itool = toolit;
                               }
                           }
                           
                       if (!SpawnerList.contains(blocka))
                       {
                       blockh.setHarvestLevel(itool, value);
                       }
                       if (SpawnerList.contains(blocka))
                       {
                           //If it is a spawner it will set it's level to 0 however they will only drop with enchantments if your configed harvest level is right
                           blockh.setHarvestLevel(itool, 0);
                       }
                       //System.out.println(itool + " Block: " + blocka + " HarvestLevel:" + blockh.getHarvestLevel(0));
                       
                   }
               }
               
              BiomeGenBase[] biome = BiomeGenBase.getBiomeGenArray();
              for (BiomeGenBase bio : biome)
              {
            	  if(bio != null)
            	  if(bio.isEqualTo(BiomeGenBase.sky))
            	  {
					List<BiomeGenBase.SpawnListEntry> a = bio.getSpawnableList(EnumCreatureType.monster);
					List<BiomeGenBase.SpawnListEntry> b = bio.getSpawnableList(EnumCreatureType.ambient);
					List<BiomeGenBase.SpawnListEntry> c = bio.getSpawnableList(EnumCreatureType.creature);
					List<BiomeGenBase.SpawnListEntry> d = bio.getSpawnableList(EnumCreatureType.waterCreature);
						ge(a);
						ge(b);
						ge(c);
						ge(d);
            	  }
              }

               /*
               public static CreativeTabs MonsterSpawners = new CreativeTabs("MonsterSpawners")
               {
                  @Override
                  public Item getTabIconItem(){
                      return new ItemStack(Blocks.mob_spawner).getItem();
                  }
               };
               */
			
	  }
	 public static void ge(List<BiomeGenBase.SpawnListEntry> mr_renchen_dies)
     {
		for (BiomeGenBase.SpawnListEntry b : mr_renchen_dies)
	  	{
	  		if(b.entityClass != null && EntityList.classToStringMapping.containsKey(b.entityClass))
	  		{
	  			end_ents.add(EntityList.classToStringMapping.get(b.entityClass).toString());
	  		}
	  	}
     }
	
}

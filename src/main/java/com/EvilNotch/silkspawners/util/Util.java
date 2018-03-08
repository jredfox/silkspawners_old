package com.EvilNotch.silkspawners.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.EvilNotch.silkspawners.CustomMobSpawner;
import com.EvilNotch.silkspawners.DungeonSpawner;
import com.EvilNotch.silkspawners.EBlock;
import com.EvilNotch.silkspawners.EChecker;
import com.EvilNotch.silkspawners.MainJava;
import com.EvilNotch.silkspawners.SilkSpawners;
import com.EvilNotch.silkspawners.WorldLoadEvent;
import com.EvilNotch.silkspawners.config;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class Util {

	//Returns true for survival mode unless debug mode is on
	public static boolean isSurvival(EntityPlayer player) 
	{
		if(player == null || player.capabilities.isCreativeMode && !config.Debug|| !player.capabilities.allowEdit && !config.Debug)
		{
			return false;
		}
		
		return true;
	}

	/**
	 * Returns True If is Spawner or is aloud To Be A Spawner
	 */
	public static boolean isSpawner(Block b, TileEntity tile)
	{
		boolean blockcheck = isBlock(b, MainJava.SpawnerList);
		boolean isAutoBlacklisted = isBlock(b, MainJava.SpawnerBlackList);
		boolean auto =  tile instanceof TileEntityMobSpawner;
		
		if(tile != null && config.Debug)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			tile.writeToNBT(nbt);
				System.out.println("Defined:" + blockcheck + " Auto:" + auto + " BlackListed:" + isAutoBlacklisted + " TileNBT: " + nbt);
		}

		if(blockcheck == false && config.AutoDetectSpawners == false || !(tile instanceof TileEntityMobSpawner) && blockcheck == false || isAutoBlacklisted && blockcheck == false)
			return false;
		
		return true;
	}
	/**Sees if the harvest level is applicable to the block
	 * Returns true can harvest block
	 */
	public static boolean isHarvestLevel(Block b, ItemStack stack)
	{
		int blockharvest = config.harvestlevel;
		Iterator it = MainJava.HarvestLevel.iterator();
		
		//Re-Calculates virtual harvest level if any
		while(it.hasNext())
		{
			LineObj line = new LineObj(it.next().toString());//New Line instance <modid:block = int>
			if(GameRegistry.findBlock(line.getmodid(), line.getName()) == b && config.UseGlobalHarvestLevel == false)
				blockharvest = line.getHead();
		}

		if(config.Debug)
		System.out.println("Global:" + config.harvestlevel + " BlockHarvest:" + blockharvest + " Level:" + stack.getItem().getHarvestLevel(stack, getToolClass(stack)));
		
		if(stack.getItem().getHarvestLevel(stack, getToolClass(stack)) >= blockharvest)
			return true;
		
		//Calculates Tools Ignoring Harvest Level
	    Iterator toolit = MainJava.IToolList.iterator();
	    
	    if(config.GoldenPickaxeAlwaysEnabled)
	    while (toolit.hasNext())
	    {
	    	LineObj line = new LineObj(toolit.next().toString());
	    	if(GameRegistry.findItem(line.getmodid(), line.getName()) == stack.getItem())
	    		return true;
	    }
			
		return false;
	}
	/**
	 * Returns true if has right enchantment
	 */
	public static boolean isEnchantSilky(Block b, ItemStack stack)
	{

		boolean global = isGlobalEnchant(stack);
		boolean isoverriden = isBlockEnchant(b,stack);
		
		if(config.Debug)
		System.out.println("Enchants:\nGlobal: " + global + " Override: " + isoverriden);
		
		if(!isoverriden && global || config.DropWithoutEnchants == true && config.DoesEnchantOverride == false || config.DropWithoutEnchants && config.DoesEnchantOverride && !isoverriden)
			return true; //returns true if global and no overrides
		
		LineObj line = getBlockLine(b,MainJava.EListOverride);
		
		if(config.Debug)
		System.out.println("Line: " + line);
		
		if(line == null && !global || !global && !isoverriden)
			return false; //Returns if it's not a global id nor a block override
		
		List<String> list = getList(line.enchants);
		boolean correctoverride = isEnchantment(stack, list);
		
		if(config.Debug)
		System.out.println("Correct Override: " + correctoverride);

		if(!correctoverride || list.isEmpty() && isoverriden)
		{
			return false;
		}

		return true;
	}
	/**
	 * Finds out if is enchantments
	 * @param stack
	 * @param list
	 * @return boolean
	 * @Format <33:1>
	 */
	public static boolean isEnchantment(ItemStack stack, List list)
	{
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			LineObj line = new LineObj(it.next().toString());
			int enchid = 0;
			int level = 0;
			if(!line.getmodid().toLowerCase().equals("null") && !line.getName().toLowerCase().equals("null"))
			{
				enchid = Integer.parseInt(line.getmodid());
				level = Integer.parseInt(line.getName());
			}
			if(enchid == 0 && level == 0)
				return true; //Disabler for enchantment super method
			
				if(!EChecker.getEnchantmentLevel(enchid, stack).equals(""))
				{
					LineObj eline = new LineObj(EChecker.getEnchantmentLevel(enchid, stack));
					int lvl = Integer.parseInt(eline.getName());
					if(level > lvl || config.isEnchantsExcat && level != lvl)
						return false;
				}
				else
					return false;//Returns false if one enchantment that is required isn't found
		}
		return true;
	}
	/**
	 * Finds if Global Enchantments are right
	 * @param stack
	 */
	public static boolean isGlobalEnchant(ItemStack stack)
	{
		return isEnchantment(stack, MainJava.EList);
	}
	/**
	 * Returns true if block is on Enchant Override List
	 * @param block
	 * @param stack
	 */
	public static boolean isBlockEnchant(Block b, ItemStack stack)
	{
		boolean isblock = isBlock(b, MainJava.EListOverride);
		
		if(!isblock || !isBlock(b, MainJava.EListOverride))
			return false;
		

		return true;
	}
	public static List<String> getList(String[] str)
	{
		List<String> list = new ArrayList<String>();
		for(int i=0;i<str.length;i++)
			list.add(str[i]);
		
		return list;
	}
	
	/**Returns the string of the classes tool if any
	 * 
	 */
	public static String getToolClass(ItemStack heldItem)
	{
		  int w = 0;
		  String strtool = "pickaxe";
		    
		    //Gets the tool string name.  If it's not null and not = to pickaxe else it will print is tool? with the tool along with keeping the default as a pickaxe...
		   Set<String> settool = heldItem.getItem().getToolClasses(heldItem);
		   Iterator setit = settool.iterator();
		   while (setit.hasNext())
		   {
			   String itset = setit.next().toString();
			   
			   if (w == 0 && itset != null && !itset.equals("pickaxe"))
			   {
				   strtool = itset;
				   w++;
			   }
			   else{
				//   System.out.println("Is Not Tool? " + strtool);
			   	}
			   
		   }
		return strtool;
	}
	
	/**Gets Whitelisted Spawner from definespawners.txt
	 * returns true if is block
	 */
	public static boolean isBlock(Block b, List list)
	{
		return getBlockLine(b,list) != null;
	}
	/**Returns LineObj if block exists in list
	 * 
	 */
	public static LineObj getBlockLine(Block b, List list)
	{
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			LineObj line = new LineObj(it.next().toString());
			Block block = GameRegistry.findBlock(line.getmodid(), line.getName());
			if(block != null && block == b)
				return line;
		}
		return null;
	}
	/**
	 * Drops Spawner with NBT and name if possible
	 * @param tile
	 * @param b
	 * @param w
	 * @param x
	 * @param y
	 * @param z
	 * @param metadata
	 */
	public static void DropSpawner(TileEntity tile, Block b, World w, int x, int y, int z ,int metadata) 
	{
		NBTTagCompound nbt = new NBTTagCompound();
		tile.writeToNBT(nbt);
		w.setBlockToAir(x, y, z); //Destroys block
		if(modSupport(b,nbt,w,x,y,z,metadata))
			return;
		removeSpawnerTags(nbt); //Removes Spawner tags first so it can properly swap later
		System.out.println("Block:" + String.valueOf(b == GameRegistry.findBlock("SSR","tile.null")) + " TSTING:" + String.valueOf(b == GameRegistry.findBlock("SSR","tile\\.null")));
		
		ItemStack stack = swapNEIStack(b, nbt, w, metadata); //Remove for future versions and replace with new ItemStack(b,1,metadata);
		
		
		//If nbt is not applyable to the spawner drop block anyways
		if(!isNBTApplyable(nbt,stack,tile))
		{
			EBlock.DropBlock(w, x, y, z, stack);
			return;
		}
		addNames(nbt,stack,w,tile); //Adds Names if is applicable
		
		if(!(tile instanceof TileEntityMobSpawner) )
			nbt.setString("SilkTileEntity", "found");
		
		stack.setTagCompound(nbt);
		
		EBlock.DropBlock(w, x, y, z, stack);
		if(config.Debug)
			System.out.println("Dropped: " + nbt);
	}
	public static boolean modSupport(Block b, NBTTagCompound nbt2, World w, int x, int y, int z, int metadata)
	{
		//If someone defines soul shards as a spawner drop block and cancle event
		if(b == GameRegistry.findBlock("SSR","tile.null") || b == GameRegistry.findBlock("SSR","ssr_soul_cage") || b == GameRegistry.findBlock("SSTOW","sstow_soul_cage"))
		{
			DropBlock(b,w,x,y,z,metadata);//If someone is stupid to define these as a block drop spawner
			return true;
		}
		//HEE Support Their Tile Entity only works with structures so defaults to vanilla
		if(b == GameRegistry.findBlock("HardcoreEnderExpansion","custom_spawner"))
		{
			ItemStack stack = new ItemStack(Blocks.mob_spawner);
			NBTTagCompound nbt = new NBTTagCompound();
			int logic = (int)nbt2.getByte("logicId");

			if(logic ==0 || logic >= 4)
				nbt.setString("EntityId", "HardcoreEnderExpansion.AngryEnderman");
			if(logic ==1 || logic == 3)
				nbt.setString("EntityId", "Silverfish");
			if(logic ==2)
				nbt.setString("EntityId", "HardcoreEnderExpansion.Louse");
			Util.addNames(nbt, stack, w, null);
			stack = swapNEIStack(Blocks.mob_spawner, nbt, w, metadata);
			stack.setTagCompound(nbt);
			EBlock.DropBlock(w, x, y, z, stack);
			return true;
		}
		return false;
	}
	/**
	 * Work around to make it work as properly with NEI as possible
	 * @param b
	 * @param nbt
	 * @param w
	 * @param metadata
	 * @return
	 */
	public static ItemStack swapNEIStack(Block b, NBTTagCompound nbt, World w, int metadata) 
	{
		Entity entity = EntityList.createEntityByName(nbt.getString("EntityId"), w);
		if(!Loader.isModLoaded("NotEnoughItems") && config.cross_nei_compat && b == Blocks.mob_spawner)
		{	 
			  if (entity instanceof EntityLivingBase)
			  {
				  int global = EntityList.getEntityID(entity);
	            	if (global > 0 && !nbt.getString("EntityId").equals("EnderDragon"))
	            	{
	            		return new ItemStack(Blocks.mob_spawner, 1, global);
	            	}
			  }
				return new ItemStack(MainJava.chocoSpawner);
		}
		if(Loader.isModLoaded("NotEnoughItems") && b == Blocks.mob_spawner)
		{
			if(!useNEI(nbt, entity))
				return new ItemStack(MainJava.chocoSpawner);
			else{
				 int global = EntityList.getEntityID(entity);
			     return new ItemStack(Blocks.mob_spawner,1,global);
			}
		}
		
		return new ItemStack(b,1,metadata);
	}
	/**
	 * Used When NEI is Loaded On the Server Not Recommended
	 * @param nbt
	 * @param entity
	 * @return
	 */
	public static boolean useNEI(NBTTagCompound nbt, Entity entity)
	{
        //calls which method to use
        boolean use_nei = true;
        
        if( !(entity instanceof EntityLivingBase) )
    		use_nei = false;
        if(entity instanceof EntityLivingBase)
        {
    	  int global = EntityList.getEntityID(entity);
    	  if(global > 0)
    		  use_nei = true;
    	  else
    		  use_nei = false;
        }
        if(nbt.getString("EntityId").equals("EnderDragon")) //Hard Coded if ender dragon....
        	use_nei = false;
        
        //Checks SpawnData{} && SpawnPotentials:[]
        if (nbt.getTag("SpawnData") != null || SilkSpawners.getTagList(nbt, "SpawnPotentials", 10) != null)
        {
        	NBTTagCompound tag = SilkSpawners.getNBT(nbt,"SpawnData");
        	NBTTagList poten = new NBTTagList();
        	boolean check = false;
        	
        	if (SilkSpawners.getTagList(nbt, "SpawnPotentials", 10) != null)
        	{
        		poten = SilkSpawners.getTagList(nbt, "SpawnPotentials", 10);
        		if (poten.tagCount() > 0)
        			check = true;
        	}
        	if(tag.hasNoTags() == false || check == true)
        		use_nei = false;
        }
        if (config.UseChocoSpawner == true || SilkSpawners.isCustomSpawnerPos(nbt) == true || hasData(nbt) )
        	use_nei = false;
       
        return use_nei;
	}
	/**
	 * Gets if NBT has custom data used for NEI swap stacks Doesn't Check SpawnData:{} or SpawnPotentials:[]
	 * @param nbt
	 * @return
	 */
	public static boolean hasData(NBTTagCompound nbt)
	{
		if(nbt == null)
			return false;
		ArrayList<String> list = new ArrayList<String>();
		list.add("MaxNearbyEntities");
		list.add("RequiredPlayerRange");
		list.add("SpawnCount");
		list.add("MaxSpawnDelay");
		list.add("SpawnRange");
		list.add("MinSpawnDelay");
		list.add("Delay");
		for(int i=0;i<list.size();i++)
		{
			String s = list.get(i);
			if(nbt.hasKey(s))
			{
				int number = (int)nbt.getShort(s);
				if(number == 0)
					number = nbt.getInteger(s);
				
				System.out.println(s + ":" + number);
				//System.out.println("Delay:" + hasCustomDelay(nbt));
				if(s.equals("MaxNearbyEntities") && number != 6)
					return true;
				if(s.equals("RequiredPlayerRange") && number != 16)
					return true;
				if(s.equals("SpawnCount") && number != 4)
					return true;
				if(s.equals("MaxSpawnDelay") && number != 800)
					return true;
				if(s.equals("SpawnRange") && number != 4)
					return true;
				if(s.equals("MinSpawnDelay") && number != 200)
					return true;
				if(s.equals("Delay") && hasCustomDelay(nbt))
					return true;
			}
		}
		return false;
	}

	//Drops Block Without NBT
	public static void DropBlock(Block b, World w, int x, int y, int z, int metadata) 
	{
		w.setBlockToAir(x, y, z);
		EBlock.DropBlock(w, x, y, z, new ItemStack(b,1,metadata));
	}
	//Drops Block With NBT
	public static void DropBlock(Block b, World w, int x, int y, int z, int metadata, NBTTagCompound nbt) 
	{
		w.setBlockToAir(x, y, z);
		ItemStack stack = new ItemStack(b,1,metadata);
		stack.setTagCompound(nbt);
		EBlock.DropBlock(w, x, y, z, stack);
	}
	/**Hard Coded for now will have a smarter way to keep tags in the future. 
	 * Removes Spawner Tags Via Config (Converted to method for Config OBJ Update Preperation)
	 * @param nbt
	 */
	public static void removeSpawnerTags(NBTTagCompound nbt)
	{
		nbt.removeTag("display"); //Removes Previous Display
		if(!config.MaxNearbyEntities && nbt.hasKey("MaxNearbyEntities"))
			nbt.setInteger("MaxNearbyEntities",6);
		if(!config.RequiredPlayerRange && nbt.hasKey("RequiredPlayerRange"))
			nbt.setShort("RequiredPlayerRange",(short)16);
		if(!config.SpawnCount && nbt.hasKey("SpawnCount"))
			nbt.setShort("SpawnCount",(short)4);
		if(!config.MaxSpawnDelay && nbt.hasKey("MaxSpawnDelay"))
			nbt.setShort("MaxSpawnDelay",(short)800);
		if(!config.SpawnRange && nbt.hasKey("SpawnRange"))
			nbt.setShort("SpawnRange",(short)4);
		if(!config.Delay && !hasCustomDelay(nbt))
			nbt.removeTag("Delay");
		if(!config.MinSpawnDelay && nbt.hasKey("MinSpawnDelay"))
			nbt.setShort("MinSpawnDelay",(short)200);
		if(!config.SpawnData)
			nbt.removeTag("SpawnData");
		if(!config.SpawnPotentials)
			nbt.removeTag("SpawnPotentials");
		if (!SilkSpawners.isCustomSpawnerPos(nbt))
	    {
	    	nbt.setInteger("x", 0);
	    	nbt.setInteger("y", 0);
	    	nbt.setInteger("z", 0);
	    }

	}

	public static boolean hasCustomDelay(NBTTagCompound nbt) {
		int max = nbt.getShort("MaxSpawnDelay");
		int min = nbt.getShort("MinSpawnDelay");
		int delay = nbt.getShort("Delay");
		if(max == 0)
			max =  nbt.getInteger("MaxSpawnDelay");
		if(min == 0)
			min = nbt.getInteger("MinSpawnDelay");
		if(delay == 0)
			delay = nbt.getInteger("Delay");
		
		if(delay > max  && nbt.hasKey("MaxSpawnDelay") && delay != 0)
			return true;
		return false;
	}

	public static void addNames(NBTTagCompound nbt, ItemStack stack, World world,TileEntity tile)
	{
		if(!isNameApplyable(nbt,stack,tile) )
		{
			System.out.println("Returning:" + stack.getUnlocalizedName() );
			return;
		}
		
		String trans = getItemTranslation(nbt,stack,true);
    	
    	NBTTagCompound name = new NBTTagCompound();
    	name.setString("Name", trans);
    	nbt.setTag("display", name);
    	
	}
	/**
	 * Returns the entire translated entity string to an item/block
	 * @param nbt
	 * @param stack
	 * @return
	 */
	public static String getItemTranslation(NBTTagCompound nbt, ItemStack s, boolean isBlock)
	{
		//Null Checkers
		if(s == null)
			return "";
		if(nbt == null && isBlock)
			return ("" + StatCollector.translateToLocal(s.getItem().getUnlocalizedName() + ".name")).trim();
		if(nbt == null && !isBlock)
			return ("" + StatCollector.translateToLocal(s.getItem().getUnlocalizedName() + ".name")).trim();
		ItemStack stack = new ItemStack(s.getItem());
		stack.setTagCompound(nbt);
		
		String EntityLocalizedName = SilkSpawners.getCurrentTranslatedEntity(SilkSpawners.getJockeyId(stack));
    	EntityLocalizedName = SilkSpawners.getJockey(nbt, EntityLocalizedName, stack); //tells if current string is jockey
    	
    	if(!EntityLocalizedName.equals(""))
    	{
    		if(stack.getItem() != MainJava.forge_egg && stack.getItem() != MainJava.caged_egg)
    			EntityLocalizedName += " "; //fix in case there isn't a detected entity
    	}
    	SilkSpawners.cacheEntities(); //Caches entities if not already done <String to EntityOBJ>
    	
    	String color = SilkSpawners.getColoredEntityText(SilkSpawners.entities.get(SilkSpawners.getJockeyId(stack)));

    	String block = "";
    		if(isBlock)
    			block = ("" + StatCollector.translateToLocal(Block.getBlockFromItem(stack.getItem()).getUnlocalizedName() + ".name")).trim();
    		else
    			block = ("" + StatCollector.translateToLocal(stack.getItem().getUnlocalizedName() + ".name")).trim();
    		if(stack.getItem() == MainJava.forge_egg || stack.getItem() == MainJava.caged_egg)
    		{
    			block = ("" + StatCollector.translateToLocal(Items.spawn_egg.getUnlocalizedName() + ".name")).trim();
    			block += " ";
    		}
    		if(isBlock && config.AllSpawnersSameFormat && hasEntityId(nbt) )
    			block = config.MonsterSpawner;
    		//System.out.println("Before:" + block);
    		
        	//Hard Codded Fix For AOA Spawners
    		String wBlock = LineObj.toWhiteSpaced(block).toLowerCase();
    		String wentity = LineObj.toWhiteSpaced(EntityLocalizedName).toLowerCase();
    	if(wBlock.contains(wentity))
    	{
    		String temp = "";
    		for(int i=0;i<wentity.length();i++)
    		{
    			temp += wBlock.substring(i, i+1);
    		}
    		if(temp.equals(wentity))
    			EntityLocalizedName = "";
    	}
    	String trans = EntityLocalizedName;
    	if(trans.equals(""))
    		trans = block; //Fix if the entity names returns "";
    	if(EntityLocalizedName.length() + block.length() < 35 && stack.getItem() != MainJava.forge_egg && stack.getItem() != MainJava.caged_egg)
 			trans = color + EntityLocalizedName + block + EnumChatFormatting.RESET;
    	if(stack.getItem() == MainJava.forge_egg || stack.getItem() == MainJava.caged_egg)
    		trans = color + block + EntityLocalizedName + EnumChatFormatting.RESET;
    	if(EntityLocalizedName.length() + block.length() >= 35 && !config.AllSpawnersSameFormat && isBlock)
    		trans = color + EntityLocalizedName + EnumChatFormatting.RESET;//Makes it have color regardless of length of the text
    	if(config.AllSpawnersSameFormat && isBlock)
    		trans = color + EntityLocalizedName + block + EnumChatFormatting.RESET;//Makes it have color regardless of length of the text
    	if(EntityLocalizedName.length() + block.length() >= 35 && stack.getItem() != MainJava.forge_egg && stack.getItem() != MainJava.caged_egg && !isBlock)
 			trans = color + EntityLocalizedName + EnumChatFormatting.RESET;
		//System.out.println("After:" + block + " " + trans);
    	return trans;
	}
	public static boolean hasEntityId(NBTTagCompound nbt)
	{
		return !getEntityId(nbt).equals("");
	}
	public static String getEntityId(NBTTagCompound nbt)
	{
		if(nbt == null)
			return "";
		
		if(nbt.hasKey("EntityId"))
			return nbt.getString("EntityId");
		
		return "";
	}
	/**
	 * Makes names applicable if they are aloud to be applied. Will use NBT in the future
	 * @param nbt
	 * @param stack
	 * @return
	 */
	public static boolean isNBTApplyable(NBTTagCompound nbt, ItemStack stack, TileEntity tile)
	{
		Block eblock = Block.getBlockFromItem(stack.getItem() );
		//1st checks modded spawner (whitelisted) 2d checks only auto detection 3d checks if it's a vanilla spawner
		if (config.WriteModdedSpawnersToNBT == true && !isVanillaSpawner(eblock) && isBlock(eblock, MainJava.SpawnerList)|| tile instanceof TileEntityMobSpawner && config.AutoDetectSpawners == true && config.autowrite == true && !isBlock(eblock, MainJava.SpawnerList) || isVanillaSpawner(eblock) && isBlock(eblock, MainJava.SpawnerList))
			return true;
		
		return false;
	}
	/**
	 * Future nbt args
	 * @param nbt
	 * @param stack
	 * @param tile
	 * @return
	 */
	public static boolean isNameApplyable(NBTTagCompound nbt, ItemStack stack, TileEntity tile)
	{	
		Block eblock = Block.getBlockFromItem(stack.getItem());
		if(config.setModdedSpawnersName && !isVanillaSpawner(eblock) && isBlock(eblock, MainJava.SpawnerList) || tile instanceof TileEntityMobSpawner && config.AutoDetectSpawners == true && config.autoName== true && !isVanillaSpawner(eblock) && !isBlock(eblock, MainJava.SpawnerList) || eblock == Blocks.mob_spawner && isBlock(eblock, MainJava.SpawnerList) || eblock == Blocks.mob_spawner && config.AutoDetectSpawners && config.autoName)
				return true;
		
		return false;
	}
	public static boolean isVanillaSpawner(Block b)
	{
		if(b instanceof CustomMobSpawner || b instanceof DungeonSpawner || b == Blocks.mob_spawner)
			return true;
		
		return false;
	}

	/**
	 * Drops Spawner Eggs Based On NBT
	 * @param eventiles
	 * @param eblock
	 * @param heldItem
	 */
	public static void DropEggs(TileEntity eventiles,Block eblock, ItemStack heldItem, List<ItemStack> list) 
	{
		int chance = getEggChance(heldItem);
		if(chance < 3)
			return; //Return if chance isn't met fortune increases the chances of an egg dropping
		NBTTagCompound nbt = new NBTTagCompound();
		eventiles.writeToNBT(nbt);
		Util.removeSpawnerTags(nbt);
		
		//Ender Io Method
		if(Loader.isModLoaded("EnderIO"))
		{
			if(!Util.getEntityId(nbt).equals("Blank") || !Util.getEntityId(nbt).equals(""))
			{
			//mobType
				ItemStack stack = new ItemStack(GameRegistry.findItem("EnderIO", "itemBrokenSpawner"));
				NBTTagCompound nbt2 = new NBTTagCompound();
				if(!Util.getEntityId(nbt).equals(""))
				{
				nbt2.setString("mobType", Util.getEntityId(nbt));
				stack.setTagCompound(nbt2);
				if(!(eblock == GameRegistry.findBlock("EnderIO","blockPoweredSpawner") ) )
					list.add(stack);
				return;
				}
			}
		}
			
		if(!Util.hasEntityId(nbt) || isEntityNull(nbt))
		{
			if(Util.getEntityId(nbt).equals("Blank"))
			{
				ItemStack stack = new ItemStack(MainJava.forge_egg);
				NBTTagCompound nnbt = new NBTTagCompound();
				nnbt.setString("EntityId",Util.getEntityId(nbt));
				stack.setTagCompound(nnbt);
				stack.stackSize = getEggDropSize(heldItem);
				list.add(stack);
			}
			return;
		}
			
			String entityid = Util.getEntityId(nbt);
			ItemStack stack = new ItemStack(MainJava.forge_egg);
			
			if(isEntityNonLiving(nbt) )
				stack = new ItemStack(MainJava.caged_egg);
			else{
				stack = getItemStackEgg(nbt);
			}
			if(stack.getItem() != Items.spawn_egg)
				stack.setTagCompound(getSpawnerToEggNBT(nbt));
			stack.stackSize = getEggDropSize(heldItem);
			list.add(stack);
	}
	/**
	 * Sets Stack size based on fortune Dynamically
	 * @param stack
	 * @return
	 */
	public static int getEggDropSize(ItemStack stack) 
	{
		int rnd = (int)(Math.random() * 2+1);
		int fortune = EnchantmentHelper.getEnchantmentLevel(35, stack);
		int count = 1;
		if(fortune > 0)
		{
			if(fortune == 1 && rnd == 2)
			    count +=1;
			if(fortune == 2)
			{
				if(rnd == 2)
					count += (int)(Math.random() * 2);
			}
			if(fortune < 10 && fortune > 2)
			{
				count += (int)(Math.random() * fortune);
			}
			if(fortune >= 10 && fortune < 100)
			{
				count += 2;
				count += (int)(Math.random() * (fortune+1) );
			}
			if(fortune >= 100)
			{
				count += fortune;
			}
			if(count > config.EggCap)
				count = config.EggCap;
		}
		System.out.println("Fortune:" + fortune + " Count:" + count + " RND:" + rnd);
		return count;
	}

	public static int getEggChance(ItemStack stack) 
	{
		if(stack == null || stack.getItem() == null)
			return 0;
		int rnd = (int)(Math.random() * 3+1);

		int fortune = EnchantmentHelper.getEnchantmentLevel(35, stack);
		rnd += fortune;
		if(fortune > 1)
			rnd -=1;//preventative from increasing chances too much
		if(config.DisableEggDropChanceFromSpawner)
			rnd = 3;//Allows the drop chance to be disabled
		System.out.println("Chance:" + rnd);
		return rnd;
	}

	/**
	 * Method for swapping which egg is which. Will also be used for ender eggs During Mounting Update II
	 * Also in the future it will have the abiltity to have modded eggs drop if has no custom data
	 * @param nbt
	 * @return ItemStack Egg
	 */
	public static ItemStack getItemStackEgg(NBTTagCompound nbt) 
	{
		if(hasGlobalId(nbt) && EntityList.entityEggs.containsKey(getGlobalId(nbt)) && !spawnerHasEntityNBT(nbt) )
			return new ItemStack(Items.spawn_egg,1, getGlobalId(nbt));
		
		return new ItemStack(MainJava.forge_egg);
	}
	public static boolean hasGlobalId(NBTTagCompound nbt){return getGlobalId(nbt) > 0;}
	
	public static int getGlobalId(NBTTagCompound nbt)
	{
		Entity entity = getEnt(nbt);
		if(entity == null || isEntityNonLiving(nbt) && !getEntityId(nbt).equals("Pig") || spawnerHasEntityNBT(nbt))
			return 0;
		if(EntityList.getEntityID(entity) > 0)
			return EntityList.getEntityID(entity);
		
		return 0;
	}
	public static boolean spawnerHasEntityNBT(NBTTagCompound nbt)
	{
		if(nbt != null)
		{
			if(nbt.getTag("SpawnData") != null)
			{
				NBTTagCompound data = (NBTTagCompound)nbt.getTag("SpawnData");
				if(!data.hasNoTags())
					return true;
			}
			if(SilkSpawners.getTagList(nbt, "SpawnPotentials", 10).tagCount() > 0)
			{
				NBTTagList list = nbt.getTagList("SpawnPotentials", 10);
				for(int i=0;i<list.tagCount();i++)
				{
					NBTTagCompound tag = list.getCompoundTagAt(i);
					if(!tag.hasNoTags() && tag.hasKey("Properties"))
					{
						return true;
					}
				}
			}
		}//Properties
		return false;
	}

	public static NBTTagCompound getSpawnerToEggNBT(NBTTagCompound nbt) 
	{
		NBTTagCompound egg = new NBTTagCompound();
		egg.setString("EntityId",getEntityId(nbt));
		
		NBTTagList list = new NBTTagList();
		
		//Formats all types into an nbt iterable tag list. Still may contain riding tags however will be fixed later.
		if(nbt.hasKey("SpawnData"))
		{
			egg.setTag("EntityNBT", nbt.getTag("SpawnData"));
		}
	
		list.appendTag(egg);
		egg = toMountingTags(egg);
		int Tier = getTier(nbt);
		System.out.println("Tier:" + Tier + " " + nbt);
		if(Tier > 1)
			egg.setInteger("ForgeLvl", Tier);
		return egg;
	}
	/**
	 * Gets an egg tier from spawner to egg based on tags
	 * @param nbt
	 * @return
	 */
	public static int getTier(NBTTagCompound nbt)
	{
		int tier = 1;
		if(nbt.hasKey("SpawnPotentials"))
		{
			NBTTagList list = SilkSpawners.getTagList(nbt, "SpawnPotentials", 10);
			for(int i=0;i<list.tagCount();i++)
			{
				NBTTagCompound tag = (NBTTagCompound) list.getCompoundTagAt(i).getTag("Properties");
				int index = Tier(tag);
				if(index > tier)
					tier = index;
			}
		}
		if(nbt.hasKey("SpawnData"))
		{
			int data = Tier(nbt);
			if(data > tier)
				tier = data;
		}
		return tier;
	}
	public static int Tier(NBTTagCompound nbt)
	{

		if(nbt == null)
			return 1;
		System.out.println(nbt + " uuid:" + nbt.hasKey("UUIDMost") + " HealF" + nbt.hasKey("HealF") + " Equipment:" + nbt.hasKey("Equipment"));
		if(nbt.hasKey("UUIDMost"))
			return 4;
		if(nbt.hasKey("HealF"))
			return 3;
		if(nbt.hasKey("Equipment"))
			return 2;
		return 1;
	}
	/**
	 * In format of SpawnerNBT
	 * @param list
	 * @returnd
	 */
	public static NBTTagCompound toMountingTags(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		NBTTagCompound index0 = new NBTTagCompound();
		index0.setString("id", nbt.getString("EntityId"));
		
		if(!nbt.hasKey("EntityNBT",10))
			list.appendTag(index0);
		if(nbt.hasKey("EntityId") && nbt.hasKey("EntityNBT",10))
		{
			NBTTagCompound tag =(NBTTagCompound)nbt.getTag("EntityNBT").copy();
			NBTTagCompound ent = (NBTTagCompound) nbt.getTag("EntityNBT");
			ent.removeTag("Riding");
			if(!ent.hasNoTags())
				index0.setTag("EntityNBT", ent);
			list.appendTag(index0);
			while(tag.hasKey("Riding",10))
			{
				tag = (NBTTagCompound)tag.getTag("Riding");
				NBTTagCompound copy = (NBTTagCompound)tag.copy();
				copy.removeTag("Riding");
				NBTTagCompound append = new NBTTagCompound();
				append.setString("id", copy.getString("id"));
				copy.removeTag("id");
				if(!copy.hasNoTags())
					append.setTag("EntityNBT", copy);
				list.appendTag(append);
			}
		}
		if(config.Debug)
			System.out.println("List: " + list + "\nInverted List:" + reverseNBTList(list));
		return reverseNBT(list);
	}
	/**
	 * Takes NBT List Reverts then formats it back to tagcompound
	 * @param list
	 * @return
	 */
	public static NBTTagCompound reverseNBT(NBTTagList list)
	{
		list = reverseNBTList(list); //inverts lists
		NBTTagCompound nbt_tst = list.getCompoundTagAt(0);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("EntityId", nbt_tst.getString("id"));
		if(nbt_tst.hasKey("EntityNBT",10))
			nbt.setTag("EntityNBT", nbt_tst.getTag("EntityNBT"));
		if(list.tagCount() > 0)
			list.removeTag(0);
		if(list.tagCount() > 0)
			nbt.setTag("Mounting", list);
		return Util.removeCustomNames(nbt, true);
	}

	/**
	 * Inverts NBTTagList specifically used for ToSpawner or ToMounting Processes
	 * @param nbt
	 */
	public static NBTTagList reverseNBTList(NBTTagList l)
	{
		NBTTagList list = new NBTTagList();
		
		if(l.tagCount() > 0)
		for(int i=l.tagCount()-1;i>=0;i--)
		{
			list.appendTag(l.getCompoundTagAt(i));
		}
		return list;
	}

	public static boolean isEntityNull(NBTTagCompound nbt)
	{
		return getEnt(nbt) == null;
	}
	public static Entity getEnt(NBTTagCompound nbt)
	{
		return EntityList.createEntityByName(Util.getEntityId(nbt),WorldLoadEvent.world2);
	}
	public static boolean isEntityNonLiving(NBTTagCompound nbt)
	{
		if(isEntityNull(nbt) || getEnt(nbt) instanceof EntityLivingBase)
			return false;
		
		return true;
	}
	public static boolean isLiving(NBTTagCompound nbt)
	{
		return getEnt(nbt) instanceof EntityLivingBase;
	}

	public static NBTTagCompound removeCustomNames(NBTTagCompound nbt, boolean eggFormat) 
	{
		if(eggFormat)
		{
			if(nbt.hasKey("EntityNBT",10))
			{
				NBTTagCompound tag = (NBTTagCompound) nbt.getTag("EntityNBT");
				if(tag.getString("CustomName") == null || tag.getString("CustomName").equals("") || tag.getString("CustomName").equals("\"\""))
					tag.removeTag("CustomName");
			}
			NBTTagList list = SilkSpawners.getTagList(nbt, "Mounting", 10);
			if(list.tagCount() > 0)
				list = nbt.getTagList("Mounting", 10);
			
			for(int i=0;i<list.tagCount();i++)
			{
				NBTTagCompound index = list.getCompoundTagAt(i);
				if(index.hasKey("EntityNBT",10))
				{
					NBTTagCompound tag = (NBTTagCompound) index.getTag("EntityNBT");
					if(tag.getString("CustomName") == null || tag.getString("CustomName").equals("") || tag.getString("CustomName").equals("\"\""))
						tag.removeTag("CustomName");
				}
			}
		}
		else{//Pure Entity Format Supports riding tags
			if(nbt.getString("CustomName") == null || nbt.getString("CustomName").equals("") || nbt.getString("CustomName").equals("\"\""))
				nbt.removeTag("CustomName");
			
			//Removes all instances of Jockey names
			NBTTagCompound tag = nbt;
			
			if(tag != null)
			while(tag.hasKey("Riding",10))
			{
				tag = (NBTTagCompound)tag.getTag("Riding");
				if(tag.getString("CustomName") == null || tag.getString("CustomName").equals("") || tag.getString("CustomName").equals("\"\""))
				{
					tag.removeTag("CustomName");
				}
			}
		}
		return nbt;
	}

	/**
	 * For future use of my own Tile Entity Override. Maybe have custom model render like NEI does. Till then using a work around with creating entity.writeToNBT(); if !isJockey
	 * @param nbt
	 * @return
	 * @Format
	 * Inverted EggList
	 */
	public static NBTTagCompound getFormatEggToSpawnerNBT(NBTTagCompound nbt, World w, int x, int y, int z) 
	{

    	//NO NBT SUPPORT YET
		if(!isJockey(nbt,true))
		{
			NBTTagCompound spawner = new NBTTagCompound();
			if(nbt.hasKey("EntityNBT",10))
				spawner.setTag("SpawnData", nbt.getTag("EntityNBT"));
			spawner.setString("EntityId", Util.getEntityId(nbt));
			spawner.setInteger("x", x);
			spawner.setInteger("y", y);
			spawner.setInteger("z", z);
    		NBTTagList spawnpot = new NBTTagList();
    		NBTTagCompound index0 = new NBTTagCompound();
    		
    		index0.setString("Type",  Util.getEntityId(nbt));
    		index0.setInteger("Weight", 1);
			if(nbt.hasKey("EntityNBT",10))
				index0.setTag("Properties", nbt.getTag("EntityNBT"));
    		spawnpot.appendTag(index0);
    		if(nbt.hasKey("EntityNBT",10))
    			spawner.setTag("SpawnPotentials", spawnpot);
			return spawner;
		}
		
			NBTTagCompound temp = new NBTTagCompound();
    		ItemStack stack = new ItemStack(MainJava.forge_egg);
    		stack.setTagCompound(nbt);
    		
    		System.out.println("NBT:" + nbt + " ToList:" + Util.eggToList(nbt));
    		String entid = Util.getEntityId(nbt);
        	nbt = Util.toRidingTags(Util.eggToList(nbt));
    		//SilkSpawners.spawnCreature(w,stack, x, y, z, false).writeToNBT(temp); //Fixes vanilla TileEntityMobSpawnerIssue

        	removeCustomNames(nbt,false);
    		NBTTagCompound spawner = new NBTTagCompound();
    		NBTTagList spawnpot = new NBTTagList();
    		NBTTagCompound index0 = new NBTTagCompound();
    		
    		index0.setString("Type", entid);
    		index0.setInteger("Weight", 1);
    		index0.setTag("Properties", nbt);
    		spawnpot.appendTag(index0);
    		
    		spawner.setString("EntityId", entid);
    		spawner.setTag("SpawnData", nbt);
    		spawner.setTag("SpawnPotentials", spawnpot);
    		spawner.setInteger("x", x);
    		spawner.setInteger("y", y);
    		spawner.setInteger("z", z);
    		
    		return spawner;
	}
	/**
	 * Returns pure entity format of entity any mounted has riding tags
	 * @param list
	 * @return
	 * @Format
	 * Inverted toEgg() NBTList
	 */
	public static NBTTagCompound toRidingTags(NBTTagList list) 
	{
		if(list == null || list.tagCount() == 0)
			return null;
		filterTiers(list); //Makes any Tier I spawns become Tier II so it keeps equipment... Work around for vanilla for now
		System.out.println("Filtered:" + list);
		
		NBTTagCompound index0 = (NBTTagCompound) list.getCompoundTagAt(0).copy();
		NBTTagCompound nbt = new NBTTagCompound();
		if(index0.hasKey("EntityNBT"))
		{
			NBTTagCompound ent = (NBTTagCompound) index0.getTag("EntityNBT");
			Set<String> sets = ent.func_150296_c();
			Iterator it = sets.iterator();
			while(it.hasNext())
			{
				String s = it.next().toString();
				NBTBase in = (NBTBase) ent.getTag(s);
				nbt.setTag(s, in);
			}
		}
		list.removeTag(0);
		System.out.println("Pure EntityNBT Format:" + nbt);
		
		//Actual Work For TORidingTags()
		NBTTagCompound riding = new NBTTagCompound();
		riding.setTag("Riding", new NBTTagCompound());
		NBTTagCompound work = (NBTTagCompound) riding.getTag("Riding");
		for(int i=0;i<list.tagCount() && work != null;i++)
		{
			NBTTagCompound index = list.getCompoundTagAt(i);
			NBTTagCompound append = (NBTTagCompound) index.getTag("EntityNBT");
			
			if(append != null)
			{
				Set<String> sets = append.func_150296_c();
				Iterator it = sets.iterator();
				while(it.hasNext())
				{
					String s = it.next().toString();
					NBTBase in = (NBTBase) append.getTag(s);
					work.setTag(s, in);
				}
			}
			work.setString("id", index.getString("id"));
			if(i+1<list.tagCount())
				work.setTag("Riding", new NBTTagCompound());
			work = (NBTTagCompound) work.getTag("Riding");
		}
		nbt.setTag("Riding", riding.getTag("Riding"));
		System.out.println("Allocation:" + nbt);
		return nbt;	
	}
	public static void filterTiers(NBTTagList list)
	{
		for(int i=0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			if(!nbt.hasKey("EntityNBT",10))
			{
				ItemStack stack = new ItemStack(MainJava.forge_egg);
				NBTTagCompound ee = new NBTTagCompound();
				ee.setString("EntityId", nbt.getString("id"));
				stack.setTagCompound(ee);
				Entity e = SilkSpawners.spawnCreature(WorldLoadEvent.world2, stack, 0, 0, 0, false);//Since not spawning them in doesn't need coords or a valid stack
				NBTTagCompound data = new NBTTagCompound();
				e.writeToNBT(data);
				removeEggNBT(e,data,2);
				nbt.setTag("EntityNBT", data);
			}	
		}
	}
	/**
	 * In egg form translates to iterable list
	 * @param nbt
	 * @return
	 */
	public static NBTTagList eggToList(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		NBTTagCompound index0 = new NBTTagCompound();
		NBTTagList mounting = SilkSpawners.getTagList(nbt,"Mounting", 10);
		index0.setString("id", Util.getEntityId(nbt));
		if(nbt.hasKey("EntityNBT"))
			index0.setTag("EntityNBT", nbt.getTag("EntityNBT"));
		list.appendTag(index0);
		for(int i=0;i<mounting.tagCount();i++)
			list.appendTag(mounting.getCompoundTagAt(i));
	
		return list;
	}

	public static boolean isJockey(NBTTagCompound nbt, boolean isEgg)
	{
		if(isEgg)
			return SilkSpawners.getTagList(nbt, "Mounting", 10).tagCount() > 0;
		else{
	         if (nbt.hasKey("SpawnData"))
	         {
	         	NBTTagCompound data = (NBTTagCompound)nbt.getTag("SpawnData");
	         	if (data.getTag("Riding") != null)
	         	{
	         		return true;
	         	}
	         }
	         if (nbt.hasKey("SpawnPotentials"))
	         {
	         	NBTTagList data = new NBTTagList();
	         	if (nbt.getTagList("SpawnPotentials", 10) != null)
	         	{
	         		data = nbt.getTagList("SpawnPotentials", 10);
	         		
	         		if (data.tagCount() > 0)
	         		{
	         			for(int i=0;i<data.tagCount();i++)
	         			{
	         				if(data.getCompoundTagAt(i).hasKey("Properties"))
	         				{
	         					NBTTagCompound tag = (NBTTagCompound)data.getCompoundTagAt(i).getTag("Properties");
	         					if (tag.hasKey("Riding"))
	         						return true;
	         				}
	         			}
	         		}
	         	}
	         }
		}
		return false;
		
	}
	/**
	 * Stub. Will be recalPos if has custom pos only Avilabe if manually configed or a new Tier IV Egg
	 * @param nbt
	 * @return
	 */
	public static NBTTagCompound recalPos(NBTTagCompound nbt) 
	{
		return nbt;
	}
	/**
	 * Removes NBT Based on egg Tier and makes spawners/eggs stackable if they can be
	 * @param e
	 * @param data
	 * @param Tier
	 */
	public static void removeEggNBT(Entity e, NBTTagCompound data, int Tier) 
	{
		data.removeTag("Pos"); //Removes Pos so it doesn't have the pos to be applied specifically to the spawner...
		data.removeTag("Riding"); //fix so it doesn't get the whole stack and kill a single entity dead
		if(Tier == 4)
			return;//Returns if Tier is too high
		Util.removeCustomNames(data, false);
		Util.removeUUIdModifiers(data,Tier);
		if(e instanceof EntityLivingBase)
		{
			data.removeTag("Motion");
			data.removeTag("Rotation");
		}
		String[] tierII = {"id","DropChances","Attributes","HealF","FallDistance","AbsorptionAmount","UUIDMost","UUIDLeast","PortalCooldown","Dimension","HurtTime","Health","Fire","DeathTime","AttackTime","Air","PersistenceRequired","OnGround","Leashed","Invulnerable","CustomNameVisible","CanPickUpLoot","carried","carriedData","Fuse","ignited","ExplosionRadius","ConversionTime","CanBreakDoors","ExplosionPower","CanPickUpLoot","Anger","Angry","BatFlags","Saddle","InLove","Sheared","IsChickenJockey","Sitting","OwnerUUID","Items","Bred","ChestedHorse","EatingHaystack","HasReproduced","Temper","Riches","Offers","PlayerCreated"};
		String[] tierIII = {"UUIDMost","UUIDLeast"};
		if(Tier == 2)
			for(String s : tierII)
				data.removeTag(s);
		if(Tier == 3)
			for(String s : tierIII)
				data.removeTag(s);
		if(config.Debug)
			System.out.println("BLankNBT: " + data);
	}

	private static void removeUUIdModifiers(NBTTagCompound data, int Tier) 
	{
		NBTTagList list = SilkSpawners.getTagList(data, "Attributes", 10);
		if(list.tagCount() > 0)
			list = data.getTagList("Attributes", 10);
		
		for(int i=0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.getCompoundTagAt(i);
			if(nbt.hasKey("Modifiers"))
			{
				if(Tier == 2)
					nbt.removeTag("Modifiers");
				if(Tier == 3)
				{
					NBTTagList list2 = SilkSpawners.getTagList(nbt, "Modifiers", 10);
					if(list2.tagCount() > 0)
						list2 = nbt.getTagList("Modifiers", 10);
					
					for(int j=0;j<list2.tagCount();j++)
					{
						NBTTagCompound nbt2 = (NBTTagCompound)list2.getCompoundTagAt(j);
						nbt2.removeTag("UUIDLeast");
						nbt2.removeTag("UUIDMost");
						nbt2.removeTag("Amount");
					}
				}
			}
		}
	}
	/**
	 * Gets Entity String From ItemDamage
	 * @param itemDamage
	 * @return
	 */
	public static String getEntNEI(int itemDamage) 
	{
		Entity entity = EntityList.createEntityByID(itemDamage, WorldLoadEvent.world2);
		if(entity != null)
		{
			String s = EntityList.getEntityString(entity);
			if(s != null)
				return s;
		}
		return "";
	}
	
}

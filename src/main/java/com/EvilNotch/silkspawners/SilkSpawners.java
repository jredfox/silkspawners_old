package com.EvilNotch.silkspawners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.EvilNotch.silkspawners.util.LineObj;
import com.EvilNotch.silkspawners.util.Util;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class SilkSpawners {

	public static HashMap<String,String> forge_eggs = new HashMap<String,String>();
	public static HashMap<String,String> mob_spawners = new HashMap<String,String>();
	public static HashMap<String,String> caged_eggs = new HashMap<String,String>();
	public static HashMap<String,Entity> entities = new HashMap<String, Entity>();
	
	/**
     * Gets String For Translation and Jockey if the NBT and parameters are correct
     */
	public static String getJockey(NBTTagCompound nbt, String str, ItemStack stack)
    {
		if (str == null || nbt == null)
		{
			return "null";
		}
		
		//Spawners Start
    	 boolean used = false;
    	 if(Util.isJockey(nbt, false) && !isEgg(stack))
    		 return str + " Jockey";
    	 
         if(isEgg(stack))
        	 return getJockeyName(stack);
         
         return str;
    }
	
	//Prints Colored Chat from player
	public static void printChat(EntityPlayer player, EnumChatFormatting c_player, EnumChatFormatting c_msg, String messege)
	{
		player.addChatComponentMessage(new ChatComponentText(c_player + player.getDisplayName() + " " + c_msg + messege));
	}
	
	//Prints Colored Chat from player needs enum chat formatting in string form. Use method below to acess this supports bold font
	public static void printChat(EntityPlayer player, String c_player, String c_msg, String messege)
	{
		player.addChatComponentMessage(new ChatComponentText(c_player + player.getDisplayName() + " " + c_msg + messege));
	}
	public static String getEnumColor(EnumChatFormatting e)
	{
		return e + "";
	}
	
	//Takes in NBTTagCompound and returns entitie's string
	public static String getJockeyName(ItemStack stack)
	{
		String e = "";
		NBTTagCompound nbt = getNBT(stack);
		
		if(nbt.getString("EntityId").equals(""))
			return e;
		
		e = SilkSpawners.getCurrentTranslatedEntity(nbt.getString("EntityId")); //Translates Current Entity In case there is no jockey
		
		//Egg Method to Get Jockey Name Own NBT Setup Vanilla Spawners are already pre-calculated
		if(SilkSpawners.isEgg(stack))
			if (nbt.getTagList("Mounting", 10) != null && nbt.getTagList("Mounting", 10).tagCount() > 0)
			{
				NBTTagList list = (NBTTagList)nbt.getTagList("Mounting", 10);
     		
				NBTTagCompound checknbt = list.getCompoundTagAt(list.tagCount()-1); //Goes to the last index and then sees what entity it finds
				if (checknbt != null && checknbt.hasKey("id"))
				{
					e = SilkSpawners.getCurrentTranslatedEntity(checknbt.getString("id")) + " Jockey";
				}
			}
		
		return e;
	}
	public static String getJockeyId(ItemStack stack)
	{
		String e = "";
		NBTTagCompound nbt = getNBT(stack);
		
		if(nbt.getString("EntityId").equals(""))
			return e;
		
		e = nbt.getString("EntityId"); //Translates Current Entity In case there is no jockey
		
		//Egg Method to Get Jockey Name Own NBT Setup Vanilla Spawners are already pre-calculated
		if(SilkSpawners.isEgg(stack))
			if (nbt.getTagList("Mounting", 10) != null && nbt.getTagList("Mounting", 10).tagCount() > 0)
			{
				NBTTagList list = (NBTTagList)nbt.getTagList("Mounting", 10);
     		
				NBTTagCompound checknbt = list.getCompoundTagAt(list.tagCount()-1); //Goes to the last index and then sees what entity it finds
				if (checknbt != null && checknbt.hasKey("id"))
				{
					e = checknbt.getString("id");
				}
			}
		
		return e;
	}
	
	
    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     *
     */
    public static Entity spawnCreature(World world, ItemStack stack, double x, double y, double z, boolean spawn)
    {
    	if(stack == null || stack.getTagCompound() == null)
    		return null;
    	NBTTagCompound nbt = (NBTTagCompound) stack.getTagCompound().copy();
    	Entity entity = null;
    	NBTTagList list = new NBTTagList();
    	NBTTagCompound nbtbase = new NBTTagCompound();
    	
    	nbtbase.setString("id", Util.getEntityId(nbt));
    	if(nbt.hasKey("EntityNBT",10))
    		nbtbase.setTag("EntityNBT", nbt.getTag("EntityNBT"));
    	
    	list.appendTag(nbtbase);
    	NBTTagList iterate = SilkSpawners.getTagList(nbt, "Mounting", 10);
    	for(int i=0;i<iterate.tagCount();i++)
    		list.appendTag(iterate.getCompoundTagAt(i));
    	
    	if(spawn) //Reverses nbt outside of spawn method if not actual spawning them from dev testing :(
    		list = Util.reverseNBTList(list);
    	
    	//Removes null entities if spawning them in the world else let's null entities for blank spawners in for later dev features
    	if(spawn)
    	{
    		for(int i=0;i<list.tagCount();i++)
    		{
    			NBTTagCompound tag = list.getCompoundTagAt(i);
    			if(EntityList.createEntityByName(tag.getString("id"), WorldLoadEvent.world2) == null)
    				list.removeTag(i);
    		}
    	}
    	if(config.Debug)
    		System.out.println("List: " + list);
    	Entity entity2 = null;
    	for(int i=0;i<list.tagCount();i++)
    	{
    		if(i == 0)
    		{
    			entity = spawnEntity(world,null,list.getCompoundTagAt(i),x,y,z, spawn,i);
    			entity2 = entity;
    		}
    		else
    			entity2 = spawnEntity(world,entity2,list.getCompoundTagAt(i),x,y,z, spawn,i);
    	}
    	
        return entity;
    }
    /**
     * Spawns In a single Entity and mounts it to the base if it has one
     */
    protected static Entity spawnEntity(World w, Entity base, NBTTagCompound nbt, double x, double y, double z, boolean spawn, int index)
    {
    	//nbt = (NBTTagCompound) nbt.copy();
    	String entityid = nbt.getString("id");
    	
    	Entity entity;
    	if(nbt.hasKey("EntityNBT",10))
    	{
    		NBTTagCompound ent = (NBTTagCompound) nbt.getTag("EntityNBT");
    		ent.setString("id", entityid);
    		entity = EntityList.createEntityFromNBT(ent, w);
    	}
    	else{
    		entity = EntityList.createEntityByName(entityid, w);
    		if(entity instanceof EntityLiving)
    			((EntityLiving)entity).onSpawnWithEgg((IEntityLivingData)null);
    	}
		if(!spawn && base != null)
		{
			base.mountEntity(entity); //fix for mounting spawners
		}
		
		if(spawn && entity != null)
		{
			entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(w.rand.nextFloat() * 360.0F), 0.0F);
			if(entity instanceof EntityLiving)
			{
				((EntityLiving)entity).rotationYawHead = ((EntityLiving)entity).rotationYaw;
				((EntityLiving)entity).renderYawOffset = ((EntityLiving)entity).rotationYaw;
			}
			if(base != null)
				base.mountEntity(entity);
			
			w.spawnEntityInWorld(entity);
			
			if(index < 5 && entity instanceof EntityLiving)
				((EntityLiving)entity).playLivingSound();
		}
		
    	return entity;
    }
	
	
    /**
     * Sets Mounted Mob Spawner Based On Egg Or Non Mounted Spawner
     *
     */
    public static void setMountedSpawner(ItemStack stack, TileEntity tile, World world, int x, int y, int z)
    {
    	if (stack.getTagCompound() == null || stack.getTagCompound().getString("EntityId").equals("") || !(tile instanceof TileEntity) || !Util.isSpawner(world.getBlock(x, y, z), tile))
    		return;
    	tile = setChocoToVanilla(world,x,y,z); //NEI crapping support
    	NBTTagCompound nbt = eggToSpawner(stack,world,x,y,z);
   		tile.readFromNBT(nbt);
   		tile.markDirty();
   		world.markBlockForUpdate(x,y,z);
   		System.out.println("SpawnedNBT:" + nbt);
    }
    /**
     * From Egg to Spawner Auto Formatted
     * @param stack
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static NBTTagCompound eggToSpawner(ItemStack stack, World world, int x, int y, int z)
    {
    	if(stack.getTagCompound() == null || stack.getTagCompound().getString("EntityId").equals(""))
    		return null;
    	NBTTagCompound nbt = (NBTTagCompound) stack.getTagCompound().copy();
    	NBTTagCompound tag = new NBTTagCompound();
    	tag.setString("id", Util.getEntityId(nbt));
    	if(nbt.hasKey("EntityNBT",10))
    		tag.setTag("EntityNBT", nbt.getTag("EntityNBT"));
    	NBTTagList list = new NBTTagList();
    	NBTTagList mounting = SilkSpawners.getTagList(stack, "Mounting", 10);
    	list.appendTag(tag);
    	for(int i=0;i<mounting.tagCount();i++)
    	{
    		list.appendTag(mounting.getCompoundTagAt(i));
    	}
    	nbt = Util.reverseNBT(list);
    	nbt = Util.removeCustomNames(nbt,true);
    	nbt = Util.recalPos(nbt);				//NEEDS TO BE IN ENTITY TO RECAL POS ALSO
   		nbt = Util.getFormatEggToSpawnerNBT(nbt,world, x, y, z); //Gets Entity For now during Mounting Update Two Will have own tile entity to do stuff with it
   		return nbt;
    }
    
    public static TileEntity setChocoToVanilla(World world, int x, int y, int z)
    {
	 	// @Deprecated
    	if (config.cross_nei_compat == true)
		{
			Block block = world.getBlock(x,y,z);
			if (block == MainJava.chocoSpawner)
			{
			 	world.setBlock(x, y, z, Blocks.mob_spawner);
			}
		}
    	return world.getTileEntity(x,y,z);
    }
    
    /**
     * Returns Correct NBT if any
     */
	public static NBTTagCompound getNBT(ItemStack stack)
	{
		if (stack.getTagCompound() != null)
		return (NBTTagCompound)stack.getTagCompound().copy();
		
		return new NBTTagCompound();
	}
	/**
     * Returns Correct NBT if any from NBT With Null Protection
     */
	public static NBTTagCompound getNBT(NBTTagCompound nbt, String str)
	{
		if (nbt == null)
			return new NBTTagCompound();
		for (int i=0;i<12;i++)
		{
			if (SilkSpawners.getTagList(nbt, str, i).tagCount() > 0)
				return new NBTTagCompound();
		}
		if(nbt.getTag(str) != null)
			return (NBTTagCompound)nbt.getTag(str);
		
		return new NBTTagCompound();
	}
	 /**
     * Returns Correct NBTTagList if any from an itemstack
     */
	public static NBTTagList getTagList(ItemStack stack, String str, int integer)
	{
		NBTTagCompound tag = getNBT(stack);
			
		NBTTagList list = tag.getTagList(str, integer);
		if (list != null && list.tagCount() > 0)
		{
			return (NBTTagList)tag.getTagList(str, integer).copy();
		}

		return new NBTTagList();
	}
	 /**
     * Returns Correct NBTTagList if any from nbt if any
     */
	public static NBTTagList getTagList(NBTTagCompound nbt, String str, int integer)
	{
		if (nbt == null)
			return new NBTTagList();
		
		NBTTagList list = nbt.getTagList(str, integer);
		if (list != null && list.tagCount() > 0)
		{
			return (NBTTagList)nbt.getTagList(str, integer).copy();
		}

		return new NBTTagList();
	}
	public static boolean isCustomSpawnerPos(NBTTagCompound nbt)
	{
		if (nbt == null || !(nbt.hasKey("SpawnData") ) && !(nbt.hasKey("SpawnPotentials")) )
		{
			return false;
		}
		if (nbt.getTag("SpawnData") != null)
		{
			NBTTagCompound tag = (NBTTagCompound)nbt.getTag("SpawnData");
			if (tag.hasKey("Pos"))
				return true;
		}
		if (nbt.getTag("SpawnPotentials") != null)
		{
			NBTTagList list = getTagList(nbt,"SpawnPotentials",10);
			if (list.tagCount() > 0)
			{
				for (int i=0;i<list.tagCount();i++)
				{
					if(list.getCompoundTagAt(i).hasKey("Properties",10))
					{
						NBTTagCompound tag = list.getCompoundTagAt(i);
						tag = (NBTTagCompound)tag.getTag("Properties");
						if (tag.hasKey("Pos"))
						{
							return true;
						}
					}
				}
			}
		}
			
		return false;
	}
	/**GetColor Based on Entity Attributes and or classes
	 * returns EnumChatFormatting format to apply to a string to colorize it
	 * Dynamic Colored Text if not vanilla
	 * Custom Vanilla Support
	 * Custom Configed Support
	 */
	public static String getColoredEntityText(Entity e)
	{
		if(e == null || config.colorText == false)
			return EnumChatFormatting.WHITE + "";
		//Sees if it's and ender mob
		if(MainJava.end_ents.contains(EntityList.getEntityString(e)) && EntityList.getEntityString(e) != null)
				return EnumChatFormatting.DARK_PURPLE + "";
		
		//Scans Enum Class for modded enum types
		EnumCreatureType[] k = EnumCreatureType.values();
		boolean ismoded = false;
		for(Object a : k)
		{
			String b = a.toString();
			if(!b.equals("ambient") && !b.equals("creature") && !b.equals("creature") && !b.equals("monster") && !b.equals("waterCreature"))
				ismoded = e.isCreatureType((EnumCreatureType) a, false);
		}
			if(ismoded)
				return EnumChatFormatting.STRIKETHROUGH + "" + EnumChatFormatting.BOLD + "";
		
		boolean ambient = e.isCreatureType(EnumCreatureType.ambient, false);
		boolean creature = e.isCreatureType(EnumCreatureType.creature, false);
		boolean water = e.isCreatureType(EnumCreatureType.waterCreature, false);
		if(!water && e instanceof EntityLivingBase)
			water = ((EntityLivingBase)e).canBreatheUnderwater();
		boolean fire = e.isImmuneToFire();
		boolean monster = e.isCreatureType(EnumCreatureType.monster, false);

		//if(config.Debug)
		//System.out.println("Water:" + water + " Fire:" + fire + " Monster:" + monster + " Name:" + EntityList.getEntityString(e));
		//Future Code
		if(e instanceof EntityLivingBase)
		{
			EntityLivingBase en = (EntityLivingBase)e;
			EnumCreatureAttribute enums = en.getCreatureAttribute();
			boolean undead = enums == EnumCreatureAttribute.UNDEAD;
			boolean arthropod = enums == EnumCreatureAttribute.ARTHROPOD;
			boolean undefined = enums == EnumCreatureAttribute.UNDEFINED;
		}
		//Checks Enum Attributes
		if(ambient && !fire && !(e instanceof EntityTameable) && !(e instanceof EntityFlying) && !(e instanceof IRangedAttackMob) && !(e instanceof IBossDisplayData) && !(e instanceof IEntityMultiPart))
			return EnumChatFormatting.DARK_GRAY + "";
		if(creature && !fire && !(e instanceof EntityTameable) && !(e instanceof EntityFlying) && !(e instanceof IRangedAttackMob) && !(e instanceof IBossDisplayData) && !(e instanceof IEntityMultiPart))
			return EnumChatFormatting.LIGHT_PURPLE + "";
		if(water && !fire  && !(e instanceof EntityTameable) && !(e instanceof EntityFlying) && !(e instanceof IRangedAttackMob) && !(e instanceof IBossDisplayData) && !(e instanceof IEntityMultiPart))
			return EnumChatFormatting.AQUA + "";
		if(fire && !(e instanceof EntityTameable) && !(e instanceof EntityFlying) && !(e instanceof IRangedAttackMob) && !(e instanceof IBossDisplayData) && !(e instanceof IEntityMultiPart))
			return EnumChatFormatting.GOLD + "";
		if(monster && !fire  && !(e instanceof EntityTameable) && !(e instanceof EntityFlying) && !(e instanceof IRangedAttackMob) && !(e instanceof IBossDisplayData) && !(e instanceof IEntityMultiPart))
			return EnumChatFormatting.RED + "";
		
		
		//Checks Classes if hasn't Returned
		if(e instanceof EntityTameable)
			return EnumChatFormatting.DARK_BLUE + "";
		
		if(e instanceof IBossDisplayData || e instanceof IEntityMultiPart)
			return EnumChatFormatting.BOLD + "" + EnumChatFormatting.DARK_PURPLE;
		
		if(e instanceof EntityFlying)
			return EnumChatFormatting.BOLD + "" + EnumChatFormatting.YELLOW;
		
		if(e instanceof IRangedAttackMob && e instanceof EntityMob)
			return EnumChatFormatting.DARK_RED + "";	
		
		if(e instanceof EntityAmbientCreature && !(e instanceof EntityAnimal))
			return EnumChatFormatting.DARK_GRAY + "";
		
		if(e instanceof EntityAnimal && !(e instanceof EntityCreature) || e instanceof IAnimals && !(e instanceof EntityCreature))
			return EnumChatFormatting.LIGHT_PURPLE + "";
		
		if (e instanceof EntityAgeable)
			return EnumChatFormatting.LIGHT_PURPLE + "";
		
		if(e instanceof EntityCreature)
			return EnumChatFormatting.GREEN + "";
		
		if(fire)
			return EnumChatFormatting.GOLD + "";
		
		if(e instanceof EntityWaterMob)
			return EnumChatFormatting.AQUA + "";
		
		if(e instanceof EntityMob)
			return EnumChatFormatting.RED + "";
		
		return EnumChatFormatting.WHITE + "";
	}
	
	 /**
     * Gets Anvil Cost based on two itemstacks
     */
	public static int anvilGetCost(ItemStack lstack, ItemStack rstack, boolean combine)
	{
		int resault = 0; //only uses whatever is to the left so only calculates the amount of eggs there
		if (combine == true)
		{
			resault += SilkSpawners.getEntCount(lstack);
			resault += SilkSpawners.getEntCount(rstack);
		}
		else{
			if(getNBT(lstack).hasKey("EntityId"))
				resault += 1;
			if(getNBT(rstack).hasKey("EntityId"))
				resault += 1;
			if(rstack.getItem() == Items.spawn_egg)
				resault += 1;
			resault += getTagList(lstack,"Mounting",10).tagCount();
			resault += getTagList(rstack,"Mounting",10).tagCount();
		}
		return resault;
	}
	 /**
     * Get's Egg Combinations and Return NBT
     */
	public static NBTTagCompound combineEggRecipe(ItemStack lstack, ItemStack rstack, boolean combine_all)
	{
		if(lstack.getTagCompound() == null && rstack.getTagCompound() == null || getNBT(lstack).getString("EntityId") == "" && getNBT(rstack).getString("EntityId") == "")
		return null;
		if(lstack.getTagCompound().getString("EntityId").equals("Blank") || rstack.getTagCompound().getString("EntityId").equals("Blank"))
			return null;
		
		//Initializes Variables
		NBTTagCompound lnbt = getNBT(lstack);
		lnbt.removeTag("CreativeTab");
		NBTTagCompound rnbt = getNBT(rstack);
		rnbt.removeTag("CreativeTab");
		NBTTagList Llist = getTagList(lstack,"Mounting",10);
		NBTTagList Rlist = getTagList(rstack,"Mounting",10);
		NBTTagList to_append = new NBTTagList();
		boolean used = false;
		if (combine_all == true)
		{
			for (int j=0;j<lstack.stackSize-1;j++)
			{
				if (lnbt.getString("EntityId") != "")
				{
					NBTTagCompound append = new NBTTagCompound();
					append.setString("id", lnbt.getString("EntityId"));
					if (lnbt.getTag("EntityNBT") != null)
					append.setTag("EntityNBT", lnbt.getTag("EntityNBT"));
					to_append.appendTag(append);
				}
				for (int i=0;i<Llist.tagCount();i++)
				{
					NBTTagCompound tag = Llist.getCompoundTagAt(i);
					to_append.appendTag(tag);
				}
			}
			for(int k=0;k<to_append.tagCount();k++)
			{
				Llist.appendTag(to_append.getCompoundTagAt(k));
			}
		}
		if (lnbt.getString("EntityId") == "" && rnbt.getString("EntityId") != "")
		{
			lnbt.setTag("EntityId", rnbt.getTag("EntityId"));
			if (rnbt.getTag("EntityNBT") != null)
			lnbt.setTag("EntityNBT", rnbt.getTag("EntityNBT"));
			used = true;
		}
		int loopCount = 1;
		if (combine_all == true)
			loopCount = rstack.stackSize;
		for (int j=0;j<loopCount;j++)
		{
			if (used == false && rnbt.getTag("EntityId") != null)
			{
				NBTTagCompound append = new NBTTagCompound();
				append.setTag("id", rnbt.getTag("EntityId"));
				if (rnbt.getTag("EntityNBT") != null)
					append.setTag("EntityNBT", rnbt.getTag("EntityNBT"));
				Llist.appendTag(append);
			}
			for (int i=0;i<Rlist.tagCount();i++)
			{
				NBTTagCompound tag = Rlist.getCompoundTagAt(i);
				if (tag != null)
					Llist.appendTag(tag);
			}
			lnbt.setTag("Mounting", Llist);
		}
		return lnbt;
	}
	public static NBTTagCompound combineVanilla(ItemStack lstack, ItemStack rstack, boolean combine)
	{
		NBTTagCompound ltag = getNBT(lstack);
		ltag.removeTag("CreativeTab");
		NBTTagCompound rtag = getNBT(rstack);
		rtag.removeTag("CreativeTab");
		NBTTagCompound resault = new NBTTagCompound();
		NBTTagList Llist = getTagList(lstack,"Mounting",10);
		String ent = EntityList.getStringFromID(rstack.getItemDamage());
		if(ent == null)
		{
			return null;
		}
		
		int loopCount = 1;
		if (combine == true)
			loopCount = rstack.stackSize;
		
		for (int i=0;i<loopCount;i++)
		{
			if (rstack.getItem() == Items.spawn_egg)
			{
				NBTTagCompound append = new NBTTagCompound();
				append.setString("id", ent);
				if (ltag.getString("EntityId") == "")
				{
					ltag.setString("EntityId", ent);
				}
				else{
					Llist.appendTag(append);
					ltag.setTag("Mounting", Llist);
				}
			}
			else{
				//Future defined eggs goes here
			}
		}
		return ltag;
	}
	public static boolean isEgg(ItemStack stack)
	{
		if(stack == null || stack.getItem() == null)
			return false;
		//update to all egg eventually get all eggs combined....
		Iterator eggit = MainJava.Egg_List.iterator();
		while (eggit.hasNext())
		{
			Item item = (Item)eggit.next();
			if (item == stack.getItem())
				return true;
		}
		return false;
	}
	public static double recalDouble(double pos, int oldx, int newx)
	{
		String str = String.valueOf(pos);
		String[] parts = str.split("\\.");
		String strEdit = parts[0];
		String strdeci = parts[1];
		
		double oldfront = Double.parseDouble(strEdit);
		double deci = Double.parseDouble(strdeci);
		double offset = (oldx - pos) * -1;

		return (double)(newx + offset);
	}
	
	
	
	public static String toFileCharacters(String s)
	{ 
		String invalid = ".,[]{}()!;\"'*?<>|/\\||?*^!@#$%&<>:=";
		String resault = "";
		String sub = "";
		for (int i=0;i<s.length();i++)
		{
			sub = s.substring(i, i+1);
			if (!invalid.contains(sub) && resault.length() < 240)
				resault = resault + sub;
		}
		if (resault.toUpperCase().equals("CON") || resault.toUpperCase().equals("PRN") || resault.toUpperCase().equals("AUX") || resault.toUpperCase().equals("NUL"))
		{
			return resault + " null";
		}
		for (int j=0;j<10;j++)
		{
			String com = "COM" + String.valueOf(j);
			String lpt = "LPT" + String.valueOf(j);
			if (resault.toUpperCase().equals(com) || resault.toUpperCase().equals(lpt))
			{
				return resault + " null";
			}
		}
		if(resault.equals(""))
			resault = "failedModName";
		
		return resault;
	}
	
	//Takes valid raw entity list and returns a HashMap<Key=srcname, Value=translated_name>
	//This is called during init of forge egg lists to prevent crashing on loading the world It also fixes organizational bugs
	 public static HashMap<String,String> TranslateEntityList(List<String> list) 
	 {
		 	//Iterates Through entire array list
		 	HashMap<String,String> hash = new HashMap<String,String>();
		 	for(String s : list)
		 	{
		 		String EntityName = SilkSpawners.TranslateEntity(s);
		 		if(!forge_eggs.containsKey(s))
		 			hash.put(s, EntityName);
		 		else
		 			hash.put(s, forge_eggs.get(s));
		 	}
		 
			return hash;
	 }
	 
	 //Translates non living and living entities along with a trying method to always get the proper translation...
	 public static String TranslateEntity(String s)
	 {
		 if (s == null)
			 return "";
		 
		 String EntityName = StatCollector.translateToLocal("entity." + s + ".name");
			
			//Corrects if there is no local translation back to default namming...
         if (EntityName.equals("entity." + s + ".name"))
         {
         	EntityName = s;
         }
         
         //Experimental Code_______________________
    	 if(s.equals(EntityName))
    	 { 
    		 if (EntityList.createEntityByName(EntityName, WorldLoadEvent.world2) != null)
    		 {
    			 Entity entity = EntityList.createEntityByName(EntityName, WorldLoadEvent.world2);
    			 
    			 if(!entity.getCommandSenderName().equals("generic"))
    			 {
    				 if(!entity.getCommandSenderName().equals(EntityName) && !entity.getCommandSenderName().equals("entity." + EntityName + ".name"))
    					EntityName = entity.getCommandSenderName();
    			 }
    		 }
    	 }
    	 //End Experimental Code___________________
    	 return EntityName;
	 }
	 
	 
	 //Scans hashmaps for a specfic translation or if none of that happens it translates it dynamially
	 public static String getCurrentTranslatedEntity(String s)
	 {
		 boolean key = false;
		 String e = "";
		 
		 //Bug fix for HEE Spammed Text
		 if(SilkSpawners.forge_eggs.isEmpty() || SilkSpawners.mob_spawners.isEmpty() || SilkSpawners.caged_eggs.isEmpty())
			 SilkSpawners.genHashMaps();
		 
     	 if(SilkSpawners.forge_eggs.containsKey(s) && key == false)
     	 {
     		 e = SilkSpawners.forge_eggs.get(s);
     		 key = true;
     	 }
     	 if(SilkSpawners.caged_eggs.containsKey(s) && key == false)
     	 {
     		 e = SilkSpawners.caged_eggs.get(s);
     		 key = true;
     	 }
     	 if(SilkSpawners.mob_spawners.containsKey(s) && key == false)
     	 {
     		 e = SilkSpawners.mob_spawners.get(s);
     		 key = true;
     	 }
     	 if(!key)
     	 {
     		 e = SilkSpawners.TranslateEntity(s);
     		 if(EntityList.createEntityByName(s, WorldLoadEvent.world2) != null)
     		 {
     			 for(int i=0;i<10;i++)
     				 System.out.println("Error 404 Entity Not Found: " + s + " TranslatedForm: " + e);
     		 }
     	 }
     	 return e;
	 }

	//Debugging Method For Printing a Map/HashMap...
	public static void printMap(Map<String, String> map) 
	{
		  Iterator iterator = map.entrySet().iterator();
		  List<String> key = new ArrayList<String>();
		  List<String> value = new ArrayList<String>();
		  
	        while (iterator.hasNext())
	        {
	        	 Map.Entry pair2 = (Map.Entry)iterator.next();
	        	 key.add(pair2.getKey().toString());
	        	 value.add(pair2.getValue().toString());
	        }
	      printArray(key);
		  printArray(value);
	}
	public static void printArray(List<String> list)
	{
		System.out.print("[");
		int count = 0;
		for(String s : list)
		{
			System.out.print(s + " " + count + ",");
			count++;
		}
		System.out.print("]\n");
	}
	
	//Regenerates Custom Files Forge Files Will Not Regen Till Upon Relaunch
	public static void regenFiles() throws IOException 
	{
		//Folders
		if(!config.SilkSpawners.exists())
			config.SilkSpawners.mkdir();
		
		if(!config.DungeonTweeks.exists())
		config.DungeonTweeks.mkdir();
		
		if(!config.BlockProperties.exists())
			config.BlockProperties.mkdir();
		     
		  //Generates Define Spawners From File
          FileConstructer.loadconfig(null);
          
          //Block Properties
          FileConstructer.loadhardness(null);
          FileConstructer.loadResistence(null);
          FileConstructer.loadLight(null);
          FileConstructer.loadharvest(null);
          FileConstructer.loadtools(null);
          FileConstructer.loadQuantityDropped(null);
          FileConstructer.loadDisableDrops(null);
          FileConstructer.loadEnchant(null);
          FileConstructer.loadEnchantOverride(null);
          FileConstructer.loadIgnoringTools(null);
          FileConstructer.loadAutoSpawnerBlacklist(null);
          FileConstructer.Blacklist(null);
          FileConstructer.Whitelist(null);
          FileConstructer.ModWhiteList(null);
          FileConstructer.ModBlackList(null);
         
	}
	//Generates Entity HashMaps only once
	public static void genHashMaps() 
	{
		if(forge_eggs.isEmpty())
		{
			   Map<String, String> treeMap = new TreeMap<String, String>( SilkSpawners.TranslateEntityList(MainJava.List) );
			   HashMap<String, String> Hasher = new HashMap<String, String>(treeMap);
			 
			 Map<String, String> map = EntityLookUp.sortByValues(Hasher);
			 forge_eggs = (HashMap<String, String>) map;
		}
		if(mob_spawners.isEmpty())
		{
			   Map<String, String> treeMap = new TreeMap<String, String>( SilkSpawners.TranslateEntityList(MainJava.BlockList) );
			   HashMap<String, String> Hasher = new HashMap<String, String>(treeMap);
			 
			 Map<String, String> map = EntityLookUp.sortByValues(Hasher);
			 mob_spawners = (HashMap<String, String>) map;
		}
		
		if(caged_eggs.isEmpty())
		{
			   Map<String, String> treeMap = new TreeMap<String, String>( SilkSpawners.TranslateEntityList(MainJava.CagedList) );
			   HashMap<String, String> Hasher = new HashMap<String, String>(treeMap);
			 
			 Map<String, String> map = EntityLookUp.sortByValues(Hasher);
			 caged_eggs = (HashMap<String, String>) map;
		}
		
		
	}
	/**
	 * Gets String Id of the Item/BLock
	 * @param stack
	 * @return
	 */
	public static String getStringId(ItemStack stack)
	{
		try{	
			return GameData.itemRegistry.getNameForObject(stack.getItem());
		} 
		catch (NullPointerException e){
			return "null:null";
		}
	}
	/**
	 *Finds Entity Count For Eggs/Spawners
	 * @param nbt
	 * @return
	 */
	public static int getEntCount(NBTTagCompound nbt) 
	{
		if(nbt.hasKey("SilkEnt"))
		{
			return nbt.getInteger("SilkEnt");
		}
		int count = 0;
		if(nbt.hasKey("EntityId"))
			count++;
		count += SilkSpawners.getTagList(nbt, "Mounting", 10).tagCount();
		
		nbt.setInteger("SilkEnt", count);
		return count;
	}
	public static int getEntCount(ItemStack stack)
	{
		if(stack.getItem() == Items.spawn_egg)
			return stack.stackSize;
		else
			return getEntCount(getNBT(stack)) * stack.stackSize;
	}

	public static void addEggToolTip(ItemStack stack, List list, boolean b) 
	{
		 if (stack.getTagCompound() == null)
		 {
			 String tooltip = EnumChatFormatting.DARK_AQUA  + "Teir " + 1;
			 if(!list.contains(tooltip))
			 list.add(tooltip);
			 return;
		 }
			 
		 if (stack.getTagCompound().hasKey("ForgeLvl") && !stack.getTagCompound().hasKey("CreativeTab"))
		 {
			 int lvl = stack.getTagCompound().getInteger("ForgeLvl");
			 String strlvl = String.valueOf(lvl);
			 String tooltip = EnumChatFormatting.DARK_AQUA  + "Teir " + strlvl;
			 if(!(list.contains(tooltip)))
			 {
				 list.add(tooltip);
			 }
		 }
		 else{
			 String tooltip = EnumChatFormatting.DARK_AQUA  + "Teir " + 1;
			 if(!(list.contains(tooltip)) && !stack.getTagCompound().hasKey("CreativeTab"))
			 {
			 	list.add(tooltip);
			 }
		 }
		 
		 NBTTagCompound nbt = SilkSpawners.getNBT(stack);
		 if(SilkSpawners.getEntCount(nbt) > 1)
		 {
		 
		 String tooltip = EnumChatFormatting.DARK_PURPLE + "" + "Entity Count: " + String.valueOf(SilkSpawners.getEntCount(nbt));
		 if(!list.contains(tooltip))
			 list.add(tooltip);
		 }
	 }
	public static String getJockeyModName(ItemStack stack)
	{
		 Entity entity = EntityList.createEntityByName(SilkSpawners.getJockeyId(stack), WorldLoadEvent.world2);
    	 String modname = "";
    	 if(entity != null && EntityList.getEntityString(entity) != null)
    	 {
    		 LineObj line = new LineObj(EntityLookUp.getEntityMod(entity));
    		 modname = line.getmodid();
    	 }
    	 if(modname.equals("") || modname.equals("Silk Spawners"))
    	 {
    		 Item item = Item.getItemFromBlock(MainJava.chocoSpawner);
    		 if(stack.getItem() == item || stack.getItem() == MainJava.minecartSpawner)
    			 return "Minecraft";
    		 
    		 modname = modname(stack);
    	 }
    	 return modname;
	}
	public static String modname (ItemStack stack)
	{
	try{
		ModContainer mod = GameData.findModOwner(GameData.itemRegistry.getNameForObject(stack.getItem()));
		String modname = mod == null ? "Minecraft" : mod.getName();		
		return modname;
	} 
	catch (NullPointerException e){
		return "Minecraft";
	}
	}
	/**
	 * Instantiates Cached StringId to Entities For Colored Mobs
	 */
	public static void cacheEntities()
	{
		if(!entities.isEmpty())
			return;
		for(String s : MainJava.Entities)
		{
			entities.put(s, EntityList.createEntityByName(s, WorldLoadEvent.world2));
		}
	}
    public static void replaceVanillaEntity(Class<? extends Entity> newEntityClass, int entityId)
    {
      String name = EntityList.getStringFromID(entityId);
      if ((name == null) || (EntityList.stringToClassMapping.remove(name) == null) || (EntityList.IDtoClassMapping.remove(Integer.valueOf(entityId)) == null)) {
        throw new IllegalStateException("Error replacing entity with ID " + entityId + ", entity entry missing!");
      }
      EntityList.addMapping(newEntityClass, name, entityId);
    }
}
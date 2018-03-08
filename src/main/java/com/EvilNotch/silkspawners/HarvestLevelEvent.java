package com.EvilNotch.silkspawners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.EvilNotch.silkspawners.util.Util;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class HarvestLevelEvent {
	
	public static boolean check = false;
	public static Map<String, String> hashCheck = new HashMap<String, String>();
	public static Map<String, String> hashCheck2 = new HashMap<String, String>();
	public static Map<String, String> HashRemove = new HashMap<String, String>();
	private ArrayList<ItemStack> clear = new ArrayList<ItemStack>();
	public static ArrayList<ItemStack> dropping = new ArrayList<ItemStack>();

	public void BlockEvent(BreakEvent e)
	{ 
		if(e.getPlayer() == null || e.getPlayer().getCurrentEquippedItem() == null)
			return;
		if(!Util.isHarvestLevel(e.block, e.getPlayer().getCurrentEquippedItem()) || Util.isSpawner(e.block, EventArrayBlockDrop.eventiles))
			return;
		e.world.setBlockToAir(e.x, e.y, e.z);
	}
	
	//Disable Drops Handler Completely Disables Any Drops
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBlockBreak(BreakEvent event)
	{
		if (event.world.isRemote)
		{
			return;
		}

		Iterator it = MainJava.DisableDropsList.iterator();
		while(it.hasNext())
		{
			String strit = it.next().toString();
			String replaced = strit.replaceFirst(":", "\u00A9");
			String[] parts = replaced.split("\u00A9");
			String modid = parts[0];
			String name = parts[1];
			
			if (event.block == GameRegistry.findBlock(modid, name))
			{
				event.world.setBlockToAir(event.x, event.y, event.z);
				event.setCanceled(true);
			}
		    
		}
			
	}
	
	
	//Changes The Quantity Dropped Of The Block
	//__________________________________________\\
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void yourPlayerHarvestEvent(HarvestDropsEvent event)
	{ 
	if(!event.world.isRemote)
	{

	if (event.harvester != null)
	{

		Iterator it = MainJava.QuantityDroppedList.iterator();
		while(it.hasNext())
		{
			String strit = it.next().toString();
			String[] parts = strit.split("=");
	        String strid = parts[0];
	        String strquantity = parts[1];
	        int quantity = Integer.parseInt(strquantity);
	        String strjk = strid.replaceFirst(":", "\u00A9");
			String[] partblock = strjk.split("\u00A9");
			String modid = partblock[0];
			String name = partblock[1];
			
			if (event.block == GameRegistry.findBlock(modid, name))
			{
				ItemStack block = new ItemStack(event.block);
				
				//Full Support For Any BlockMob Spawner Quantity Dropped (Doesn't Disable XP If You Want To Disable Xp Define Spawner as A Spawner In DefineSpawners.txt)
				if (EventArrayBlockDrop.eventiles instanceof TileEntityMobSpawner)
				{
					NBTTagCompound tilenbt = new NBTTagCompound();
					EventArrayBlockDrop.eventiles.writeToNBT(tilenbt);
					ItemStack Else = new ItemStack(MainJava.CustomMobSpawner);
					tilenbt.setInteger("x", 0);
					tilenbt.setInteger("y", 0);
					tilenbt.setInteger("z", 0);
					tilenbt.removeTag("MaxSpawnDelay");
					tilenbt.removeTag("Delay");
					tilenbt.removeTag("SpawnRange");
					tilenbt.removeTag("MinSpawnDelay");
					
					if (event.block instanceof BlockMobSpawner || config.WriteModdedSpawnersToNBT == true)
		        	{
						String entity = tilenbt.getString("EntityId");
						Entity ent = EntityList.createEntityByName(entity, event.world);
						if (ent == null)
						{
							ent = EntityList.createEntityByName("Creeper", event.world);
						}
						String en = EntityLookUp.getEntityMod(ent);
						String[] parts4 = en.split("\u00A9");
						String modname = parts4[0]; 
						tilenbt.setString("modid", modname);
						tilenbt.setString("EntityInfo", entity);
						NBTTagCompound local = new NBTTagCompound();
				    	NBTTagCompound modded = new NBTTagCompound();
				    	String EntityLocalizedName = "" + StatCollector.translateToLocal("entity." + entity + ".name");
				    	local.setString("Name", EntityLocalizedName + " " + Blocks.mob_spawner.getLocalizedName());
				    	modded.setString("Name", EntityLocalizedName + " " + event.block.getLocalizedName());
				    	Block eblock = event.block;
				    	
				    	if (!Loader.isModLoaded("NotEnoughItems"))
				    	{
				    		if (event.block == Blocks.mob_spawner)
					    	{
					    		tilenbt.setTag("display", local);
					    	}
					    	
					    	
					    	if (eblock != Blocks.mob_spawner && eblock != MainJava.CustomMobSpawner && eblock != MainJava.NetherSpawner && eblock != MainJava.EndSpawner && eblock != MainJava.RustedSpawner && eblock != MainJava.IceSpawner && eblock != MainJava.CaveSpawner && eblock != MainJava.SnowSpawner && eblock != MainJava.ForestSpawner && eblock != MainJava.End_Supreme_Spawner)
					    	{
					    		if (config.setModdedSpawnersName == true)
					    		{
					    			//Spawner.setStackDisplayName(EntityLocalizedName + " " + eventDivineRPG1.block.getLocalizedName() );
					    			tilenbt.setTag("display", modded);
					    		}
					    		
					    	}
					    	block.setTagCompound(tilenbt);
				    	}
				    	if(Loader.isModLoaded("NotEnoughItems"))
				    	{
				    		if (eblock != Blocks.mob_spawner && eblock != MainJava.CustomMobSpawner && eblock != MainJava.NetherSpawner && eblock != MainJava.EndSpawner && eblock != MainJava.RustedSpawner && eblock != MainJava.IceSpawner && eblock != MainJava.CaveSpawner && eblock != MainJava.SnowSpawner && eblock != MainJava.ForestSpawner && eblock != MainJava.End_Supreme_Spawner)
		        	    	{
		        	    		if (config.setModdedSpawnersName == true)
		        	    		{
		        	    			tilenbt.setTag("display", modded);
		        	    		}
		        	    		
		        	    	}
				       	    Class a = (Class) EntityList.stringToClassMapping.get(entity);
				    	    
				    	    if (EntityLiving.class.isAssignableFrom(a))
				    	    {
				    	    Entity	z = EntityList.createEntityByName(entity, event.world);
				    	    
				    	    int globalID = EntityList.getEntityID(z);
				    	    if (globalID > 0)
				    	    {
			    	    		ItemStack global = new ItemStack(Blocks.mob_spawner,1,globalID);
			    	    		block = global;
				    	    	if (config.cross_nei_compat == true)
			    		    	{
				    	    		block.setTagCompound(tilenbt);
			    		    	}
				    	    }
				    	    else{
				    	    	if (event.block == Blocks.mob_spawner)
						    	{
				    	    		block =  Else;
				    	    		block.setTagCompound(tilenbt);
						    	}
				    	     }
				    	   }
				    	}
				    	
		        	}
					
						
		       }

				if (event.block == Blocks.redstone_ore || event.block == Blocks.lit_redstone_ore)
				{
					ItemStack redstone = new ItemStack(Blocks.redstone_ore);
					block = redstone;
				}
				String strblock = block.toString();

				
				//Adds All Drops if it's not The Block Itself To The Array List
				Iterator ijk = event.drops.iterator();
				while (ijk.hasNext())
				{
					Object index = ijk.next();
				    String str = index.toString();
				    ItemStack stackindex = (ItemStack) index;

					if (!str.equals(strblock))
					{
						dropping.add(stackindex);
					}
				}
				
				//Clears This For Further Compatibility Configurations
				event.drops.clear();
				
				//Adds Rest of drops that were not the block if any....
				Iterator it1 = dropping.iterator();
				while (it1.hasNext())
				{
					ItemStack stack = (ItemStack) it1.next();
					event.drops.add(stack);
				}
				
				//Adds Quantity Dropped If any
				for (int i=0;i < quantity;i++)
				{
					event.drops.add(block);
				}

			}
			
		}
		//Clears list for next event
		dropping.clear();
	}
	}
	}
	
	

}

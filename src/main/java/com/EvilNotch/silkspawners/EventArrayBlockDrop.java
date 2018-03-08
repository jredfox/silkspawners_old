package com.EvilNotch.silkspawners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import com.EvilNotch.silkspawners.util.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

public class EventArrayBlockDrop {
	
	
	protected static TileEntity eventiles;
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void BlockEvent(BreakEvent event)
	{ 
		eventiles = event.world.getTileEntity(event.x, event.y, event.z); //Inits Tile entity and bug to prevent very weird bugs
		Block eblock = event.block;
		
		if(event.world.isRemote || event.getPlayer() == null || event.getPlayer().getCurrentEquippedItem() == null || !Util.isSurvival(event.getPlayer()) )
			return;
		
		ItemStack stack = event.getPlayer().getCurrentEquippedItem();
		
		//Returns if it's not a spawner or if it's blacklisted
		if(!Util.isSpawner(eblock,eventiles))
		{
			return;
		}
		//Returns if it's not the right virtual harvest level
		if(!Util.isHarvestLevel(eblock, stack))
			return;
		
		//Returns if it's not the right applicable enchantment
		if(!Util.isEnchantSilky(eblock, stack))
			return;
		
		if(config.Debug)
			System.out.println("Made It");
		
		if(eventiles instanceof TileEntity) //Drops blocks and modifys NBT
			Util.DropSpawner(eventiles,eblock, event.world,event.x, event.y,event.z,event.blockMetadata);
		else //If isn't a tile enity to drop block in world
			Util.DropBlock(eblock,event.world,event.x,event.y,event.z,event.blockMetadata);
		
		event.setCanceled(true); //Cancels Event to prevent experience With AOA/DivineRPG/EssenceOfTheGods aka Jorney Into The Light
	}
	
			//end of array event<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>
	
			//reads nbt data upon block placement
			@SubscribeEvent
			public void BlockEvent(PlaceEvent event2)
			{

			if (!event2.world.isRemote) 
			{

			if (event2.itemInHand == null)
			{
				return;
			}
			//Sub section to return if the player has an item in hand rather then a block....
			LineObj tst = new LineObj(SilkSpawners.getStringId(event2.itemInHand));
			System.out.println("Line: " + tst);
			Block block = GameRegistry.findBlock(tst.getmodid(), tst.getName());
			if(block == null)
				return;
			
			Item item = Item.getItemFromBlock(Blocks.mob_spawner);
			if (event2.itemInHand.getTagCompound() == null && event2.itemInHand.getItem() != item || event2.itemInHand.getItemDamage() == 0 && event2.itemInHand.getItem() == item && event2.itemInHand.getTagCompound() == null || config.cross_nei_compat == false && event2.itemInHand.getItem() == item && event2.itemInHand.getTagCompound() == null) 
			{
				return;
			}
			TileEntity eventile = event2.world.getTileEntity(event2.x, event2.y, event2.z);

			//String un = block.blockRegistry.getNameForObject(block);
			
			if (eventile instanceof TileEntityMobSpawner && event2.itemInHand.getTagCompound() != null || eventile instanceof TileEntity && SilkSpawners.getNBT(event2.itemInHand).hasKey("SilkTileEntity"))
			{
				
			ItemStack varhand = event2.itemInHand;
			NBTTagCompound varTest = new NBTTagCompound();
			
			if (varhand.getTagCompound() != null)
			{
				varTest = (NBTTagCompound) varhand.getTagCompound().copy();
			}
			if(Loader.isModLoaded("EnderIO") && event2.block == GameRegistry.findBlock("EnderIO", "blockPoweredSpawner"))
			{
				varTest.removeTag("face");
			}
				
		    int varx = event2.x;
		    int vary = event2.y;
		    int varz = event2.z;
		    
		    int oldx = varTest.getInteger("x");
		    int oldy = varTest.getInteger("y");
		    int oldz = varTest.getInteger("z");
		    varTest.setInteger("x",varx);
		    varTest.setInteger("y", vary);
		    varTest.setInteger("z", varz);
		    
		    //Custom Pos Repositioning
		    if(varTest.getTag("SpawnData") != null)
		    {
		    	NBTTagCompound tag = SilkSpawners.getNBT(varTest,"SpawnData");
		    	if (tag.hasKey("Pos"))
		    	{
		    		NBTTagList list = SilkSpawners.getTagList(tag, "Pos", 6);
		    		if (list.tagCount() > 0)
		    		{
		    			NBTTagList modlist = tag.getTagList("Pos", 6);
		    			for (int i=0;i<3;i++)
		    			{
		    				//System.out.println("made it");
		    				 double pos = modlist.func_150309_d(i);
		    				 double print = pos;
		    				 if (i == 0)
		    				 {
		    					 double new_pos = SilkSpawners.recalDouble(pos, oldx, varx);
		    					 NBTTagDouble k = new NBTTagDouble(new_pos); 
		    					 modlist.func_150304_a(i, k);
		    					 //System.out.println("D:" + SilkSpawners.recalDouble(pos, oldx, varx) + " EX:" + varx + " Old PosX:" + print);
		    				 }
		    				 if (i == 1)
		    				 {
		    					 double new_pos = SilkSpawners.recalDouble(pos, oldy, vary);
		    					 NBTTagDouble k = new NBTTagDouble(new_pos); 
		    					 modlist.func_150304_a(i, k);
		    					// System.out.println("D:" + SilkSpawners.recalDouble(pos, oldy, vary) + " EY:" + vary + " Old PosY:" + print);
		    				 }
		    				 if (i == 2)
		    				 {
		    					 double new_pos = SilkSpawners.recalDouble(pos, oldz, varz);
		    					 NBTTagDouble k = new NBTTagDouble(new_pos); 
		    					 modlist.func_150304_a(i, k);
		    					// System.out.println("Silky D: ======>" + SilkSpawners.recalDouble(pos, oldz, varz) + " EZ:" + varz + " Old PosZ:" + print);
		    				 }
		    				 
		    			}
		    		}
		    	}
		    }

		    //Custom Pos Repositioning 
		    NBTTagList fakelist = SilkSpawners.getTagList(varTest, "SpawnPotentials", 10);
		    if (fakelist.tagCount() > 0)
		    {
		    	NBTTagList listing = varTest.getTagList("SpawnPotentials", 10);
		    	for (int j=0;j<listing.tagCount();j++)
		    	{
		    		NBTTagCompound indextag = listing.getCompoundTagAt(j);
		    		if (indextag.getTag("Properties") != null)
		    		{
		    			NBTTagCompound tag = (NBTTagCompound)indextag.getTag("Properties");
		    		
			    	if (tag.hasKey("Pos"))
			    	{
			    		NBTTagList list = SilkSpawners.getTagList(tag, "Pos", 6);
			    		if (list.tagCount() > 0)
			    		{
			    			NBTTagList modlist = tag.getTagList("Pos", 6);
			    			for (int i=0;i<3;i++)
			    			{
			    				//System.out.println("made it");
			    				 double pos = modlist.func_150309_d(i);
			    				 double print = pos;
			    				 if (i == 0)
			    				 {
			    					 double new_pos = SilkSpawners.recalDouble(pos, oldx, varx);
			    					 NBTTagDouble k = new NBTTagDouble(new_pos); 
			    					 modlist.func_150304_a(i, k);
			    					// System.out.println("D:" + SilkSpawners.recalDouble(pos, oldx, varx) + " EX:" + varx + " Old PosX:" + print);
			    				 }
			    				 if (i == 1)
			    				 {
			    					 double new_pos = SilkSpawners.recalDouble(pos, oldy, vary);
			    					 NBTTagDouble k = new NBTTagDouble(new_pos); 
			    					 modlist.func_150304_a(i, k);
			    					 //System.out.println("D:" + SilkSpawners.recalDouble(pos, oldy, vary) + " EY:" + vary + " Old PosY:" + print);
			    				 }
			    				   if (i == 2)
			    				   {
			    					 double new_pos = SilkSpawners.recalDouble(pos, oldz, varz);
			    					 NBTTagDouble k = new NBTTagDouble(new_pos); 
			    					 modlist.func_150304_a(i, k);
			    					// System.out.println("D:" + SilkSpawners.recalDouble(pos, oldz, varz) + " EZ:" + varz + " Old PosZ:" + print);
			    				   }
			    				}
			    			}
			    		}
		    		}
		    	}
		    }
		    
		    TileEntity tile = event2.world.getTileEntity(varx,vary,varz);
		    System.out.println("NBT: " + varTest);
		    tile.readFromNBT(varTest);
		    tile.markDirty();
		    event2.world.markBlockForUpdate(event2.x, event2.y, event2.z);
		    return;
			}
			}
			if(config.cross_nei_compat == false)
    		{
				return;
    		}
			
			//For future users delete this code segment if is causing issues....
			Item item = Item.getItemFromBlock(Blocks.mob_spawner);
			if (event2.itemInHand.getItemDamage() != 0 && event2.itemInHand.getItem() == item) 
			{
					Entity entity = EntityList.createEntityByID(event2.itemInHand.getItemDamage(), event2.world);
					if (entity == null)
					{
						return;
					}
					String ent = EntityList.getEntityString(entity);
					
					if (ent == null)
					{
						ent = "Pig";
					}
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("EntityId", ent);
				    nbt.setInteger("x",event2.x);
				    nbt.setInteger("y", event2.y);
				    nbt.setInteger("z", event2.z);
				    TileEntity tile = event2.world.getTileEntity(event2.x, event2.y, event2.z);
				    tile.readFromNBT(nbt);
				    tile.markDirty();
				    event2.world.markBlockForUpdate(event2.x, event2.y, event2.z);
			}
			
			}

			//Upon right click makes the tile entity mob spawner read it which then updates the tile entity
			@SubscribeEvent
			public void onClick(PlayerInteractEvent event)
			{
			if (!event.world.isRemote)
			{
				
			if (event.entityPlayer.getHeldItem() == null)
			 {
				return;
			 }
			ItemStack rightclick = event.entityPlayer.getHeldItem();
			
			if (rightclick.getItem() != Items.spawn_egg)
			{
				return;
			}
			
			if (rightclick.getItem() == Items.spawn_egg)
			{
				MovingObjectPosition pos = ItemPos.getMovingObjectPositionFromPlayer1(event.world, event.entityPlayer, true);
				if (pos == null)
				{
					return;
				}
				if (pos.typeOfHit.BLOCK == null)
				{
					System.out.println("Returning");
					return;
				}

				int x = pos.blockX;
				int y = pos.blockY;
				int z = pos.blockZ;
			if (!(event.world.getTileEntity(x, y, z) instanceof TileEntityMobSpawner))
			{
			   if (event.face == 0)
          	   {
				   if (event.world.getBlock(x,y-1,z) instanceof BlockLiquid)
				   {
					   y -= 1;
				   }
          	   }
          	   if (event.face == 1)
          	   {
          		   
          		  if (event.world.getBlock(x,y+1,z) instanceof BlockLiquid)
				  {
          			  y += 1;
				  }
          	   }
          	   if (event.face == 2)
          	   {
          		 if (event.world.getBlock(x,y,z-1) instanceof BlockLiquid)
				 {
          		   z -= 1;
				 }
          	   }
          	   if (event.face == 3)
          	   {
          		 if (event.world.getBlock(x,y,z+1) instanceof BlockLiquid)
				 {
          		   z += 1;
				 }
          	   }
          	   if (event.face == 4)
          	   {
          		 if (event.world.getBlock(x-1,y,z) instanceof BlockLiquid)
				 {
          		   x -= 1;
				 }
          	   }
          	   if (event.face == 5)
          	   {
          		   if (event.world.getBlock(x+1,y,z) instanceof BlockLiquid)
				   {
          			   x += 1;
				   }
          	   }
		    }
				Block block = event.world.getBlock(x, y, z);
				
				 if (block instanceof BlockLiquid)
				 {
					TileEntity tiled = event.world.getTileEntity(x,y,z);
					if (tiled instanceof TileEntityMobSpawner)
					{
					NBTTagCompound vartiledebug = new NBTTagCompound();
					//tiled.writeToNBT(vartiledebug);
					
					String GlobalId = EntityList.getStringFromID(rightclick.getItemDamage());
					
					vartiledebug.setString("EntityId", GlobalId);
					vartiledebug.setInteger("x", x);
					vartiledebug.setInteger("y", y);
					vartiledebug.setInteger("z", z);
				//	System.out.println("NBT VarTileDebug: " + vartiledebug);
					tiled.readFromNBT(vartiledebug);
					ItemStack var21 = event.entityPlayer.getCurrentEquippedItem();
						
					event.setCanceled(true);
					
					if (!event.entityPlayer.capabilities.isCreativeMode)
					{
					     var21.stackSize = var21.stackSize -1;
					}
					return;
				  }
				}

				

			if (event.action == event.action.RIGHT_CLICK_BLOCK)
			{
				TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);	    
			 if (tile instanceof TileEntityMobSpawner)
			 {

				TileEntity vartilereset = event.world.getTileEntity(event.x, event.y, event.z);
				NBTTagCompound vartiledebug = new NBTTagCompound();
				
				String GlobalId = EntityList.getStringFromID(rightclick.getItemDamage());
				if (GlobalId == null)
				{
					GlobalId = "Pig";
				}
				vartiledebug.setString("EntityId", GlobalId);
				vartiledebug.setInteger("x", x);
				vartiledebug.setInteger("y", y);
				vartiledebug.setInteger("z", z);
				
				// @Deprecated
				if (config.cross_nei_compat == true)
				{
					if (block == MainJava.chocoSpawner)
					{
						event.world.setBlock(event.x,event.y,event.z, Blocks.mob_spawner);
						vartilereset = event.world.getTileEntity(event.x,event.y,event.z);
					}
				}
				vartilereset.readFromNBT(vartiledebug);
				ItemStack spawnegg = event.entityPlayer.getCurrentEquippedItem();
				ItemStack var21 = event.entityPlayer.getCurrentEquippedItem();
					
				event.setCanceled(true);
				
				if (!event.entityPlayer.capabilities.isCreativeMode)
				{
				     var21.stackSize = var21.stackSize -1;
				}
			}
			}	
			}
			}
			}
			
			//drops spawn eggs based on if it has a global id if not is forge egg if entity instanceof entityliving or if it's not entity living it's a caged egg
			@SubscribeEvent
			public void yourPlayerHarvestEvent(HarvestDropsEvent e)
			{
				if(e.world.isRemote || !Util.isSurvival(e.harvester) || e.harvester.getCurrentEquippedItem() == null || config.EggCap == 0|| config.DisableEggDropFromSpawner || Loader.isModLoaded("EnderIO") && e.block instanceof BlockMobSpawner)
				{
					return;
				}
				Block eblock = e.block;
				ItemStack heldItem = e.harvester.getCurrentEquippedItem();
				
				if(eventiles == null || !Util.isSpawner(eblock, eventiles) || !Util.isHarvestLevel(eblock, heldItem))
					return;
				
				Util.DropEggs(eventiles,eblock,heldItem,e.drops);
				
				if(config.Debug)
					System.out.println("Made It");
			}

}

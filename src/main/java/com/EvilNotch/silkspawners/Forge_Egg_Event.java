package com.EvilNotch.silkspawners;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.EvilNotch.silkspawners.util.Util;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class Forge_Egg_Event {
	

    public Map<Block, Item> buckets = new HashMap<Block, Item>();
	public static List<String> List = new ArrayList<String>();
	public static List<String> List2 = new ArrayList<String>();
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent e)
	{
		System.out.println(EntityList.getEntityString(e.entity) + " " + EntityList.getEntityString(e.entityLiving));
	}
	
	//Allows Dyanamic Spawners To have a tooltip based on entity
	@SubscribeEvent (priority = EventPriority.LOWEST)
	public void onToolTip1(ItemTooltipEvent event)
	{
          ItemStack stack = event.itemStack;

          if (stack.getTagCompound() == null)
          {
        	  return;
          }
          
			//Waila ToolTip Remover
			Block block = Block.getBlockFromItem(event.itemStack.getItem());
			if (block == null && !SilkSpawners.isEgg(event.itemStack) && event.itemStack.getItem() != MainJava.minecartSpawner|| stack.getTagCompound().getString("EntityId").equals("") )//hard coded for now
				return;
			
			String un = SilkSpawners.getStringId(event.itemStack);
			String r_un = un.replaceFirst(":", "\u00A9");
			String[] part = r_un.split("\u00A9");
			String modid = part[0];
			String modname = EntityLookUp.getModName(modid);
			String k = EnumChatFormatting.BLUE + "" + EnumChatFormatting.ITALIC + modname;
			event.toolTip.remove(k);
			
            removeNEIToolTips(event.itemStack, event.toolTip); //Removes NEI instances of ToolTips
            String tooltip = EnumChatFormatting.BLUE + "" + EnumChatFormatting.ITALIC + SilkSpawners.getJockeyModName(stack);
            if(!event.toolTip.contains(tooltip))
            	event.toolTip.add(tooltip);
		}
		public static void removeNEIToolTips(ItemStack stack, List<String> list)
		{
			if(!config.colorText || !Loader.isModLoaded("NotEnoughItems"))
				return;
			NBTTagCompound nbt = SilkSpawners.getNBT(stack);
			String ent = nbt.getString("EntityId");
			
			try{
			Entity tester = SilkSpawners.entities.get(ent);
			if(tester instanceof EntityLivingBase)
			{
				int global = EntityList.getEntityID(tester);
				if(stack.getItem() == Item.getItemFromBlock(Blocks.mob_spawner) &&  global > 0)
				{
					Entity e = EntityList.createEntityByID(global, WorldLoadEvent.world2);
					if(e != null)
					{
						String s = EntityList.getEntityString(e);
						if(s != null)
						{
							list.remove(EnumChatFormatting.DARK_RED + s);
							list.remove(EnumChatFormatting.DARK_AQUA + s);
						}
					}
				}
			}
			}
			catch(Exception e){e.printStackTrace();}
			
			list.remove(EnumChatFormatting.DARK_RED + ent);
			list.remove(EnumChatFormatting.DARK_AQUA + ent);
			
		}
	
		//tooltips
		@SubscribeEvent (priority = EventPriority.HIGHEST)
		public void onToolTip(ItemTooltipEvent e)
		{
			if(e.itemStack == null || e.itemStack.getTagCompound() == null || e.itemStack.getEnchantmentTagList() == null && !(e.itemStack.getItem() == Items.enchanted_book))
				return;
			List<String> list = new ArrayList<String>();
			NBTTagList ench = e.itemStack.getEnchantmentTagList();
			if(ench == null || ench.tagCount() == 0)
				ench = SilkSpawners.getTagList(e.itemStack, "StoredEnchantments", 10);
			if(ench.tagCount() == 0)
				return;
			
			for(int j=0;j<e.toolTip.size();j++)
				list.add(e.toolTip.get(j)); //Allocates
			
			//Remove All Enchantments from my list
			for(int i=0;i<list.size();i++)
			{
				String s = list.get(i);
				for(int j=0;j<ench.tagCount();j++)
				{
					NBTTagCompound nbt = (NBTTagCompound)ench.getCompoundTagAt(j);
					int id = nbt.getInteger("id");
					int lvl = nbt.getInteger("lvl");
					if(id == 0)
						id = (int)nbt.getShort("id");
					if(lvl == 0)
						lvl = (int)nbt.getShort("lvl");
					
					String Roman = RomanNumerals.translateIntToRoman(lvl);
					if(lvl >= 4000)
						Roman = String.valueOf(lvl);
					
					try{
						Enchantment enchantment = Enchantment.enchantmentsList[id];
						String enchname = StatCollector.translateToLocal(enchantment.getName());
						if(s.contains(enchname + " " + Roman) || s.contains(enchname + " enchantment.level"))
							list.set(i,enchname + " " + Roman);
					}
					catch(Exception ee){ee.printStackTrace();}
				}
			}
			e.toolTip.clear();
			
			for(String s : list)
				e.toolTip.add(s);
		}
				//Get's My modified Milk Bucket to return default silk touch bucket after
				@SubscribeEvent
				public void onClicking1(EntityInteractEvent event)
				{

					ItemStack heldItem = event.entityPlayer.getCurrentEquippedItem();
					 if (event.entityPlayer.worldObj.isRemote || heldItem == null || event.target == null || event.target instanceof EntityPlayer)
				     {
					      return;
					 }
					 
				if (event.target instanceof EntityCow && heldItem.getItem() == Items.bucket)
				{
					 if (EnchantmentHelper.getEnchantmentLevel(33, heldItem) == 0)
					 {
						 return;
					 }
					 ItemStack milk = new ItemStack(MainJava.CustomMilkBucket);
					 milk.addEnchantment(Enchantment.silkTouch, 1);
					 if (!event.entityPlayer.capabilities.isCreativeMode)
					 {
						heldItem.stackSize = heldItem.stackSize - 1;
						
						if (event.entityPlayer.inventory.getFirstEmptyStack() == -1 && heldItem.stackSize >= 1)
						{
							EntityItem emilk = new EntityItem(event.entityPlayer.worldObj, event.entityPlayer.posX, event.entityPlayer.posY, event.entityPlayer.posZ, milk);
							event.entityPlayer.worldObj.spawnEntityInWorld(emilk);
							event.setResult(Result.ALLOW);
					     	event.setCanceled(true);
					     	return;
						}
						if (event.entityPlayer.inventory.getFirstEmptyStack() != -1 && heldItem.stackSize >= 1)
						{
							event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.getFirstEmptyStack(), milk);
							event.setResult(Result.ALLOW);
							event.setCanceled(true);
							return;
						}
						else{
							event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, milk);
							event.setResult(Result.ALLOW);
							event.setCanceled(true);
						}
				     	
					 }
				}
				boolean use = false;
				if (heldItem.stackSize > 1)
				{
					use = true;
				}
				heldItem = ItemStack.copyItemStack(event.entityPlayer.getCurrentEquippedItem());
				heldItem.stackSize = 1;
				
				//Gets Blank Forge Egg's Entity For Survival Forge Egg Mounting :)
				if (heldItem.getItem() == MainJava.forge_egg_blank)
				{
					String entstr = EntityList.getEntityString(event.target);
					Entity targ = event.target;
				
			    //Get's Entity From Part then resets entstr's string/entity...
				if (entstr == null)
				{
					java.util.List sk = event.entityPlayer.worldObj.getEntitiesWithinAABBExcludingEntity(EntityList.createEntityByName("Pig", event.entityPlayer.worldObj), event.target.boundingBox);
					for (int i=0;i<sk.size();i++)
					{
						Entity tst = (Entity)sk.get(i);
						if (tst != null)
						{
							if (EntityList.getEntityString(tst) != null && tst instanceof EntityLiving)
							{
								entstr = EntityList.getEntityString(tst);
								targ = tst;
							}	
						}
					}
				}
				if (entstr != null)
				{
					
					NBTTagCompound nbt = heldItem.getTagCompound();
					boolean flag = false;
					boolean flag_dead = false;
					if (nbt == null)
					{
						heldItem.setTagCompound(new NBTTagCompound());
						nbt = heldItem.getTagCompound();
						flag = true;
					}
					if (nbt.getString("EntityId") == "")
					{
						flag = true;
					}
					NBTTagList list = heldItem.getTagCompound().getTagList("Mounting", 10);
					if (list == null)
					{
						NBTTagList newList = new NBTTagList();
						NBTTagCompound nbtnew = new NBTTagCompound();
						nbtnew.setTag("Mounting", newList);
						heldItem.setTagCompound(nbtnew);
						list = heldItem.getTagCompound().getTagList("Mounting", 10);
					}
				
					NBTTagCompound entity_nbt = new NBTTagCompound();
					NBTTagCompound data = new NBTTagCompound();
					
						targ.writeToNBT(data);
						Util.removeEggNBT(targ, data, nbt.getInteger("ForgeLvl"));
						
						entity_nbt.setTag("EntityNBT", data);

						if (!(nbt.hasKey("ForgeLvl") ) || nbt.getInteger("ForgeLvl") == 1)
						{
							entity_nbt.removeTag("EntityNBT");
						}

					if(nbt.hasKey("EntityId") && flag == false)
					{
						//it's plus one to count the EntityId spawn capture
						if (!(list.tagCount() + 1 >= config.blankCaptureCap))
						{
							entity_nbt.setString("id", entstr);
							list.appendTag(entity_nbt);
							flag_dead = true;
						}
						if (list.tagCount() + 1 < config.blankCaptureCap)
						{	                         
							nbt.setTag("Mounting", list);
						}
					}
					else{
						if (config.blankCaptureCap != 0)
						{
							nbt.setString("EntityId", entstr);
							
							if (nbt.getInteger("ForgeLvl") > 1)
							{
								nbt.setTag("EntityNBT", data);
							}
							
							flag_dead = true;
						}
					}

					//Iterates through all itemstacks and tries to find an available slot...
					Iterator tst = event.entityPlayer.inventoryContainer.inventoryItemStacks.iterator();
					while (tst.hasNext())
					{
						ItemStack stack = (ItemStack)tst.next();
						if (ItemStack.areItemStacksEqual(stack, heldItem) == true && stack.stackSize < 64)
						{
							use = true;
						}
					}
					
					//If Flagged Dead sets itemstack to inventory or on ground depending on inventory status
					if (flag_dead == true)
					{
						//heldItem.setTagCompound(new NBTTagCompound());
						targ.setDead();
						if (use == true)
						{
							boolean k = event.entityPlayer.inventory.addItemStackToInventory(heldItem);
							event.entityPlayer.inventoryContainer.detectAndSendChanges();
							if (k == false)
							{
								event.entityPlayer.worldObj.spawnEntityInWorld(new EntityItem(event.entityPlayer.worldObj, event.entityPlayer.posX, event.entityPlayer.posY, event.entityPlayer.posZ, heldItem));
							}
							event.entityPlayer.getCurrentEquippedItem().stackSize -=1; //Actual itemstack subtraction
						}
						else{
							event.entityPlayer.getCurrentEquippedItem().setTagCompound(nbt);
						}
						System.out.println("NBT Blank Egg: " + heldItem.getTagCompound());

					}
					else{
						event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + event.entityPlayer.getDisplayName() + " " + EnumChatFormatting.DARK_RED + "Hit Max Entity Capture"));
					}
				}
					else{
						event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + event.entityPlayer.getDisplayName() + " " + EnumChatFormatting.DARK_RED + "Entity Not Registered"));
					}
			}
				
			//Blank book capture's Entities' String Id and NBT To Clipboard...
			heldItem = event.entityPlayer.getCurrentEquippedItem();
			if (heldItem.getItem() == MainJava.book_capture && !(event.target instanceof EntityPlayer) )
			{
				NBTTagCompound nbt = new NBTTagCompound();
				//event.target
				Entity targ = event.target;
				
				String ent = EntityList.getEntityString(event.target);
				boolean ok = true;
				if (ent == null)
				{
					java.util.List sk = event.entityPlayer.worldObj.getEntitiesWithinAABBExcludingEntity(EntityList.createEntityByName("Pig", event.entityPlayer.worldObj), event.target.boundingBox);
					for (int i=0;i<sk.size();i++)
					{
						Entity tst = (Entity)sk.get(i);
						if (tst != null)
						{
							if (EntityList.getEntityString(tst) != null && tst instanceof EntityLiving)
							{
								ent = EntityList.getEntityString(tst);
								targ = tst;
							}	
						}
					}
					if (ent == null)
					{
						return;
					}
				}
				NBTTagCompound nbt2 = new NBTTagCompound();
				targ.writeToNBT(nbt2);
				nbt2.removeTag("Pos"); //Removes Pos so it doesn't have the pos to be applied specifically to the spawner...
				Util.removeCustomNames(nbt2, false);
				nbt.setString("EntityId", ent);
				nbt.setTag("EntityNBT", nbt2);

				String display = SilkSpawners.getCurrentTranslatedEntity(ent);
				event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + display + EnumChatFormatting.DARK_RED + " Copied To ClipBoard"));
				String strnbt = nbt.toString();
				if (ok == true)
				{
					ent = strnbt;
				}
				writeToClipboard(ent, null);

			}
				
		}
				public void writeToClipboard(String s, ClipboardOwner owner) 
				{
					if(s == null)
					{
						s = "null";
					}
				    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				    Transferable transferable = new StringSelection(s);
				    clipboard.setContents(transferable, owner);
				}

		//Bucket Event Handler Fill bucket
        @SubscribeEvent
        public void onBucketFill(FillBucketEvent event) 
        {
        	if (event.world.isRemote)
        	{
        		return;
        	}
            if (event.target.typeOfHit.BLOCK == null)
            {
            	return;
            }
            if(event.current.getItem() != Items.bucket)
            {
            	return;
            }
            int x = event.target.blockX;
            int y = event.target.blockY;
            int z = event.target.blockZ;
            Block block = event.world.getBlock(x, y, z);
            
            
            int silklvl = EnchantmentHelper.getEnchantmentLevel(33, event.current);
            
            if (silklvl == 0)
            {
            	if (block == MainJava.WaterSpawner || block == MainJava.LavaSpawner)
            	{
            		//System.out.println("Water Or lava spawner Not Silk Touch Bucket");
            		if (!event.world.isRemote)
            		{
            			Random rand = new Random();
						int rnd = rand.nextInt(100-15) + 15;
						int i = rnd;//the while loop creates xp dropping multiple orbs until the rnd is done(1 to 5.2 levels)...
						while (i>0)
						{
							event.world.spawnEntityInWorld(new EntityXPOrb(event.world, event.target.blockX, event.target.blockY, event.target.blockZ, (int)rnd/4));
							i = i/4;
						}
            		}
            	}
            	return;
            }
            if (event.current.getEnchantmentTagList() == null)
            {
            	return;
            }
            
            if ( !(block instanceof BlockLiquid) && !(block instanceof BlockDynamicLiquid) && !(block instanceof BlockStaticLiquid))
            {
            	//System.out.println("Returning Not Right Liquid");
            	event.setResult(Result.DENY);
            	return;
            }
            if ( block instanceof BlockLiquid || block instanceof BlockDynamicLiquid)
            {
            	if (block == Blocks.water || block == Blocks.flowing_water)
            	{
            		ItemStack water = new ItemStack(Items.water_bucket);
            		water.addEnchantment(Enchantment.silkTouch, 1);
            		event.result = water;
            		event.setResult(Result.ALLOW);
            		event.world.setBlockToAir(event.target.blockX, event.target.blockY, event.target.blockZ);
            		event.world.setBlockToAir(event.target.blockX, event.target.blockY,event.target.blockZ);
            		return;
            	}
            	
            	if (block == Blocks.lava || block == Blocks.flowing_lava)
            	{
            		ItemStack lava = new ItemStack(Items.lava_bucket);
            		lava.addEnchantment(Enchantment.silkTouch, 1);
            		event.result = lava;
            		event.setResult(Result.ALLOW);
            		event.world.setBlockToAir(event.target.blockX, event.target.blockY,event.target.blockZ);
            		return;
            	}
            	
            	if (block != MainJava.WaterSpawner && block != MainJava.LavaSpawner)
            	{
            		if (!event.world.isRemote)
            		{
            			event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + event.entityPlayer.getDisplayName() + " " + EnumChatFormatting.DARK_RED + "Modded Liquid Detected Denied Silk Touch On"));
            			event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "Bucket"));
            		}
                	event.setCanceled(true);
                	return;
            	}
            	
            }
           
                ItemStack stack = ItemStack.copyItemStack(event.current); //current equipped item
                TileEntity tile = event.world.getTileEntity(x, y, z); //gets the tile entity
                
            if (tile == null || stack == null)
            {
            	   return;
            }

            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound tilenbt = new NBTTagCompound();
            tile.writeToNBT(tilenbt);
            String entity = tilenbt.getString("EntityId");
            nbt.setString("EntityId", entity);
            stack.setTagCompound(nbt);
		    ItemStack result = fillCustomBucket(event.world, stack, event.target);
		    
             if (result == null)
                     return;
             
             	if (result != null) //check so doesn't get rid of current item stacks
             	{
             		event.result = result;
                	event.setResult(Result.ALLOW);
             	}
        }

        private ItemStack fillCustomBucket(World world,ItemStack stack, MovingObjectPosition pos) 
        {
                Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
                TileEntity tile = world.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
                if (stack.getTagCompound() == null)
                {
                	return null;
                }
                NBTTagCompound nbt = new NBTTagCompound();
                tile.writeToNBT(nbt);
                nbt.setInteger("x", 0);
                nbt.setInteger("y", 0);
                nbt.setInteger("z", 0);
                String entid = nbt.getString("EntityId");
                Entity ent = EntityList.createEntityByName(entid, world);
                if (ent == null)
                {
                	ent = EntityList.createEntityByName("Creeper", world);
                }
               if (block == MainJava.WaterSpawner || block == MainJava.LavaSpawner)
               {
            	   ItemStack bucket = new ItemStack(MainJava.water_spawner_bucket);
                   if (block == MainJava.LavaSpawner)
                   {
                   		ItemStack lava = new ItemStack(MainJava.lava_spawner_bucket);
                   		bucket = lava;
                   }
            	   bucket.setTagCompound(nbt);
            	   bucket.addEnchantment(Enchantment.silkTouch, 1);
            	   world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
                   return bucket;
               }
               
            	   return null;       
        }
        
        //Bucket Place event regardless if it says buciket fill it activates on both events setup for placing bucket event though...
        @SubscribeEvent
        public void onBucketPlace(FillBucketEvent event) 
        {
        	 if (event.world.isRemote)
        	 {
        		 return;
        	 }
        	 if (event.target.typeOfHit.BLOCK == null)
             {
        		System.out.println("returning block null");
             	return;
             }
             if(event.current.getItem() != MainJava.water_spawner_bucket && event.current.getItem() != MainJava.lava_spawner_bucket)
             {
             	return;
             }
            
             int x = event.target.blockX;
             int y = event.target.blockY;
             int z = event.target.blockZ;

               ItemStack stack = ItemStack.copyItemStack(event.current); //current equipped item
               
       
             ItemStack result = PlaceBucket(event.world, stack, event.target);

              if (result == null)
              {
                   return;
              }
              
              	if (result != null) //check so doesn't get rid of current item stacks
              	{
              		event.result = result;
                 	event.setResult(Result.ALLOW);
              	}
        }
        
        private ItemStack PlaceBucket(World world,ItemStack stack, MovingObjectPosition pos) 
        {
                Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
         	   ItemStack bucket = new ItemStack(Items.bucket);
         	   bucket.addEnchantment(Enchantment.silkTouch, 1);
              
               if (block != null)
               {
            	   int x = pos.blockX;
            	   int y = pos.blockY;
            	   int z = pos.blockZ;
            	   
            	   //temp fix for this will have to move place bucket inside custom bucket's class
            	  if (!block.isReplaceable(world, x, y, z))
            	  {
            	   if (pos.sideHit == 0)
            	   {
            		   y -= 1;
            	   }
            	   if (pos.sideHit == 1)
            	   {
            		   y += 1;
            	   }
            	   if (pos.sideHit == 2)
            	   {
            		   z -= 1;
            	   }
            	   if (pos.sideHit == 3)
            	   {
            		   z += 1;
            	   }
            	   if (pos.sideHit == 4)
            	   {
            		   x -= 1;
            	   }
            	   if (pos.sideHit == 5)
            	   {
            		   x += 1;
            	   }
            	  }
            	  NBTTagCompound nbt = new NBTTagCompound();
            	  boolean hastag = false;
            	  if (stack.getTagCompound() != null)
            	  {
            		  nbt = (NBTTagCompound)stack.getTagCompound().copy();
            		  hastag = true;
            	  }
            	  if (stack.getItem() == MainJava.water_spawner_bucket && hastag == false  || stack.getItem() == MainJava.lava_spawner_bucket && hastag == false || nbt.getString("EntityId") == "" && stack.getItem() == MainJava.water_spawner_bucket || nbt.getString("EntityId") == "" && stack.getItem() == MainJava.lava_spawner_bucket)
                  {
            		    if (stack.getItem() == MainJava.water_spawner_bucket)
              	        {
              	    	 world.setBlock(x, y, z, MainJava.WaterSpawner);  
              	        }
                  		if (stack.getItem() == MainJava.lava_spawner_bucket)
                  		{
                  			world.setBlock(x, y, z, MainJava.LavaSpawner); 
                  		}
              	      
                  		  return bucket;
                  }
            	  
                  ItemStack bucketa = new ItemStack(Items.bucket);
                  if (EnchantmentHelper.getEnchantmentLevel(33, stack) > 0)
                  {
                	  bucketa.addEnchantment(Enchantment.silkTouch, 1);
                  }
                  if (stack.getItem() == Items.lava_bucket|| stack.getItem() == Items.water_bucket || stack.getItem() == Items.milk_bucket)
                  {
                	if(EnchantmentHelper.getEnchantmentLevel(33, stack) == 0 && block != Blocks.snow_layer)
                		return null;

               	   if (stack.getItem() == Items.lava_bucket)
               	   {
               		   world.setBlock(x, y, z, Blocks.flowing_lava);
               		   world.markBlockForUpdate(x, y, z);
               	   }
               	   if (stack.getItem() == Items.water_bucket && !world.provider.isHellWorld)
               	   {
               		   world.setBlock(x, y, z, Blocks.flowing_water);
               		   world.markBlockForUpdate(x, y, z);
               	   }
               	  
               	   return bucketa;
                  }

            	  if (stack.getTagCompound() == null || stack.getTagCompound().getString("EntityId") == "" && stack.getItem() != Items.water_bucket && stack.getItem() != Items.lava_bucket)
                  {
            	      
                	  return bucket;
                  }
            	  

                  nbt.setInteger("x", x);
                  nbt.setInteger("y", y);
                  nbt.setInteger("z", z);
            	  Block block2 = world.getBlock(x, y, z);

                   if (stack.getItem() == MainJava.water_spawner_bucket && !world.provider.isHellWorld)
                   {
                   		world.setBlock(x, y, z, MainJava.WaterSpawner);
                   		TileEntity tile = world.getTileEntity(x, y , z);
                   		if (tile == null)
                   		{
                   			return null;
                   		}
                   		tile.readFromNBT(nbt);
                   		tile.markDirty();
                   }
                   if (stack.getItem() == MainJava.lava_spawner_bucket)
                   {
                	   if (block2.getMaterial() == Material.water)
                	   {
                		   return null;
                	   }
                	   world.setBlock(x, y, z, MainJava.LavaSpawner); //if lava spawner bucket do this
                  	   TileEntity tile = world.getTileEntity(x, y, z);
                  	   if (tile == null)
                  	   {
                  		   return null;
                  	   }
                  		tile.readFromNBT(nbt);
                  		tile.markDirty(); //Makes the mob spawner dirty, jokes it actually saves it to the disk
                   }


                   return bucket;
               }
               else{
            	   return null;
               }
               
                        
        }
        
        //Fix for Buckets Transferring Silk Touch (lava water and milk)....
        @SubscribeEvent
        public void onVanillaBucket(FillBucketEvent event) 
        {
        	if (event.world.isRemote) 
        	{
        		return;
        	}
        	ItemStack stack = event.current;
        	if (stack == null)
        	{
        		return;
        	}
        	Item item = stack.getItem();
        	
        	MovingObjectPosition pos = event.target;
     	   int x = pos.blockX;
       	   int y = pos.blockY;
       	   int z = pos.blockZ;
       	   if (pos.sideHit == 0)
       	   {
       		   y -= 1;
       	   }
       	   if (pos.sideHit == 1)
       	   {
       		   y += 1;
       	   }
       	   if (pos.sideHit == 2)
       	   {
       		   z -= 1;
       	   }
       	   if (pos.sideHit == 3)
       	   {
       		   z += 1;
       	   }
       	   if (pos.sideHit == 4)
       	   {
       		   x -= 1;
       	   }
       	   if (pos.sideHit == 5)
       	   {
       		   x += 1;
       	   }
       	 Block block = event.world.getBlock(x,y,z);
       	 if (block == null)
       	 {
       		 return;
       	 }
        	
        	if (item == Items.lava_bucket || item == Items.water_bucket)
        	{
        	   
        		int silklvl = EnchantmentHelper.getEnchantmentLevel(33, stack);
        		if (silklvl == 0)
        		{
        			if (block instanceof LiquidMobSpawner)
        			{
        				TileEntity tile = event.world.getTileEntity(x, y, z);
        				NBTTagCompound tilenbt = new NBTTagCompound();
        				tile.writeToNBT(tilenbt);
        				event.setCanceled(true);
        				tile.readFromNBT(tilenbt);
        				tile.markDirty();
        				event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + event.entityPlayer.getDisplayName() + " " + EnumChatFormatting.DARK_RED + "Cancled Event To Prevent Liquid Spawners From"));
        				event.entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "Disappearing"));
        	 			event.setCanceled(true);
        				return;
        			}
        			return;
        				
        		}

        		ItemStack k = PlaceBucket(event.world, stack, event.target);
        		if (k == null)
        		{
        			return;
        		}
        		event.result = k;
        		event.setResult(Result.ALLOW);
        		
        	}
        }
		
	
}

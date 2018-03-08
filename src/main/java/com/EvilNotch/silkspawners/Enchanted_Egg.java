package com.EvilNotch.silkspawners;

import java.util.List;

import com.EvilNotch.silkspawners.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.util.Facing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class Enchanted_Egg extends Item{
	
	    Enchanted_Egg(String name)
		{
			this.setUnlocalizedName(name);
			this.setTextureName("silkspawners:enchanted_egg");
			this.setCreativeTab(CreativeTabs.tabMisc);
			this.setHasSubtypes(true);
		}
		
		
	    @Override
	    public String getItemStackDisplayName(ItemStack stack)
	    {
	        if (stack.getTagCompound() == null || stack.getTagCompound().getString("EntityId") == "" || stack.getTagCompound().getString("EntityId") == null)
	        {
	            String a = ("" + StatCollector.translateToLocal(MainJava.enchanted_egg.getUnlocalizedName() + ".name")).trim();
	            return a;
	        }
	        return Util.getItemTranslation(SilkSpawners.getNBT(stack), stack, false);
	    }
		
		/**
	     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	     */
		@Override
	    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int offset, float p_77648_8_, float p_77648_9_, float p_77648_10_)
	    {
	        if (world.isRemote)
	        {
	            return true;
	        }
	        if (!stack.hasTagCompound())
	        {
	        	return true;
	        }
	        if (stack.getTagCompound().getString("EntityId") == "")
	        {
	        	return true;
	        }
	        else
	        {
	            Block block1 = world.getBlock(x, y, z);
	            x += Facing.offsetsXForSide[offset];
	            y += Facing.offsetsYForSide[offset];
	            z += Facing.offsetsZForSide[offset];
	            double d0 = 0.0D;

	            if (offset == 1 && block1.getRenderType() == 11)
	            {
	                d0 = 0.5D;
	            }
	            Block block = world.getBlock(x, y, z);
	            if (block instanceof BlockLiquid && block.getMaterial() == Material.lava)
	            {
	            	NBTTagCompound nbt = stack.getTagCompound();
	                world.setBlock(x, y, z, MainJava.LavaSpawner);
	                TileEntity tile = world.getTileEntity(x, y, z);
	                Entity entity1 = EntityList.createEntityByName(nbt.getString("EntityId"), world);
	                if (entity1 == null)
	                {
	               	 	return false;
	                }
	                if (tile instanceof TileEntityMobSpawner)
	                {
	                	SilkSpawners.setMountedSpawner(stack, tile, world, x, y, z);
	                	
	                     if (!player.capabilities.isCreativeMode)
	                     {
	                         --stack.stackSize;
	                     }

	                     return true;
	                }
	            }
	            
	           return true;
	        }
	    }
	    
	    
	    /**
	     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	     */
		@Override
	    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	    {
	        if (world.isRemote)
	        {
	        	//System.out.println("movingobj");
	            return stack;
	        }
	        if (!stack.hasTagCompound())
	        {
	        	return stack;
	        }
	        if (stack.getTagCompound().getString("EntityId") == "")
	        {
	        	//System.out.println("movingobj");
	        	return stack;
	        }
	        else
	        {
	            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

	            if (movingobjectposition == null)
	            {
	            	//System.out.println("movingobj");
	                return stack;
	            }
	            else
	            {
	                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
	                {
	                    int i = movingobjectposition.blockX;
	                    int j = movingobjectposition.blockY;
	                    int k = movingobjectposition.blockZ;

	                    if (!world.canMineBlock(player, i, j, k))
	                    {
	                    	//System.out.println("mineing");
	                        return stack;
	                    }

	                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack))
	                    {
	                    	//System.out.println("Edit");
	                        return stack;
	                    }
	                    int x = i;
	                    int y = j;
	                    int z = k;
	                    //System.out.println(world.getBlock(i, j, k).getLocalizedName());
	                    if (!(world.getBlock(i, j, k) instanceof BlockLiquid))
	                    {
	                       if (movingobjectposition.sideHit == 0)
	                  	   {
	                  		   j -= 1;
	                  	   }
	                  	   if (movingobjectposition.sideHit == 1)
	                  	   {
	                  		   j += 1;
	                  	   }
	                  	   if (movingobjectposition.sideHit == 2)
	                  	   {
	                  		   k -= 1;
	                  	   }
	                  	   if (movingobjectposition.sideHit == 3)
	                  	   {
	                  		   k += 1;
	                  	   }
	                  	   if (movingobjectposition.sideHit == 4)
	                  	   {
	                  		   i -= 1;
	                  	   }
	                  	   if (movingobjectposition.sideHit == 5)
	                  	   {
	                  		   i += 1;
	                  	   }
	                    }
	                    //System.out.println("After:" + world.getBlock(i, j, k).getLocalizedName());
	                    if (world.getBlock(i, j, k) instanceof BlockLiquid && world.getBlock(x, y, z).getMaterial() == Material.lava)
	                    {
	                   	 NBTTagCompound nbt = stack.getTagCompound();
	                     world.setBlock(x, y, z, MainJava.LavaSpawner);
	                     TileEntity tile = world.getTileEntity(x, y, z);
	                     Entity entity1 = EntityList.createEntityByName(nbt.getString("EntityId"), world);
	                     if (entity1 == null)
	                     {
	                    	// System.out.println("Null");
	                    	 return stack;
	                     }
	                     if (tile instanceof TileEntityMobSpawner)
	                     {
	                    	 SilkSpawners.setMountedSpawner(stack, tile, world, x, y, z);
	                    	 
	                          if (!player.capabilities.isCreativeMode)
	                          {
	                              --stack.stackSize;
	                          }
	                          return stack;
	                      
	                     }

	                    }
	                }

	                return stack;
	            }
	        }
	    }
		
		 @Override
		 @SideOnly(Side.CLIENT)
		 public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) 
		 {
			 SilkSpawners.addEggToolTip(stack, list, b);
		 }

}

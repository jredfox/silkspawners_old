package com.EvilNotch.silkspawners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.EvilNotch.silkspawners.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class caged_egg extends Item{

	//debugging egg for modpack / mod creators
	
	caged_egg (String name)
	{
		this.setUnlocalizedName(name);
		this.setTextureName("silkspawners:caged_egg");
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setHasSubtypes(true);
	}
	
	
	@Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getTagCompound() == null || stack.getTagCompound().getString("EntityId") == "" || stack.getTagCompound().getString("EntityId") == null)
        {
            String a = ("" + StatCollector.translateToLocal(MainJava.caged_egg.getUnlocalizedName() + ".name")).trim();
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
        else
        {
            Block block = world.getBlock(x, y, z);
           int x1 = x + Facing.offsetsXForSide[offset];
           int y1 = y + Facing.offsetsYForSide[offset];
           int z1 = z + Facing.offsetsZForSide[offset];
            double d0 = 0.0D;

            if (offset == 1 && block.getRenderType() == 11)
            {
                d0 = 0.5D;
            }

            TileEntity tile = world.getTileEntity(x1, y1, z1);
            TileEntity tile1 = world.getTileEntity(x,y,z);
            
            NBTTagCompound nbt = stack.getTagCompound();
            String entityname1 = nbt.getString("EntityId");
            
            if (world.getBlock(x1, y1, z1) instanceof BlockLiquid && tile instanceof TileEntityMobSpawner)
            {
                Entity entity1 = EntityList.createEntityByName(nbt.getString("EntityId"), world);
                if (entity1 == null)
                {
               	 	return false;
                }

                SilkSpawners.setMountedSpawner(stack, tile, world, x1, y1, z1); //New Super Cool Optimization Method For Spawners

                 
                if (!player.capabilities.isCreativeMode)
                {
                         --stack.stackSize;
                }

                     return true;
            }
            if (tile1 instanceof TileEntityMobSpawner)
            {
            	 Entity entity1 = EntityList.createEntityByName(nbt.getString("EntityId"), world);
                 if (entity1 == null)
                 {
                	 return false;
                 }
                 
                 SilkSpawners.setMountedSpawner(stack, tile1, world, x, y, z);
                 
                      if (!player.capabilities.isCreativeMode)
                      {
                          --stack.stackSize;
                      }

                      return true;
            }
           String entityname0 = nbt.getString("EntityId");
           // Entity entity = spawnCreature(p_77648_3_, p_77648_1_.getItemDamage(), (double)p_77648_4_ + 0.5D, (double)p_77648_5_ + d0, (double)p_77648_6_ + 0.5D);
            Entity entity = SilkSpawners.spawnCreature(world, stack, (double)x1 + 0.5D,  (double)y1 + d0, (double)z1 + 0.5D, true);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                {
                	if (!stack.getDisplayName().equals(""))
                	{
                		((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
                	}
                }

                if (!player.capabilities.isCreativeMode)
                {
                    --stack.stackSize;
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
                        return stack;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack))
                    {
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
                    if (world.getBlock(i, j, k) instanceof BlockLiquid)
                    {
                   	 NBTTagCompound nbt = stack.getTagCompound();
                   	 String entityname1 = nbt.getString("EntityId");
                     TileEntity tile = world.getTileEntity(i, j, k);
                     Entity entity1 = EntityList.createEntityByName(nbt.getString("EntityId"), world);
                     if (entity1 == null)
                     {
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
                        Entity entity = SilkSpawners.spawnCreature(world, stack, (double)x + 0.5D, (double)y, (double)z + 0.5D, true);
                       

                        if (entity != null)
                        {
                            if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                            {
                                ((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
                            }

                            if (!player.capabilities.isCreativeMode)
                            {
                                --stack.stackSize;
                            }
                        }
                    }
                }

                return stack;
            }
        }
    }
    
    

    
	
    @Override
	 @SideOnly(Side.CLIENT)
	    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
	    {
    	  
 		SilkSpawners.genHashMaps(); //Generates All HashMaps if they are not empty
 		
	  Iterator iterator = SilkSpawners.caged_eggs.entrySet().iterator();

	        while (iterator.hasNext())
	        {
	        	    Map.Entry pair2 = (Map.Entry)iterator.next();
	                String it = pair2.getKey().toString();
		        	
		 	        NBTTagCompound nbt = new NBTTagCompound();
		 	        NBTTagCompound local = new NBTTagCompound();
		 	        World world = DimensionManager.getWorld(0);
	                Entity entity = EntityList.createEntityByName(it, world);
	                
	                if (entity != null && config.caged_eggs)
	                {
	                	String raw = EntityLookUp.getEntityMod(entity);
	                	String[] parts = raw.split("\u00A9");
	                	String modname = parts[0];
	                	nbt.setString("EntityId", it);
	                	nbt.setString("modid", modname);
	                	nbt.setString("CreativeTab","true");
	                	ItemStack jk = new ItemStack(MainJava.caged_egg, 1, 0);
	 	        		jk.setTagCompound(nbt);
	 	       
	 	        		p_150895_3_.add(jk);
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

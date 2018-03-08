package com.EvilNotch.silkspawners;

import com.EvilNotch.silkspawners.util.Util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemMinecartSpawner extends ItemMinecart
{

	public ItemMinecartSpawner() 
	{
		super(4);
		this.setUnlocalizedName("MineCartSpawner");
		this.setTextureName("silkspawners:MineCartSpawner");
		this.setCreativeTab(CreativeTabs.tabTransport);
	}
	
	@Override
    public String getItemStackDisplayName(ItemStack stack)
    {
		
        String a = ("" + StatCollector.translateToLocal(MainJava.minecartSpawner.getUnlocalizedName() + ".name")).trim();
        if (stack.getTagCompound() == null || stack.getTagCompound().getString("EntityId") == "" || stack.getTagCompound().getString("EntityId") == null)
        {
            return EnumChatFormatting.LIGHT_PURPLE + a + EnumChatFormatting.RESET;
        }
        ItemStack s = stack;
        Block b = Blocks.mob_spawner;
        NBTTagCompound nbt = SilkSpawners.getNBT(stack);
        if(nbt.hasKey("DisplayTile"))
        {
        	int blockid = nbt.getInteger("DisplayTile");
        	if(blockid != 0 && Util.isBlock(Block.getBlockById(blockid), MainJava.SpawnerList))
        		b = Block.getBlockById(blockid);
        }
        if(b != Blocks.mob_spawner && b != MainJava.chocoSpawner)
        	s = new ItemStack(b);

        String name = Util.getItemTranslation(nbt, s, false);
        return name;
    }
	
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (BlockRailBase.func_150051_a(p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_)))
        {
            if (!p_77648_3_.isRemote)
            {
               // EntityMinecart entityminecart = EntityMinecart.createMinecart(p_77648_3_, (double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F), this.minecartType);
                EntityMinecartMobSpawner entityminecart = new EntityMinecartMobSpawner(p_77648_3_,(double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F));
                if (stack.hasDisplayName())
                {
                    entityminecart.setMinecartName(stack.getDisplayName());
                }
                try{
                	if(stack.getTagCompound() != null)
                	{
                	 	System.out.println("HASNBT");
                	 	NBTTagCompound tag = (NBTTagCompound) stack.getTagCompound().copy();
                	 	tag.setString("TileId", tag.getString("id"));
                	 	if(tag.getString("id").equals(""))
                	 		tag.setString("TileId", "MobSpawner");
                	 	tag.setString("id", "MinecartSpawner");
                	 	Entity e = EntityList.createEntityFromNBT(tag, p_77648_3_);
                	 	System.out.println("Entity:" + e + " NBT:" + tag);
                	 	if(e != null)
                	 	{
                	 		e.setLocationAndAngles((double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F),MathHelper.wrapAngleTo180_float(p_77648_3_.rand.nextFloat() * 360.0F), 0.0F);
                	 		p_77648_3_.spawnEntityInWorld(e);
                	 	}
                	 	--stack.stackSize;
                	 	  return true;
                	}
                	else{
                		NBTTagCompound nbt = new NBTTagCompound();
                		nbt.setString("id", "MinecartSpawner");
                		nbt.setString("EntityId", "Pig");
                		nbt.setString("TileId", "MobSpawner");
        				nbt.setBoolean("CustomDisplayTile", true);
        				nbt.setInteger("DisplayTile", Block.getIdFromBlock(MainJava.chocoSpawner));
        				nbt.setInteger("DisplayOffset", 6);
                		Entity e = EntityList.createEntityFromNBT(nbt, p_77648_3_);
                		if(e != null)
                		{
                			e.setLocationAndAngles((double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F),MathHelper.wrapAngleTo180_float(p_77648_3_.rand.nextFloat() * 360.0F), 0.0F);
                			p_77648_3_.spawnEntityInWorld(e);
                			--stack.stackSize;
                			return true;
                		}
                	}
                }
                catch(Exception e){e.printStackTrace(); System.out.println("Exception e");}
                return true;
              }
            }

        else
        {
            return false;
        }
		return true;
    }
   
}

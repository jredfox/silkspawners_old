package com.EvilNotch.silkspawners.entity;

import com.EvilNotch.silkspawners.MainJava;
import com.EvilNotch.silkspawners.WorldLoadEvent;
import com.EvilNotch.silkspawners.util.Util;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class MinecartMobSpawner extends EntityMinecartMobSpawner
{
	private String Tileid = "";

	public MinecartMobSpawner(World p_i1725_1_) 
	{
		super(p_i1725_1_);
	}
	
	public MinecartMobSpawner(World w, double p_i1726_2_, double p_i1726_4_, double p_i1726_6_)
    {
        super(w, p_i1726_2_, p_i1726_4_, p_i1726_6_);
    }
	@Override
	public Block func_145817_o()
	{
		return MainJava.chocoSpawner;
	}
	
	@Override
	public void killMinecart(DamageSource d)
    {
		
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeEntityToNBT(nbt);

		System.out.println("MineCart NBT:" + nbt);
        this.setDead();
        ItemStack itemstack = new ItemStack(Items.minecart, 1);
        Block b = Blocks.mob_spawner;
        if(nbt.hasKey("DisplayTile"))
        {
        	int blockid = nbt.getInteger("DisplayTile");
        	if(blockid != 0 && Util.isBlock(Block.getBlockById(blockid), MainJava.SpawnerList))
        		b = Block.getBlockById(blockid);
        }

		if(d.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)d.getEntity();
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack != null)
			{
				if(Util.isHarvestLevel(b, stack) && Util.isEnchantSilky(b, stack))
				{
					ItemStack s = new ItemStack(MainJava.minecartSpawner);
					nbt.setString("id", nbt.getString("TileId"));
					nbt.removeTag("TileId");
					s.setTagCompound(nbt);
					this.entityDropItem(s, 0.0F);
					return;
				}
			}
		}
		//Fix if Dropping regular mob spanwer to always try and get the default mob spawner rather then choco mob spawner
        if(nbt.hasKey("DisplayTile"))
        {
        	int blockid = nbt.getInteger("DisplayTile");
    	if(Block.getBlockById(blockid) == MainJava.chocoSpawner)
    		b = Blocks.mob_spawner;
        }
    	
        ItemStack stack = new ItemStack(b);
        stack = Util.swapNEIStack(b, nbt, WorldLoadEvent.world2, 0);
        Util.removeSpawnerTags(nbt);
        Util.addNames(nbt, stack, WorldLoadEvent.world2, null);
		nbt.removeTag("CustomDisplayTile");
		nbt.removeTag("DisplayTile");
		nbt.removeTag("DisplayOffset");
		nbt.removeTag("DisplayData");
		nbt.setString("id", nbt.getString("TileId"));
		nbt.removeTag("TileId");
        stack.setTagCompound(nbt);
        
        this.entityDropItem(itemstack, 0.0F);
        this.entityDropItem(stack, 0.0F);
    }
	
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        if(!nbt.getString("TileId").equals(""))
        	this.Tileid = nbt.getString("TileId");
        super.readEntityFromNBT(nbt);

        System.out.println("READNBT:" + this.Tileid);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
    	super.writeEntityToNBT(nbt);
    	nbt.setString("TileId", this.Tileid);
    	System.out.println("WriteNBT:" + nbt.getString("TileId"));
    }


}

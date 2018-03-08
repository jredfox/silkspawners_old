package com.EvilNotch.silkspawners;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EBlock extends Block{
	protected static ThreadLocal<Boolean> captureDrops1 = new ThreadLocal<Boolean>()
    {
        @Override protected Boolean initialValue() { return false; }
    };
    protected static ThreadLocal<List<ItemStack>> capturedDrops = new ThreadLocal<List<ItemStack>>()
    {
        @Override protected List<ItemStack> initialValue() { return new ArrayList<ItemStack>(); }
    };
    protected List<ItemStack> captureDrops(boolean start)
    {
        if (start)
        {
            captureDrops.set(true);
            capturedDrops.get().clear();
            return null;
        }
        else
        {
            captureDrops.set(false);
            return capturedDrops.get();
        }
    }
	

	protected EBlock(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	public static void DropBlock(World world, int x, int y, int z, ItemStack stack)
    {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            if (captureDrops1.get())
            {
                capturedDrops.get().add(stack);
                return;
            }
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }
	
	  

}

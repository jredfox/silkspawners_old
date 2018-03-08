package com.EvilNotch.silkspawners;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.creativetab.CreativeTabs;

public class DungeonSpawner extends BlockMobSpawner{

	DungeonSpawner(String name)
	{
		this.setBlockName(name);
		this.setHardness(5.0F);
		this.setStepSound(soundTypeMetal);
		this.setHarvestLevel("pickaxe", 0);
		this.disableStats();
		this.setCreativeTab(CreativeTabs.tabRedstone);
		
	}
	
	/**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

	
	
}

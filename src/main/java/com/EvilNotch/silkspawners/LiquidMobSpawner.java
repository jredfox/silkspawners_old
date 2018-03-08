package com.EvilNotch.silkspawners;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

public class LiquidMobSpawner extends BlockLiquid implements ITileEntityProvider{

	LiquidMobSpawner(Material mat, String name)
	{
		super(mat);
		this.setBlockName(name);
		this.setHardness(100.0F);
		this.setStepSound(soundTypeMetal);
		this.setHarvestLevel("pickaxe", 0);
		this.disableStats();
		this.setLightOpacity(3);
		this.disableStats();
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	  /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityMobSpawner();
    }

}

package com.EvilNotch.silkspawners;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class ForgedFence extends BlockPane{

	protected ForgedFence(String name, String name1, Material mat, boolean a) {
		super(name, name1, mat, a);
		this.setHardness(5.0F);
		this.setHarvestLevel("pickaxe", 1);
		this.setStepSound(soundTypeMetal);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		// TODO Auto-generated constructor stub
	}

}

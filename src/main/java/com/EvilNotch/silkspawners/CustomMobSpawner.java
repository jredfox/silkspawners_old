package com.EvilNotch.silkspawners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.EvilNotch.silkspawners.util.Util;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CustomMobSpawner extends BlockMobSpawner {


	protected CustomMobSpawner(String name) {
		super();
		this.setBlockName(name);
		this.setHardness(5.0F);
		this.setStepSound(soundTypeMetal);
		this.setHarvestLevel("pickaxe", 0);
		this.disableStats();
		this.setCreativeTab(CreativeTabs.tabRedstone);
		
	}
	 
	
	

}

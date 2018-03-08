package com.EvilNotch.silkspawners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CustomBucket extends ItemBucket{

	public CustomBucket(Block block) {
		super(block);
		this.setHasSubtypes(true);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String a = ("" + StatCollector.translateToLocal(stack.getUnlocalizedName() + ".name")).trim();
        if (stack.getTagCompound() == null || stack.getTagCompound().getString("EntityId") == "" || stack.getTagCompound().getString("EntityId") == null)
        {
            return a;
        }
        return SilkSpawners.getJockey(stack.getTagCompound(), stack.getTagCompound().getString("EntityId"),stack) + " " + a;
    }
	
	 @Override
	 @SideOnly(Side.CLIENT)
	 public void getSubItems(Item item, CreativeTabs p_150895_2_, List p_150895_3_)
	 {
		ItemStack jk = new ItemStack(MainJava.water_spawner_bucket);
		ItemStack bucket = new ItemStack(Items.bucket);
		ItemStack jkml = new ItemStack(MainJava.lava_spawner_bucket);
		jk.addEnchantment(Enchantment.silkTouch, 1);
		jkml.addEnchantment(Enchantment.silkTouch, 1);
		bucket.addEnchantment(Enchantment.silkTouch, 1);
		if (item == MainJava.water_spawner_bucket)
		{
			p_150895_3_.add(jk);
			p_150895_3_.add(bucket);
		}
		if (item == MainJava.lava_spawner_bucket){
			p_150895_3_.add(jkml);
		}
		
	 }
	 
	 @Override
	 @SideOnly(Side.CLIENT)
	    public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
		 if (stack.getTagCompound() == null)
    	 {
    		 return;
    	 }
    	 if (stack.getTagCompound().getString("modid") == "")
    	 {
    		 return;
    	 }
		 
		 if (stack.getTagCompound().getString("modid") != "")
		 {
			 String modid = stack.getTagCompound().getString("modid");
			 String tooltip = EnumChatFormatting.BLUE + "" + EnumChatFormatting.ITALIC + modid;
			 list.add(tooltip);
		 }
	 }
		 
		    //Doesn't Try To Place Liquid
		    @Override
		    public boolean tryPlaceContainedLiquid(World p_77875_1_, int p_77875_2_, int p_77875_3_, int p_77875_4_)
		    {
		         return false;
		    }
		     
		 
		 
		    

}

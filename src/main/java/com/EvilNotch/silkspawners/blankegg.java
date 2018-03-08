package com.EvilNotch.silkspawners;

import java.util.List;

import com.EvilNotch.silkspawners.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class blankegg extends Item {
	
	blankegg(String name)
	{
		this.setUnlocalizedName(name);
		this.setTextureName(MainJava.modid + name);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getTagCompound() == null || stack.getTagCompound().getString("EntityId") == "" || stack.getTagCompound().getString("EntityId") == null)
        {
            String a = ("" + StatCollector.translateToLocal(stack.getItem().getUnlocalizedName() + ".name")).trim();
            return a;
        }
        return Util.getItemTranslation(SilkSpawners.getNBT(stack), stack, false);
    }
	
	 @Override
	 @SideOnly(Side.CLIENT)
	 public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) 
	 {
		 SilkSpawners.addEggToolTip(stack, list, b);
	 }

}

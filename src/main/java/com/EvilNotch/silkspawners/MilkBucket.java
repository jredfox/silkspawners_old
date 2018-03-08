package com.EvilNotch.silkspawners;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MilkBucket extends Item{

	
	public MilkBucket()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

        if (!world.isRemote)
        {
            player.curePotionEffects(new ItemStack(Items.milk_bucket));
        }
        ItemStack bucket = new ItemStack(Items.bucket);
        bucket.addEnchantment(Enchantment.silkTouch, 1);

        return stack.stackSize <= 0 ? bucket : stack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.drink;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack stack, World p_77659_2_, EntityPlayer player)
    {
        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }
}

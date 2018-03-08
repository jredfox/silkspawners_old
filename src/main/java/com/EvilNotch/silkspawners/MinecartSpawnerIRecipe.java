package com.EvilNotch.silkspawners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.EvilNotch.silkspawners.util.Util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MinecartSpawnerIRecipe implements IRecipe
{
	public ItemStack spawner;
	public ItemStack minecartSpawner;
	/**Detects If a vanilla minecart and a spawner block is in there
	 * 
	 */
	@Override
	public boolean matches(InventoryCrafting i, World w) 
	{	
		boolean minecart = false;
		boolean spawner = false;
		for(int j=0;j<i.getSizeInventory();j++)
		{
			ItemStack slot = (ItemStack)i.getStackInSlot(j);
			if(slot != null)
			{
				Item item = slot.getItem();
				boolean test = Util.isSpawner(Block.getBlockFromItem(item),null);
				if(item != null && item != Item.getItemFromBlock(Blocks.air) && item != Items.minecart && !test)
					return false;
				if(item == Items.minecart)
				{
					if(minecart)
						return false;
					minecart = true;
				}
				if(test)
				{
					NBTTagCompound nbt = SilkSpawners.getNBT(slot);
					if(!Util.getEntityId(nbt).equals("") && nbt.getString("SilkTileEntity").equals("") && item != Item.getItemFromBlock(Blocks.mob_spawner) || item == Item.getItemFromBlock(Blocks.mob_spawner))
					{
						if(spawner)
							return false;
						spawner = true;
						this.spawner = slot;
					}
				}
			}
		}
		return minecart && spawner;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) 
	{
		ItemStack stack = this.spawner;
		ItemStack resault = new ItemStack(MainJava.minecartSpawner);
		if(stack == null)
			return null;
		if(stack.getTagCompound() == null && stack.getItem() != Item.getItemFromBlock(Blocks.mob_spawner))
		{
			return new ItemStack(MainJava.minecartSpawner);
		}
		if(Item.getItemFromBlock(Blocks.mob_spawner) == this.spawner.getItem() && config.cross_nei_compat == true && stack.getTagCompound() == null)
		{
			if(this.spawner.getItemDamage() == 0)
				return new ItemStack(MainJava.minecartSpawner);
			String entity = Util.getEntNEI(spawner.getItemDamage());
			if(!entity.equals(""))
			{
				NBTTagCompound nbt2 = new NBTTagCompound();
				nbt2.setString("EntityId",entity);
				nbt2.setBoolean("CustomDisplayTile", true);
				nbt2.setInteger("DisplayTile", Block.getIdFromBlock(MainJava.chocoSpawner));
				nbt2.setInteger("DisplayOffset", 6);
				nbt2.removeTag("display");
				ItemStack sub = new ItemStack(MainJava.minecartSpawner);
				sub.setTagCompound(nbt2);
				return sub;
			}
			else
				return new ItemStack(MainJava.minecartSpawner);
		}
		// {CustomDisplayTile:1,DisplayTile:354,DisplayOffset:0}
		Block block = Block.getBlockFromItem(stack.getItem());
		if(block == Blocks.mob_spawner)
			block = MainJava.chocoSpawner;
		NBTTagCompound nbt = SilkSpawners.getNBT(stack);
		nbt.setBoolean("CustomDisplayTile", true);
		nbt.setInteger("DisplayTile", Block.getIdFromBlock(block));
		nbt.setInteger("DisplayOffset", 6);
		nbt.removeTag("display");
		resault.setTagCompound(nbt);
		System.out.println("Crafted:" + nbt);
		this.minecartSpawner = resault;
		return resault;
	}

	@Override
	public int getRecipeSize() 
	{
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput() 
	{
		return this.minecartSpawner;
	}
	
}

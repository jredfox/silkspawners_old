package com.EvilNotch.silkspawners;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class EChecker {
	
	 /**
     * Returns the enchantment and level of enchantment on the ItemStack passed.
     *  
     * @param enchID The ID for the enchantment you are looking for.
     * @param stack The ItemStack being searched.
     */
    public static String getEnchantmentLevel(int enchID, ItemStack stack)
    {
        if (stack == null)
        {
            return "";
        }
        else
        {
            NBTTagList nbttaglist = stack.getEnchantmentTagList();

            if (nbttaglist == null)
            {
                return "";
            }
            else
            {
                for (int j = 0; j < nbttaglist.tagCount(); ++j)
                {
                    short short1 = nbttaglist.getCompoundTagAt(j).getShort("id");
                    short short2 = nbttaglist.getCompoundTagAt(j).getShort("lvl");

                    if (short1 == enchID)
                    {
                    	String id =  String.valueOf(short1);
                    	String lvl = String.valueOf(short2);
                        return id + "\u00A9" + lvl;
                    }
                }

                return "";
            }
        }
    }

}

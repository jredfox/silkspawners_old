package com.EvilNotch.silkspawners;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Book_capture extends Item {
	
		public Book_capture(String s)
		{
			this.setUnlocalizedName(s);
			this.setTextureName("silkspawners:" + s);
			this.setHasSubtypes(true);
			this.setCreativeTab(CreativeTabs.tabRedstone);
		}

}

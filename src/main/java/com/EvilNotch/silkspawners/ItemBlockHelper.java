package com.EvilNotch.silkspawners;



import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.EvilNotch.silkspawners.util.LineObj;
import com.EvilNotch.silkspawners.util.Util;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ItemBlockHelper extends ItemBlock
{

    public ItemBlockHelper(Block par1)
    {
        super(par1);
        this.setHasSubtypes(true);
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String s = ("" + StatCollector.translateToLocal(Blocks.mob_spawner.getUnlocalizedName() + ".name")).trim();
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);
        if (stack.getTagCompound() == null || stack.getTagCompound().getString("EntityId") == "")
        {
            String a = ("" + StatCollector.translateToLocal(Blocks.mob_spawner.getUnlocalizedName() + ".name")).trim();
            String b = "";
            if (block == MainJava.CustomMobSpawner)
            {
            	 b = "Forge " + a; 
            }
            else if (block == MainJava.CaveSpawner)
            {
            	b = "Cave " + a;
            }
            else if (block == MainJava.End_Supreme_Spawner)
            {
            	b = "Ender Supreme " + a;
            }
            else  if (block == MainJava.EndSpawner)
            {
            	b = "Ender " + a;
            }
            else if (block == MainJava.ForestSpawner)
            {
            	b = "Forest " + a;
            }
            else if (block == MainJava.IceSpawner)
            {
            	b = "Ice " + a;
            }
            else if (block == MainJava.LavaSpawner)
            {
            	b = "Lava " + a;
            }
            else if (block == MainJava.NetherSpawner)
            {
            	b = "Nether " + a;
            }
            else if (block == MainJava.RustedSpawner)
            {
            	b = "Rusted Cage";
            }
            else if (block == MainJava.SnowSpawner)
            {
            	b = "Snow " + a;
            }
            else if (block == MainJava.WaterSpawner)
            {
            	b = "Water " + a;
            }
            else if (block == MainJava.chocoSpawner)
            {
            	b = a;
            }
            return b;
        }
        if (stack.getTagCompound() != null && stack.getTagCompound().getString("EntityId") != "")
        {
        	return Util.getItemTranslation(SilkSpawners.getNBT(stack), stack, true);
        }
 
        return s;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs p_150895_2_, List p_149666_3_)
	{
    	if(item != Item.getItemFromBlock(MainJava.CustomMobSpawner))
    		return;
 		SilkSpawners.genHashMaps(); //Generates All HashMaps if they are not empty
 		
	  Iterator iterator = SilkSpawners.mob_spawners.entrySet().iterator();
	  
	  	ItemStack forge = new ItemStack(MainJava.CustomMobSpawner);
	  	p_149666_3_.add(forge);
	  	
        while (iterator.hasNext())
        {
            Map.Entry pair2 = (Map.Entry)iterator.next();
            String it = pair2.getKey().toString();
            String Name = pair2.getValue().toString();
        	
 	        NBTTagCompound nbt = new NBTTagCompound();
 	        NBTTagCompound local = new NBTTagCompound();
            Entity entity = EntityList.createEntityByName(it, WorldLoadEvent.world2);
            
            if(entity != null)
            {
            	String raw = EntityLookUp.getEntityMod(entity);
            	String[] parts = raw.split("\u00A9");
            	String modname = parts[0];
            	nbt.setString("EntityId", it);
            	nbt.setString("modid", modname);
            	ItemStack jk = new ItemStack(MainJava.chocoSpawner, 1, 0);
 	        
 	        if (!Loader.isModLoaded("NotEnoughItems"))
 	        {
 	        	ItemStack nei = new ItemStack(Blocks.mob_spawner);
 	        	jk = nei;
 	        	jk.setTagCompound(nbt);
 	        	Util.addNames(nbt, jk, WorldLoadEvent.world2, null);
 	        	p_149666_3_.add(jk);
 	        }
 	       if (Loader.isModLoaded("NotEnoughItems"))
 	       {  
 	    		  int globalID = EntityList.getEntityID(entity);
 	    		  jk.setTagCompound(nbt);
 	    		  
 	    		  if (globalID > 0)
 	    		  {
 	    			if (config.NEIGlobalIdForgeSpawners == true || it.equals("EnderDragon")) //Check ender dragon since nie hardcoded to remove it
 	    			{
 	    				p_149666_3_.add(jk);
 	    			}
 	    		  }
			   
 	    		  else{
 	    			  	p_149666_3_.add(jk);
 	    		  	 }
			}
 	        
 	        
 	      

 	        }
        }
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean torf)
    {

    	 if (stack == null || stack.getTagCompound() == null)
    	 {
    		 return;
    	 }
    	 if (stack.getTagCompound().getString("modid") == "")
    	 {
    		 return;
    	 }
    	
    	 String tooltip = EnumChatFormatting.BLUE + "" + EnumChatFormatting.ITALIC + SilkSpawners.getJockeyModName(stack);
        
     }
    
}
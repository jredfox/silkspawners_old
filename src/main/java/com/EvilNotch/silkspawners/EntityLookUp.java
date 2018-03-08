package com.EvilNotch.silkspawners;

import java.util.*;
import java.util.Map.Entry;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.Entity;

public class EntityLookUp {
	
	
	//couldn't seem to get method anywhere copied from waila.  This is my only copied code for the mod
	//Choonster helped me getting started but, ultimately except for this I got from figuring out..... :(
	
	
	 public static String getEntityMod(Entity entity){
	    	String modName = "";
	    	String modid = "";
	    	try{
	    		EntityRegistration er = EntityRegistry.instance().lookupModSpawn(entity.getClass(), true);
	    		ModContainer modC = er.getContainer();
	    		modName = modC.getName();
	    		modid = modC.getModId();
	    	} catch (NullPointerException e){
	    		modName = "Minecraft";
	    		modid = "minecraft";
	    	}
			return modName + "\u00A9" + modid;
	 }
	 public static String getModName(String par1Str)
	    {
	        for (ModContainer mod : Loader.instance().getModList())
	        {
	            if (mod.getModId().equals(par1Str) )
	            {
	                return mod.getName();
	            }
	        }
	        return "Minecraft";
	    }
	 
	 

	  public static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }

	 
	   
}

package com.EvilNotch.silkspawners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.AnvilUpdateEvent;

public class AnvilEventHandler {
	
	public static Map<Integer, Integer> leftItemHash = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> rightItemHash = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> outputhash = new HashMap<Integer, Integer>();
		
			//Forge Your Eggs Anvil Recipes Here...
			@SubscribeEvent
			public void AnvilForgeEgg(AnvilUpdateEvent event)
			{
				ItemStack left = event.left;
				ItemStack right = event.right;
				event.cost = left.stackSize;

				//Creation of a Forge Egg
				if (left.getItem() == MainJava.forge_egg_blank && right.getItem() == Items.lava_bucket)
				{
					ItemStack output = new ItemStack(MainJava.forge_egg,left.stackSize,0);
					event.cost += SilkSpawners.getTagList(left, "Mounting", 10).tagCount();
					if (left.getTagCompound() != null)
					{
						output.setTagCompound(left.getTagCompound());
					}
					output.stackSize = left.stackSize;
					event.output = output;

				}

				boolean clear = false;
				if (SilkSpawners.isEgg(left) && right.getItem() == Items.gold_ingot)
					clear = true;
				
				//clears all egg types based on gold ingots
				if (clear == true)
				{
					if (left.getTagCompound() != null)
					{
						if (left.stackSize > right.stackSize)
							return;
						event.cost += SilkSpawners.getTagList(left, "Mounting", 10).tagCount();
						event.cost += left.stackSize;
						event.materialCost = left.stackSize;
						ItemStack output = new ItemStack(left.getItem());
						output.stackSize = left.stackSize;
						event.output = output;
					}
				}
				
				//Modifier To get more of entities tier 2.....
				if (left.getItem() == MainJava.forge_egg_blank && right.getItem() == Items.ender_eye && left.stackSize <= right.stackSize)
				{	
					ItemStack output = new ItemStack(MainJava.forge_egg_blank);
					NBTTagCompound nbt = new NBTTagCompound();
					
					if (left.getTagCompound() != null)
					{
						nbt = (NBTTagCompound) left.getTagCompound().copy();
					}
					if(nbt.getInteger("ForgeLvl") + 1 < 5)
					{
						if(nbt.hasKey("ForgeLvl"))
							nbt.setInteger("ForgeLvl", nbt.getInteger("ForgeLvl") + 1);
						else
							nbt.setInteger("ForgeLvl",2);
						event.cost += SilkSpawners.getTagList(left, "Mounting", 10).tagCount();
						event.materialCost = left.stackSize;
						output.setTagCompound(nbt);
						output.stackSize = left.stackSize;
						event.output = output;
					}
				}
				//Creation of a Caged Egg
				if (left.getItem() == MainJava.caged_egg_blank && right.getItem() == Items.lava_bucket)
				{
					ItemStack output = new ItemStack(MainJava.caged_egg,left.stackSize,0);
					if (left.getTagCompound() != null)
					{
						output.setTagCompound(left.getTagCompound());
						event.cost += SilkSpawners.getTagList(left, "Mounting", 10).tagCount();
					}
					output.stackSize = left.stackSize;
					event.output = output;
				}
				//Creation of a Water Egg
				if (left.getItem() == MainJava.forge_egg_blank && right.getItem() == Items.water_bucket)
				{
					ItemStack output = new ItemStack(MainJava.egg_water);
					if (left.getTagCompound() != null)
					{
						output.setTagCompound(left.getTagCompound());
						event.cost += SilkSpawners.getTagList(left, "Mounting", 10).tagCount();
					}

					output.stackSize = left.stackSize;
					event.output = output;
				}

				//Silk Buckets recipe if override enchantments is off....
				if (config.OverrideEnchants == false && left.getItem() == Items.bucket && right.getItem() == Items.enchanted_book && left.stackSize == 1)
				{
						if (right.getTagCompound() != null && right.getTagCompound().getTagList("StoredEnchantments", 10) != null)
						{
						int echeck = EnchantmentHelper.getEnchantmentLevel(33, left);
						if (echeck == 0)
						{
							//get enchanted books working...
							ItemStack bucket = ItemStack.copyItemStack(left);
							if (bucket.getTagCompound() == null)
							{
								bucket.setTagCompound(new NBTTagCompound());
							}
							if (!bucket.stackTagCompound.hasKey("ench", 9))
						    {
								bucket.stackTagCompound.setTag("ench", new NBTTagList());
						    }
							NBTTagList rl = new NBTTagList();
							if (right.getTagCompound().getTagList("StoredEnchantments", 10) != null)
							{
								rl = (NBTTagList)right.getTagCompound().getTagList("StoredEnchantments", 10).copy();
							}
							NBTTagCompound kml = new NBTTagCompound();
							kml.setTag("ench", rl);
							ItemStack tst = new ItemStack(Items.apple);
							tst.setTagCompound(kml);
							if (rl != null && EnchantmentHelper.getEnchantmentLevel(33, tst) > 0)
							{
							  NBTTagList nbttaglist = bucket.stackTagCompound.getTagList("ench", 10);
							  NBTTagCompound k = new NBTTagCompound();
							  k.setInteger("id", 33);
							  k.setInteger("lvl", 1);
							  nbttaglist.appendTag(k);
							  bucket.stackSize = left.stackSize;
							  event.output = bucket;
							  
							}
						}
					}
				}
				
				//Forge Monster Spawner Recipe
				Item spawner = Item.getItemFromBlock(Blocks.mob_spawner);
				Item forge = Item.getItemFromBlock(MainJava.CustomMobSpawner);
				Item dirt = Item.getItemFromBlock(Blocks.dirt);
					if (left.getItem() == spawner && right.getItem() == Items.lava_bucket)
					{
						if (config.ForgeSpawners == true)
						{
							ItemStack nbtsaver = new ItemStack(MainJava.CustomMobSpawner,left.stackSize,0);
							NBTTagCompound preserve = left.getTagCompound();
							nbtsaver.setTagCompound(preserve);
							nbtsaver.stackSize = left.stackSize;
							event.output = nbtsaver;
						}
					}
					
					//Dirty Forge Spawner
					if (left.getItem() == forge && right.getItem() == dirt)
					{
						if (config.ForgeSpawners == true)
						{
							ItemStack nbtsaver = new ItemStack(Blocks.mob_spawner,left.stackSize,0);
							NBTTagCompound preserve = left.getTagCompound();
							nbtsaver.setTagCompound(preserve);
							nbtsaver.stackSize = left.stackSize;
							if (!(right.stackSize < left.stackSize))
							{
								event.output = nbtsaver;
							}
						}
					}
					//Fixes NBT 1.7.10 issues with deleting nbt data for Monster Spawners
					if (left.getItem() == spawner && right.getItem() == null || left.getItem() == forge && right.getItem() == null)
					{
						ItemStack nbtsaver = new ItemStack(Blocks.mob_spawner,left.stackSize,0);
						NBTTagCompound preserve = left.getTagCompound();
						nbtsaver.setTagCompound(preserve);
						event.output = nbtsaver;
					}
					
					//Creation of Soul Eggs
					if (left.getItem() == MainJava.egg_water && right.getItem() == Items.enchanted_book || left.getItem() == MainJava.forge_egg && right.getItem() == Items.enchanted_book)
					{
						NBTTagList a = right.getTagCompound().getTagList("StoredEnchantments", 10);
						boolean aqua = false;
						boolean fire = false;
						//Returns From Method if isn't certain egg recipes....
						if (left.stackSize > 1)
						{
							return;
						}
						if (a != null)
						{
						for (int j = 0; j < a.tagCount(); ++j)
		                {
		                    int short1 = a.getCompoundTagAt(j).getInteger("id");
		                    int short2 = a.getCompoundTagAt(j).getInteger("lvl");
		                    if (short1 == 6 || short1 == 61 || short1 == 62)
		                    {
		                    	aqua = true;
		                    }
		                    if (short1 == 1 || short1 == 20 || short1 == 50)
		                    {
		                    	fire = true;
		                    }
		                }
						if (aqua == true && fire == false && left.getItem() == MainJava.egg_water)
						{
							ItemStack bluesoul = new ItemStack(MainJava.egg_blue_soul);
							if (left.getTagCompound() != null)
							{
								bluesoul.setTagCompound(left.getTagCompound());
							}
							bluesoul.stackSize = left.stackSize;
							event.output = bluesoul;
							event.cost += SilkSpawners.getTagList(left, "Mounting", 10).tagCount() *2;
						}
						if (fire == true && aqua == false && left.getItem() == MainJava.forge_egg)
						{
							ItemStack enchantedsoul = new ItemStack(MainJava.enchanted_egg);
							if (left.getTagCompound() != null)
							{
								enchantedsoul.setTagCompound(left.getTagCompound());
							}
							enchantedsoul.stackSize = left.stackSize;
							event.output = enchantedsoul;
							event.cost += SilkSpawners.getTagList(left, "Mounting", 10).tagCount() * 2;
						}
						
					}
				}
					
					//Creation of a blue soul bucket or enchanted soul bucket
					if (left.getItem() == MainJava.egg_blue_soul && right.getItem() == Items.water_bucket || left.getItem() == MainJava.enchanted_egg && right.getItem() == Items.lava_bucket)
					{
						//Returns From Method if isn't certain egg recipes....
						if (left.stackSize > 1)
						{
							return;
						}
						ItemStack output = new ItemStack(MainJava.water_spawner_bucket);
						if (left.getItem() == MainJava.enchanted_egg)
						{
							ItemStack a = new ItemStack(MainJava.lava_spawner_bucket);
							output = a;
							
						}
						
						if (left.getTagCompound() != null)
						{
								NBTTagCompound nbt = (NBTTagCompound) left.getTagCompound().copy();
								NBTTagList list = new NBTTagList();
								NBTTagCompound nbt2 = new NBTTagCompound();
								nbt2.setInteger("id", 33);
								nbt2.setInteger("lvl", 1);
								list.appendTag(nbt2);
								nbt.setTag("ench", list);
								output.setTagCompound(nbt);
						}
						else{
							NBTTagCompound nbt = new NBTTagCompound();
							NBTTagCompound nbt2 = new NBTTagCompound();
							NBTTagList list = new NBTTagList();
							nbt.setInteger("id", 33);
							nbt.setInteger("lvl", 1);
							list.appendTag(nbt);
							nbt2.setTag("ench", list);
							nbt2.setString("EntityId", "Blank");
							output.setTagCompound(nbt2);
						}
						event.output = output;
						event.cost = SilkSpawners.anvilGetCost(left, right, false);
					}
					
				
					//Combines forge eggs
					if (SilkSpawners.isEgg(left) == true && SilkSpawners.isEgg(right) == true)
					{
						//Sets Tag Compounds and cost/material
						boolean combine = false;
						
						event.cost = SilkSpawners.anvilGetCost(left, right, combine);
						event.materialCost = left.stackSize;
						
						if (combine == false && left.stackSize > right.stackSize)
						return;
						
						NBTTagCompound out_nbt = SilkSpawners.combineEggRecipe(left, right, combine); //gets combined nbt of eggs boolean is to combine all if true
						if (out_nbt == null)
						{
							return;	//if doesn't combine stop continuing method
						}
						ItemStack output = new ItemStack(left.getItem());
						output.setTagCompound(out_nbt);
						
						if (combine == false)
						{
							event.materialCost = left.stackSize;
							output.stackSize = left.stackSize;
						}
						else{
							output.stackSize = 1;
							event.materialCost = right.stackSize;
						}						
						event.output = output;
					}
					
					//Vanilla Egg Compatibility 
					if (SilkSpawners.isEgg(left) == true && right.getItem() == Items.spawn_egg)
					{
						boolean combine = false;
						event.cost = SilkSpawners.anvilGetCost(left, right, combine);
						if (combine == false && left.stackSize > right.stackSize)
							return;
						
						NBTTagCompound out_nbt = SilkSpawners.combineVanilla(left, right, combine);
						if(out_nbt == null)
							return;
						ItemStack output = new ItemStack(left.getItem());
						output.setTagCompound(out_nbt);
						if (combine == true)
						{
							output.stackSize = 1;
							event.materialCost = right.stackSize;
						}
						else{
							output.stackSize = left.stackSize;
							event.materialCost = left.stackSize;
						}
						event.output = output;
					}	
			}
			
			//Anvil Tweaks For Enchantment And Books
			@SubscribeEvent(priority = EventPriority.HIGHEST)
			public void Anvil(AnvilUpdateEvent event)
			{
				ItemStack left = event.left;
				ItemStack right = event.right;
				event.materialCost = 1;
				
				int level = 3;
				boolean isSilk = false;
				boolean flag = false;
				NBTTagCompound appendnbt2 = new NBTTagCompound();
				ItemStack stack = new ItemStack(left.getItem());
				NBTTagCompound pre = new NBTTagCompound();
				if (left.getTagCompound() != null)
				{
					pre = (NBTTagCompound)left.getTagCompound().copy();
				}
				ItemStack itemstack1 = ItemStack.copyItemStack(left);
				
				if (config.OverrideEnchants == false || left.stackSize > 1)
				{
					return;
				}
				if (right.getTagCompound() == null && !itemstack1.isItemStackDamageable() && left.getItem() != right.getItem())
				{
					return;
				}
				if (left.stackSize > 1)
				{
					return;
				}
				if (right.getItem() == Items.enchanted_book || itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(left, right) || left.getItem() == right.getItem() && itemstack1.isItemStackDamageable())
				{

					//UNIT REPAIR
	                if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(left, right))
	                {
	                   int k = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);

	                    if (k <= 0)
	                    {
	                        return;
	                    }
	                    int i = 0;
	                    int matcost = 0;
	                    int cost = 0;
	                    for (int l = 0; k > 0 && l < right.stackSize; ++l)
	                    {
	                        int i1 = itemstack1.getItemDamageForDisplay() - k;
	                        itemstack1.setItemDamage(i1);
	                        matcost += 1;
	                        cost += 1;
	                        k = Math.min(itemstack1.getItemDamageForDisplay(), itemstack1.getMaxDamage() / 4);
	                    }

	                   NBTTagList nbteL = new NBTTagList();
	                   ItemStack output = ItemStack.copyItemStack(left);
	                   if (left.getTagCompound() != null)
	                   {
	                	   output.setTagCompound(left.getTagCompound());
	                   }
	                   if (left.getEnchantmentTagList() != null)
	                   {
	                	   nbteL = left.getEnchantmentTagList();
	                	   for (int b=0;b< nbteL.tagCount(); b++)
	                	   {
	   	                       int short2 = nbteL.getCompoundTagAt(b).getInteger("lvl");
	   	                       int ab =  short2;
	   	                       cost += ab;
	                	   }
	                   }
	                   output.setItemDamage(itemstack1.getItemDamage());
	                   event.output = output;
	                   event.cost = cost;
	                   event.materialCost = matcost;
	                   return;
	                }
	                
	                	 int max = left.getMaxDamage();
	                	 boolean flag_repair = false;
	                	 int repair = 0;
	    				 if (itemstack1.isItemStackDamageable() && left.getItem() == right.getItem())
	    				 {
	    				 int j = left.getMaxDamage() - left.getItemDamage();
	    				 int k = right.getMaxDamage() - right.getItemDamage();
	    				 int l = k + itemstack1.getMaxDamage() * 12 / 100;
	    				 int i1 = j + l;
	    				 if (i1 > max)
	    				 {
	    					 i1 = max;
	    				 }
	    				 repair = max - i1;
	    				 flag_repair = true;
	    				  }
	    			
					
						//Returning methods
					 	if (left.getItem() == Items.enchanted_book || left.getItem() == Items.enchanted_book)
					 	{
						 	if (left.getTagCompound() == null && left.getItem() == Items.enchanted_book|| left.getItem() == Items.enchanted_book && right.getTagCompound() == null)
							{
								return;
							}
							if (left.getItem() == Items.enchanted_book && left.getTagCompound().getTagList("StoredEnchantments", 10) == null || right.getItem() == Items.enchanted_book && right.getTagCompound().getTagList("StoredEnchantments", 10) == null)
							{
								return;
							}
					 	}
					NBTTagCompound nbt = right.getTagCompound();
					NBTTagList nbttaglist = new NBTTagList();
					NBTTagList Llist = new NBTTagList();
					
					if (left.getItem() == Items.enchanted_book)
					{
						Llist= left.getTagCompound().getTagList("StoredEnchantments", 10);
					}
					if (left.getItem() != Items.enchanted_book)
					{
						Llist = left.getEnchantmentTagList();
					}
					//System.out.println(Llist);
					NBTTagList appendlist = new NBTTagList();

					if (right.getItem() == Items.enchanted_book)
					{
						nbttaglist = nbt.getTagList("StoredEnchantments", 10);
					}
					if (right.getItem() != Items.enchanted_book)
					{
						nbttaglist = right.getEnchantmentTagList();
					}
					
					if (nbttaglist != null)
					{
					for (int j = 0; j < nbttaglist.tagCount(); ++j)
	                {
	                    int short1 = nbttaglist.getCompoundTagAt(j).getInteger("id");
	                    int short2 = nbttaglist.getCompoundTagAt(j).getInteger("lvl");
	                    boolean flage = false;
	                    Enchantment e1 = Enchantment.enchantmentsList[short1];
	                     	if (Llist != null)
	                     	{
	                	   	for (int i = 0; i < Llist.tagCount(); ++i)
	                		{
								int Lshort1 = Llist.getCompoundTagAt(i).getInteger("id");
								int Lshort2 = Llist.getCompoundTagAt(i).getInteger("lvl");
							    Enchantment e2 = Enchantment.enchantmentsList[Lshort1];
							     if (config.VanillaLogicEnchants == true)
							     {  
								    if ( !(e2.canApplyTogether(e1) && e1.canApplyTogether(e2)) )
									{
										flage = true;
									}
				                 }

							     //Prevents silk touch and fortune being on the same pick as that causes issues with vanilla and modded
							     if (Lshort1 == 35 && short1 == 33 || Lshort1 == 33 && short1 == 35)
							     {
							    	
							    	 flage = true;
							     }
							     
							     if (config.FireandWaterEnchants == false)
							     {
							    	 //Prevents God Pickaxe's Harvesting Both Water and Lava Spawner ... {Enchantment api that two enchantments can't go with each other soon?}....
							    	
							    	 //__________________________AQUA_____________________________________________________________________________________________________________________________________________________________\\
							    	if (Lshort1 == 6 && short1 == 20 || Lshort1 == 20 && short1 == 6 || Lshort1 == 6 && short1 == 50 || Lshort1 == 50 && short1 == 6 || Lshort1 == 6 && short1 == 1 || Lshort1 == 1 && short1 == 6)
							    	{
							    		 flage = true;
							    	}
							    	
							    	//__________________________LUCK OF SEA_____________________________________________________________________________________________________________________________________________________________\\
							    	if (Lshort1 == 61 && short1 == 20 || Lshort1 == 20 && short1 == 61 || Lshort1 == 61 && short1 == 50 || Lshort1 == 50 && short1 == 61 || Lshort1 == 61 && short1 == 1 || Lshort1 == 1 && short1 == 61 )
							    	{
							    		 flage = true;
							    	}
							    	
							    	//__________________________LURE_____________________________________________________________________________________________________________________________________________________________\\
							    	if (Lshort1 == 62 && short1 == 20 || Lshort1 == 20 && short1 == 62 || Lshort1 == 62 && short1 == 50 || Lshort1 == 50 && short1 == 62 || Lshort1 == 62 && short1 == 1 || Lshort1 == 1 && short1 == 62 )
							    	{
							    		 flage = true;
							    	}
							     }
	                		  }
	                     	}

	                	   	if (flage == false)
	                	   	{
	                	   		rightItemHash.put(short1, short2);
	                	   	}
	                   }
					}
	                
					//start if list exists
					if (Llist != null)
					{
						for (int i = 0; i < Llist.tagCount(); ++i)
	                	{
							int Lshort1 = Llist.getCompoundTagAt(i).getInteger("id");
							int Lshort2 = Llist.getCompoundTagAt(i).getInteger("lvl");
	                    	leftItemHash.put(Lshort1, Lshort2);
	                	}
					}
					if (Llist == null && nbttaglist == null)
					{
						return;
					}
					
				//	System.out.println("LeftHash:" + leftItemHash + " RightHash:" + rightItemHash);
					
					//Right HashMap (Book)
					Map var_right = rightItemHash;
					Iterator itright = var_right.entrySet().iterator();
					while(itright.hasNext())
					{
						Map.Entry pair = (Map.Entry)itright.next();
						String strid = pair.getKey().toString();
						String strlvl = pair.getValue().toString();
						int id = Integer.parseInt(strid);
						int lvl = Integer.parseInt(strlvl);
						outputhash.put(id, lvl);
					}
					
					//Left HashMap(Specified Item)
					Map var_left = leftItemHash;
					Iterator itleft = var_left.entrySet().iterator();
					while (itleft.hasNext())
					{
						Map.Entry pair = (Map.Entry)itleft.next();
						String strid = pair.getKey().toString();
						String strlvl = pair.getValue().toString();
						int id = Integer.parseInt(strid);
						int lvl = Integer.parseInt(strlvl);
						if (!rightItemHash.containsKey(id))
						{
							outputhash.put(id, lvl);
						}
						if (rightItemHash.containsKey(id))
						{
							 int rightlevel = rightItemHash.get(id);
							 if (lvl > rightlevel)
							 {
								 outputhash.put(id, lvl); //preserves the nbt of the itemstack
							 }
							 if (lvl == rightlevel)
							 {
								 outputhash.put(id, lvl+1);
							 }
						}
					}
					//System.out.println("Left:" + leftItemHash + " Right:" + rightItemHash + " Output:" + outputhash);
					
					//appendlist;
					Map varoutput = outputhash;
					Iterator itout = varoutput.entrySet().iterator();
					while (itout.hasNext())
					{
						Map.Entry pair = (Map.Entry)itout.next();
						String strid = pair.getKey().toString();
						String strlvl = pair.getValue().toString();
						int id = Integer.parseInt(strid);
						int lvl = Integer.parseInt(strlvl);
						NBTTagCompound in = new NBTTagCompound();
						in.setInteger("id", id);
						in.setInteger("lvl", lvl);
						level += lvl;
						appendlist.appendTag(in);
					}
					//System.out.println("NBTTagList:" + appendlist);
					NBTTagCompound enchants = new NBTTagCompound();
					if (right.getItem() == Items.enchanted_book && left.getItem() == Items.enchanted_book)
					{
						enchants.setTag("StoredEnchantments", appendlist);
						pre.removeTag("StoredEnchantments");
						pre.setTag("StoredEnchantments", appendlist);
					}
					else{
						enchants.setTag("ench", appendlist);
						pre.removeTag("ench");
						pre.setTag("ench", appendlist);
					}
					//So no cheating by enchanting one thing and getting max durability back...
					if (left.isItemDamaged() == true)
					{
						int damage = left.getItemDamage();
						if (flag_repair == false)
						{
							stack.setItemDamage(damage);
						}
						else{
							stack.setItemDamage(repair);
						}
					}
					
					stack.setTagCompound(pre);
					event.output = stack;
					event.cost = level;
					rightItemHash.clear();
					leftItemHash.clear();
					outputhash.clear();
	
			}
		}
			
			
			
		
}

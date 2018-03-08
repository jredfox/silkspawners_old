package com.EvilNotch.silkspawners.util;

public class LineObj 
{
	public String[] enchants; //Format of <enchid:lvl,enchid:lvl...>
	private int head; //<modid:block = int> formatting
	private String strid = "null:null";
	private String modid = "";
	private String name = "";
	public boolean hasHead = true;
	public boolean isDynamic = false;
	
	public LineObj(String s)
	{
		try{
		if(s != null && !s.equals(""))
		{
			if(s.contains("="))
			{
					String[] parts = getParts(s,"=");
					this.strid = toWhiteSpaced(parts[0]); //Initializes Block names
					
					String[] id = getParts(this.strid,":");
					this.modid = id[0];
					this.name = id[1]; //Initializes modid:block into two seperate strings
					
					//Method for dynamic strings modid:block = 1:1,2:2 format || modid:block = 1:1 format
					if(parts[1].contains(","))
					{
						this.isDynamic = true;
						
						String[] epart = parts[1].split(",");
						this.enchants = new String[epart.length];
						for(int i=0;i<epart.length;i++)
						{
							this.enchants[i] = toWhiteSpaced(epart[i]);
						}
					}
					if(parts[1].contains(":") && !parts[1].contains(","))
					{
						this.isDynamic = true;
						
						this.enchants = new String[1];
						this.enchants[0] = toWhiteSpaced(parts[1]);
					}
					//Initializes The head if it has one in modid:block = int format
					if(!parts[1].contains(":") && !parts[1].contains(","))
						this.head = Integer.parseInt(toWhiteSpaced(parts[1]));
					
			}//Below is the formating lineObj without it's head
			else{
				this.strid = toWhiteSpaced(s);
				this.hasHead = false;
				
				String[] parts = null;
				if(s.contains(":"))
					parts = getParts(s,":");
				if(s.contains("\u00A9"))
				  parts = getParts(s,"\u00A9");
				
				  this.modid = parts[0];
				  this.name = parts[1];
			}
		 }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Ejects a string that is whitespaced
	 * @param s
	 * @return
	 */
	public static String toWhiteSpaced(String s)
	{
		return s.replaceAll("\\s+", "");
	}
	/**
	 * Separates a string dynamically supports vanilla format
	 * @param s
	 * @param split
	 * @return
	 */
	public static String[] getParts(String s, String split)
	{
		if(split.equals(":"))
		{
			s = s.replaceFirst(":", "\u00A9");
			return s.split("\u00A9");
		}
		return s.split(split);
	}
	/**
	 * Gets the head however returns -1 if doesn't have a head
	 * @return
	 */
	public int getHead()
	{
		return this.head; //Returns -1 if doesn't have a head
	}
	/**
	 * gets modid:block || integer:integer of the LineObj
	 * @return
	 */
	public String getStringId()
	{
		return this.strid;
	}
	/**
	 * gets the modid in the format of modid:block || first int in strid
	 * @return
	 */
	public String getmodid()
	{
		return this.modid;
	}
	/**
	 * gets the block in the format of modid:block || second int in strid
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	/**
	 * Gets index of enchants[] id
	 * @return
	 */
	public int getEnchantId(int index)
	{
		if(this.enchants.length > 0)
		{
			String s = this.enchants[index];
			String[] parts = getParts(s, ":");
				return Integer.parseInt(parts[0]);
		}
		else
			return -1;
	}
	/**
	 * Gets index of enchants[] level
	 * @return
	 */
	public int getEnchantLevel(int index)
	{
		if(this.enchants.length > 0)
		{
			String s = this.enchants[index];
			String[] parts = getParts(s, ":");
				return Integer.parseInt(parts[1]);
		}
		else
			return -1;
	}
	/**
	 * A string representation of the enchantments for the println method
	 * @return
	 */
	public String getEnchantString()
	{
		String s = "";
		for(int j=0;j<this.enchants.length;j++)
		{
			s += this.enchants[j];
			if(j+1 != this.enchants.length)
				s += ",";
		}
		return s;
	}
	/**Returns String Representation Based On If It Has Head To Didsplay
	 * 
	 */
	@Override
	public String toString()
	{
		if(this.hasHead && !this.isDynamic)
			return "[" + this.strid + " = " + this.head + "]";
		if(!this.isDynamic)
			return "[" + this.strid + "]";
		else{
			return "[" + this.strid + "=" + getEnchantString() + "]";
		}
	}

}
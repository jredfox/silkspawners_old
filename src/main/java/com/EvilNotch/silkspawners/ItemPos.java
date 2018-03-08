package com.EvilNotch.silkspawners;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemPos extends Item{
	
	 public static MovingObjectPosition getMovingObjectPositionFromPlayer1(World p_77621_1_, EntityPlayer p_77621_2_, boolean p_77621_3_)
	    {
	        float f = 1.0F;
	        float f1 = p_77621_2_.prevRotationPitch + (p_77621_2_.rotationPitch - p_77621_2_.prevRotationPitch) * f;
	        float f2 = p_77621_2_.prevRotationYaw + (p_77621_2_.rotationYaw - p_77621_2_.prevRotationYaw) * f;
	        double d0 = p_77621_2_.prevPosX + (p_77621_2_.posX - p_77621_2_.prevPosX) * (double)f;
	        double d1 = p_77621_2_.prevPosY + (p_77621_2_.posY - p_77621_2_.prevPosY) * (double)f + (double)(p_77621_1_.isRemote ? p_77621_2_.getEyeHeight() - p_77621_2_.getDefaultEyeHeight() : p_77621_2_.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
	        double d2 = p_77621_2_.prevPosZ + (p_77621_2_.posZ - p_77621_2_.prevPosZ) * (double)f;
	        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
	        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
	        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
	        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
	        float f6 = MathHelper.sin(-f1 * 0.017453292F);
	        float f7 = f4 * f5;
	        float f8 = f3 * f5;
	        double d3 = 5.0D;
	        if (p_77621_2_ instanceof EntityPlayerMP)
	        {
	            d3 = ((EntityPlayerMP)p_77621_2_).theItemInWorldManager.getBlockReachDistance();
	        }
	        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
	        return p_77621_1_.func_147447_a(vec3, vec31, p_77621_3_, !p_77621_3_, false);
	    }

}

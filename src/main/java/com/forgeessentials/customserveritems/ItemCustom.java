package com.forgeessentials.customserveritems;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCustom extends Item
{

    public ItemCustom()
    {
        setUnlocalizedName("custom_item");
        setTextureName(CustomServerItems.MODID + ":custom_item");
        setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey(CustomServerItems.TAG_NAME))
            return stack.getTagCompound().getString(CustomServerItems.TAG_NAME);
        return super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean shiftPressed)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null)
            return;
        if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile()))
            return;
        info.add("texture = " + tag.getString(CustomServerItems.TAG_TEXTURE));
        info.add("durability = " + tag.getString(CustomServerItems.TAG_DURABILITY));
    }

    @Override
    public int getMaxDamage(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(CustomServerItems.TAG_DURABILITY))
            return tag.getInteger(CustomServerItems.TAG_DURABILITY);
        return super.getMaxDamage(stack);
    }

    @Override
    public boolean isDamageable()
    {
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2)
    {
        stack.damageItem(1, entity2);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity)
    {
        if (block.getBlockHardness(world, x, y, z) != 0.0D)
            stack.damageItem(1, entity);
        return true;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Multimap getAttributeModifiers(ItemStack stack)
    {
        Multimap result = super.getAttributeModifiers(stack);
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(CustomServerItems.TAG_DAMAGE))
        {
            double damage = tag.getDouble(CustomServerItems.TAG_DAMAGE);
            result.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", damage, 0));
        }
        return result;
    }

}

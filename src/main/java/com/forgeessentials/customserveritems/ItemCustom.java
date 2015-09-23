package com.forgeessentials.customserveritems;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
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
    }

}

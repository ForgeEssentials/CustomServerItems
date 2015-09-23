package com.forgeessentials.customserveritems;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.lang3.StringUtils;

public class CommandCustomServerItem extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "customServerItem";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.customservercommand.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] argArray)
    {
        if (!(sender instanceof EntityPlayer))
            throw new WrongUsageException("commands.customservercommand.noConsole");
        EntityPlayer player = (EntityPlayer) sender;

        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null || stack.getItem() != CustomServerItems.ITEM)
            throw new WrongUsageException("commands.customservercommand.itemNeeded");

        LinkedList<String> args = new LinkedList<String>(Arrays.asList(argArray));
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");

        String subArg = args.remove().toLowerCase();
        switch (subArg)
        {
        case "texturelist":
            func_152373_a(player, this, "commands.customservercommand.textures", StringUtils.join(CustomServerItems.getTextureNames(), ", "));
            break;
        case "texture":
            parseTexture(player, args, stack);
            break;
        case "name":
            parseName(player, args, stack);
            break;
        case "damage":
            parseDamage(player, args, stack);
            break;
        case "durability":
            parseDurability(player, args, stack);
            break;
        default:
            throw new WrongUsageException("commands.customservercommand.invalidsubcmd");
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, "texture", "name", "damage", "durability", "texturelist");
        if (args.length == 2)
        {
            switch (args[0].toLowerCase())
            {
            case "texture":
                return getListOfStringsFromIterableMatchingLastWord(args, CustomServerItems.getTextureNames());
            }
        }
        return null;
    }

    public void parseTexture(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        String id = args.remove();

        NBTTagCompound tag = stack.getTagCompound();
        tag.setString(CustomServerItems.TAG_TEXTURE, id);
        stack.setTagCompound(tag);

        func_152373_a(player, this, "commands.customservercommand.iconSet", id);
    }

    public void parseName(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        String name = StringUtils.join(args, " ");

        NBTTagCompound tag = stack.getTagCompound();
        tag.setString(CustomServerItems.TAG_NAME, name);
        stack.setTagCompound(tag);

        func_152373_a(player, this, "commands.customservercommand.nameSet", name);
    }

    public void parseDamage(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        double damage = parseDouble(player, args.remove());

        NBTTagCompound tag = stack.getTagCompound();
        tag.setDouble(CustomServerItems.TAG_DAMAGE, damage);
        stack.setTagCompound(tag);

        func_152373_a(player, this, "commands.customservercommand.damageSet", damage);
    }

    public void parseDurability(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        int durability = parseInt(player, args.remove());

        NBTTagCompound tag = stack.getTagCompound();
        tag.setInteger(CustomServerItems.TAG_DURABILITY, durability);
        stack.setTagCompound(tag);

        func_152373_a(player, this, "commands.customservercommand.durabilitySet", durability);
    }

}

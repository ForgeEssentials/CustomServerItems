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

    public static final String[] subCommands = new String[] { "texture", "name", "tooltip", "tool", "damage", "durability", "meta", "texturelist" };

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

        LinkedList<String> args = new LinkedList<String>(Arrays.asList(argArray));
        if (args.isEmpty())
        {
            func_152373_a(player, this, getCommandUsage(sender));
            func_152373_a(player, this, "commands.customservercommand.help2", StringUtils.join(subCommands, ", "));
            return;
        }

        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null || !(stack.getItem() instanceof ItemCustom))
            throw new WrongUsageException("commands.customservercommand.itemNeeded");

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
        case "tooltip":
            parseTooltip(player, args, stack);
            break;
        case "tool":
            parseTool(player, args, stack);
            break;
        case "damage":
            parseDamage(player, args, stack);
            break;
        case "durability":
            parseDurability(player, args, stack);
            break;
        case "meta":
            parseMeta(player, args, stack);
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
            return getListOfStringsMatchingLastWord(args, subCommands);
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

        NBTTagCompound tag = getOrCreateTag(stack);
        tag.setString(CustomServerItems.TAG_TEXTURE, id);
        func_152373_a(player, this, "commands.customservercommand.iconSet", id);
    }

    public void parseName(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        String name = StringUtils.join(args, " ");

        NBTTagCompound tag = getOrCreateTag(stack);
        tag.setString(CustomServerItems.TAG_NAME, name);
        func_152373_a(player, this, "commands.customservercommand.nameSet", name);
    }

    public void parseTooltip(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        String name = StringUtils.join(args, " ");

        NBTTagCompound tag = getOrCreateTag(stack);
        tag.setString(CustomServerItems.TAG_TOOLTIP, name);
        func_152373_a(player, this, "commands.customservercommand.tooltipSet", name);
    }

    public void parseTool(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        boolean isTool = parseBoolean(player, args.remove());
        stack.func_150996_a(isTool ? CustomServerItems.ITEM_TOOL : CustomServerItems.ITEM);
        func_152373_a(player, this, "commands.customservercommand.toolSet", isTool ? "tool" : "item");
    }

    public void parseDamage(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        double damage = parseDouble(player, args.remove());

        NBTTagCompound tag = getOrCreateTag(stack);
        tag.setDouble(CustomServerItems.TAG_DAMAGE, damage);
        func_152373_a(player, this, "commands.customservercommand.damageSet", damage);
    }

    public void parseDurability(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        int durability = parseInt(player, args.remove());

        NBTTagCompound tag = getOrCreateTag(stack);
        tag.setInteger(CustomServerItems.TAG_DURABILITY, durability);
        func_152373_a(player, this, "commands.customservercommand.durabilitySet", durability);
    }

    public void parseMeta(EntityPlayer player, LinkedList<String> args, ItemStack stack)
    {
        if (args.isEmpty())
            throw new WrongUsageException("commands.notEnoughArguments");
        int meta = parseIntWithMin(player, args.remove(), 0);

        stack.setItemDamage(meta);
        func_152373_a(player, this, "commands.customservercommand.metaSet", meta);
    }

    public static NBTTagCompound getOrCreateTag(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null)
        {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        return tag;
    }

}

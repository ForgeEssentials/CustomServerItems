package com.forgeessentials.customserveritems;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class TextureRegistry implements IMessageHandler<PacketTexture, IMessage>
{

    public static final Map<String, DynamicTexture> textures = new HashMap<>();

    public static final Map<String, ResourceLocation> texturesLocations = new HashMap<>();

    @Override
    public IMessage onMessage(PacketTexture message, MessageContext ctx)
    {
        try
        {
            try (ByteArrayInputStream is = new ByteArrayInputStream(message.data))
            {
                BufferedImage image = ImageIO.read(is);
                DynamicTexture texture = new DynamicTexture(image);
                ResourceLocation textureLocation = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation("csi_" + message.id, texture);
                textures.put(message.id, texture);
                texturesLocations.put(message.id, textureLocation);
            }
        }
        catch (IOException e)
        {
            ChatComponentText cmsg = new ChatComponentText(String.format("Could not load custom server icon %s: %s", message.id, e.getMessage()));
            cmsg.getChatStyle().setColor(EnumChatFormatting.RED);
            FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(cmsg);
            e.printStackTrace();
        }
        return null;
    }

}

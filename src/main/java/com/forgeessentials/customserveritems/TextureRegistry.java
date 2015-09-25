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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureRegistry
{

    public static final Map<String, DynamicTexture> textures = new HashMap<>();

    public static final Map<String, ResourceLocation> texturesLocations = new HashMap<>();

    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(CustomServerItems.MODID, "textures/items/custom_item.png");

    protected static boolean enabled = false;

    public synchronized static void loadTexture(String id, byte[] data)
    {
        try
        {
            try (ByteArrayInputStream is = new ByteArrayInputStream(data))
            {
                BufferedImage image = ImageIO.read(is);
                DynamicTexture texture = new DynamicTexture(image);
                ResourceLocation textureLocation = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation("csi_" + id, texture);
                textures.put(id, texture);
                texturesLocations.put(id, textureLocation);
            }
        }
        catch (IOException e)
        {
            ChatComponentText cmsg = new ChatComponentText(String.format("Could not load custom server icon %s: %s", id, e.getMessage()));
            cmsg.getChatStyle().setColor(EnumChatFormatting.RED);
            Minecraft.getMinecraft().thePlayer.addChatMessage(cmsg);
            e.printStackTrace();
        }
    }

    public synchronized static void clear()
    {
        for (DynamicTexture texture : textures.values())
            texture.deleteGlTexture();
        textures.clear();
        texturesLocations.clear();
    }

    public static ResourceLocation getTexture(String id)
    {
        if (!enabled)
            return DEFAULT_TEXTURE;
        ResourceLocation texture = texturesLocations.get(id);
        if (texture == null)
        {
            texturesLocations.put(id, DEFAULT_TEXTURE);
            try
            {
                CustomServerItems.CHANNEL.sendToServer(new PacketRequestTexture(id));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return DEFAULT_TEXTURE;
        }
        else
            return texture;
    }

}

package com.forgeessentials.customserveritems;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    protected static final List<DynamicTexture> releaseList = new ArrayList<>();

    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(CustomServerItems.MODID, "textures/items/custom_item.png");

    protected static boolean enabled = false;

    public static void loadTexture(String id, byte[] data)
    {
        try (ByteArrayInputStream is = new ByteArrayInputStream(data))
        {
            BufferedImage image = ImageIO.read(is);
            DynamicTexture texture = new DynamicTexture(image);
            loadTexture(id, texture);
        }
        catch (IOException e)
        {
            ChatComponentText cmsg = new ChatComponentText(String.format("Could not load custom server icon %s: %s", id, e.getMessage()));
            cmsg.getChatStyle().setColor(EnumChatFormatting.RED);
            Minecraft.getMinecraft().thePlayer.addChatMessage(cmsg);
            e.printStackTrace();
        }
    }

    public static synchronized void loadTexture(String id, DynamicTexture texture)
    {
        ResourceLocation textureLocation = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation("csi_" + id, texture);
        DynamicTexture oldTexture = textures.put(id, texture);
        if (oldTexture != null)
            releaseList.add(oldTexture);
        texturesLocations.put(id, textureLocation);
    }

    public static synchronized void clear()
    {
        releaseList.addAll(textures.values());
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

    public static synchronized void tick()
    {
        for (DynamicTexture texture : releaseList)
            texture.deleteGlTexture();
        releaseList.clear();
    }

}

package com.forgeessentials.customserveritems;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = CustomServerItems.MODID, version = CustomServerItems.VERSION)
public class CustomServerItems implements IMessageHandler<PacketRequestTexture, IMessage>
{

    public static final String MODID = "customserveritems";

    public static final String VERSION = "0.1";

    public static final String TAG_TEXTURE = "csi_texture";

    public static final String TAG_NAME = "csi_name";

    public static final String TAG_DAMAGE = "csi_damage";

    public static final String TAG_DURABILITY = "csi_durability";

    public static final String TAG_TOOLTIP = "csi_tooltip";

    public static final ItemCustom ITEM = new ItemCustom();

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    public static final File TEXTURE_DIRECTORY = new File("config/CustomServerItems");

    @SideOnly(Side.CLIENT)
    public static final TextureRegistry TEXTURE_REGISTRY = new TextureRegistry();

    public static final FilenameFilter PNG_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name)
        {
            return name.toLowerCase().endsWith(".png");
        }
    };

    @SideOnly(Side.CLIENT)
    public static CustomItemRenderer itemRenderer;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerItem(ITEM, "custom_item");

        CHANNEL.registerMessage(this, PacketRequestTexture.class, 0, Side.SERVER);
        CHANNEL.registerMessage(PacketTexture.class, PacketTexture.class, 1, Side.CLIENT);

        TEXTURE_DIRECTORY.mkdirs();

        if (event.getSide() == Side.CLIENT)
        {
            MinecraftForgeClient.registerItemRenderer(ITEM, new CustomItemRenderer());
        }
    }

    @EventHandler
    public void clientDisconnectEvent(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandCustomServerItem());
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void clientDisconnectEvent(ClientDisconnectionFromServerEvent event)
    {
        TextureRegistry.clear();
    }

    @Override
    public IMessage onMessage(PacketRequestTexture message, MessageContext ctx)
    {
        try
        {
            File file = new File(TEXTURE_DIRECTORY, message.id + ".png");
            if (file.exists())
                return new PacketTexture(message.id, file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getTextureNames()
    {
        List<String> textures = new ArrayList<String>();
        for (String name : TEXTURE_DIRECTORY.list(PNG_FILTER))
            textures.add(name.substring(0, name.length() - 4));
        return textures;
    }

}

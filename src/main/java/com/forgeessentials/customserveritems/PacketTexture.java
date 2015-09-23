package com.forgeessentials.customserveritems;

import io.netty.buffer.ByteBuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketTexture implements IMessage, IMessageHandler<PacketTexture, IMessage>
{

    public String id;

    public byte[] data;

    public PacketTexture()
    {
    }

    public PacketTexture(String id, File icon) throws IOException
    {
        this.id = id;
        try (FileInputStream is = new FileInputStream(icon))
        {
            data = new byte[(int) icon.length()];
            is.read(data);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = ByteBufUtils.readUTF8String(buf);
        int size = buf.readInt();
        data = new byte[size];
        buf.readBytes(data);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, id);
        buf.writeInt(data.length);
        buf.writeBytes(data);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(PacketTexture message, MessageContext ctx)
    {
        CustomServerItems.TEXTURE_REGISTRY.loadTexture(message.id, message.data);
        return null;
    }

}

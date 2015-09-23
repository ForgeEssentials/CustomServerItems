package com.forgeessentials.customserveritems;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PacketRequestTexture implements IMessage
{

    public String id;

    public PacketRequestTexture()
    {
    }

    public PacketRequestTexture(String id) throws IOException
    {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, id);
    }

}

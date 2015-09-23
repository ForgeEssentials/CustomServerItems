package com.forgeessentials.customserveritems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class CustomItemRenderer implements IItemRenderer
{

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type)
    {
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey(CustomServerItems.TAG_TEXTURE);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper)
    {
        switch (helper)
        {
        case ENTITY_ROTATION:
        case ENTITY_BOBBING:
            return true;
        case BLOCK_3D:
        case INVENTORY_BLOCK:
        case EQUIPPED_BLOCK:
        default:
            return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
    {
        String id = stack.getTagCompound().getString(CustomServerItems.TAG_TEXTURE);
        ResourceLocation texture = TextureRegistry.getTexture(id);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        Tessellator tessellator = Tessellator.instance;
        int iconWidth = 32;
        int iconHeight = 32;

        // IIcon icon = stack.getIconIndex();
        // iconWidth = icon.getIconWidth();
        // iconHeight = icon.getIconHeight();

        GL11.glPushMatrix();
        switch (type)
        {
        case ENTITY:
            GL11.glTranslatef(-0.5f, -0.3f, 0);
        case EQUIPPED:
        case EQUIPPED_FIRST_PERSON:
            TextureUtil.func_152777_a(false, false, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            ItemRenderer.renderItemIn2D(tessellator, 1, 0, 0, 1, iconWidth, iconHeight, 0.0625F);
            break;
        case INVENTORY:
        default:
            GL11.glEnable(GL11.GL_BLEND);
            double zLevel = 4;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(0, 16, zLevel, 0, 1);
            tessellator.addVertexWithUV(16, 16, zLevel, 1, 1);
            tessellator.addVertexWithUV(16, 0, zLevel, 1, 0);
            tessellator.addVertexWithUV(0, 0, zLevel, 0, 0);
            tessellator.draw();
            break;
        }
        GL11.glPopMatrix();
    }
}

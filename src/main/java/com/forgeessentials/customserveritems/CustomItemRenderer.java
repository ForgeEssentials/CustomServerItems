package com.forgeessentials.customserveritems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomItemRenderer implements IItemRenderer
{

    public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

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
        case EQUIPPED_BLOCK:
        case INVENTORY_BLOCK:
        default:
            return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
    {
        String id = stack.getTagCompound().getString(CustomServerItems.TAG_TEXTURE);
        ResourceLocation texture = TextureRegistry.getTexture(id);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(texture);

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

            if (stack.hasEffect(0) || stack.getTagCompound().getBoolean(CustomServerItems.TAG_GLINT))
            {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                textureManager.bindTexture(RES_ITEM_GLINT);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(768, 1, 1, 0);
                GL11.glColor4f(0.5F * 0.76F, 0.25F * 0.76F, 0.8F * 0.76F, 1.0F);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glPushMatrix();
                float f8 = 0.125F;
                GL11.glScalef(f8, f8, f8);
                float f9 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
                GL11.glTranslatef(f9, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f8, f8, f8);
                f9 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
                GL11.glTranslatef(-f9, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }

            break;
        case INVENTORY:
        default:
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            double zLevel = RenderItem.getInstance().zLevel;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(0, 16, zLevel, 0, 1);
            tessellator.addVertexWithUV(16, 16, zLevel, 1, 1);
            tessellator.addVertexWithUV(16, 0, zLevel, 1, 0);
            tessellator.addVertexWithUV(0, 0, zLevel, 0, 0);
            tessellator.draw();

            if (stack.hasEffect(0) || stack.getTagCompound().getBoolean(CustomServerItems.TAG_GLINT))
                RenderItem.getInstance().renderEffect(textureManager, 0, 0);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            break;
        }
        GL11.glPopMatrix();
    }

}

package com.sekai.personabattlemod.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.client.entity.model.ShadowEntityModel;
import com.sekai.personabattlemod.entities.ShadowEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.WGLNVFloatBuffer;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ThirdEyeEvent {
    public float anim;
    private float animMax = 100f;
    private float animRate = 1f;

    public boolean alreadyActivated = false;

    public static final Method preRender = ObfuscationReflectionHelper.findMethod(LivingRenderer.class, "func_225620_a_", LivingEntity.class/*entity.getClass()*/, MatrixStack.class, float.class);

    public boolean isKeyDown() {
        return PersonaBattle.keyThirdEye.isKeyDown();
    }

    public boolean isKeyPressed() {
        return PersonaBattle.keyThirdEye.isPressed();
    }

    @SubscribeEvent
    public void clientTickEvent(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().world == null)
            return;

        if (isKeyPressed())
        {

        }
        if (isKeyDown())
        {
            //Minecraft.getInstance().skipRenderWorld = true;
            if(anim >= animMax)
                anim = animMax;
            else
                anim += animRate;
        }
        else
        {
            //Minecraft.getInstance().skipRenderWorld = false;
            //anim -= animRate;
            if(anim <= 0f)
                anim = 0f;
            else
                anim -= animRate;
        }
    }

    /*@SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt)
    {
        if(isKeyDown()) {
            //double doubleX = Minecraft.getInstance().player.getPositionVec().getX() - 0.5;
            //double doubleY = Minecraft.getInstance().player.getPositionVec().getY() + 0.1;
            //double doubleZ = Minecraft.getInstance().player.getPositionVec().getZ() - 0.5;

            //GL11.glPushMatrix();
            //Vec3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
            //GL11.glTranslated(-doubleX, -doubleY, -doubleZ);
            //GL11.glColor3ub((byte) 255, (byte) 0, (byte) 0);
            //float mx = 9;
            //float my = 9;
            //float mz = 9;
            //GL11.glBegin(GL11.GL_LINES);
            //GL11.glVertex3f(mx + 0.4f, my, mz + 0.4f);
            //GL11.glVertex3f(mx - 0.4f, my, mz - 0.4f);
            //GL11.glVertex3f(mx + 0.4f, my, mz - 0.4f);
            //GL11.glVertex3f(mx - 0.4f, my, mz + 0.4f);
            //GL11.glEnd();
            //GL11.glPopMatrix();

            //LogManager.getLogger().info(playerEntity.getEyePosition(partialTicks));
            //Vec3d eyePos = playerEntity.getEyePosition(partialTicks);
            //Vec3d eyePos = Minecraft.getInstance().getRenderViewEntity().getEyePosition(partialTicks);

            RenderSystem.pushMatrix();

            //GlStateManager.translated(-eyePos.x, -eyePos.y, -eyePos.z);
            double x = 6; double y = 6; double z = 6;
            Vec3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
            float pitch = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getPitch();
            float yaw = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getYaw();
            //Quaternion quaternion = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getRotation();
            //RenderSystem.rotatef(pitch, 0, 0, 0);
            //RenderSystem.rotatef(yaw, 0, 1, 0);
            //RenderSystem.rotatef(pitch, 0, 0, 1);
            //RenderSystem.translated(projectedView.x, -projectedView.y, projectedView.z);
            RenderSystem.translated(-projectedView.x, -projectedView.y, -projectedView.z);
            //evt.getMatrixStack().func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(pitch)); temp
            //evt.getMatrixStack().func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(yaw + 180.0F)); temp
            //evt.getMatrixStack()
            RenderSystem.translated(6, 6, 6);
            //RenderSystem.rotatef(pitch, yaw, 0, 0);
            //RenderSystem.rotatef(quaternion.getX(), quaternion.getY(), quaternion.getZ(), quaternion.getW());
            //RenderSystem.rotatef(yaw, 0, 1, 0);
            //RenderSystem.rotatef(yaw, 1, 0, 0);

            //RenderSystem.translated(-x, -y - playerEntity.getEyeHeight(), -z);
            //RenderSystem.translated(10, 10, 10);

            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();

            RenderSystem.color4f(1, 1, 1, 1);
            //GlStateManager.lineWidth(1);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();

            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

            bufferBuilder.pos(0, 0, 0).endVertex();
            bufferBuilder.pos(1, 0, 0).endVertex();
            bufferBuilder.pos(1, 0, 1).endVertex();
            bufferBuilder.pos(0, 0, 1).endVertex();

            bufferBuilder.pos(0, 1, 0).endVertex();
            bufferBuilder.pos(1, 1, 0).endVertex();
            bufferBuilder.pos(1, 1, 1).endVertex();
            bufferBuilder.pos(0, 1, 1).endVertex();
            //bufferBuilder.func_225582_a_(x + 0, y + 0, z + 0).endVertex();
            //bufferBuilder.func_225582_a_(x + 1, y + 0, z + 0).endVertex();
            //bufferBuilder.func_225582_a_(x + 1, y + 0, z + 1).endVertex();
            //bufferBuilder.func_225582_a_(x + 0, y + 0, z + 1).endVertex();

            //bufferBuilder.func_225582_a_(x + 0, y + 1, z + 0).endVertex();
            //bufferBuilder.func_225582_a_(x + 1, y + 1, z + 0).endVertex();
            //bufferBuilder.func_225582_a_(x + 1, y + 1, z + 1).endVertex();
            //bufferBuilder.func_225582_a_(x + 0, y + 1, z + 1).endVertex();

            tessellator.draw();

            RenderSystem.enableDepthTest();
            RenderSystem.enableTexture();

            RenderSystem.popMatrix();
        }
    }*/

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getInstance().world == null)
            return;

        int distThirdEye = 16;

        //Minecraft.getInstance().world.getEnti
        List<MobEntity> mobEntities = Minecraft.getInstance().world.getEntitiesWithinAABB(MobEntity.class, (new AxisAlignedBB(Minecraft.getInstance().player.getPosition())).grow(distThirdEye*2));

        for (MobEntity mobEntity : mobEntities) {
            BlockPos blockPos = mobEntity.getPosition().subtract(Minecraft.getInstance().player.getPosition());
            int dist = Math.abs(blockPos.getX()) + Math.abs(blockPos.getY()) + Math.abs(blockPos.getZ());
            if (isKeyDown() && dist <= distThirdEye) {
                mobEntity.getPersistentData().putIntArray("fullbright", new int[]{255, 0, 0, (int) (255 * (1f * (distThirdEye - dist) / distThirdEye))});
            } else {
                mobEntity.getPersistentData().remove("fullbright");
            }
        }

        //List<SkeletonEntity> skeletonEntities = Minecraft.getInstance().world.getEntitiesWithinAABB(EntityType.SKELETON, (new AxisAlignedBB(Minecraft.getInstance().player.getPosition())).grow(distThirdEye), entity -> true);

        /*for (SkeletonEntity skeletonEntity : skeletonEntities) {
            BlockPos blockPos = skeletonEntity.getPosition().subtract(Minecraft.getInstance().player.getPosition());
            int dist = Math.abs(blockPos.getX()) + Math.abs(blockPos.getY()) + Math.abs(blockPos.getZ());
            if(isKeyDown()) {
                skeletonEntity.getPersistentData().putIntArray("fullbright", new int[]{255, 0, 0, (int) (255 * (1f * dist / distThirdEye))});
            } else {
                skeletonEntity.getPersistentData().remove("fullbright");
            }*/
            //creeper.ren
            /*double d1 = creeper.prevPosX;
            double d2 = creeper.getPosX();
            creeper.prevPosX = d1 + 1;
            creeper.setPosition(d2 + 1, creeper.getPosY(), creeper.getPosZ());
            event.getMatrixStack().push();
            GL11.glColor4f(1f,0f,0f,1f);
            renderEntityLayer(creeper, event.getPartialTicks(), event.getMatrixStack());
            GL11.glColor4f(1f,1f,1f,1f);
            event.getMatrixStack().pop();
            System.out.println("rendering a creeper ow man");
            creeper.prevPosX = d1;
            creeper.setPosition(d2, creeper.getPosY(), creeper.getPosZ());*/
        //}
    }

    @SubscribeEvent
    public void renderFake(RenderWorldLastEvent event) {
        alreadyRendered.clear();
    }

    List<LivingEntity> alreadyRendered = new ArrayList<>();

    @SubscribeEvent
    public void onLivingRenderPre(RenderLivingEvent.Pre event) {
        LivingEntity entity = event.getEntity();

        if(alreadyRendered.contains(entity)) {
            event.setCanceled(true);
            return;
        }

        int[] fullbright = entity.getEntity().getPersistentData().getIntArray("fullbright");
        if(fullbright.length == 4) {
            /*OutlineLayerBuffer outlinelayerbuffer = Minecraft.getInstance().getRenderTypeBuffers().getOutlineBufferSource();
            outlinelayerbuffer.setColor(fullbright[0], fullbright[1], fullbright[2], fullbright[3]);
            //event.getRenderer().getEntityModel().render(event.getMatrixStack(), event.getBuffers().getBuffer(event.getRenderer().getEntityModel().getRenderType(event.getRenderer().getEntityTexture(event.getEntity()))), event.getLight(), 0, 1f, 1f, 1f, 0.5f);
            event.getRenderer().getEntityModel().render(event.getMatrixStack(), ((IRenderTypeBuffer) outlinelayerbuffer).getBuffer(event.getRenderer().getEntityModel().getRenderType(event.getRenderer().getEntityTexture(event.getEntity()))), event.getLight(), 0, 1f, 1f, 1f, 1f);
            event.setCanceled(true);*/

            float partialTicks = event.getPartialRenderTick();
            EntityModel entityModel = event.getRenderer().getEntityModel();
            MatrixStack matrixStackIn = event.getMatrixStack();
            IRenderTypeBuffer bufferIn = event.getBuffers();
            int packedLightIn = event.getLight();

            /*OutlineLayerBuffer outlinelayerbuffer = Minecraft.getInstance().getRenderTypeBuffers().getOutlineBufferSource();
            outlinelayerbuffer.setColor(fullbright[0], fullbright[1], fullbright[2], fullbright[3]);
            bufferIn = outlinelayerbuffer;*/

            matrixStackIn.push();
            entityModel.swingProgress = entity.getSwingProgress(partialTicks);//getSwingProgress(entity, partialTicks);

            boolean shouldSit = entity.isPassenger() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
            entityModel.isSitting = shouldSit;
            entityModel.isChild = entity.isChild();
            float f = MathHelper.interpolateAngle(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
            float f1 = MathHelper.interpolateAngle(partialTicks, entity.prevRotationYawHead, entity.rotationYawHead);
            float f2 = f1 - f;
            if (shouldSit && entity.getRidingEntity() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity.getRidingEntity();
                f = MathHelper.interpolateAngle(partialTicks, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
                f2 = f1 - f;
                float f3 = MathHelper.wrapDegrees(f2);
                if (f3 < -85.0F) {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F) {
                    f3 = 85.0F;
                }

                f = f1 - f3;
                if (f3 * f3 > 2500.0F) {
                    f += f3 * 0.2F;
                }

                f2 = f1 - f;
            }

            float f6 = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
            if (entity.getPose() == Pose.SLEEPING) {
                Direction direction = entity.getBedDirection();
                if (direction != null) {
                    float f4 = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                    matrixStackIn.translate((double)((float)(-direction.getXOffset()) * f4), 0.0D, (double)((float)(-direction.getZOffset()) * f4));
                }
            }

            float f7 = (float)entity.ticksExisted + partialTicks;//handleRotationFloat(entity, partialTicks);
            applyRotations(entity, matrixStackIn, f7, f, partialTicks);
            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
            //todo : this bullshit
            //ObfuscationReflectionHelper.findMethod(LivingRenderer.class, "func???", entity.getClass(), MatrixStack.class, float.class);
            //ObfuscationReflectionHelper.findMethod(LivingRenderer.class, "func_225620_a_", entity.getClass(), MatrixStack.class, float.class);
            preRender.setAccessible(true);
            try {
                preRender.invoke(event.getRenderer(), entity, matrixStackIn, partialTicks);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //event.getRenderer().preRenderCallback(entity, matrixStackIn, partialTicks);
            //((LivingExtender) event.getRenderer()).preRenderCallback(entity, matrixStackIn, partialTicks);
            //todo : or what if i just extended LivingRenderer and casted to that new class just to expose that method? need further testing
            matrixStackIn.translate(0.0D, (double)-1.501F, 0.0D);
            float f8 = 0.0F;
            float f5 = 0.0F;
            if (!shouldSit && entity.isAlive()) {
                f8 = MathHelper.lerp(partialTicks, entity.prevLimbSwingAmount, entity.limbSwingAmount);
                f5 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
                if (entity.isChild()) {
                    f5 *= 3.0F;
                }

                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }

            entityModel.setLivingAnimations(entity, f5, f8, partialTicks);
            entityModel.setRotationAngles(entity, f5, f8, f7, f2, f6);
            boolean flag = !entity.isInvisible();//isVisible(entity);
            boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getInstance().player);

            //RenderType rendertype = func_230042_a_(entity, flag, flag1);
            RenderType rendertype;
            //ResourceLocation resourcelocation = event.getRenderer().getEntityTexture(entity);
            ResourceLocation resourcelocation = new ResourceLocation(PersonaBattle.MOD_ID, "textures/entity/black.png");
            if (flag1) {
                rendertype = RenderType.getEntityTranslucent(resourcelocation);
            } else if (flag) {
                rendertype = entityModel.getRenderType(resourcelocation);
            } else {
                rendertype = entity.isGlowing() ? RenderType.getOutline(resourcelocation) : null;
            }

            RenderType rendertype1;
            ResourceLocation resourcelocation1 = event.getRenderer().getEntityTexture(entity);
            if (flag1) {
                rendertype1 = RenderType.getEntityTranslucent(resourcelocation1);
            } else if (flag) {
                rendertype1 = entityModel.getRenderType(resourcelocation1);
            } else {
                rendertype1 = entity.isGlowing() ? RenderType.getOutline(resourcelocation1) : null;
            }
            //
            //rendertype = RenderType.getOutline(resourcelocation);

            if (rendertype != null) {
                //IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
                OutlineLayerBuffer outlinelayerbuffer = Minecraft.getInstance().getRenderTypeBuffers().getOutlineBufferSource();
                outlinelayerbuffer.setColor(fullbright[0], fullbright[1], fullbright[2], fullbright[3]);
                //IVertexBuilder ivertexbuilder = ((IRenderTypeBuffer) outlinelayerbuffer).getBuffer(entityModel.getRenderType(event.getRenderer().getEntityTexture(event.getEntity())));
                IVertexBuilder ivertexbuilder = ((IRenderTypeBuffer) outlinelayerbuffer).getBuffer(entityModel.getRenderType(resourcelocation));

                //IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(rendertype1); RenderType.getEyes(resourcelocation1)


                //int i = getPackedOverlay(entity, getOverlayProgress(entity, partialTicks));
                //todo : i omitted a method in there that needs to exist, it's the 0f value, find it in livingrenderer
                int i = OverlayTexture.getPackedUV(OverlayTexture.getU(0.0F), OverlayTexture.getV(entity.hurtTime > 0 || entity.deathTime > 0));
                //entityModel.render(matrixStackIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
                entityModel.render(matrixStackIn, ivertexbuilder, packedLightIn, 0, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
                //entityModel.render(matrixStackIn, ivertexbuilder1, packedLightIn, 0, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);

                IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(RenderType.getEntitySolid(resourcelocation1));
                if(fullbright[3] != 255)
                    entityModel.render(matrixStackIn, ivertexbuilder1, packedLightIn, 0, 1.0F, 1.0F, 1.0F, (255-fullbright[3])/255f);
                //entityModel.render(matrixStackIn, ((IRenderTypeBuffer) outlinelayerbuffer).getBuffer(event.getRenderer().getEntityModel().getRenderType(event.getRenderer().getEntityTexture(event.getEntity()))), event.getLight(), 0, 1f, 1f, 1f, 1f);
            }

            //todo : fuck off
            /*if (!entity.isSpectator()) {
                for(LayerRenderer<T, M> layerrenderer : layerRenderers) {
                    layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entity, f5, f8, partialTicks, f7, f2, f6);
                }
            }*/

            matrixStackIn.pop();
            float entityYaw = MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw);
            alreadyRendered.add(entity);
            event.getRenderer().render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

            event.setCanceled(true);
        }
        /*if (entity instanceof CreeperEntity) {
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.disableAlphaTest();
            RenderSystem.defaultBlendFunc();
            GlStateManager.disableDepthTest();
            //RenderSystem.color4f(1F, 0F, 0F, 1.0F);
            //RenderSystem.shadeModel(7425);
            //GL11.glColor4f(1f, 1f, 0f, 1f);
            //GL11.glColor4f(1f, 1f, 0f, 1f);

            //IRenderTypeBuffer irendertypebuffer = Minecraft.getInstance().getRenderTypeBuffers().getOutlineBufferSource();
            OutlineLayerBuffer outlinelayerbuffer = Minecraft.getInstance().getRenderTypeBuffers().getOutlineBufferSource();
            IRenderTypeBuffer irendertypebuffer = outlinelayerbuffer;
            outlinelayerbuffer.setColor(255, 0, 0, 255);
            //event.getRenderer().getEntityModel().render(event.getMatrixStack(), event.getBuffers().getBuffer(event.getRenderer().getEntityModel().getRenderType(event.getRenderer().getEntityTexture(event.getEntity()))), event.getLight(), 0, 1f, 1f, 1f, 0.5f);
            event.getRenderer().getEntityModel().render(event.getMatrixStack(), irendertypebuffer.getBuffer(event.getRenderer().getEntityModel().getRenderType(event.getRenderer().getEntityTexture(event.getEntity()))), event.getLight(), 0, 1f, 1f, 1f, 0.5f);
            event.setCanceled(true);

            GlStateManager.enableDepthTest();
            //RenderSystem.color4f(1F, 1F, 1F, 1F);
            //RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableTexture();

            //System.out.println(event.getLight());
        }*/
        /*RayTraceResult objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
            EntityLivingBase entity = event.getEntity();
            if (objectMouseOver.entityHit == entity) {

                needToPop = true;
                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();

                float red = Config.entityOverlayModel_red;
                float green = Config.entityOverlayModel_green;
                float blue = Config.entityOverlayModel_blue;
                float alpha = 1f;
                FloatBuffer colourBuffer = RenderHelper.setColorBuffer(red, green, blue, alpha);
                glLightModel(GL_LIGHT_MODEL_AMBIENT, colourBuffer);
                for (int i = 0; i < 8; ++i) {
                    GlStateManager.glLight(GL_LIGHT0 + i, GL_DIFFUSE, colourBuffer);
                }

                GlStateManager.color(red, green, blue, alpha);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }
        }*/
    }

    public class LivingExtender extends LivingRenderer {
        public LivingExtender(EntityRendererManager rendererManager, EntityModel entityModelIn, float shadowSizeIn) {
            super(rendererManager, entityModelIn, shadowSizeIn);
        }

        @Override
        public void preRenderCallback(LivingEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
            super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
        }

        @Override
        public ResourceLocation getEntityTexture(Entity entity) {
            return null;
        }
    }

    protected <T extends LivingEntity> void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        Pose pose = entityLiving.getPose();
        if (pose != Pose.SLEEPING) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        }

        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * 90f));
        } else if (entityLiving.isSpinAttacking()) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90.0F - entityLiving.rotationPitch));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(((float)entityLiving.ticksExisted + partialTicks) * -75.0F));
        } else if (pose == Pose.SLEEPING) {
            Direction direction = entityLiving.getBedDirection();
            float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90f));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270.0F));
        } else if (entityLiving.hasCustomName() || entityLiving instanceof PlayerEntity) {
            String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName().getString());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof PlayerEntity) || ((PlayerEntity)entityLiving).isWearing(PlayerModelPart.CAPE))) {
                matrixStackIn.translate(0.0D, (double)(entityLiving.getHeight() + 0.1F), 0.0D);
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
            }
        }

    }

    private static float getFacingAngle(Direction facingIn) {
        switch(facingIn) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    /*@SubscribeEvent
    public void renderFake(RenderWorldLastEvent event) {
        ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
        BlockPos pos = new BlockPos(0, 64, 0); // Just somewhere in the world

        MatrixStack mtx = event.getMatrixStack();
        mtx.push(); // push
        mtx.translate(-renderInfo.getProjectedView().getX(), -renderInfo.getProjectedView().getY(), -renderInfo.getProjectedView().getZ()); // translate back to camera
        Matrix4f matrix4f = mtx.getLast().getMatrix(); // get final transformation matrix, handy to get yaw+pitch transformation
        RenderSystem.multMatrix(matrix4f);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("textures/block/stone.png"));
        Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        drawBlock(Tessellator.getInstance().getBuffer(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 1, 0, 1, 0.5,0.5,0.5);
        Tessellator.getInstance().draw();
        //mtx.func_227861_a_(0, 0, 0); // reset translation
        mtx.pop(); // pop
    }

    private static void drawBlock(final BufferBuilder bufferbuilder, final double x, final double y, final double z, final float minU, final float maxU, final float minV, final float maxV, final double x_size, final double y_size, final double z_size) {
        // UP
        bufferbuilder.pos(-x_size + x, y_size + y, -z_size + z).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, z_size + z).tex(maxU, minV).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, z_size + z).tex(minU, minV).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, -z_size + z).tex(minU, maxV).endVertex();

        // DOWN
        bufferbuilder.pos(-x_size + x, -y_size + y, z_size + z).tex(minU, minV).endVertex();
        bufferbuilder.pos(-x_size + x, -y_size + y, -z_size + z).tex(minU, maxV).endVertex();
        bufferbuilder.pos(x_size + x, -y_size + y, -z_size + z).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(x_size + x, -y_size + y, z_size + z).tex(maxU, minV).endVertex();

        // LEFT
        bufferbuilder.pos(x_size + x, -y_size + y, z_size + z).tex(maxU, minV).endVertex();
        bufferbuilder.pos(x_size + x, -y_size + y, -z_size + z).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, -z_size + z).tex(minU, maxV).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, z_size + z).tex(minU, minV).endVertex();

        // RIGHT
        bufferbuilder.pos(-x_size + x, -y_size + y, -z_size + z).tex(minU, maxV).endVertex();
        bufferbuilder.pos(-x_size + x, -y_size + y, z_size + z).tex(minU, minV).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, z_size + z).tex(maxU, minV).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, -z_size + z).tex(maxU, maxV).endVertex();

        // BACK
        bufferbuilder.pos(-x_size + x, -y_size + y, -z_size + z).tex(minU, maxV).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, -z_size + z).tex(minU, minV).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, -z_size + z).tex(maxU, minV).endVertex();
        bufferbuilder.pos(x_size + x, -y_size + y, -z_size + z).tex(maxU, maxV).endVertex();

        // FRONT
        bufferbuilder.pos(x_size + x, -y_size + y, z_size + z).tex(maxU, minV).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, z_size + z).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, z_size + z).tex(minU, maxV).endVertex();
        bufferbuilder.pos(-x_size + x, -y_size + y, z_size + z).tex(minU, minV).endVertex();
    }*/

    //@SubscribeEvent
    //public void renderFake(RenderWorldLastEvent event) {
        //Minecraft mc = Minecraft.getInstance();
        //ActiveRenderInfo renderInfo = mc.gameRenderer.getActiveRenderInfo();
        //BlockPos pos = new Blockpos(0, 64, 0); // Just somewhere in the world

        /*List<CreeperEntity> creepers = mc.world.getEntitiesWithinAABB(EntityType.CREEPER, (new AxisAlignedBB(mc.player.getPosition())).grow(16), entity -> true);

        for (CreeperEntity creeper : creepers) {
            //creeper.ren
            double d1 = creeper.prevPosX;
            double d2 = creeper.getPosX();
            creeper.prevPosX = d1 + 1;
            creeper.setPosition(d2 + 1, creeper.getPosY(), creeper.getPosZ());
            event.getMatrixStack().push();
            GL11.glColor4f(1f,0f,0f,1f);
            renderEntityLayer(creeper, event.getPartialTicks(), event.getMatrixStack());
            GL11.glColor4f(1f,1f,1f,1f);
            event.getMatrixStack().pop();
            System.out.println("rendering a creeper ow man");
            creeper.prevPosX = d1;
            creeper.setPosition(d2, creeper.getPosY(), creeper.getPosZ());
        }*/

        /*matrixStack.func_227860_a_(); // push
        matrixStack.func_227861_a_(-renderInfo.getProjectedView().getX(), -renderInfo.getProjectedView().getY(), -renderInfo.getProjectedView().getZ()); // translate back to camera
        Matrix4f matrix4f = matrixStack.func_227866_c_().func_227870_a_(); // get final transformation matrix, handy to get yaw+pitch transformation
        RenderSystem.multMatrix(matrix4f);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("textures/block/stone.png"));
        Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        drawBlock(Tessellator.getInstance().getBuffer(), pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 0, 1, 0, 1, 0.5,0.5,0.5);
        Tessellator.getInstance().draw();
        matrixStack.func_227861_a_(0, 0, 0); // reset translation
        matrixStack.func_227865_b_(); // pop*/
    //}

    private void renderEntityLayer(Entity entity, float partialTicks, MatrixStack matrixStackIn) {
        Vec3d vec3d = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        double d0 = vec3d.getX();
        double d1 = vec3d.getY();
        double d2 = vec3d.getZ();

        if (entity.ticksExisted == 0) {
            entity.lastTickPosX = entity.getPosX();
            entity.lastTickPosY = entity.getPosY();
            entity.lastTickPosZ = entity.getPosZ();
        }

        entity.lastTickPosX = entity.getPosX();
        entity.lastTickPosY = entity.getPosY();
        entity.lastTickPosZ = entity.getPosZ();

        //IRenderTypeBuffer irendertypebuffer = renderTypeTextures.getBufferSource();
        IRenderTypeBuffer irendertypebuffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        renderEntity(entity, d0, d1, d2, partialTicks, matrixStackIn, irendertypebuffer);
    }

    private void renderEntity(Entity entity, double camX, double camY, double camZ, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) {
        EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();
        double d0 = MathHelper.lerp(partialTicks, entity.lastTickPosX, entity.getPosX());
        double d1 = MathHelper.lerp(partialTicks, entity.lastTickPosY, entity.getPosY());
        double d2 = MathHelper.lerp(partialTicks, entity.lastTickPosZ, entity.getPosZ());
        float f = MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw);
        renderEntityStatic(entity, d0 - camX, d1 - camY, d2 - camZ, f, partialTicks, matrixStackIn, bufferIn, renderManager.getPackedLight(entity, partialTicks));
    }

    private <E extends Entity> void renderEntityStatic(E entity, double xIn, double yIn, double zIn, float rotationYawIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        EntityRenderer<? super E> entityrenderer = Minecraft.getInstance().getRenderManager().getRenderer(entity);
        //EntityRenderer<? super E> entityrenderer = (LivingRenderer<E extends LivingEntity, M extends EntityModel<E>>)Minecraft.getInstance().getRenderManager().getRenderer(entity);
        //Minecraft.getInstance().getEntityRenderDispatcher
        //Minecraft.getInstance().getRenderManager().getRenderer()
        //entityrenderer.getEntityModel();
        //Field privateModel = ObfuscationReflectionHelper.findField(entityrenderer.getClass(), "entityModel");//LivingRenderer.class.getDeclaredField("entityModel");
        //Field privateModel = ObfuscationReflectionHelper.findField(entityrenderer.getClass(), "entityModel");//LivingRenderer.class.getDeclaredField("entityModel");

        //privateModel.setAccessible(true);

        //getEntityModel(entityrenderer);

        //EntityModel<T> fieldValue = (EntityModel<T>) privateModel.get(entityrenderer);


        Vec3d vec3d = entityrenderer.getRenderOffset(entity, partialTicks);
        double d2 = xIn + vec3d.getX();
        double d3 = yIn + vec3d.getY();
        double d0 = zIn + vec3d.getZ();
        matrixStackIn.push();
        matrixStackIn.translate(d2, d3, d0);
        entityrenderer.render(entity, rotationYawIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        /*if (entity.canRenderOnFire()) {
            renderFire(matrixStackIn, bufferIn, entity);
        }*/

        matrixStackIn.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
        /*if (options.entityShadows && renderShadow && entityrenderer.shadowSize > 0.0F && !entity.isInvisible()) {
            double d1 = getDistanceToCamera(entity.getPosX(), entity.getPosY(), entity.getPosZ());
            float f = (float)((1.0D - d1 / 256.0D) * (double)entityrenderer.shadowOpaque);
            if (f > 0.0F) {
                renderShadow(matrixStackIn, bufferIn, entity, f, partialTicks, world, entityrenderer.shadowSize);
            }
        }

        if (debugBoundingBox && !entity.isInvisible() && !Minecraft.getInstance().isReducedDebug()) {
            renderDebugBoundingBox(matrixStackIn, bufferIn.getBuffer(RenderType.getLines()), entity, partialTicks);
        }*/

        matrixStackIn.pop();
    }

    private void renderEntityFinal() {

    }

    //RenderLivingEvent<T extends LivingEntity, M extends EntityModel<T>>

    /*@SubscribeEvent
    public void onFogDensityEvent(EntityViewRenderEvent.FogDensity event) {
        //if (PersonaBattle.keyThirdEye.isKeyDown())
        if(anim != 0f)
        {
            //GlStateManager.
            GL11.glFogi(GL11.GL_FOG_MODE, GlStateManager.FogMode.LINEAR.field_187351_d);
            //GL11.glFogf(GL11.GL_FOG_START, -20f * (1f - (anim/animMax)));
            GL11.glFogf(GL11.GL_FOG_START, 0f);
            GL11.glFogf(GL11.GL_FOG_END, 1f + 999f * (1f - (anim/animMax)));
            event.setDensity(0.5F - 0.3F * (anim/animMax));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onFogColorsEvent(EntityViewRenderEvent.FogColors event) {
        //if (PersonaBattle.keyThirdEye.isKeyDown()) {
        if(anim != 0f) {
            float percent = (1 - (anim/animMax));
            event.setRed(event.getRed() * percent);
            event.setGreen(event.getGreen() * percent);
            event.setBlue(event.getBlue() * percent);
            //event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderLivingEvent(RenderLivingEvent.Pre event) {
        if(event.getEntity() instanceof ShadowEntity) {
            //GL11.glColor3f(1f, 0f, 0f);
            if(!alreadyActivated)
            {
                alreadyActivated = true;

                //event.getRenderer()
                event.setCanceled(true);
                //event.getRenderer().doRender((AbstractClientPlayer) event.getEntity(), 2f, 2f, 0, event.getEntity().rotationYaw, event.getPartialRenderTick());
                //event.getRenderer().

                GL11.glPushMatrix();

                MatrixStack matrixstack = new MatrixStack();
                matrixstack.func_227861_a_(event.getEntity().getPosition().getX(), event.getEntity().getPosition().getY(), event.getEntity().getPosition().getZ());

                EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
                entityrenderermanager.setRenderShadow(false);
                IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().func_228019_au_().func_228487_b_();
                entityrenderermanager.func_229084_a_(event.getEntity(), 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
                irendertypebuffer$impl.func_228461_a_();
                entityrenderermanager.setRenderShadow(true);

                GL11.glPopMatrix();

                //event.getRenderer().entityModel.func_225597_a_(p_225623_1_, f5, f8, f7, f2, f6);

                alreadyActivated = false;
            }
        }
    }*/

    /*@SubscribeEvent
    public void renderWorldLastEvent(TickEvent.RenderTickEvent evt){
    }*/

    /*@SubscribeEvent
    public void onRenderLivingEvent(RenderLivingEvent.Pre event) {
        if(event.getEntity() instanceof ShadowEntity) {
            //event.setCanceled(true);
            RenderSystem.color4f(1.0f, 0f, 0f, 1.0f);
        }
    }
    @SubscribeEvent
    public void onRenderLivingEvent(RenderLivingEvent.Post event) {
        if(event.getEntity() instanceof ShadowEntity) {
            //event.setCanceled(true);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }*/

    /*@SubscribeEvent
    public void onRenderLivingEvent(RenderLivingEvent.Pre event) {
        if(event.getEntity() instanceof ShadowEntity) {
            if(isKeyDown()) {
                event.getEntity().setGlowing(true);
                //World w = event.getEntity().getEntityWorld();
                //w.getScoreboard().createTeam("shadow");
                //w.getScoreboard().addPlayerToTeam(event.getEntity().getUniqueID().toString(), w.getScoreboard().getTeam("shadow"));
            }
            //event.getEntity().getEntityWorld().getScoreboard().entityHasObjective()
            //event.getEntity().setTeam();//REDRED
            //event.getEntity().setInvisible(true);
        }
    }
    @SubscribeEvent
    public void onRenderLivingEvent(RenderLivingEvent.Post event) {
        if(event.getEntity() instanceof ShadowEntity) {
            //System.out.println(event.getEntity().func_225510_bt_());
            if(!isKeyDown()) {
                event.getEntity().setGlowing(false);

                //World w = event.getEntity().getEntityWorld();
                //w.getScoreboard().removeTeam(w.getScoreboard().getTeam("shadow"));
            }
            //event.getEntity().setInvisible(false);
        }
    }*/
}
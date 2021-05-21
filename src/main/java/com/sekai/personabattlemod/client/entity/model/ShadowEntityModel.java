package com.sekai.personabattlemod.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sekai.personabattlemod.entities.ShadowEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ShadowEntityModel<T extends ShadowEntity> extends EntityModel<T> {
    private final ModelRenderer Body;
    private final ModelRenderer AnimBody;
    private final ModelRenderer Cape;
    private final ModelRenderer CapePiece;
    private final ModelRenderer CapePiece2;
    private final ModelRenderer CapePiece3;
    private final ModelRenderer CapePiece4;
    private final ModelRenderer Head;

    public ShadowEntityModel() {
        textureWidth = 64;
        textureHeight = 32;

        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 24.0F, 0.0F);
        Body.setTextureOffset(14, 21).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        AnimBody = new ModelRenderer(this);
        AnimBody.setRotationPoint(0.0F, -8.0F, 0.0F);
        Body.addChild(AnimBody);


        Cape = new ModelRenderer(this);
        Cape.setRotationPoint(0.0F, 0.0F, 0.0F);
        AnimBody.addChild(Cape);


        CapePiece = new ModelRenderer(this);
        CapePiece.setRotationPoint(-1.0F, 0.0F, 0.0F);
        Cape.addChild(CapePiece);
        setRotationAngle(CapePiece, 0.0F, 0.0F, 0.0873F);
        CapePiece.setTextureOffset(43, 5).addBox(0.0F, 8.0F, -1.0F, 1.0F, -8.0F, 2.0F, 0.0F, false);

        CapePiece2 = new ModelRenderer(this);
        CapePiece2.setRotationPoint(1.0F, 0.0F, 0.0F);
        Cape.addChild(CapePiece2);
        setRotationAngle(CapePiece2, 0.0F, 0.0F, -0.0873F);
        CapePiece2.setTextureOffset(43, 5).addBox(-1.0F, 8.0F, -1.0F, 1.0F, -8.0F, 2.0F, 0.0F, false);

        CapePiece3 = new ModelRenderer(this);
        CapePiece3.setRotationPoint(0.0F, 0.0F, 1.0F);
        Cape.addChild(CapePiece3);
        setRotationAngle(CapePiece3, 0.0873F, 0.0F, 0.0F);
        CapePiece3.setTextureOffset(43, 5).addBox(-1.0F, 8.0F, -1.0F, 2.0F, -8.0F, 1.0F, 0.0F, false);

        CapePiece4 = new ModelRenderer(this);
        CapePiece4.setRotationPoint(0.0F, 0.0F, -1.0F);
        Cape.addChild(CapePiece4);
        setRotationAngle(CapePiece4, -0.0873F, 0.0F, 0.0F);
        CapePiece4.setTextureOffset(43, 5).addBox(-1.0F, 8.0F, 0.0F, 2.0F, -8.0F, 1.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, -8.0F, 0.0F);
        Body.addChild(Head);
        Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
        //this.Head.rotateAngleX = this.func_205060_a(this.bipedHead.rotateAngleX, p_225597_6_ * ((float)Math.PI / 180F), this.swimAnimation);
        this.Head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
    }

    @Override
    public void setLivingAnimations(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public ModelRenderer getHead() {
        return Head;
    }
}
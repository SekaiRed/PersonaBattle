package com.sekai.personabattlemod.client.entity.render;

import com.sekai.personabattlemod.PersonaBattle;
import com.sekai.personabattlemod.client.entity.model.ShadowEntityModel;
import com.sekai.personabattlemod.entities.ShadowEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class ShadowEntityRender extends MobRenderer<ShadowEntity, ShadowEntityModel<ShadowEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PersonaBattle.MOD_ID,
            "textures/entity/shadow_entity.png");


    public ShadowEntityRender(EntityRendererManager rendererManagerIn) {
        //that last parameter is shadow size
        super(rendererManagerIn, new ShadowEntityModel<ShadowEntity>(), 0.4f);
    }

    @Override
    public ResourceLocation getEntityTexture(ShadowEntity entity) {
        return TEXTURE;
    }


}

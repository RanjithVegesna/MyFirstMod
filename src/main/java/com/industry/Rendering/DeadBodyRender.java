package com.industry.Rendering;

import com.industry.Mod;
import com.industry.entities.DeadBodyEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Identifier;

public class DeadBodyRender extends LivingEntityRenderer<DeadBodyEntity, PlayerEntityModel> {
    public DeadBodyRender(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return Identifier.of(Mod.MOD_ID, "textures/entity/graviton.png");
    }

    private PlayerEntityModel<DeadBodyEntity> model;

    @Override
    public void render(DeadBodyEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

    }
}

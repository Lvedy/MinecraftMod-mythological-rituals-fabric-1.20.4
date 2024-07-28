package lvedy.mr.special.Entity.client;

import lvedy.mr.MythologicalRituals;
import lvedy.mr.special.Entity.custom.EndProtectorEntity;
import lvedy.mr.special.Entity.custom.ShadowEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShadowRender extends GeoEntityRenderer<ShadowEntity> {
    public ShadowRender(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ShadowModel());
    }

    public Identifier getTextureLocation(ShadowEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"textures/entity/shadow.png");
    }

    @Override
    public void render(ShadowEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.scale(1.0f,1.0f,1.0f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

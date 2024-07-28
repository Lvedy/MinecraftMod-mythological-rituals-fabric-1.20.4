package lvedy.mr.special.Entity.client;

import lvedy.mr.MythologicalRituals;
import lvedy.mr.special.Entity.custom.EndProtectorEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EndProtectorRender extends GeoEntityRenderer<EndProtectorEntity> {
    public EndProtectorRender(EntityRendererFactory.Context renderManager) {
        super(renderManager, new EndProtectorModel());
    }

    @Override
    public Identifier getTextureLocation(EndProtectorEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"textures/entity/endprotector.png");
    }

    @Override
    public void render(EndProtectorEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
            poseStack.scale(1.8f,1.8f,1.8f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

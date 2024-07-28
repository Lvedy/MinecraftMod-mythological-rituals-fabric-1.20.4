package lvedy.mr.special.Entity.client;

import lvedy.mr.MythologicalRituals;
import lvedy.mr.special.Entity.custom.EndProtectorEntity;
import lvedy.mr.special.Entity.custom.ScarletPhantomEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ScarletPhantomRender extends GeoEntityRenderer<ScarletPhantomEntity> {
    public ScarletPhantomRender(EntityRendererFactory.Context renderManager) {
        super(renderManager,  new ScarletPhantomModel());
    }

    public Identifier getTextureLocation(ScarletPhantomEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"textures/entity/scarletphantom.png");
    }

    public void render(ScarletPhantomEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.scale(1f,1f,1f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

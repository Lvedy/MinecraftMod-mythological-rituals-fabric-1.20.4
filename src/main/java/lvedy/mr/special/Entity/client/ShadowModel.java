package lvedy.mr.special.Entity.client;

import lvedy.mr.MythologicalRituals;
import lvedy.mr.special.Entity.custom.ShadowEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ShadowModel extends GeoModel<ShadowEntity> {
    @Override
    public Identifier getModelResource(ShadowEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"geo/shadow.geo.json");
    }

    @Override
    public Identifier getTextureResource(ShadowEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"textures/entity/shadow.png");
    }

    @Override
    public Identifier getAnimationResource(ShadowEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"animations/shadow.animation.json");
    }

    public void setCustomAnimations(ShadowEntity animatable, long instanceId, AnimationState<ShadowEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if((head != null)){
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}

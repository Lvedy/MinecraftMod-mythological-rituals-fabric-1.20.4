package lvedy.mr.special.Entity.client;

import lvedy.mr.MythologicalRituals;
import lvedy.mr.special.Entity.custom.ScarletPhantomEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ScarletPhantomModel extends GeoModel<ScarletPhantomEntity> {
    @Override
    public Identifier getModelResource(ScarletPhantomEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"geo/scarletphantom.geo.json");
    }

    @Override
    public Identifier getTextureResource(ScarletPhantomEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"textures/entity/scarletphantom.png");
    }

    @Override
    public Identifier getAnimationResource(ScarletPhantomEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"animations/scarletphantom.animation.json");
    }

    public void setCustomAnimations(ScarletPhantomEntity animatable, long instanceId, AnimationState<ScarletPhantomEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
    }
}

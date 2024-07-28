package lvedy.mr.special.Entity.client;

import lvedy.mr.MythologicalRituals;
import lvedy.mr.special.Entity.custom.EndProtectorEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class EndProtectorModel extends GeoModel<EndProtectorEntity> {
    public Identifier getModelResource(EndProtectorEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"geo/endprotector.geo.json");
    }

    @Override
    public Identifier getTextureResource(EndProtectorEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"textures/entity/endprotector.png");
    }

    @Override
    public Identifier getAnimationResource(EndProtectorEntity animatable) {
        return new Identifier(MythologicalRituals.MOD_ID,"animations/endprotector.animation.json");
    }

    @Override
    public void setCustomAnimations(EndProtectorEntity animatable, long instanceId, AnimationState<EndProtectorEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if((head != null) && (!animatable.isAttacking2())){
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}

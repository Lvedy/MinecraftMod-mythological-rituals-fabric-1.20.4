package lvedy.mr.registry;

import lvedy.mr.MythologicalRituals;
import lvedy.mr.special.Entity.client.ScarletPhantomRender;
import lvedy.mr.special.Entity.client.ShadowRender;
import lvedy.mr.special.Entity.custom.EndProtectorEntity;
import lvedy.mr.special.Entity.custom.ScarletPhantomEntity;
import lvedy.mr.special.Entity.custom.ShadowEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import lvedy.mr.special.Entity.client.EndProtectorRender;

public class ModEntity {
    public static final EntityType<EndProtectorEntity> END_PROTECTOR = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MythologicalRituals.MOD_ID,"end_protector"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE,EndProtectorEntity::new).
                    dimensions(EntityDimensions.fixed(1.0f,3.0f)).build());
    public static final EntityType<ScarletPhantomEntity> SCARLET_PHANTOM = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MythologicalRituals.MOD_ID,"scarlet_phantom"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE,ScarletPhantomEntity::new).
                    dimensions(EntityDimensions.fixed(1.0f,2.0f)).build());
    public static final EntityType<ShadowEntity> SHADOW = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MythologicalRituals.MOD_ID,"shadow"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE,ShadowEntity::new).
                    dimensions(EntityDimensions.fixed(1.0f,2.0f)).build());

    public static void main_registerEntity(){
        //end_protector
        FabricDefaultAttributeRegistry.register(ModEntity.END_PROTECTOR,EndProtectorEntity.createrEndProtector());
        EntityRendererRegistry.register(ModEntity.END_PROTECTOR, EndProtectorRender::new);
        //scarlet_phantom
        FabricDefaultAttributeRegistry.register(ModEntity.SCARLET_PHANTOM, ScarletPhantomEntity.createrScarletPhantom());
        EntityRendererRegistry.register(ModEntity.SCARLET_PHANTOM, ScarletPhantomRender::new);
        //shadow
        FabricDefaultAttributeRegistry.register(ModEntity.SHADOW, ShadowEntity.createrShadow());
        EntityRendererRegistry.register(ModEntity.SHADOW, ShadowRender::new);
    }
}

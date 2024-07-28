package lvedy.mr.registry;

import lvedy.mr.MythologicalRituals;
import lvedy.mr.special.ItemType.CommonBlockItem;
import lvedy.mr.special.ItemType.CommonItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final CommonBlockItem shixiang = registerItem("shixiang",new CommonBlockItem(ModBlock.shixiang,new FabricItemSettings()));
    public static final CommonBlockItem PUPPET_BLOCK = registerItem("puppet_block",new CommonBlockItem(ModBlock.puppetBlock,new FabricItemSettings()));
    public static final CommonItem SCARLET_CORD = registerItem("scarlet_cord",new CommonItem(new FabricItemSettings(),"item_introduce.scarlet_cord", Formatting.GRAY));
    public static final Item END_PROTECTOR_SPAWN_EGG = registerItem("end_protector_spawn_egg",new SpawnEggItem(ModEntity.END_PROTECTOR,0xf2f5b9, 0xaf91c0,new FabricItemSettings()));
    public static final Item SCARLET_PHANTOM_SPAWN_EGG = registerItem("scarlet_phantom_spawn_egg",new SpawnEggItem(ModEntity.SCARLET_PHANTOM,0x7b3535, 0x0b1212,new FabricItemSettings()));
    public static final Item SHADOW_SPAWN_EGG = registerItem("shadow_spawn_egg",new SpawnEggItem(ModEntity.SHADOW,0x0b1212, 0x7b3535,new FabricItemSettings()));
    public static SpawnEggItem registerItem(String name, SpawnEggItem item){
        SpawnEggItem registerItem;
        registerItem = Registry.register(Registries.ITEM, new Identifier(MythologicalRituals.MOD_ID, name), item);
        return registerItem;
    };
    public static CommonItem registerItem(String name, CommonItem item){
        CommonItem registerItem;
        registerItem = Registry.register(Registries.ITEM, new Identifier(MythologicalRituals.MOD_ID, name), item);
        return registerItem;
    };
    public static CommonBlockItem registerItem(String name, CommonBlockItem item){
        CommonBlockItem registerItem;
        registerItem = Registry.register(Registries.ITEM, new Identifier(MythologicalRituals.MOD_ID, name), item);
        return registerItem;
    };
    public static void main_registerItem(){

    }
}

package lvedy.mr.registry;

import lvedy.mr.MythologicalRituals;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup test_ItemGroup = Registry.register(Registries.ITEM_GROUP,
            new Identifier(MythologicalRituals.MOD_ID,"mr_item_group"),FabricItemGroup.builder().displayName(Text.translatable("itemgroup.mr")).
                    icon(()->new ItemStack(Items.SUNFLOWER)).entries((displayContext, entries) -> {
                        entries.add(ModItems.SCARLET_CORD);//从这里添加该选项卡所拥有的物品
                        entries.add(ModItems.shixiang);
                        entries.add(ModItems.PUPPET_BLOCK);
                        entries.add(ModItems.END_PROTECTOR_SPAWN_EGG);
                        entries.add(ModItems.SCARLET_PHANTOM_SPAWN_EGG);
                        entries.add(ModItems.SHADOW_SPAWN_EGG);
                    }).build());
    public static void main_registerItemGroup(){

    }
}

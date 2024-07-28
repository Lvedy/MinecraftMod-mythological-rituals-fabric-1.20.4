package lvedy.mr.registry;

import lvedy.mr.MythologicalRituals;
import lvedy.mr.special.Block.PuppetBlock;
import lvedy.mr.special.Block.Shixiang;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlock {
    public static final Shixiang shixiang = registerBlock("shixiang",new Shixiang(FabricBlockSettings.create().strength(2.0f,5.0f)));
    public static final PuppetBlock puppetBlock = registerBlock("puppet_block",new PuppetBlock(FabricBlockSettings.create().strength(2.0f,5.0f)));
    public static Shixiang registerBlock(String name, Shixiang block){
        Shixiang registerBlock;
        registerBlock = Registry.register(Registries.BLOCK, new Identifier(MythologicalRituals.MOD_ID, name), block);
        return registerBlock;
    };
    public static PuppetBlock registerBlock(String name, PuppetBlock block){
        PuppetBlock registerBlock;
        registerBlock = Registry.register(Registries.BLOCK, new Identifier(MythologicalRituals.MOD_ID, name), block);
        return registerBlock;
    };
    public static void main_registerBlock(){

    }
}

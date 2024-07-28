package lvedy.mr.registry;

import lvedy.mr.MythologicalRituals;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSound {
    public static final SoundEvent DONG = registerSoundEvent("dong");

    public static SoundEvent registerSoundEvent(String name){
        Identifier id =new Identifier(MythologicalRituals.MOD_ID,name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    };

    public static void main_registerSound(){

    }
}

package lvedy.mr.registry;

import lvedy.mr.special.Event.EndTickEvent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ModEvent {
    public static void main_registerEvent(){
        ServerTickEvents.END_WORLD_TICK.register(new EndTickEvent());
    }
}

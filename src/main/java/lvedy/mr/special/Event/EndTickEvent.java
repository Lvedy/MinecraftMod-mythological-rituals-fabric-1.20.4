package lvedy.mr.special.Event;

import lvedy.mr.mixinOther.KillStats;
import lvedy.mr.mixinOther.PlayerEntityDataSaver;
import lvedy.mr.registry.ModEntity;
import lvedy.mr.special.Entity.custom.EndProtectorEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class EndTickEvent implements ServerTickEvents.EndWorldTick {

    @Override
    public void onEndTick(ServerWorld world) {
        for(ServerPlayerEntity player: world.getPlayers()){
            if(KillStats.getEndermanKillStats((PlayerEntityDataSaver)player) == 221){
                KillStats.setEndermanKillStats((PlayerEntityDataSaver)player,0);
                EndProtectorEntity endProtectorEntity = ModEntity.END_PROTECTOR.create(world);
                endProtectorEntity.refreshPositionAfterTeleport(player.getX(),player.getY(),player.getZ());
                world.spawnEntity(endProtectorEntity);
            }
            if(KillStats.getEndermanKillStats((PlayerEntityDataSaver)player) == 200){
                player.sendMessage(Text.translatable("textkey.end_protector_summon"));
                KillStats.setEndermanKillStats((PlayerEntityDataSaver)player,201);
            }
        }
    }
}

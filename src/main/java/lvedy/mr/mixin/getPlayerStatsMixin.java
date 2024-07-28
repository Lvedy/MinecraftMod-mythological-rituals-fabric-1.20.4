package lvedy.mr.mixin;

import lvedy.mr.mixinOther.KillStats;
import lvedy.mr.mixinOther.PlayerEntityDataSaver;
import net.minecraft.client.font.UnihexFont;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;


@Mixin(PlayerEntity.class)
public class getPlayerStatsMixin implements PlayerEntityDataSaver {
    private NbtCompound persistentData;
    @Override
    public NbtCompound getPersistentData() {
         if(this.persistentData == null){
             this.persistentData = new NbtCompound();
         }
         return persistentData;
    }

    @Inject(method = "writeCustomDataToNbt",at = @At("HEAD"))
    protected void InjectWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo ci){
        if(persistentData != null){
            nbt.put("mr.EndermanKill",persistentData);
        }
    }

    @Inject(method = "readCustomDataFromNbt",at = @At("HEAD"))
    protected void InjectReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
        if(nbt.contains("mr.EndermanKill",0)){
            persistentData = nbt.getCompound("mr.EndermanKill");
        }
    }

    @Inject(method = "onKilledOther",at = @At("HEAD"))
    protected void InjectOnKilledOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir){
        if((other instanceof EndermanEntity) && (other.getWorld().getDimensionKey() == DimensionTypes.THE_END)  ){
            KillStats.addEndermanKillStats(this,1);
        }
    }
}


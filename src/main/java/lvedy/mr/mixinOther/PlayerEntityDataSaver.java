package lvedy.mr.mixinOther;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public interface PlayerEntityDataSaver {
    NbtCompound getPersistentData();
}

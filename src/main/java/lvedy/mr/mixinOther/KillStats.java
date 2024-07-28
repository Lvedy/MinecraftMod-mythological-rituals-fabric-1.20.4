package lvedy.mr.mixinOther;

import net.minecraft.nbt.NbtCompound;

public class KillStats {

    public static int addEndermanKillStats(PlayerEntityDataSaver player,int amount) {
        NbtCompound nbt = player.getPersistentData();
        int EndermanKill = nbt.getInt("EndermanKill");
        EndermanKill = EndermanKill + amount;
        nbt.putInt("EndermanKill",EndermanKill);
        return EndermanKill;
    }

    public static int setEndermanKillStats(PlayerEntityDataSaver player,int amount) {
        NbtCompound nbt = player.getPersistentData();
        int EndermanKill = nbt.getInt("EndermanKill");
        EndermanKill = amount;
        nbt.putInt("EndermanKill",EndermanKill);
        return EndermanKill;
    }
    public static int getEndermanKillStats(PlayerEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getInt("EndermanKill");
    }
}

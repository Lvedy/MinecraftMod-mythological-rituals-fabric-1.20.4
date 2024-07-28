package lvedy.mr.special.Block;

import lvedy.mr.mixinOther.KillStats;
import lvedy.mr.mixinOther.PlayerEntityDataSaver;
import lvedy.mr.registry.ModSound;
import lvedy.mr.special.BlockType.FacingAndWaterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;


public class Shixiang extends FacingAndWaterBlock {
    public Shixiang(Settings settings) {
        super(settings);
    }
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context){
        SHAPE = Block.createCuboidShape(6, 0, 6, 10, 10, 10);
        return SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        //末影人击杀数获取案例
        player.getWorld().playSound(null,pos, ModSound.DONG, SoundCategory.BLOCKS,5,1);
        int a = KillStats.getEndermanKillStats((PlayerEntityDataSaver) player);
        String b = Integer.toString(a);
        if(!world.isClient)
            player.sendMessage(Text.of(b));
        return ActionResult.SUCCESS;
    }
}

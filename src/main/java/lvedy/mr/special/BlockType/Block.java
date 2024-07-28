package lvedy.mr.special.BlockType;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class Block  extends HorizontalFacingBlock {
    protected Block(Settings settings) {
        super(settings);
    }
    protected static VoxelShape SHAPE;
    //继承后重写此方法来设置碰撞箱
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context){
        SHAPE = net.minecraft.block.Block.createCuboidShape(0, 0, 0, 16, 16, 16);
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }
}

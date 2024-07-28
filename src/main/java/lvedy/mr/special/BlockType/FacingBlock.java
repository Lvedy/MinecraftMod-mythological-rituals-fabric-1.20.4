package lvedy.mr.special.BlockType;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class FacingBlock extends HorizontalFacingBlock{
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public FacingBlock(Settings settings){
        super(settings);
    }
    protected static VoxelShape SHAPE;
    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }
    //继承后重写此方法来设置碰撞箱
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context){
        SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);
        return SHAPE;
    }
    public BlockState getPlacementState(ItemPlacementContext ctx){
        return this.getDefaultState().with(FACING,ctx.getHorizontalPlayerFacing().getOpposite());
    }
    public BlockState rotate(BlockState state, BlockRotation rotation){
        return state.with(FACING,rotation.rotate(state.get(FACING)));
    }
    public BlockState mirror(BlockState state, BlockMirror mirror){
        return state.rotate((mirror.getRotation(state.get(FACING))));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}

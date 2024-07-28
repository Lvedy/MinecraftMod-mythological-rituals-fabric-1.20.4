package lvedy.mr.special.BlockType;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class FacingAndWaterBlock extends HorizontalFacingBlock implements Waterloggable{
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public FacingAndWaterBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(WATERLOGGED, false));
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
        return (BlockState) this.getDefaultState().with(FACING,ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED,ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }
    public BlockState rotate(BlockState state, BlockRotation rotation){
        return state.with(FACING,rotation.rotate(state.get(FACING)));
    }
    public BlockState mirror(BlockState state, BlockMirror mirror){
        return state.rotate((mirror.getRotation(state.get(FACING))));
    }
    //让方块可以含水
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING,WATERLOGGED);
    }
}

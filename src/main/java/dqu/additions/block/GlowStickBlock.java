package dqu.additions.block;

import dqu.additions.AdditionalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class GlowStickBlock extends FallingBlock {
    public static final VoxelShape shape = Block.box(2, 0, 2,14, 2, 14);
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");

    public GlowStickBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(FLIPPED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(FLIPPED);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return shape;
    }

    @Override
    public Item asItem() {
        return AdditionalRegistry.GLOW_STICK_ITEM.get();
    }
}

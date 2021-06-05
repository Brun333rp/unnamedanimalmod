package teamdraco.unnamedanimalmod.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.item.Item.Properties;

public class UAMBucketItem extends BucketItem {
    private final Supplier<EntityType<?>> entityType;

    public UAMBucketItem(Supplier<EntityType<?>> entityType, Properties properties) {
        super(Fluids.EMPTY, properties);
        this.entityType = entityType;
    }

    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        else {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            }
            else {
                blockpos1 = blockpos.relative(direction);
            }
            Supplier<EntityType<?>> entitytype = entityType;
            Entity entityType = entitytype.get().spawn((ServerWorld) world, itemstack, context.getPlayer(), blockpos1, SpawnReason.BUCKET, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (entityType != null) {
                if(!context.getPlayer().abilities.instabuild) {
                    itemstack.shrink(1);
                    context.getPlayer().addItem(new ItemStack(Items.BUCKET));
                }

                playEmptySound(context.getPlayer(), world, blockpos);
            }
            return ActionResultType.SUCCESS;
        }
    }

    protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }
}
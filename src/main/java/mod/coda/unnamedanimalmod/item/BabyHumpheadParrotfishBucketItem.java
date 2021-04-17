package mod.coda.unnamedanimalmod.item;

import mod.coda.unnamedanimalmod.entity.HumpheadParrotfishEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.function.Supplier;

public class BabyHumpheadParrotfishBucketItem extends BucketItem {
    private final Supplier<? extends Fluid> fluid;

    public BabyHumpheadParrotfishBucketItem(Supplier<? extends EntityType<?>> entityType, Supplier<? extends Fluid> fluid, Properties builder) {
        super(fluid, builder);
        this.fluid = fluid;
        this.entityTypeSupplier = entityType;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        BlockRayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
        ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
        if (ret != null) return ret;
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return ActionResult.resultPass(itemstack);
        } else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.resultPass(itemstack);
        } else {
            BlockPos blockpos = raytraceresult.getPos();
            Direction direction = raytraceresult.getFace();
            BlockPos blockpos1 = blockpos.offset(direction);
            if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos1, direction, itemstack)) {
                BlockState blockstate = worldIn.getBlockState(blockpos);
                BlockPos blockpos2 = blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer) blockstate.getBlock()).canContainFluid(worldIn, blockpos, blockstate, fluid.get()) ? blockpos : blockpos1;
                this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos2, raytraceresult);
                if (worldIn instanceof ServerWorld) this.placeEntity((ServerWorld)worldIn, itemstack, blockpos2);
                if (playerIn instanceof ServerPlayerEntity) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerIn, blockpos2, itemstack);
                }

                playerIn.addStat(Stats.ITEM_USED.get(this));
                return ActionResult.func_233538_a_(this.emptyBucket(itemstack, playerIn), worldIn.isRemote());
            } else {
                return ActionResult.resultFail(itemstack);
            }
        }
    }

    private void placeEntity(ServerWorld worldIn, ItemStack stack, BlockPos pos) {
        Entity entity = this.entityTypeSupplier.get().spawn(worldIn, stack, (PlayerEntity)null, pos, SpawnReason.BUCKET, true, false);
        if (entity != null) {
            if (entity instanceof HumpheadParrotfishEntity) {
                ((HumpheadParrotfishEntity)entity).setGrowingAge(-24000);
            }
        }
    }

    private final java.util.function.Supplier<? extends EntityType<?>> entityTypeSupplier;
    protected EntityType<?> getEntityType() {
        return entityTypeSupplier.get();
    }

    @Override
    protected ItemStack emptyBucket(ItemStack stack, PlayerEntity player) {
         return !player.abilities.isCreativeMode ? new ItemStack(Items.BUCKET) : stack;
    }
}

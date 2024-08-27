package org.ajls.megawallsclasses.nmsmodify;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.ajls.megawallsclasses.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftMob;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;

public class TamedTeleport {
//    public final RandomSource random = null;
//
//
//    public void tryToTeleportToOwner(LivingEntity entity) {
//        this.random = world != null && !world.purpurConfig.entitySharedRandom ? RandomSource.create() : SHARED_RANDOM;
//        net.minecraft.world.entity.LivingEntity entityliving = entity;
//        if (entityliving != null) {
//            this.teleportToAroundBlockPos(entityliving.blockPosition());
//        }
//
//    }
//
//    private void teleportToAroundBlockPos(BlockPos pos) {
//        for(int i = 0; i < 10; ++i) {
//            int j = super.random.nextIntBetweenInclusive(-3, 3);
//            int k = super.random.nextIntBetweenInclusive(-3, 3);
//            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
//                int l = super.random.nextIntBetweenInclusive(-1, 1);
//                if (this.maybeTeleportTo(pos.getX() + j, pos.getY() + l, pos.getZ() + k)) {
//                    return;
//                }
//            }
//        }
//
//    }
    public void teleportToOwner(org.bukkit.entity.Mob mobWolf, org.bukkit.entity.LivingEntity livingEntityPlayer) {
        CraftMob craftMobWolf = (CraftMob) mobWolf;
        CraftLivingEntity craftLivingEntityPlayer = (CraftLivingEntity) livingEntityPlayer;
        Mob wolf = craftMobWolf.getHandle();
        LivingEntity entityliving = craftLivingEntityPlayer.getHandle();
        BlockPos pos = entityliving.blockPosition();
        for(int i = 0; i < 10; ++i) {
            int j = Utils.random(-3, 3);
            int k = Utils.random(-3, 3);
            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                int l = Utils.random(-1, 1);
                if (this.maybeTeleportTo(wolf, pos.getX() + j, pos.getY() + l, pos.getZ() + k)) {
                    return;
                }
            }
        }

    }

    private boolean maybeTeleportTo(Mob wolf, int x, int y, int z) {
        if (!this.canTeleportTo(wolf, new BlockPos(x, y, z))) {
            return false;
        } else {
            EntityTeleportEvent event = CraftEventFactory.callEntityTeleportEvent(wolf, (double)x + 0.5, (double)y, (double)z + 0.5);
            if (!event.isCancelled() && event.getTo() != null) {
                Location to = event.getTo();
                wolf.moveTo(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
                Mob mob = (Mob) wolf;
                mob.getNavigation().stop();
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean canTeleportTo(Mob wolf, BlockPos pos) {
        PathType pathtype = WalkNodeEvaluator.getPathTypeStatic(wolf, pos);
        if (pathtype != PathType.WALKABLE) {
            return false;
        } else {
            BlockState iblockdata = wolf.level().getBlockState(pos.below());
            if (iblockdata.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockposition1 = pos.subtract(wolf.blockPosition());
                return wolf.level().noCollision(wolf, wolf.getBoundingBox().move(blockposition1));
            }
        }
    }

}

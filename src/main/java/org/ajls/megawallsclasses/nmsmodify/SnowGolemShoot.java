package org.ajls.megawallsclasses.nmsmodify;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.Snowball;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class SnowGolemShoot {

    public static void snowGolemShoot(org.bukkit.entity.LivingEntity livingEntityShooter, LivingEntity livingEntityTarget) {
        CraftLivingEntity craftLivingEntityShooter = (CraftLivingEntity) livingEntityShooter;
        CraftLivingEntity craftLivingEntityTarget = (CraftLivingEntity) livingEntityTarget;
        net.minecraft.world.entity.LivingEntity shooter = craftLivingEntityShooter.getHandle();
        net.minecraft.world.entity.LivingEntity target = craftLivingEntityTarget.getHandle();
        Snowball entitysnowball = new Snowball(shooter.level(), shooter);
        double d0 = target.getEyeY() - 1.100000023841858;
        double d1 = target.getX() - shooter.getX();
        double d2 = d0 - entitysnowball.getY();
        double d3 = target.getZ() - shooter.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.20000000298023224;
        entitysnowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        shooter.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
        shooter.level().addFreshEntity(entitysnowball);
    }

}

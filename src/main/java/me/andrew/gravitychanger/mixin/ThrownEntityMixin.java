package me.andrew.gravitychanger.mixin;

import me.andrew.gravitychanger.accessor.EntityAccessor;
import me.andrew.gravitychanger.api.GravityChangerAPI;
import me.andrew.gravitychanger.util.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin implements EntityAccessor {

    @Shadow protected abstract float getGravity();

    @Override
    public Direction gravitychanger$getAppliedGravityDirection() {
        return GravityChangerAPI.getGravityDirection((ThrownEntity)(Object)this);
    }

    @ModifyVariable(
            method = "tick",
            at = @At(
                    value = "STORE"
            )
            ,ordinal = 0
    )
    public Vec3d tick(Vec3d modify){
        //if(this instanceof RotatableEntityAccessor) {
            modify = new Vec3d(modify.x, modify.y + this.getGravity(), modify.z);
            modify = RotationUtil.vecWorldToPlayer(modify, this.gravitychanger$getAppliedGravityDirection());
            modify = new Vec3d(modify.x, modify.y - this.getGravity(), modify.z);
            modify = RotationUtil.vecPlayerToWorld(modify, this.gravitychanger$getAppliedGravityDirection());
       // }
        return  modify;
    }

    @ModifyArgs(
            method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/thrown/ThrownEntity;<init>(Lnet/minecraft/entity/EntityType;DDDLnet/minecraft/world/World;)V",
                    ordinal = 0
            )
    )
    private static void modifyargs_init_init_0(Args args, EntityType<? extends ThrownEntity> type, LivingEntity owner, World world) {
        Direction gravityDirection = ((EntityAccessor) owner).gravitychanger$getAppliedGravityDirection();
        if(gravityDirection == Direction.DOWN) return;

        Vec3d pos = owner.getEyePos().subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.10000000149011612D, 0.0D, gravityDirection));
        args.set(1, pos.x);
        args.set(2, pos.y);
        args.set(3, pos.z);
    }
}

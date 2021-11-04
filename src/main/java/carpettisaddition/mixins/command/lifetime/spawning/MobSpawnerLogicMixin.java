package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin
{
	private Entity spawnedEntity$lifeTimeTracker;

	@ModifyArg(
			method = "update",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;shouldCreateNewEntityWithPassenger(Lnet/minecraft/entity/Entity;)Z"
			),
			index = 0
	)
	private Entity recordSpawnedEntity(Entity entity)
	{
		this.spawnedEntity$lifeTimeTracker = entity;
		return entity;
	}

	@Inject(
			method = "update",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"
			)
	)
	private void onSpawnerLogicSpawnEntityLifeTimeTracker(CallbackInfo ci)
	{
		if (this.spawnedEntity$lifeTimeTracker != null)
		{
			((LifetimeTrackerTarget)this.spawnedEntity$lifeTimeTracker).recordSpawning(LiteralSpawningReason.SPAWNER);
		}
	}
}

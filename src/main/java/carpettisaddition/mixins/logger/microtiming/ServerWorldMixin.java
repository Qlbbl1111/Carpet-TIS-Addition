package carpettisaddition.mixins.logger.microtiming;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.interfaces.IServerWorld;
import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import net.minecraft.block.Block;
import net.minecraft.class_6757;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements IServerWorld
{
	@Shadow @Final private class_6757<Block> blockTickScheduler;
	@Shadow @Final private class_6757<Fluid> fluidTickScheduler;

	private MicroTimingLogger microTimingLogger;

	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed)
	{
		super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
	}

	@Inject(
			method = "<init>",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		this.microTimingLogger = new MicroTimingLogger((ServerWorld)(Object)this);
		((ITileTickListWithServerWorld)this.blockTickScheduler).setServerWorld((ServerWorld)(Object)this);
		((ITileTickListWithServerWorld)this.fluidTickScheduler).setServerWorld((ServerWorld)(Object)this);
	}

	@Override
	public MicroTimingLogger getMicroTimingLogger()
	{
		return this.microTimingLogger;
	}
}

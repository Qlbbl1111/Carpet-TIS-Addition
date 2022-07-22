package carpettisaddition.mixins.rule.lightEngineMaxBatchSize;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(DummyClass.class)
public abstract class ServerLightingProviderMixin
{
	// fabric carpet already has rule lightEngineMaxBatchSize in 1.16+
}
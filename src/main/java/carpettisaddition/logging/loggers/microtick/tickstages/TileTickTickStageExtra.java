package carpettisaddition.logging.loggers.microtick.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.utils.ToTextAble;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;

public class TileTickTickStageExtra implements ToTextAble
{
	private final ScheduledTick<?> nextTickListEntry;
	private final int order;

	public TileTickTickStageExtra(ScheduledTick<?> nextTickListEntry, int order)
	{
		this.nextTickListEntry = nextTickListEntry;
		this.order = order;
	}

	@Override
	public BaseText toText()
	{
		BlockPos pos = this.nextTickListEntry.pos;
		TickPriority priority = this.nextTickListEntry.priority;
		Object target = this.nextTickListEntry.getObject();
		BaseText text = Messenger.c(
				String.format("w Order: %d\n", this.order),
				String.format("w Priority: %d (%s)\n", priority.getIndex(), priority),
				String.format("w Position: [%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ())
		);
		if (target instanceof Block)
		{
			text = Messenger.c(
					"w Block: ",
					MicroTickUtil.getTranslatedName((Block)target),
					"w \n",
					text
			);
		}
		return text;
	}
}
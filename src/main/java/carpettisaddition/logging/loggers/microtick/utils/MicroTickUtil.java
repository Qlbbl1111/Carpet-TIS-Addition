package carpettisaddition.logging.loggers.microtick.utils;

import carpet.utils.Messenger;
import carpet.utils.WoolTool;
import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.utils.Util;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.BaseText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;

public class MicroTickUtil
{
	public static final Direction[] DIRECTION_VALUES = Direction.values();
	private static final Map<DyeColor, String> COLOR_STYLE = Maps.newHashMap();
	static
	{
		COLOR_STYLE.put(DyeColor.WHITE, "w");
		COLOR_STYLE.put(DyeColor.ORANGE, "d");
		COLOR_STYLE.put(DyeColor.MAGENTA, "m");
		COLOR_STYLE.put(DyeColor.LIGHT_BLUE, "c");
		COLOR_STYLE.put(DyeColor.YELLOW, "y");
		COLOR_STYLE.put(DyeColor.LIME, "l");
		COLOR_STYLE.put(DyeColor.PINK, "r");
		COLOR_STYLE.put(DyeColor.GRAY, "f");
		COLOR_STYLE.put(DyeColor.LIGHT_GRAY, "g");
		COLOR_STYLE.put(DyeColor.CYAN, "q");
		COLOR_STYLE.put(DyeColor.PURPLE, "p");
		COLOR_STYLE.put(DyeColor.BLUE, "v");
		COLOR_STYLE.put(DyeColor.BROWN, "n");
		COLOR_STYLE.put(DyeColor.GREEN, "e");
		COLOR_STYLE.put(DyeColor.RED, "r");
		COLOR_STYLE.put(DyeColor.BLACK, "k");
	}

	public static String getColorStyle(DyeColor color)
	{
		return COLOR_STYLE.getOrDefault(color, "w");
	}

	public static BaseText getColoredValue(Object value)
	{
		BaseText text = Messenger.s(value.toString());
		if (Boolean.TRUE.equals(value))
		{
			text.getStyle().setColor(Formatting.GREEN);
		}
		else if (Boolean.FALSE.equals(value))
		{
			text.getStyle().setColor(Formatting.RED);
		}
		return text;
	}

	public static BaseText getSuccessText(boolean bool, boolean showReturnValue)
	{
		BaseText hintText = bool ?
				Messenger.c("e " + MicroTickLoggerManager.tr("Successful")) :
				Messenger.c("r " + MicroTickLoggerManager.tr("Failed"));
		if (showReturnValue)
		{
			hintText.append(Messenger.c(
					String.format("w \n%s: ", MicroTickLoggerManager.tr("Return value")),
					getColoredValue(bool)
			));
		}
		return bool ?
				Util.getFancyText("e", Messenger.s("√"), hintText, null) :
				Util.getFancyText("r", Messenger.s("×"), hintText, null);
	}

	public static Optional<DyeColor> getWoolColor(World world, BlockPos pos)
	{
		if (!MicroTickLoggerManager.isLoggerActivated())
		{
			return Optional.empty();
		}
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		BlockPos woolPos = pos;

		if (block == Blocks.OBSERVER || block == Blocks.END_ROD ||
				block instanceof PistonBlock || block instanceof PistonExtensionBlock)
		{
			woolPos = pos.offset(state.get(Properties.FACING).getOpposite());
		}
		else if (block instanceof AbstractButtonBlock || block instanceof LeverBlock)
		{
			Direction facing;
			if (state.get(Properties.WALL_MOUNT_LOCATION) == WallMountLocation.FLOOR)
			{
				facing = Direction.UP;
			}
			else if (state.get(Properties.WALL_MOUNT_LOCATION) == WallMountLocation.CEILING)
			{
				facing = Direction.DOWN;
			}
			else
			{
				facing = state.get(Properties.HORIZONTAL_FACING);
			}
			woolPos = pos.offset(facing.getOpposite());
		}
		else if (block == Blocks.REDSTONE_WALL_TORCH || block == Blocks.TRIPWIRE_HOOK)
		{
			woolPos = pos.offset(state.get(Properties.HORIZONTAL_FACING).getOpposite());
		}
		else if (block instanceof AbstractRailBlock ||
				block instanceof AbstractRedstoneGateBlock ||
				block == Blocks.REDSTONE_TORCH ||
				block instanceof AbstractPressurePlateBlock)  // on block
		{
			woolPos = pos.down();
		}
		else
		{
			return Optional.empty();
		}

		return Optional.ofNullable(WoolTool.getWoolColorAtPosition(world.getWorld(), woolPos));
	}

	public static Optional<DyeColor> getEndRodWoolColor(World world, BlockPos pos)
	{
		for (Direction facing: DIRECTION_VALUES)
		{
			BlockPos blockEndRodPos = pos.offset(facing);
			BlockState iBlockState = world.getBlockState(blockEndRodPos);
			if (iBlockState.getBlock() == Blocks.END_ROD && iBlockState.get(FacingBlock.FACING).getOpposite() == facing)
			{
				Optional<DyeColor> color = MicroTickUtil.getWoolColor(world, blockEndRodPos);
				if (color.isPresent())
				{
					return color;
				}
			}
		}
		return Optional.empty();
	}

	public static Optional<DyeColor> getWoolOrEndRodWoolColor(World world, BlockPos pos)
	{
		Optional<DyeColor> optionalDyeColor = getWoolColor(world, pos);
		if (!optionalDyeColor.isPresent())
		{
			optionalDyeColor = getEndRodWoolColor(world, pos);
		}
		return optionalDyeColor;
	}

	public static BaseText getTranslatedText(Block block)
	{
		BaseText name = new TranslatableText(block.getTranslationKey());
		name.getStyle().setColor(Formatting.WHITE);
		return name;
	}

	public static Optional<?> getBlockStateProperty(BlockState blockState, Property<?> property)
	{
		try
		{
			return Optional.of(blockState.get(property));
		}
		catch (IllegalArgumentException ignored)
		{
			return Optional.empty();
		}
	}
}

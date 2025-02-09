package com.guhao.star.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class CobwebBreakEvent {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            execute(event, event.player.level, event.player.getX(), event.player.getY(), event.player.getZ());
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z) {
        execute(null, world, x, y, z);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z) {
        if ((world.getBlockState(new BlockPos(x, y, z))).getBlock() == Blocks.COBWEB) {
            {
                BlockPos _pos = new BlockPos(x, y, z);
                Block.dropResources(world.getBlockState(_pos), world, new BlockPos(x, y, z), null);
                world.destroyBlock(_pos, false);
            }
        }
        if ((world.getBlockState(new BlockPos(x, y+1, z))).getBlock() == Blocks.COBWEB) {
            {
                BlockPos _pos = new BlockPos(x, y+1, z);
                Block.dropResources(world.getBlockState(_pos), world, new BlockPos(x, y+1, z), null);
                world.destroyBlock(_pos, false);
            }
        }
    }
}

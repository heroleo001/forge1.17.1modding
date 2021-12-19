package net.leo.herotech.item.custom;

import com.google.common.collect.ImmutableMap;
import net.leo.herotech.block.ModBlocks;
import net.leo.herotech.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class SmartBlowTorchItem extends Item {

    public static final Map<Block, Item> CAN_BLOW_TORCH =
            new ImmutableMap.Builder<Block, Item>()
                    .put(ModBlocks.TITANIUM_BLOCK.get(), ModItems.TITANIUM_NUGGET.get())
                    .put(Blocks.SAND, Blocks.GLASS.asItem())
                    .build();

    public SmartBlowTorchItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide){
            Level level = pContext.getLevel();
            BlockPos posClicked = pContext.getClickedPos();
            Block blockClicked = level.getBlockState(posClicked).getBlock();

            if (CAN_BLOW_TORCH.containsKey(blockClicked)){
                ItemEntity entityItem = new ItemEntity(level,
                        posClicked.getX(), posClicked.getY(), posClicked.getZ(),
                        new ItemStack(CAN_BLOW_TORCH.get(blockClicked), 1));

                level.destroyBlock(posClicked, false);
                level.addFreshEntity(entityItem);
                pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(), p -> {
                    p.broadcastBreakEvent(pContext.getHand());
                });
            }
        }
        return InteractionResult.SUCCESS;
    }
}

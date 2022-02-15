package com.mrcrayfish.backpacked.inventory;

import com.mrcrayfish.backpacked.Backpacked;
import com.mrcrayfish.backpacked.util.InventoryHelper;
import com.mrcrayfish.backpacked.util.PickpocketUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class BackpackInventory extends SimpleContainer
{
    private final Player player;
    private final ItemStack stack;

    public BackpackInventory(int cols, int rows, Player player, ItemStack stack)
    {
        super(rows * cols);
        this.player = player;
        this.stack = stack;
        this.loadBackpackContents(player);
    }

    private void loadBackpackContents(Player player)
    {
        CompoundTag compound = this.stack.getOrCreateTag();
        if(compound.contains("Items", Tag.TAG_LIST))
        {
            InventoryHelper.loadAllItems(compound.getList("Items", Tag.TAG_COMPOUND), this, player.level, player.position());
        }
    }

    public ItemStack getBackpackStack()
    {
        return this.stack;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return Backpacked.getBackpackStack(this.player).equals(this.stack) && (this.player.equals(player) || PickpocketUtil.canPickpocketEntity(this.player, player));
    }

    @Override
    public void setChanged()
    {
        super.setChanged();
        CompoundTag compound = this.stack.getOrCreateTag();
        compound.put("Items", InventoryHelper.saveAllItems(new ListTag(), this));
    }
}

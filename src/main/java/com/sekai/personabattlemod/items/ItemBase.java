package com.sekai.personabattlemod.items;

import com.sekai.personabattlemod.PersonaBattle;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemBase extends Item {
    public ItemBase() {
        super(new Item.Properties().group(PersonaBattle.TAB));
    }

    //public abstract void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> subItems);
}

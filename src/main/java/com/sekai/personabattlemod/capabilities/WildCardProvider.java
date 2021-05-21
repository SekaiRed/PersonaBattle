package com.sekai.personabattlemod.capabilities;

import com.sekai.personabattlemod.persona.impl.WildCard;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//@Mod.EventBusSubscriber
public class WildCardProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(WildCard.class)
    public static final Capability<WildCard> WC_CAP = null;

    public LazyOptional<WildCard> instance = LazyOptional.of(WildCard::new);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        //return cap == WC_CAP ? WC_CAP.<T>cast(this.instance) : null;
        return cap == WC_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        //return cap == WC_CAP ? WC_CAP.<T>cast(this.instance) : null;
        //return WC_CAP.orEmpty(cap, instance);
        //return instance;
        return cap == WC_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        /*return WC_CAP.getStorage().writeNBT(WC_CAP, this.instance.orElse(new WildCard()), null);*/
        NonNullSupplier<WildCard> nonNullSupplier = new NonNullSupplier<WildCard>() {
            @Nonnull
            @Override
            public WildCard get() {
                return null;
            }
        };
        return (CompoundNBT) WC_CAP.getStorage().writeNBT(WC_CAP, instance.orElseThrow(() -> new IllegalArgumentException("This is indeed quite the bruh moment")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        //WC_CAP.getStorage().readNBT(WC_CAP, this.instance.orElse(new WildCard()), null, nbt);
        NonNullSupplier<WildCard> nonNullSupplier = new NonNullSupplier<WildCard>() {
            @Nonnull
            @Override
            public WildCard get() {
                return null;
            }
        };
        WC_CAP.getStorage().readNBT(WC_CAP, instance.orElseGet(nonNullSupplier), null, nbt);
    }
}

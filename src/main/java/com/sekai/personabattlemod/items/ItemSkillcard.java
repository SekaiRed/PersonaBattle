package com.sekai.personabattlemod.items;

import com.sekai.personabattlemod.capabilities.WildCardProvider;
import com.sekai.personabattlemod.battle.move.MoveDatabase;
import com.sekai.personabattlemod.battle.move.declarations.MoveBase;
import com.sekai.personabattlemod.battle.move.property.MoveType;
import com.sekai.personabattlemod.packets.PacketCapabilitiesWildCard;
import com.sekai.personabattlemod.battle.persona.impl.WildCard;
import com.sekai.personabattlemod.util.PacketHandler;
import com.sekai.personabattlemod.util.RegistryHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemSkillcard extends ItemBase {
    public ItemSkillcard()
    {
        super();
    }

    public static void setSkill(ItemStack stack, String ID)
    {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        compoundNBT.putString("skill", ID);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return !stack.getOrCreateTag().getString("skill").equals("");
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        String skill = stack.getOrCreateTag().getString("skill");

        if(skill.equals(""))
            return new TranslationTextComponent("skill.empty").applyTextStyles(TextFormatting.WHITE);

        MoveBase move = MoveDatabase.getMove(stack.getOrCreateTag().getString("skill"));
        if(move == MoveDatabase.NULL)
            return new TranslationTextComponent("skill.corrupted").applyTextStyles(TextFormatting.WHITE);

        return new TranslationTextComponent(move.getUnlocalizedName()).applyTextStyles(move.getType().getTextFormatColor());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!player.getHeldItem(hand).getItem().equals(RegistryHandler.SKILLCARD.get()) || player.getHeldItem(hand).getOrCreateTag().getString("skill").equals(""))
            return super.onItemRightClick(world, player, hand);

        WildCard wc = player.getCapability(WildCardProvider.WC_CAP, null).orElse(null);

        if(player.world.isRemote || world.isRemote)
            return super.onItemRightClick(world, player, hand);

        MoveBase move = MoveDatabase.getMove(player.getHeldItem(hand).getOrCreateTag().getString("skill"));
        if(move == null)
            return super.onItemRightClick(world, player, hand);

        if(!wc.getEquipedPersona().addSkill(move.getStringID())) {
            ITextComponent message = new StringTextComponent("Your " + wc.getEquipedPersona().getName() + " failed to learn ");
            message.appendSibling(new TranslationTextComponent(move.getUnlocalizedName()));
            message.appendSibling(new StringTextComponent(", perhaps it already knew that skill or have too many."));
            message.applyTextStyle(TextFormatting.RED);
            player.sendMessage(message);
            return super.onItemRightClick(world, player, hand);
        }

        PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketCapabilitiesWildCard(wc));

        ITextComponent message = new StringTextComponent(wc.getEquipedPersona().getName() + " just learned ");
        message.appendSibling(new TranslationTextComponent(move.getUnlocalizedName()));
        message.appendSibling(new StringTextComponent("."));

        player.sendMessage(message);

        world.playSound(null, player.getPositionVec().x, player.getPositionVec().y, player.getPositionVec().z, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);

        if (!player.abilities.isCreativeMode) {
            player.getHeldItem(hand).shrink(1);
        }

        return super.onItemRightClick(world, player, hand);

        //PacketHandler.NET.send(PacketDistributor.PLAYER.noArg(), new PacketCapabilitiesWildCard(wc));
        //PacketHandler.NET.sendTo(new PacketCapabilitiesWildCard(wc), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        //PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketCapabilitiesWildCard(wc));
        //PacketHandler.NET.send(PacketDistributor.SERVER.with(() -> (ServerPlayerEntity) player), new PacketCapabilitiesWildCard(wc));
        //PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketCapabilitiesWildCard(wc));
        //PacketHandler.NET.send(PacketDistributor.SERVER.noArg(), new PacketCapabilitiesWildCard(wc));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String skill = stack.getOrCreateTag().getString("skill");

        if(skill.equals("")) {
            tooltip.add(new TranslationTextComponent("skill.empty.desc").applyTextStyles(TextFormatting.GRAY));
            return;
        }

        MoveBase move = MoveDatabase.getMove(stack.getOrCreateTag().getString("skill"));
        if(move == MoveDatabase.NULL) {
            tooltip.add(new TranslationTextComponent("skill.corrupted.desc").applyTextStyles(TextFormatting.GRAY));
            return;
        }

        tooltip.add(new TranslationTextComponent(move.getUnlocalizedDescription()).applyTextStyles(TextFormatting.DARK_GRAY));
        tooltip.add(getFormattedCost(move));
        /*MoveBase move = MoveDatabase.getMove(stack.getOrCreateTag().getString("skill"));
        if(move == null)
            tooltip.add(new TranslationTextComponent("skill.empty.desc").applyTextStyles(TextFormatting.GRAY));
        else
            tooltip.add(new TranslationTextComponent(move.getUnlocalizedDescription()).applyTextStyles(TextFormatting.DARK_GRAY));
        super.addInformation(stack, worldIn, tooltip, flagIn);*/
    }

    private ITextComponent getFormattedCost(MoveBase move) {
        TextComponent text = new StringTextComponent("");
        text.appendText(String.valueOf(move.getCost()));
        if(move.getType() == MoveType.PHYS || move.getType() == MoveType.BOW) {
            text.appendText("% HP");
            text.applyTextStyles(TextFormatting.AQUA);
        } else {
            text.appendText(" SP");
            text.applyTextStyles(TextFormatting.LIGHT_PURPLE);
        }
        return text;
        //return new StringTextComponent(move.getCost()).applyTextStyles(TextFormatting.DARK_GRAY);
    }

    @Override
    public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> subItems)
    {
        if (this.isInGroup(tab)) {
            subItems.add(new ItemStack(this, 1));
            Set<String> IDs = MoveDatabase.getAllMoveIDs();
            for(String ID: IDs) {
                //System.out.println("for the id " + ID + ", found " + (MoveDatabase.getMove(ID)!=null?MoveDatabase.getMove(ID).getStringID():"null"));
                ItemStack subItemStack = new ItemStack(this, 1);
                setSkill(subItemStack, ID);
                subItems.add(subItemStack);
            }
            /*for (int i = 0; i<MoveDatabase.moveList.size(); i++) {
                System.out.println("index i : " + i + ", move " + (MoveDatabase.getMove(i)!=null?MoveDatabase.getMove(i).getStringID():"null"));
                //ItemStack subItemStack = new ItemStack(this, 1);
                //setSkill(subItemStack, MoveDatabase.getMove(i));
                //subItems.add(subItemStack);
            }*/
            /*for (MoveBase move : MoveDatabase.moveList.values()) {
                ItemStack subItemStack = new ItemStack(this, 1);
                setSkill(subItemStack, move);
                subItems.add(subItemStack);
            }*/
        }
    }
}

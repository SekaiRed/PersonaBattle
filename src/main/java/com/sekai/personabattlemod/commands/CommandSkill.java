package com.sekai.personabattlemod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sekai.personabattlemod.capabilities.WildCardProvider;
import com.sekai.personabattlemod.battle.move.MoveDatabase;
import com.sekai.personabattlemod.packets.PacketCapabilitiesWildCard;
import com.sekai.personabattlemod.util.PacketHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class CommandSkill {
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent("commands.skill.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> giveSkillCommand
                = Commands.literal("skill")
                .requires((commandSource) -> commandSource.hasPermissionLevel(0))
                .then(Commands.literal("give")
                        .then(Commands.argument("skill", ArgumentSkill.skill()).suggests(ArgumentSkill.AVAILABLE_SKILLS)
                                .executes(CommandSkill::giveSkill)
                        )
                ).then(Commands.literal("remove")
                        .then(Commands.argument("skill", ArgumentSkill.skill()).suggests(ArgumentSkill.AVAILABLE_SKILLS)
                                .executes(CommandSkill::removeSkill)
                        )
                ).then(Commands.literal("clear")
                        .executes(CommandSkill::clearSkill)
                );
        /*LiteralArgumentBuilder<CommandSource> giveSkillCommand
                = Commands.literal("skill")
                .requires((commandSource) -> commandSource.hasPermissionLevel(0))
                .then(Commands.literal("give")
                        .then(Commands.argument("skill", MessageArgument.message())
                                .executes(CommandSkill::giveSkill)
                        )
                ).then(Commands.literal("remove")
                        .then(Commands.argument("skill", MessageArgument.message())
                                .executes(CommandSkill::removeSkill)
                        )
                ).then(Commands.literal("clear")
                        .executes(CommandSkill::clearSkill)
                );*/
        dispatcher.register(giveSkillCommand);
    }

    static int giveSkill(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
        String skillString = ArgumentSkill.getSkill(commandContext, "skill");

        if(MoveDatabase.getMove(skillString) == MoveDatabase.NULL)
            throw FAILED_EXCEPTION.create();

        Entity entity = commandContext.getSource().getEntity();
        if (entity != null) {
            if(entity instanceof PlayerEntity) {
                entity.getCapability(WildCardProvider.WC_CAP).ifPresent(wc -> {
                    wc.addSkill(skillString);
                    PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity), new PacketCapabilitiesWildCard(wc));
                    commandContext.getSource().sendFeedback(new TranslationTextComponent("commands.skill.give.success", new TranslationTextComponent(MoveDatabase.getMove(skillString).getUnlocalizedName()), wc.getEquipedPersona().getName()), true);
                });
            }
        }
        return 1;
    }

    static int removeSkill(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
        String skillString = ArgumentSkill.getSkill(commandContext, "skill");

        if(MoveDatabase.getMove(skillString) == MoveDatabase.NULL)
            throw FAILED_EXCEPTION.create();

        Entity entity = commandContext.getSource().getEntity();
        if (entity != null) {
            if(entity instanceof PlayerEntity) {
                entity.getCapability(WildCardProvider.WC_CAP).ifPresent(wc -> {
                    wc.removeSkill(skillString);
                    PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity), new PacketCapabilitiesWildCard(wc));
                    commandContext.getSource().sendFeedback(new TranslationTextComponent("commands.skill.remove.success", new TranslationTextComponent(MoveDatabase.getMove(skillString).getUnlocalizedName()), wc.getEquipedPersona().getName()), true);
                });
            }
        }
        return 1;
    }

    static int clearSkill(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
        Entity entity = commandContext.getSource().getEntity();
        if (entity != null) {
            if(entity instanceof PlayerEntity) {
                entity.getCapability(WildCardProvider.WC_CAP).ifPresent(wc -> {
                    int count = wc.getSkillCount();
                    for(int i = 0; i < count; i++)
                        wc.removeSkill(0);
                    PacketHandler.NET.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity), new PacketCapabilitiesWildCard(wc));
                    commandContext.getSource().sendFeedback(new TranslationTextComponent("commands.skill.clear.success", wc.getEquipedPersona().getName()), true);
                });
            }
        }
        return 1;
    }

    /**
     * Read the command's "message" argument, convert it to pig latin, then send as a chat message
     */
    /*static int sendPigLatinMessage(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
        ITextComponent messageValue = MessageArgument.getMessage(commandContext, "message");
        String unformattedText = messageValue.getString();
        String pigifiedText = convertParagraphToPigLatin(unformattedText);
        ITextComponent pigifiedTextComponent = new StringTextComponent(pigifiedText);

        TranslationTextComponent finalText = new TranslationTextComponent("chat.type.announcement",
                commandContext.getSource().getDisplayName(), pigifiedTextComponent);

        Entity entity = commandContext.getSource().getEntity();
        if (entity != null) {
            commandContext.getSource().getServer().getPlayerList().func_232641_a_(finalText, ChatType.CHAT, entity.getUniqueID());
            //func_232641_a_ is sendMessage()
        } else {
            commandContext.getSource().getServer().getPlayerList().func_232641_a_(finalText, ChatType.SYSTEM, Util.DUMMY_UUID);
        }
        return 1;
    }*/
}

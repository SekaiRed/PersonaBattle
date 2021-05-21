package com.sekai.personabattlemod.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.sekai.personabattlemod.move.MoveDatabase;
import com.sekai.personabattlemod.move.declarations.MoveBase;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ArgumentSkill implements ArgumentType<String> {
    public static final SuggestionProvider<CommandSource> AVAILABLE_SKILLS = SuggestionProviders.register(new ResourceLocation("available_skills"), (p_197495_0_, p_197495_1_) -> {
        //return ISuggestionProvider.suggestIterable(p_197495_0_.getSource().getSoundResourceLocations(), p_197495_1_);
        CompletableFuture<Suggestions> suggestions = new CompletableFuture<>();
        String s = p_197495_1_.getRemaining().toLowerCase(Locale.ROOT);
        /*for(int i = 0; i < MoveDatabase.moveList.size(); i++) {
            if(MoveDatabase.getMove(i).getStringID().contains(s)) {
                p_197495_1_.suggest(MoveDatabase.getMove(i).getStringID());
            }
        }*/
        Set<String> IDs = MoveDatabase.getAllMoveIDs();
        for(String ID: IDs) {
            if(ID.contains(s)) {
                p_197495_1_.suggest(ID);
            }
        }
        return p_197495_1_.buildFuture();
        //return suggestions;
    });

    private static final DynamicCommandExceptionType SKILL_NOT_FOUND = new DynamicCommandExceptionType((p_208676_0_) -> {
        return new TranslationTextComponent("skill.notFound", p_208676_0_);
    });

    public static ArgumentSkill skill() {
        return new ArgumentSkill();
    }

    public static String getSkill(CommandContext<CommandSource> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while(reader.canRead() && isValidPathCharacter(reader.peek())) {
            reader.skip();
        }

        String s = reader.getString().substring(i, reader.getCursor());

        MoveBase move = MoveDatabase.getMove(s);
        if(move == null)
            throw SKILL_NOT_FOUND.createWithContext(reader, s);
        else
            return s;

        /*try {
            return new ResourceLocation(s);
        } catch (ResourceLocationException var4) {
            reader.setCursor(i);
            throw INVALID_EXCEPTION.createWithContext(reader);
        }*/
    }

    public static boolean isValidPathCharacter(char charIn) {
        return charIn >= '0' && charIn <= '9' || charIn >= 'a' && charIn <= 'z' || charIn == '_' || charIn == ':' || charIn == '/' || charIn == '.' || charIn == '-';
    }
}

package me.ford.periodicholographicdisplays.util;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

/**
 * HindUtil
 */
public final class HintUtil {

    private HintUtil() {
        throw new IllegalStateException("Utility classes should not be initialized");
    }

    public static void sendHint(CommandSender sender, String fullHint, String hintCommand) {
        sendHint(sender, fullHint, "{command}", hintCommand);
    }

    public static void sendHint(CommandSender sender, String fullHint, String commandPlaceHolder, String hintCommand) {
        int hintStart = fullHint.indexOf(commandPlaceHolder);
        if (hintStart == -1) throw new IllegalArgumentException(String.format("The placeholder '%s' was not found in the hint message '%s'", commandPlaceHolder, fullHint));
        int hintStop = hintStart + commandPlaceHolder.length();
        String before = fullHint.substring(0, hintStart);
        String after = fullHint.substring(hintStop);
        TextComponent cb = new TextComponent(before);
        TextComponent hint = new TextComponent(hintCommand);
        hint.setClickEvent(new ClickEvent(Action.RUN_COMMAND, hintCommand));
        TextComponent ca = new TextComponent(after);
        sender.spigot().sendMessage(cb, hint, ca);
    }
    
}
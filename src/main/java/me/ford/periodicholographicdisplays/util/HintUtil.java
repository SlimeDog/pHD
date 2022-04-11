package me.ford.periodicholographicdisplays.util;

import dev.ratas.slimedogcore.api.messaging.SDCMessage;
import dev.ratas.slimedogcore.api.messaging.context.SDCVoidContext;
import dev.ratas.slimedogcore.api.messaging.delivery.MessageTarget;
import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.impl.messaging.ContextMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * HindUtil
 */
public final class HintUtil {

    private HintUtil() {
        throw new IllegalStateException("Utility classes should not be initialized");
    }

    public static void sendHint(SDCRecipient sender, String fullHint, String hintCommand) {
        sendHint(sender, fullHint, "{command}", hintCommand);
    }

    public static void sendHint(SDCRecipient sender, String fullHint, String commandPlaceHolder, String hintCommand) {
        int hintStart = fullHint.indexOf(commandPlaceHolder);
        if (hintStart == -1)
            throw new IllegalArgumentException(String.format(
                    "The placeholder '%s' was not found in the hint message '%s'", commandPlaceHolder, fullHint));
        int hintStop = hintStart + commandPlaceHolder.length();
        String before = fullHint.substring(0, hintStart);
        String after = fullHint.substring(hintStop);
        TextComponent cb = new TextComponent(before);
        TextComponent hint = new TextComponent(hintCommand);
        hint.setClickEvent(new ClickEvent(Action.RUN_COMMAND, hintCommand));
        TextComponent ca = new TextComponent(after);
        SDCMessage<SDCVoidContext> msg = new ContextMessage<SDCVoidContext>(ComponentSerializer.toString(cb, hint, ca),
                null, MessageTarget.TEXT);
        sender.sendMessage(msg);
    }

}
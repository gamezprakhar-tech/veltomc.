package com.veltomc.module.impl.misc;

import com.veltomc.module.Category;
import com.veltomc.module.Module;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Prepends a [HH:mm:ss] timestamp to incoming chat messages. */
public class ChatTimestampsModule extends Module {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ChatTimestampsModule() {
        super("Chat Timestamps", "Prefixes chat messages with the time received.", Category.MISC, false);
        ClientReceiveMessageEvents.MODIFY_GAME.register((message, overlay) -> {
            if (!isEnabled() || overlay) return message;
            String time = "[" + LocalTime.now().format(FORMAT) + "] ";
            return Text.literal(time).append(message);
        });
    }
}

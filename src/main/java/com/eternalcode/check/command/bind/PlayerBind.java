package com.eternalcode.check.command.bind;

import com.eternalcode.check.configuration.implementation.MessagesConfiguration;
import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.bind.Parameter;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import org.bukkit.entity.Player;

@ArgumentName("sender")
public class PlayerBind implements Parameter<Player> {

    private final MessagesConfiguration messages;

    public PlayerBind(MessagesConfiguration messages) {
        this.messages = messages;
    }

    @Override
    public Object apply(LiteInvocation invocation) {
        if (invocation.sender().getSender() instanceof Player) {
            return invocation.sender().getSender();
        }

        throw new ValidationCommandException(this.messages.arguments.onlyPlayer);
    }
}
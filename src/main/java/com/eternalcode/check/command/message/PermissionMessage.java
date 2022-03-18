package com.eternalcode.check.command.message;

import com.eternalcode.check.configuration.implementation.MessagesConfiguration;
import dev.rollczi.litecommands.valid.messages.LiteMessage;
import dev.rollczi.litecommands.valid.messages.MessageInfoContext;
import org.apache.commons.lang.StringUtils;
import panda.utilities.text.Joiner;

public class PermissionMessage implements LiteMessage {

    private final MessagesConfiguration messages;

    public PermissionMessage(MessagesConfiguration messages) {
        this.messages = messages;
    }

    @Override
    public String message(MessageInfoContext messageInfoContext) {
        return StringUtils.replace(this.messages.arguments.permission,
                "{PERMISSIONS}",
                Joiner.on(", ").join(messageInfoContext.getMissingPermissions()).toString());
    }
}

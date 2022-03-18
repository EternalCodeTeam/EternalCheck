package com.eternalcode.check.command.argument;

import com.eternalcode.check.configuration.implementation.MessagesConfiguration;
import com.eternalcode.check.user.User;
import com.eternalcode.check.user.UserManager;
import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.SingleArgumentHandler;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import panda.std.Option;

import java.util.List;
import java.util.stream.Collectors;

@ArgumentName("user")
public class UserArgument implements SingleArgumentHandler<User> {

    private final MessagesConfiguration messages;
    private final UserManager userManager;
    private final Server server;

    public UserArgument(MessagesConfiguration messages, UserManager userManager, Server server) {
        this.messages = messages;
        this.userManager = userManager;
        this.server = server;
    }

    @Override
    public User parse(LiteInvocation invocation, String argument) throws ValidationCommandException {
        Player player = this.server.getPlayer(argument);

        if (player == null) {
            throw new ValidationCommandException(this.messages.arguments.offlinePlayer);
        }

        Option<User> userOption = this.userManager.find(player.getUniqueId());

        if (userOption.isEmpty()) {
            throw new ValidationCommandException(this.messages.check.arguments.notChecked);
        }

        return userOption.get();
    }

    @Override
    public List<String> tabulation(String command, String[] args) {
        return this.userManager.getUsers()
                .stream()
                .map(user -> this.server.getPlayer(user.getUniqueId()))
                .map(HumanEntity::getName)
                .collect(Collectors.toList());
    }
}

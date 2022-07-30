package com.eternalcode.check.command.argument;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.user.User;
import com.eternalcode.check.user.UserService;
import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import panda.std.Result;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ArgumentName("user")
public class UserArgument implements OneArgument<User> {

    private final MessagesConfig messages;
    private final UserService userService;
    private final Server server;

    public UserArgument(MessagesConfig messages, UserService userService, Server server) {
        this.messages = messages;
        this.userService = userService;
        this.server = server;
    }

    @Override
    public Result<User, ?> parse(LiteInvocation invocation, String argument) {
        Player player = this.server.getPlayer(argument);

        if (player == null) {
            return Result.error(this.messages.argument.offlinePlayer);
        }

        Optional<User> userOptional = this.userService.find(player.getUniqueId());

        if (userOptional.isPresent()) {
            return Result.ok(userOptional.get());
        }

        return Result.error(this.messages.argument.notChecking);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.userService.getUsers()
                .stream()
                .map(User::getName)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }
}
package com.eternalcode.check.command.argument;

import com.eternalcode.check.config.implementation.MessagesConfig;
import com.eternalcode.check.user.CheckedUser;
import com.eternalcode.check.user.CheckedUserService;
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
public class CheckedUserArgument implements OneArgument<CheckedUser> {

    private final MessagesConfig messages;
    private final CheckedUserService checkedUserService;
    private final Server server;

    public CheckedUserArgument(MessagesConfig messages, CheckedUserService checkedUserService, Server server) {
        this.messages = messages;
        this.checkedUserService = checkedUserService;
        this.server = server;
    }

    @Override
    public Result<CheckedUser, ?> parse(LiteInvocation invocation, String argument) {
        Player player = this.server.getPlayer(argument);

        if (player == null) {
            return Result.error(this.messages.argument.offlinePlayer);
        }

        Optional<CheckedUser> userOptional = this.checkedUserService.find(player.getUniqueId());

        if (userOptional.isPresent()) {
            return Result.ok(userOptional.get());
        }

        return Result.error(this.messages.argument.notChecking);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.checkedUserService.checkedUsers()
                .stream()
                .map(CheckedUser::getName)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }
}
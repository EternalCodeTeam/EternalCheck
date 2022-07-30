package com.eternalcode.check.command.contextual;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.std.Result;

public class PlayerContextual implements Contextual<CommandSender, Player> {

    @Override
    public Result<Player, Object> extract(CommandSender sender, Invocation<CommandSender> invocation) {
        if (sender instanceof Player) {
            return Result.ok((Player) sender);
        }

        return Result.error("onlyPlayer");
    }
}

